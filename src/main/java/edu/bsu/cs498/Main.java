package edu.bsu.cs498;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        String fxmlPath = "/fxml/startPage.fxml";

//        String fxmlPath = "/fxml/mainPage.fxml";
//        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
//        primaryStage.setTitle("PracStat");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.setMaximized(true);
//        primaryStage.show();
        XMLFileHandler handler = new XMLFileHandler();
        handler.test();
        System.exit(0);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
