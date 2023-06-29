package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.static_part.Component;
import models.dynamic_part.Csp;
import models.static_part.Port;
import utils.TypePort;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewPort {
    @FXML
    private TextField namePortField;
    @FXML
    private ChoiceBox<String> typePortChoice;
    @FXML
    private ChoiceBox<String> positionChoice;
    @FXML
    private TextField cspTextField;
    @FXML
    private Button validateBtn;

    private boolean isValidCsp;
    private String csp;

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
        if (!isValidCsp){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText("The Csp not valid.");
            alert.show();
            return null;
        }
        Port port = new Port(name.trim(), component);
        port.setCspExpression(new Csp(name.trim(), this.csp));
        component.getPorts().add(port);
        port.setType(TypePort.valueOf(portType));

        return port;
    }

    public void ValidateFormula(ActionEvent actionEvent) {
        String cspFormule = cspTextField.getText();
        if (cspFormule.isEmpty()) validateBtn.setDisable(true);
        Pattern pExpIn = Pattern.compile(
                "([a-zA-Z]+[0-9]*\\?[a-zA-Z]+[0-9]*(?:[-]>[a-zA-Z]+[0-9]*[?!][a-zA-Z]+[0-9]*)*)(?:[-]>([a-zA-Z]+[0-9]*))");
        Pattern pExpOut = Pattern.compile(
                "([a-zA-Z]+[0-9]*![a-zA-Z]+[0-9]*(?:[-]>[a-zA-Z]+[0-9]*[?!][a-zA-Z]+[0-9]*)*)(?:[-]>([a-zA-Z]+[0-9]*))");
        Matcher cspExpressionInMatcher = pExpIn.matcher(cspFormule);
        Matcher cspExpressionOutMatcher1 = pExpOut.matcher(cspFormule);
        String type = typePortChoice.getSelectionModel().getSelectedItem();
        if (type.isEmpty()) validateBtn.setDisable(true);
        if (type.compareTo("IN") == 0) {

            if ((!cspExpressionInMatcher.matches())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setContentText("CSP invalid!");
                alert.show();
                isValidCsp = false;
                return;
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Validation Error");
                alert.setContentText("CSP Valid.");
                alert.show();
                this.csp = cspFormule;
                isValidCsp = true;
                return;
            }

        } else {
            if (!cspExpressionOutMatcher1.matches()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setContentText("CSP invalid!");
                alert.show();
                isValidCsp = false;
                return;
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Validation Error");
                alert.setContentText("CSP Valid.");
                alert.show();
                this.csp = cspFormule;
                isValidCsp = true;
                return;

            }

        }
    }
}
