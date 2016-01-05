package views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sockets.Server;
import views.viewmodels.MainController;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Server server = new Server();
        MainController mc = new MainController(server);
        server.addListener(mc);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
        fxmlLoader.setController(mc);
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(root, 600, 300));
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(600);
        primaryStage.setOnCloseRequest(e -> {
            server.closeServer();
            System.exit(0);
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
