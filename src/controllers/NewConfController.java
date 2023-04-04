package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Configuration;
import models.Model;


public class NewConfController {

    @FXML
    private TextField cTextField;

    public Configuration addConf(){
        if(!cTextField.getText().isEmpty()) {
            Model model = new Model();
            Configuration configuration = new Configuration(cTextField.getText().trim());
            model.configurations.add(configuration);
            return configuration;
        }
        return null;
    }
}
