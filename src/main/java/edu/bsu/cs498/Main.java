package edu.bsu.cs498;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {

    private static MainPageController mainPageController;
    @Override
    public void start(Stage primaryStage) throws Exception{
        String fxmlPath = "/fxml/startPage.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/startpage.css");
        primaryStage.setTitle("PracStat");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowAction);
        primaryStage.getIcons().add(new Image("images/pracstat_logo.png"));
        primaryStage.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainPage.fxml"));
        mainPageController = loader.getController();
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
