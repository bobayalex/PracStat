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
    @FXML
    private Button startButton;
    @FXML
    private ImageView imageView;
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
        String nextPage = choosePath();
        try { switchRoot(event, nextPage);}
        catch (IOException e){}
    }

    private String choosePath() {
        if (!isConfigured) {
            return "/fxml/setup.fxml";
        }
        return "/fxml/menuPage.fxml";
    }

    private void switchRoot(ActionEvent event, String resourceName) throws IOException {
        Parent updatedRoot = FXMLLoader.load(getClass().getResource(resourceName));
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.getScene().setRoot(updatedRoot);
    }
}