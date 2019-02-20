package edu.bsu.cs498;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartPageController implements Initializable {
    @FXML private Button startButton;
    @FXML private ImageView imageView;
    private String setupPath = "/fxml/newTeam.fxml";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadImage();
        setButtonActions();
    }

    private void loadImage() {
        Image logo = new Image("/images/pracstat_logo.png");
        imageView.imageProperty().set(logo);
    }

    private void setButtonActions() {
        setStartButtonAction();
    }

    private void setStartButtonAction() {
        startButton.setOnAction(this::startButtonAction);
    }

    private void startButtonAction(ActionEvent event) {
        try {
            switchRoot(event, setupPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchRoot(ActionEvent event, String resourceName) throws IOException {
        Parent updatedRoot = FXMLLoader.load(getClass().getResource(resourceName));
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.getScene().setRoot(updatedRoot);
    }
}