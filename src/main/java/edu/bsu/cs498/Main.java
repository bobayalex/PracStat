package edu.bsu.cs498;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {
    // TOD0
    // finish test cases
    // get practice/team names programmatically
    @Override
    public void start(Stage primaryStage) throws Exception{
//        String fxmlPath = "/fxml/startPage.fxml";
        String fxmlPath = "/fxml/mainPage.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        primaryStage.setTitle("PracStat");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
//        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowAction);
        primaryStage.show();
    }

    private void closeWindowAction(WindowEvent windowEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit PracStat?");
        Optional<ButtonType> result = alert.showAndWait();
        if (!result.isPresent()) {
            alert.close();
        } else if (result.get() == ButtonType.OK) {
            System.exit(0);
        } else if (result.get() == ButtonType.CANCEL) {
            windowEvent.consume();
            alert.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
