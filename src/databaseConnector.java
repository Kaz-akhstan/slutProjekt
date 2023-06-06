import javax.swing.*;
import java.sql.*;

public class databaseConnector {
    Connection connection;
    Statement statement;

    public databaseConnector() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + databaseLoginData.DBURL + ":" + databaseLoginData.port + "/" + databaseLoginData.DBname +
                            "? allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", databaseLoginData.user, databaseLoginData.password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to database", "Failed",JOptionPane.WARNING_MESSAGE);
        }
    }

    public String getDatabaseContent() {
        String result = "";
        String SQLQuery = "SELECT * FROM rj28highscores ORDER BY score DESC LIMIT 10";
        try {
            ResultSet resultSet = statement.executeQuery(SQLQuery);
            while (resultSet.next()) {
                result += "Score: " + resultSet.getInt("score") + ", Player: " + resultSet.getString("user") + "\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to get content", "Failed",JOptionPane.WARNING_MESSAGE);
        }
        return result;
    }

    public void insertNewScore(String user, int score) {
        String SQLQuery = "INSERT INTO rj28highscores(user, score) VALUES ('" + user + "', '" + score + "')";
        try {
            statement.executeUpdate(SQLQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update content", "Failed",JOptionPane.WARNING_MESSAGE);
        }
    }
}
