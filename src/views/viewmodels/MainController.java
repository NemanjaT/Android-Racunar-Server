package views.viewmodels;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import sockets.Connection;
import sockets.Server;
import sockets.dependencies.ConnectionListener;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MainController implements ConnectionListener, Initializable {
    @FXML protected TextArea serverLogTextArea;
    @FXML protected Button TFServerButton;
    @FXML protected TabPane tabPane;

    protected Server server;
    protected HashMap<Tab, ConnectionTabController> connectionTabs;

    public MainController(Server server) {
        this.server = server;
        connectionTabs = new HashMap<>();
    }

    /**
     * Ukljucivanje i iskljucivanje servera.
     */
    @FXML
    protected void TFServer_Click() {
        if(!server.running()) {
            new Thread(() -> {
                Platform.runLater(() -> TFServerButton.setDisable(true));
                server.startServer();
            }).start();
            /**
             * pokusaj konekcije servera, zatim sistem ceka 5 sekundi. Ukoliko se server ne upali do tada, iskljucuje
             * se i pokusaji prestaju. Dugme je deaktivirano tokom procesa paljenja.
             */
            new Thread(() -> {
                int i = 0;
                while (!server.running())
                    try {
                        if(i == 50) { //5000ms = 5s
                            serverLogEvent("Server timeout. Closing...");
                            server.closeServer();
                        }
                        Thread.sleep(100);
                        i++;
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                Platform.runLater(() -> TFServerButton.setDisable(false));
                if(server.running())
                    Platform.runLater(() -> TFServerButton.setText("Turn server OFF"));
            }).start();
        } else {
            /**
             * pokusaj diskonekcije servera, kada se server napokon konacno ugasi, dugme je ponovo dostupno.
             */
            new Thread(() -> {
                Platform.runLater(() -> TFServerButton.setDisable(true));
                server.closeServer();
            }).start();
            new Thread(() -> {
                while (server.running())
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                Platform.runLater(() -> TFServerButton.setDisable(false));
                Platform.runLater(() -> TFServerButton.setText("Turn server ON"));
            }).start();
        }
    }

    /**
     * Klik na dugme za log-ovanje naredbi koje su zatrazene @TODO: dodaj implementaciju sa bazom.
     */
    @FXML
    protected void requestLog_Click() {
        System.out.println("requestLog || dodati log iz baze podataka(mozda XML/JSON?)");
    }

    /**
     * Event kada server posalje poruku korisniku (npr. konekcija ostvarena...)
     * @param log poruka koja se prosledjuje
     */
    @Override
    public void serverLogEvent(String log) {
        Platform.runLater(() ->
                serverLogTextArea.setText(serverLogTextArea.getText() + log + System.lineSeparator())
        );
    }

    /**
     * event prikaza kada klijent posalje poruku (konekcije, naredbe...)
     * @param connection konekcija (socket)
     * @param msg poruka koja se prosledjuje
     */
    @Override
    public void connectionLogEvent(Connection connection, String msg) {
        connectionTabs.entrySet().stream().filter(ct -> ct.getValue().getConnection() == connection).forEach(ct -> ct.getValue().addLog(msg));
    }

    /**
     * Event kada se nova konekcija ostvari. Kreira se tab sa naznacenim izgledom (views.fxml.connectionTab.fxml).
     * @param connection konekcija(socket)
     */
    @Override
    public void connectionCreatedEvent(Connection connection) {
        try {
            ConnectionTabController controller = new ConnectionTabController(connection);
            Tab tab = new Tab();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/connectionTab.fxml"));
            fxmlLoader.setController(controller);
            tab.setText(connection.toString());
            tab.setOnCloseRequest(e -> {
                if(connection.works())
                    connection.close();
            });
            tab.setContent(fxmlLoader.load());
            Platform.runLater(() -> {
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().select(tab);
            });
            connectionTabs.put(tab, controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Event ukoliko se neka od konekcija iskljuci (sve se proveravaju). Onima koje jesu se iskljucuju tabovi.
     */
    @Override
    public void connectionClosedEvent() {
        connectionTabs.entrySet().stream().filter(ct -> !ct.getValue().getConnection().works()).forEach(ct -> {
            Tab tab = ct.getKey();
            Platform.runLater(() -> tabPane.getTabs().remove(tab));
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
