package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.static_part.Component;
import models.static_part.Method;

public class NewMethodController {
    @FXML
    private TextField methodName;
    @FXML
    private TextField methodTime;
    @FXML
    private ChoiceBox<String> component;
    @FXML
    private TableView table;
    @FXML
    private TableColumn nameCol;
    @FXML
    private TableColumn timeCol;
    @FXML
    private TableColumn componentCol;

    
    public boolean addMethod(Component component){
        int time;
        String name = methodName.getText();
        String timeM = methodTime.getText();
        if(name.isEmpty() || timeM.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText("All fields are required.");
            alert.show();
            return false;
        }
        try{
            time = Integer.parseInt(timeM);
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText(e.getMessage());
            alert.show();
            return false;
        }
        component.methods.add(new Method(name, time, component.getName()));
        return true;
    }

    public ChoiceBox<String> getComponent() {
        return component;
    }

    public TableView getTable() {
        return table;
    }

    public TableColumn getNameCol() {
        return nameCol;
    }

    public TableColumn getTimeCol() {
        return timeCol;
    }

    public TableColumn getComponentCol() {
        return componentCol;
    }
}
