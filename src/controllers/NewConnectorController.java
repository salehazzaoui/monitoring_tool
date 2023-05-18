package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.Connector;
import models.Csp;
import models.Port;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @FXML
    private TextField cspConnectorField;
    @FXML
    private TextField cspOutField;
    @FXML
    private TextField cspInField;

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

    public void initCspOutField(String csp) {
        this.cspOutField.setText(csp);
    }

    public void initCspInField(String csp) {
        this.cspInField.setText(csp);
    }

    public Connector addConnector(Port portIn, Port portOut){
        int bandwitdh;
        String name = nameConnectorField.getText();
        String band = bandConnectorField.getText();
        String expression = cspConnectorField.getText();
        if (name.isEmpty() || band.isEmpty() || expression.isEmpty()){
            this.showAlert("Validation Error", "All fields are required!", Alert.AlertType.ERROR);
            return null;
        }
        try {
            bandwitdh = Integer.parseInt(band);
        }catch (NumberFormatException e){
            this.showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
            return null;
        }

        Connector connector = new Connector(name.trim(), portIn, portOut, bandwitdh);
        if(!this.checkCsp(name, expression, connector)){
            this.showAlert("CSP Error", "your CSP formula name or expression is invalid!", Alert.AlertType.ERROR);
            return null;
        }

        this.showAlert("Validation Success", "Valid CSP expression", Alert.AlertType.INFORMATION);

        return connector;
    }

    private boolean checkCsp(String cspName, String cspExpression, Connector connector) {
        String port1 = connector.getPortIn().getName();
        String port2 = connector.getPortOut().getName();
        Pattern pName = Pattern.compile("[a-zA-Z]+[0-9]*");
        Matcher mName = pName.matcher(cspName);
        Pattern pExpOut = Pattern.compile("([a-zA-Z]+[0-9]*)![a-zA-Z]+[0-9]*((?:[-]>[a-zA-Z]+[0-9]*(?:[?!][a-zA-Z]+[0-9]*))+)(?:[-]>[a-zA-Z]+[0-9]*)?");
        Matcher cspExpressionOutMatcher1 = pExpOut.matcher(cspExpression);
        if (!mName.matches() && (!cspExpressionOutMatcher1.matches())) {
            //this.showAlert("CSP Error", "your CSP formula name and your CSP expression are invalid!", Alert.AlertType.ERROR);
            return false;

        } else if (!mName.matches()) {
            //this.showAlert("CSP Error", "your CSP formula name is invalid!", Alert.AlertType.ERROR);
            return false;
        } else if (!cspExpressionOutMatcher1.matches()) {
            //this.showAlert("CSP Error", "your CSP  expression is invalid!", Alert.AlertType.ERROR);
            return false;
        } else if (cspExpressionOutMatcher1.matches() && (mName.matches())) {
            String firstPartExp = cspExpressionOutMatcher1.group(1);
            String secondPartExp = cspExpressionOutMatcher1.group(2);
            List<String> portsInCspExpression = new ArrayList<>();
            List<String> newPortsInCspExpression = new ArrayList<>();
            Pattern firstGroupPortNamePattern1 = Pattern.compile("([a-zA-Z]+[0-9]*)");
            Matcher firstGroupPortNameMatcher = firstGroupPortNamePattern1.matcher(firstPartExp);
            //first group
            if (firstGroupPortNameMatcher.matches()) {
                portsInCspExpression.add(firstPartExp);
            }
            //second group
            Pattern secondGroupPortNamePattern = Pattern.compile("([a-zA-Z]+[0-9]*)(?=[!?])");
            Matcher secondGroupPortNameMatcher = secondGroupPortNamePattern.matcher(secondPartExp);
            while (secondGroupPortNameMatcher.find()) {
                portsInCspExpression.add(secondGroupPortNameMatcher.group(1));
                newPortsInCspExpression.add("_"+secondGroupPortNameMatcher.group(1));
            }
            //instance.getName()+
            System.out.println(port1+" "+port2);
            System.out.println("yo je ss la liste des ports"+portsInCspExpression);
            int l;
            for ( l = 0; l <portsInCspExpression.size()&&(portsInCspExpression.get(l).equals(port1)||portsInCspExpression.get(l).equals(port2)) ; l++);
            if(l <portsInCspExpression.size()){
                //this.showAlert("Validation Error", "Please check your ports name", Alert.AlertType.ERROR);
                return false;
            }
            else  {
                int index1 = portsInCspExpression.indexOf(port1);
                int index2 = portsInCspExpression.indexOf(port2);
                System.out.println("index" + index1 + "" + index2);

                cspExpression = cspExpression.replaceAll(portsInCspExpression.get(index1), connector.getPortIn().getComponent().getName() + "_" + port1);
                cspExpression = cspExpression.replaceAll(portsInCspExpression.get(index2), connector.getPortOut().getComponent().getName() + "_" + port2);

                connector.setConnectorCsp(new Csp(cspName, cspExpression));
                return true;
            }

        }
        return false;
    }

    private void showAlert(String title, String body, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(body);
        alert.show();
    }
}
