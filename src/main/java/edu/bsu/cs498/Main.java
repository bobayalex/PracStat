package edu.bsu.cs498;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.IIOParam;
import java.awt.*;
import java.util.List;

public class Main extends Application {

    static MainPageController mainPageController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        String fxmlPath = "/fxml/startPage.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainPage.fxml"));
        mainPageController = loader.getController();
        primaryStage.setTitle("PracStat");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/startpage.css");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> System.exit(0));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
