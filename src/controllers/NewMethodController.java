package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.Component;
import models.Method;

public class NewMethodController {
    @FXML
    private TextField methodName;
    @FXML
    private TextField methodTime;
    @FXML
    private ChoiceBox<String> component;
    
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
        component.methods.add(new Method(name, time, component));
        return true;
    }

    public ChoiceBox<String> getComponent() {
        return component;
    }
}
