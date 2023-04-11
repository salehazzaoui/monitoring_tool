package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.Connector;
import models.Port;

public class NewConnectorController {
    @FXML
    private TextField nameConnectorField;
    @FXML
    private TextField bandConnectorField;
    @FXML
    private ChoiceBox<String> srcCompChoice;
    @FXML
    private ChoiceBox<String> desCompChoice;
    @FXML
    private ChoiceBox<String> portOutChoice;
    @FXML
    private ChoiceBox<String> portInChoice;

    public ChoiceBox<String> getSrcCompChoice() {
        return srcCompChoice;
    }

    public ChoiceBox<String> getDesCompChoice() {
        return desCompChoice;
    }

    public ChoiceBox<String> getPortOutChoice() {
        return portOutChoice;
    }

    public ChoiceBox<String> getPortInChoice() {
        return portInChoice;
    }

    public Connector addConnector(Port portIn, Port portOut){
        int bandwitdh;
        String name = nameConnectorField.getText();
        String band = bandConnectorField.getText();
        if (name.isEmpty() || band.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText("The name must be not empty.");
            alert.show();
            return null;
        }
        try {
            bandwitdh = Integer.parseInt(band);
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText(e.getMessage());
            alert.show();
            return null;
        }
        Connector connector = new Connector(name.trim(), portIn, portOut, bandwitdh);
        return connector;
    }
}
