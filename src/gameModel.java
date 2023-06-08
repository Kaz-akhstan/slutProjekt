import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class gameModel extends Canvas {

    boolean movingLeft, movingRight, movingUp, movingDown;
    boolean isAttacking;

    player p;
    Rectangle PLAYER;

    private final int height = 20;
    private final int width = 30;
    int uiHeight = 100;

    int spriteSize = 16;
    int spriteScale = 2;

    int score = 0;
    int currency = 0;
    int addCurrency = 10;

    int spawnTimer = 0;
    int spawnCooldown = 20;

    private BufferedImage spriteimg;
    private BufferedImage wall;
    private BufferedImage mapImage;
    private BufferedImage skeletonSprite;
    private BufferedImage arrowSprite;
    private BufferedImage groundSprite;

    private Controller c;

    ArrayList<Rectangle> walls = new ArrayList<>();
    ArrayList<projectile>projectiles = new ArrayList<>();
    ArrayList<skeleton>skeletons = new ArrayList<>();

    int[] upgradeCost = {200, 200, 200};
    String[] upgradeNames = {"Fire Rate", "Speed", "Looting"};

    public Vector2 getResolution() {
        return new Vector2(width*spriteSize, height*spriteSize + uiHeight);
    }

    public gameModel(Controller c) {
        this.c = c;
        spriteSize = spriteScale * spriteSize;
        try {
            spriteimg = ImageIO.read(getClass().getResource("player.png"));
            wall = ImageIO.read(getClass().getResource("wall_mid.png"));
            mapImage = ImageIO.read(getClass().getResource("map1.png"));
            skeletonSprite = ImageIO.read(getClass().getResource("skeleton.png"));
            arrowSprite = ImageIO.read(getClass().getResource("arrow.png"));
            groundSprite = ImageIO.read(getClass().getResource("floor.png"));
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
        p = new player(32, 32, 5, 1);
        PLAYER = new Rectangle(p.x, p.y, spriteimg.getWidth()*spriteScale, spriteimg.getHeight()*spriteScale);
    }

    public skeleton isCollidingWithSkeleton (Rectangle actor, int xOffset, int yOffset) {
        for (skeleton skeleton : skeletons) {
            if (new Rectangle(actor.x + xOffset, actor.y + yOffset, actor.width, actor.height).intersects(new Rectangle(skeleton.x, skeleton.y, skeletonSprite.getWidth()*spriteScale, skeletonSprite.getHeight()*spriteScale))) {
                return skeleton;
            }
        }
        return null;
    }

    public boolean isCollidingWithWall (Rectangle actor, int xOffset, int yOffset) {
        for (Rectangle wall : walls) {
            if (new Rectangle(actor.x + xOffset, actor.y + yOffset, actor.width, actor.height).intersects(wall)) {
                return true;
            }
        }
        return false;
    }

    public void updateProjectiles() {
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
            Rectangle arrow = new Rectangle(projectiles.get(i).x, projectiles.get(i).y, arrowSprite.getWidth(), arrowSprite.getWidth());
            if(isCollidingWithWall(arrow, 0, 0)) {
                projectiles.remove(i);
            }
            skeleton s = isCollidingWithSkeleton(arrow, 0, 0);
            if(s != null) {
                skeletons.remove(s);
                score++;
                switch (score) {
                    case 20:
                        spawnCooldown = spawnCooldown-2;
                        break;
                    case 50:
                        spawnCooldown = spawnCooldown-3;
                        break;
                    case 70:
                        spawnCooldown = spawnCooldown-5;
                        break;
                    case 100:
                        spawnCooldown = spawnCooldown-5;
                        break;
                }

                currency += addCurrency;
                try {
                    projectiles.remove(i);
                }
                catch (IndexOutOfBoundsException e) {
                    System.out.println("Arrow already destroyed");
                }
            }
        }
    }

    public void spawnEnemy() {
        Random rn = new Random();
        boolean spawned = false;
        int x = rn.nextInt(width*spriteSize);
        int y = rn.nextInt(height*spriteSize);

        Rectangle s = new Rectangle(x, y, spriteSize, spriteSize);
        if(!isCollidingWithWall(s, 0, 0) && !s.intersects(PLAYER.x-spriteSize, PLAYER.y-spriteSize, spriteSize*3, spriteSize*3)) {
            skeletons.add(new skeleton(x, y, 4, 1));
        }
        else {
            spawnTimer = spawnCooldown;
        }
    }

    public void updateSkeletons() {
        for(skeleton skeleton : skeletons) {
            Rectangle s = new Rectangle(skeleton.x, skeleton.y, skeletonSprite.getWidth() * spriteScale, skeletonSprite.getHeight() * spriteScale);
            if(skeleton.x > PLAYER.x && !isCollidingWithWall(s, -3, 0)) {
                skeleton.x -= skeleton.speed;
            }
            if(skeleton.x < PLAYER.x && !isCollidingWithWall(s, 3, 0)) {
                skeleton.x += skeleton.speed;
            }
            if(skeleton.y > PLAYER.y && !isCollidingWithWall(s, 0, -3)) {
                skeleton.y -= skeleton.speed;
            }
            if(skeleton.y < PLAYER.y && !isCollidingWithWall(s, 0, 3)) {
                skeleton.y += skeleton.speed;
            }
            if(s.intersects(PLAYER)) {
                String P = JOptionPane.showInputDialog("Enter Name");
                JOptionPane.showMessageDialog(null, c.gameOver(P, score));
                System.exit(0);
            }
        }
    }

    private void updateTimers() {
        spawnTimer++;
        if(spawnTimer >= spawnCooldown) {
            spawnEnemy();
            spawnTimer = 0;
        }
        p.setAttackTimer(p.getAttackTimer()+1);
        if(p.getAttackTimer() >= p.getCooldown()) {
            isAttacking = false;
            p.setAttackTimer(0);
        }
    }

    private void updatePlayerPosition() {
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

    public void update() {
        updateTimers();
        updateSkeletons();
        updateProjectiles();
        updatePlayerPosition();
    }

    /**
     * Rita ut alla saker. Ordningen är viktig eftersom vi kan rita saker på andra saker.
     *
     * @param g grafiken
     */
    public void draw(Graphics g) {
        drawGround(g);
        drawWalls(g);
        drawProjectiles(g);
        drawSkeletons(g);
        drawPlayer(g);
        drawScore(g);
        drawGUI(g);
    }

    private void drawGUI(Graphics g) {
        for (int i = 0; i < upgradeCost.length; i++) {
            g.setColor(new Color(50+((i+1)*10), 50+((i+1)*10) ,50+((i+1)*10)));
            g.fillRect((width*spriteSize)/upgradeCost.length*i, height*spriteSize, width*spriteSize/ upgradeCost.length, uiHeight);
            g.setColor(Color.white);
            g.setFont(new Font("Dialog", Font.BOLD, 20));
            g.drawString("[" + (i+1) + "] - " + upgradeNames[i] + " - Cost: " + upgradeCost[i], (width*spriteSize)/upgradeCost.length*i + (width*spriteSize/ upgradeCost.length) / 8, height*spriteSize + uiHeight/2);
        }
    }

    private void drawGround(Graphics g) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                g.drawImage(groundSprite,j*spriteSize, i*spriteSize, groundSprite.getWidth()*spriteScale, groundSprite.getHeight()*spriteScale, null);
            }
        }
    }

    public void drawScore(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Dialog", Font.BOLD, 20));
        g.drawString("Score: " + score, spriteSize/2, spriteSize);
        g.drawString("Coins: " + currency, spriteSize*4, spriteSize);
    }

    public void drawSkeletons(Graphics g) {
        for (int i = 0; i < skeletons.size(); i++) {
            g.setColor(Color.cyan);
            g.drawImage(skeletonSprite, skeletons.get(i).x, skeletons.get(i).y, skeletonSprite.getWidth()*spriteScale, skeletonSprite.getHeight()*spriteScale, null);
        }
    }

    public void drawProjectiles(Graphics g) {
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

    public void drawWalls(Graphics g) {
        for (int i = 0; i < walls.size(); i++) {
            g.drawImage(wall, walls.get(i).x, walls.get(i).y, spriteScale*wall.getWidth(), spriteScale*wall.getHeight(), null);
        }
    }

    public void drawPlayer(Graphics g) {
        g.drawImage(spriteimg, PLAYER.x, PLAYER.y, PLAYER.width, PLAYER.height,null);
    }

    public BufferedImage rotate(BufferedImage bi, int degree) {
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

    public void playerAttack(int xOffset, int yOffset) {
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

    public void upgrade(int index) {
        switch (index) {
            case 1:
                if(currency >= upgradeCost[index-1] && p.getCooldown() >= 5) {
                    currency -= upgradeCost[index-1];
                    upgradeCost[index-1] = (int) (upgradeCost[index-1] * 1.3);
                    p.setCooldown(p.getCooldown()-2);
                }
                break;
            case 2:
                if(currency >= upgradeCost[index-1]) {
                    currency -= upgradeCost[index-1];
                    upgradeCost[index-1] = (int) (upgradeCost[index-1] * 1.3);
                    p.setSpeed(p.getSpeed()+2);
                }
                break;
            case 3:
                if(currency >= upgradeCost[index-1]) {
                    currency -= upgradeCost[index-1];
                    upgradeCost[index-1] = (int) (upgradeCost[index-1] * 1.3);
                    addCurrency = (int) (addCurrency * 1.5);
                }
                break;
        }
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }
}
