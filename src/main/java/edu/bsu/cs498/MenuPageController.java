package edu.bsu.cs498;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MenuPageController {
    @FXML private Button newTeamButton;
    @FXML private Button editTeamButton;
    @FXML private Button newPracticeButton;
    @FXML private Button viewPracticeButton;

    public void initialize(){
        setButtonActions();
    }

    private void setButtonActions(){
        newTeamButton.setOnAction(this::newTeamButtonAction);
        editTeamButton.setOnAction(this::editTeamButtonAction);
        newPracticeButton.setOnAction(this::newPracticeButtonAction);
        viewPracticeButton.setOnAction(this::viewPracticeButtonAction);
    }

    private void newTeamButtonAction(javafx.event.ActionEvent event) {
        boolean hasClearance = promptForPassword();
        if(hasClearance){
            try { switchRoot(event, "/fxml/newTeam.fxml");}
            catch (IOException ignored){}
        }
    }

    private void editTeamButtonAction(javafx.event.ActionEvent event) {
        boolean hasClearance = promptForPassword();
        if(hasClearance){
            try { switchRoot(event, "/fxml/editTeam.fxml");}
            catch (IOException ignored){}
        }
    }

    private void newPracticeButtonAction(javafx.event.ActionEvent event) {
        boolean hasClearance = promptForPassword();
        if(hasClearance){
            try { switchRoot(event, "/fxml/newPractice.fxml");}
            catch (IOException ignored){}
        }
    }

    private boolean promptForPassword() {
        AtomicBoolean isCorrect = new AtomicBoolean(false);
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Password");
        dialog.setHeaderText("Enter Password to Continue.");
        GridPane grid = new GridPane();
        PasswordField passwordField = new PasswordField();
        passwordField.requestFocus();
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(passwordField::requestFocus);
        Button button = (Button) dialog.getDialogPane().lookupButton(dialog.getDialogPane().getButtonTypes().get(0));
        button.setOnAction(event -> {
            String pass = passwordField.getText();
            if(pass.equals("pword")){
                isCorrect.set(true);
            }
        });
        dialog.showAndWait();
        if(!isCorrect.get() && !passwordField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Password");
            alert.showAndWait();
        }
        return isCorrect.get();
    }

    private void viewPracticeButtonAction(javafx.event.ActionEvent event) {
        try { switchRootMainPage(event);}
        catch (IOException ignored){}
    }

    private void switchRoot(ActionEvent event, String resourceName) throws IOException {
        Parent updatedRoot = FXMLLoader.load(getClass().getResource(resourceName));
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.getScene().setRoot(updatedRoot);
    }

    // this is a special case so that the css can be loaded for the main page
    private void switchRootMainPage(ActionEvent event) throws IOException {
        Parent updatedRoot = FXMLLoader.load(getClass().getResource("/fxml/viewPractice.fxml"));
        Scene currentScene = ((Node) event.getSource()).getScene();
        currentScene.getStylesheets().add("css/mainpage.css");
        Stage currentStage = (Stage) currentScene.getWindow();
        currentStage.getScene().setRoot(updatedRoot);
    }
}
