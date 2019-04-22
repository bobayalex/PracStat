package edu.bsu.cs498;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    // TOD0, view average stats, menubar put button actions in
    // check and handle errors, ex. csv file in use when trying to update
    // add success messages for CSV file generation, updating stats in config file
    // add 1 scrollbar for both gridpanes
    @Override
    public void start(Stage primaryStage) throws Exception{
//        String fxmlPath = "/fxml/startPage.fxml";
        String fxmlPath = "/fxml/mainPage.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        primaryStage.setTitle("PracStat");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
