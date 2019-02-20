package edu.bsu.cs498;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

public class NewTeamController {

    @FXML
    private TextField teamNameInput;

    @FXML
    private TextField numberOfPlayersInput;

    @FXML
    private TextArea output;

    public void createTeam(){
        Team team1 = new Team(teamNameInput.getText(), Integer.parseInt(numberOfPlayersInput.getText()));
        output.setText(team1.getTeamInfo());
    }

}

