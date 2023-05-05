import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    private final int height = 600;
    private final int width = 800;
    // Variabler gör det lättare att placera saker
    int x = 400;
    int y = 300;
    int vx = 1;
    int vy = 1;

    private BufferedImage spriteimg;

    /**
     * Skapa ett fönster och lägg in grafiken i det.
     */
    public graphics() {
        JFrame frame = new JFrame("Titel");
        this.setSize(width, height);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Börja med animationen avslagen
        isRunning = false;

        // Lägg till en keylistener
        this.addKeyListener(new KL());
        this.addMouseListener(new ML());
        this.addMouseMotionListener(new MML());
        this.requestFocus();
        // Läs in en bild
        try {
            spriteimg = ImageIO.read(new File("filename.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        g.fillRect(0, 0, width, height);
        draw(g);
        // Det här byter skärm
        g.dispose();
        bs.show();
    }

    /**
     * Ändra värdet på de variabler som styr animationerna
     */
    private void update() {
        x+=vx;
        y+=vy;
        if (x <= 0 || x >= width - 4 * spriteimg.getWidth())
            vx = -vx;
        if (y <= 0 || y >= height - 4 * spriteimg.getHeight())
            vy = -vy;
    }

    /**
     * Rita ut alla saker. Ordningen är viktig eftersom vi kan rita saker på andra saker.
     *
     * @param g grafiken
     */
    private void draw(Graphics g) {
        g.drawImage(spriteimg, x, y, 4*spriteimg.getWidth(), 4* spriteimg.getHeight(),null);
    }

    /**
     * Nu kan vi starta vårt program
     * Skapa först en JFrame och en canvas, starta sedan tråden som sköter animationen.
     */
    public static void main(String[] args) {
        graphics exempel = new graphics();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                exempel.setVisible(true);
            }
        });
        exempel.start();
    }

    private class KL implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == 'a') {
                System.out.print("a");
            }
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {

        }
    }

    private class ML implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            System.out.println("Click!");
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }
    }

    private class MML implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            System.out.println("Mouse: " + mouseEvent.getX() + " , " + mouseEvent.getY());
        }
    }
}