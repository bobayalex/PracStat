package edu.bsu.cs498;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MenuPageController {
    @FXML private Button newTeamButton;
    @FXML private Button editTeamButton;
    @FXML private Button newPracticeButton;
    @FXML private Button viewStatsButton;

    public void initialize(){
        setButtonActions();
    }

    public void setButtonActions(){
        newTeamButton.setOnAction(this::newTeamButtonAction);
        editTeamButton.setOnAction(this::editTeamButtonAction);
        newPracticeButton.setOnAction(this::newPracticeButtonAction);
        viewStatsButton.setOnAction(this::viewStatsButtonAction);
    }

    private void newTeamButtonAction(javafx.event.ActionEvent event) {
        boolean hasClearance = promptForPassword();
        if(hasClearance){
            try { switchRoot(event, "/fxml/newTeam.fxml");}
            catch (IOException e){}
        }
    }

    private void editTeamButtonAction(javafx.event.ActionEvent event) {
        boolean hasClearance = promptForPassword();
        if(hasClearance){
            try { switchRoot(event, "/fxml/editTeam.fxml");}
            catch (IOException e){}
        }
    }

    private void newPracticeButtonAction(javafx.event.ActionEvent event) {
        boolean hasClearance = promptForPassword();
        if(hasClearance){
            try { switchRoot(event, "/fxml/newPractice.fxml");}
            catch (IOException e){}
        }
    }

    private boolean promptForPassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Password");
        dialog.setHeaderText("Enter Password to Continue.");
        Optional<String> result = dialog.showAndWait();
        if(result.get().equals("pword")){
            return true;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText("Incorrect Password");
        alert.showAndWait();
        return false;
    }

    private void viewStatsButtonAction(javafx.event.ActionEvent event) {
        //Page does not exist yet
    }

    private void switchRoot(ActionEvent event, String resourceName) throws IOException {
        Parent updatedRoot = FXMLLoader.load(getClass().getResource(resourceName));
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.getScene().setRoot(updatedRoot);
    }



}