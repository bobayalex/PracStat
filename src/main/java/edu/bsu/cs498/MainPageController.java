package edu.bsu.cs498;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    @FXML private AnchorPane anchorPane;
    @FXML private MenuBar menuBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpMenuBar();
    }

    private void setUpMenuBar() {

    }
}
