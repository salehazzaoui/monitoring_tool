package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class AddNewPort {
    @FXML
    private TextField namePortField;
    @FXML
    private ChoiceBox<String> typePortChoice;
    @FXML
    private ChoiceBox<String> compPortChoice;

    public ChoiceBox<String> getTypePortChoice() {
        return typePortChoice;
    }

    public ChoiceBox<String> getCompPortChoice() {
        return compPortChoice;
    }
}
