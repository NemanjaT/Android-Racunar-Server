package views.viewmodels;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import sockets.Connection;

public class ConnectionTabController {
    @FXML protected TextArea connectionLogTextArea;

    private Connection connection;

    public ConnectionTabController(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void addLog(String log) {
        Platform.runLater(() ->
                connectionLogTextArea.setText(connectionLogTextArea.getText() + log + System.lineSeparator())
        );
    }

    @FXML
    protected void closeConnectionEvent() {
        connection.close();
    }
}
