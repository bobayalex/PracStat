package edu.bsu.cs498;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    @FXML private MenuBar menuBar;
    @FXML private GridPane statGrid;
    @FXML private List<Spinner<Integer>> statSpinners = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        setUpMenuBar();
        setUpGridPane();
    }

    private void printSpinnerVals() {
        for (Spinner<Integer> spinner: statSpinners) {
            System.out.println("Value is " + spinner.getValue());
        }
    }

    private void printNodeFromGridPane(){
        Spinner<?> spinner = getSpinner(0,1);
        int foo = -1;
        if (spinner != null) {
            foo = (Integer) spinner.getValue();
        }
        System.out.println("Value is " + foo);
    }

    private void setUpGridPane() {
        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 16; j++){
                Spinner<Integer> spinner = new Spinner<>(0, 10000, 0, 1);
                spinner.setPrefWidth(100);
                spinner.setPrefHeight(30);
                statSpinners.add(spinner);
                statGrid.add(spinner,i,j);
            }
        }

    }

    private Spinner<Integer> getSpinner(int row, int col){
        for(Node child: statGrid.getChildren()){
            if(GridPane.getColumnIndex(child) != null && GridPane.getRowIndex(child) != null && GridPane.getColumnIndex(child) == col && GridPane.getRowIndex(child) == row){
                return (Spinner<Integer>)child;
            }
        }
        return null;
    }

//    private void setUpMenuBar() {
//
//    }
}
