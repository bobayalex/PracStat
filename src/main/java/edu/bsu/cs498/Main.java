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

    static StartPageController startPageController;
    static MainPageController mainPageController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        String fxmlPath = "/fxml/startPage.fxml";

//        String fxmlPath = "/fxml/mainPage.fxml";

        // THE ORIGINAL BELOW:
        Parent root1 = FXMLLoader.load(getClass().getResource(fxmlPath));

        //FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/fxml/startPage.fxml"));
        //Parent root1 = loader1.load();
        //startPageController = (StartPageController)loader1.getController();

        // THE NEW ONE:
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainPage.fxml"));
        Parent root = loader.load();
        mainPageController = (MainPageController)loader.getController();

        primaryStage.setTitle("PracStat");
        Scene scene = new Scene(root1);
        scene.getStylesheets().add("css/startpage.css");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        XMLFileHandler handler = new XMLFileHandler();
        handler.test();
        //System.exit(0);
        //primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.setOnCloseRequest(e -> System.exit(0));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
