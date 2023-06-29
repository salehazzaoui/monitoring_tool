package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import models.static_part.Component;
import models.dynamic_part.ComponentConstraint;
import models.static_part.Connector;
import models.dynamic_part.ConnectorConstraint;

public class NewConstraintController {
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField compTimeField;
    @FXML
    private TextField compMemoryField;
    @FXML
    private TextField concTimeField;
    @FXML
    private TextField concBandField;
    @FXML
    private ChoiceBox<String> componentChoice;
    @FXML
    private ChoiceBox<String> connectorChoice;

    public void addComponentConstraint(Component component){
        int time;
        int memory;
        String timeVal = compTimeField.getText();
        String memoryVal = compMemoryField.getText();
        if(timeVal.isEmpty() || memoryVal.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText("All fields are required");
            alert.show();
            return;
        }
        try {
            time = Integer.parseInt(timeVal);
            memory = Integer.parseInt(memoryVal);
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText(e.getMessage());
            alert.show();
            return;
        }
        component.setConstraint(new ComponentConstraint(time, memory));
    }

    public void addConnectorConstraint(Connector connector){
        int time;
        int bandwidth;
        String timeVal = concTimeField.getText();
        String bandwidthVal = concBandField.getText();
        if(timeVal.isEmpty() || bandwidthVal.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText("All fields are required");
            alert.show();
            return;
        }
        try {
            time = Integer.parseInt(timeVal);
            bandwidth = Integer.parseInt(bandwidthVal);
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText(e.getMessage());
            alert.show();
            return;
        }
        connector.setConstraint(new ConnectorConstraint(time, bandwidth));
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public ChoiceBox<String> getComponentChoice() {
        return componentChoice;
    }

    public ChoiceBox<String> getConnectorChoice() {
        return connectorChoice;
    }

    public void setCompMemoryField(String compMemory) {
        this.compMemoryField.setText(compMemory);
    }

    public void setCompTimeField(String time) {
        this.compTimeField.setText(time);
    }

    public void setConcTimeField(String time) {
        this.concTimeField.setText(time);
    }

    public void setConcBandField(String band) {
        this.concBandField.setText(band);
    }
}
