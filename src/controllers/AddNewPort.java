package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.Component;
import models.Configuration;
import models.Port;
import utils.TypePort;

public class AddNewPort {
    @FXML
    private TextField namePortField;
    @FXML
    private ChoiceBox<String> typePortChoice;
    @FXML
    private ChoiceBox<String> positionChoice;

    public ChoiceBox<String> getTypePortChoice() {
        return typePortChoice;
    }

    public ChoiceBox<String> getPositionChoice() {
        return positionChoice;
    }

    public Port addPort(String portType, Component component){
        String name = namePortField.getText();
        if (name.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText("The name must be not empty.");
            alert.show();
            return null;
        }
        Port port = new Port(name.trim(), component);
        component.getPorts().add(port);
        port.setType(TypePort.valueOf(portType));

        return port;
    }
}
