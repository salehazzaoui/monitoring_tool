package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.Component;
import models.Configuration;


public class NewComponentController {

    @FXML
    private ChoiceBox<String> configurationsChoice;
    @FXML
    private TextField nameComp;
    @FXML
    private TextField memoryConsComp;

    public ChoiceBox<String> getConfigurationsChoice() {
        return configurationsChoice;
    }

    public Component addComponent(Configuration configuration){
         int memoryConsummation;
         String name = nameComp.getText();
         String mc = memoryConsComp.getText();
         if (name.isEmpty() || mc.isEmpty()){
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Validation Error");
             alert.setContentText("The name must be not empty.");
             alert.show();
             return null;
         }
        try {
            memoryConsummation = Integer.parseInt(mc);
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText(e.getMessage());
            alert.show();
            return null;
        }
         Component component = new Component(name.trim(), memoryConsummation, configuration);
         configuration.components.add(component);

         return component;
    }
}
