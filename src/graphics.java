import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;

public class graphics extends Canvas implements Runnable {
    private Thread thread;
    int fps = 30;
    private boolean isRunning;
    private BufferStrategy bs;
    private Controller c;
    int width, height;

    public graphics(Controller c, int fullWidth, int fullHeight) {
        isRunning = false;
        this.requestFocus();
        this.c = c;
        this.width = fullWidth;
        this.height = fullHeight;

        JFrame frame = new JFrame("Titel");
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(fullWidth, fullHeight);
        frame.pack();
        frame.setVisible(true);
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

    private void update() {
        c.getUpdate();
    }

    private void draw(Graphics g) {
        c.getDraw(g);
    }

    public void paint() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        draw(g);
        g.dispose();
        bs.show();
    }
}