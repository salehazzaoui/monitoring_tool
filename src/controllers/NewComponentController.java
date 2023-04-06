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

    public ChoiceBox<String> getConfigurationsChoice() {
        return configurationsChoice;
    }

    public Component addComponent(Configuration configuration){
         String name = nameComp.getText();
         if (name.isEmpty()){
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Validation Error");
             alert.setContentText("The name must be not empty.");
             alert.show();
             return null;
         }
         Component component = new Component(name.trim());
         configuration.components.add(component);

         return component;
    }
}
