package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.Configuration;
import models.Model;


public class NewConfController {

    @FXML
    private TextField cTextField;

    public Configuration addConf(){
        if(!cTextField.getText().isEmpty()) {
            Model model = Model.getInstance();
            Configuration configuration = new Configuration(cTextField.getText().trim());
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
