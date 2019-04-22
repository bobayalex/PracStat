package edu.bsu.cs498;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartPageController implements Initializable {
    @FXML private Button startButton;
    @FXML private ImageView imageView;
    @FXML private PasswordField pwordField;
    @FXML private Label label1;
    private boolean isConfigured;

    static MainPageController mainPageController1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        XMLFileHandler handler = new XMLFileHandler();
        isConfigured = handler.isConfigured();
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
        if (!pwordField.getText().equals("pword")) {
            label1.setText("Your password is incorrect!");
            label1.setTextFill(Color.rgb(210, 39, 30));
        } else {
            //label1.setText("Your password has been confirmed");
            //label1.setTextFill(Color.rgb(21, 117, 84));
            String path = choosePath();

            try {
                switchRoot(event, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pwordField.clear();
    }

    private String choosePath() {
        if(!isConfigured){
            return "/fxml/setup.fxml";
        }
        return "/fxml/mainPage.fxml";
    }

    private void switchRoot(ActionEvent event, String resourceName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceName));
        Parent updatedRoot = loader.load();

        mainPageController1 = (MainPageController)loader.getController();

        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.getStylesheets().add("css/mainpage.css");
        Stage currentStage = (Stage) currentScene.getWindow();

        currentStage.getScene().setRoot(updatedRoot);
    }
}