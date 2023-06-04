import org.w3c.dom.css.Rect;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Enkel grafik. Skapa en Canvas men skriv en egen metod för att anropa ritandet. För att kunna styra fps och ups
 * lägger vi den i egen tråd
 *
 * Created 2022-04-26
 *
 * @author Magnus Silverdal
 */

/**
 * Vi utökar klassen Canvas med vår bild och implementerar runnable så att den kan köras som en egen tråd
 */
public class graphics extends Canvas implements Runnable {
    // Variabler för tråden
    private Thread thread;
    int fps = 30;
    private boolean isRunning;
    // Skapa en buffrad grafik så att vi kan rita bilder i förväg, bättre än dbg från tidigare
    private BufferStrategy bs;
    // Storleken på bilden
    private int height = 20;
    private int width = 30;
    // Variabler gör det lättare att placera saker

    boolean movingLeft, movingRight, movingUp, movingDown;
    boolean isAttacking;

    player p;
    Rectangle PLAYER;

    int spriteSize = 16;
    int spriteScale = 2;

    int spawnTimer = 0;
    int spawnCooldown = 100;

    private BufferedImage spriteimg;
    private BufferedImage wall;
    private BufferedImage mapImage;
    private BufferedImage skeletonSprite;
    private BufferedImage arrowSprite;

    int[][] map;

    ArrayList<Rectangle>walls = new ArrayList<>();
    ArrayList<projectile>projectiles = new ArrayList<>();
    ArrayList<skeleton>skeletons = new ArrayList<>();

    /**
     * Skapa ett fönster och lägg in grafiken i det.
     */
    public graphics() {
        spriteSize = spriteScale * spriteSize;
        try {
            spriteimg = ImageIO.read(getClass().getResource("player.png"));
            wall = ImageIO.read(getClass().getResource("wall_mid.png"));
            mapImage = ImageIO.read(getClass().getResource("test_map.png"));
            skeletonSprite = ImageIO.read(getClass().getResource("skeleton.png"));
            arrowSprite = ImageIO.read(getClass().getResource("arrow.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(new Color(mapImage.getRGB(x, y)).equals(Color.black)) {
                    walls.add(new Rectangle(x*spriteSize, y*spriteSize, spriteScale*wall.getWidth(), spriteScale*wall.getHeight()));
                }
            }
        }
        map = new int[width][height];
        p = new player(32, 32, 5, 1);
        PLAYER = new Rectangle(p.x, p.y, spriteimg.getWidth()*spriteScale, spriteimg.getHeight()*spriteScale);

        JFrame frame = new JFrame("Titel");
        this.setSize(width*spriteSize, height*spriteSize);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Börja med animationen avslagen
        isRunning = false;

        // Lägg till en keylistener
        this.addKeyListener(new KL());
        this.requestFocus();
        // Läs in en bild
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        double deltaT = 1000.0 / fps;
        long lastTime = System.currentTimeMillis();

        while (isRunning) {
            long now = System.currentTimeMillis();
            if (now - lastTime > deltaT) {
                update();
                lastTime = now;
            }
            paint();
        }
        stop();
    }

    /**
     * Nu gör vi en egen paint. Skapa en bufferStrategy så att vi får flera skärmar att jobba på, Java sköter det åt oss
     */
    public void paint() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        // Om vi inte suddar allt ritar vi över det som redan fanns. Ibland kan det vara bättre att bara sudda en bit
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width*spriteSize, height*spriteSize);
        draw(g);
        // Det här byter skärm
        g.dispose();
        bs.show();
    }

    private boolean isCollidingWithWall (Rectangle actor, int xOffset, int yOffset) {
        for (Rectangle wall : walls) {
            if (new Rectangle(actor.x + xOffset, actor.y + yOffset, actor.width, actor.height).intersects(wall)) {
                return true;
            }
        }
        return false;
    }

    private void updateProjectiles() {
        for (int i = 0; i < projectiles.size(); i++) {
            switch (projectiles.get(i).direction) {
                case "Left":
                    projectiles.get(i).x -= projectiles.get(i).speed;
                    break;

                case "Right":
                    projectiles.get(i).x += projectiles.get(i).speed;
                    break;

                case "Up":
                    projectiles.get(i).y -= projectiles.get(i).speed;
                    break;

                case "Down":
                    projectiles.get(i).y += projectiles.get(i).speed;
                    break;
            }
            if(isCollidingWithWall(new Rectangle(projectiles.get(i).x, projectiles.get(i).y, arrowSprite.getWidth(), arrowSprite.getHeight()), 0, 0)) {
                projectiles.remove(i);
            }
        }
    }

    private void spawnEnemy() {
        Random rn = new Random();
        int x = rn.nextInt(width*spriteSize);
        int y = rn.nextInt(height*spriteSize);
        if(!isCollidingWithWall(new Rectangle(x, y, spriteSize, spriteSize), 0, 0)) {
            skeletons.add(new skeleton(x, y, 4, 1));
        }
    }

    private void update() {
        spawnTimer++;
        if(spawnTimer >= spawnCooldown) {
            spawnEnemy();
            spawnTimer = 0;
        }
        updateProjectiles();
        if(movingLeft && !isCollidingWithWall(PLAYER, -p.speed, 0)) {
            PLAYER.x-= p.speed;
        }
        else if(movingRight && !isCollidingWithWall(PLAYER, p.speed, 0)) {
            PLAYER.x+= p.speed;
        }
        else if(movingUp && !isCollidingWithWall(PLAYER, 0, -p.speed)) {
            PLAYER.y-= p.speed;
        }
        else if(movingDown && !isCollidingWithWall(PLAYER, 0, p.speed)) {
            PLAYER.y+= p.speed;
        }
        p.x = PLAYER.x;
        p.y = PLAYER.y;
    }

    /**
     * Rita ut alla saker. Ordningen är viktig eftersom vi kan rita saker på andra saker.
     *
     * @param g grafiken
     */
    private void draw(Graphics g) {
        drawPlayer(g);
        drawSkeletons(g);
        drawProjectiles(g);
        drawWalls(g);
    }

    private void drawSkeletons(Graphics g) {
        for (int i = 0; i < skeletons.size(); i++) {
            g.setColor(Color.cyan);
            g.drawRect(skeletons.get(i).x, skeletons.get(i).y, spriteSize, spriteSize);
            g.drawImage(skeletonSprite, skeletons.get(i).x, skeletons.get(i).y, skeletonSprite.getWidth()*spriteScale, skeletonSprite.getHeight()*spriteScale, null);
        }
    }

    private void drawProjectiles(Graphics g) {
        for (int i = 0; i < projectiles.size(); i++) {
            BufferedImage b;
            switch (projectiles.get(i).direction) {
                case "Up":
                    b = rotate(arrowSprite, 270);
                    g.drawImage(b, projectiles.get(i).x, projectiles.get(i).y, b.getWidth()*spriteScale, b.getHeight()*spriteScale, null);
                    break;
                case "Right":
                    g.drawImage(arrowSprite, projectiles.get(i).x, projectiles.get(i).y, arrowSprite.getWidth()*spriteScale, arrowSprite.getHeight()*spriteScale, null);
                    break;
                case "Left":
                    b = rotate(arrowSprite, 180);
                    g.drawImage(b, projectiles.get(i).x, projectiles.get(i).y, b.getWidth()*spriteScale, b.getHeight()*spriteScale, null);
                    break;
                case "Down":
                    b = rotate(arrowSprite, 90);
                    g.drawImage(b, projectiles.get(i).x, projectiles.get(i).y, b.getWidth()*spriteScale, b.getHeight()*spriteScale, null);
                    break;
            }
        }
    }

    private void drawWalls(Graphics g) {
        for (int i = 0; i < walls.size(); i++) {
            g.drawImage(wall, walls.get(i).x, walls.get(i).y, spriteScale*wall.getWidth(), spriteScale*wall.getHeight(), null);
        }
    }

    private void drawPlayer(Graphics g) {
        g.drawImage(spriteimg, PLAYER.x, PLAYER.y, PLAYER.width, PLAYER.height,null);
    }

    private BufferedImage rotate(BufferedImage bi, int degree) {
        int width = bi.getWidth();
        int height = bi.getHeight();

        BufferedImage biFlip;
        if (degree == 90 || degree == 270)
            biFlip = new BufferedImage(height, width, bi.getType());
        else if (degree == 180)
            biFlip = new BufferedImage(width, height, bi.getType());
        else
            return bi;

        if (degree == 90) {
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++)
                    biFlip.setRGB(height - j - 1, i, bi.getRGB(i, j));
        }

        if (degree == 180) {
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++)
                    biFlip.setRGB(width - i - 1, height - j - 1, bi.getRGB(i, j));
        }

        if (degree == 270) {
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++)
                    biFlip.setRGB(j, width - i - 1, bi.getRGB(i, j));
        }

        bi.flush();
        bi = null;

        return biFlip;
    }

    private class KL implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if(keyEvent.getKeyChar() == 'a') {
                movingLeft = true;
            }
            if(keyEvent.getKeyChar() == 'd') {
                movingRight = true;
            }
            if(keyEvent.getKeyChar() == 'w') {
                movingUp = true;
            }
            if(keyEvent.getKeyChar() == 's') {
                movingDown = true;
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_LEFT && !isAttacking) {
                playerAttack(-spriteSize/2, 0);
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_RIGHT && !isAttacking) {
                playerAttack(spriteSize/2, 0);
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_UP && !isAttacking) {
                playerAttack(0, -spriteSize/2);
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_DOWN && !isAttacking) {
                playerAttack(0, spriteSize/2);
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            if(keyEvent.getKeyChar() == 'a') {
                movingLeft = false;
            }
            if(keyEvent.getKeyChar() == 'd') {
                movingRight = false;
            }
            if(keyEvent.getKeyChar() == 'w') {
                movingUp = false;
            }
            if(keyEvent.getKeyChar() == 's') {
                movingDown = false;
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_LEFT) {
                isAttacking = false;
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_RIGHT) {
                isAttacking = false;
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_UP) {
                isAttacking = false;
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_DOWN) {
                isAttacking = false;
            }
        }
    }
    private void playerAttack(int xOffset, int yOffset) {
        isAttacking = true;
        if(xOffset < 0) {
            projectiles.add(new projectile(PLAYER.x + xOffset, PLAYER.y + yOffset + spriteSize/2, "Left"));
        }
        else if (xOffset > 0) {
            projectiles.add(new projectile(PLAYER.x + xOffset, PLAYER.y + yOffset + spriteSize/2, "Right"));
        }
        if(yOffset < 0) {
            projectiles.add(new projectile(PLAYER.x + xOffset + spriteSize/2, PLAYER.y + yOffset, "Up"));
        }
        else if (yOffset > 0) {
            projectiles.add(new projectile(PLAYER.x + xOffset + spriteSize/2, PLAYER.y + yOffset, "Down"));
        }
    }
}