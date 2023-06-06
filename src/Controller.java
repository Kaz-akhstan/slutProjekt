import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller extends Canvas {
    gameModel m;
    graphics v;
    public Controller() {
    }

    public void setMV(gameModel m, graphics v) {
        this.m = m;
        this.v = v;
        v.addKeyListener(new KL());
    }

    public void getUpdate() {
        m.update();
    }

    public void getDraw(Graphics g) {
        m.draw(g);
    }

    public void startGame() {
        EventQueue.invokeLater(() -> v.setVisible(true));
        v.start();
    }

    public String gameOver(String player, int score) {
        new databaseConnector().insertNewScore(player, score);
        return new databaseConnector().getDatabaseContent();
    }

    public static void main(String[] args) {
        Controller c = new Controller();
        c.setMV(new gameModel(c), new graphics(c, new gameModel(c).getResolution().x, new gameModel(c).getResolution().y));
        c.startGame();
    }

    public class KL implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if(keyEvent.getKeyChar() == 'a') {
                m.setMovingLeft(true);
            }
            if(keyEvent.getKeyChar() == 'd') {
                m.setMovingRight(true);
            }
            if(keyEvent.getKeyChar() == 'w') {
                m.setMovingUp(true);
            }
            if(keyEvent.getKeyChar() == 's') {
                m.setMovingDown(true);
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_LEFT && !m.isAttacking) {
                m.playerAttack(-1, 0);
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_RIGHT && !m.isAttacking) {
                m.playerAttack(m.spriteSize/2, 0);
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_UP && !m.isAttacking) {
                m.playerAttack(0, -1);
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_DOWN && !m.isAttacking) {
                m.playerAttack(0, m.spriteSize/2);
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            if(keyEvent.getKeyChar() == 'a') {
                m.setMovingLeft(false);
            }
            if(keyEvent.getKeyChar() == 'd') {
                m.setMovingRight(false);
            }
            if(keyEvent.getKeyChar() == 'w') {
                m.setMovingUp(false);
            }
            if(keyEvent.getKeyChar() == 's') {
                m.setMovingDown(false);
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_LEFT) {
                m.isAttacking = false;
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_RIGHT) {
                m.isAttacking = false;
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_UP) {
                m.isAttacking = false;
            }
            if(keyEvent.getKeyCode() == keyEvent.VK_DOWN) {
                m.isAttacking = false;
            }
        }
    }
}
