import javax.swing.*;
import java.awt.*;

public class Game {
    public static void main(String[] args) {
        System.out.println(new databaseConnector().getDatabaseContent());
        graphics g = new graphics();
        EventQueue.invokeLater(() -> g.setVisible(true));
        g.start();
    }
}
