package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.static_part.Configuration;
import models.Model;


public class NewConfController {

    @FXML
    private TextField cTextField;

    public Configuration addConf(){
        String confName = cTextField.getText();
        if(!confName.isEmpty()) {
            Model model = Model.getInstance();
            if(!model.configurations.stream().filter(configuration -> configuration.getName().equals(confName.trim())).toList().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setContentText("The name must be different.");
                alert.show();
                return null;
            }
            Configuration configuration = new Configuration(confName.trim());
            model.configurations.add(configuration);
            return configuration;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setContentText("The name must be not empty.");
        alert.show();
        return null;
    }
}
