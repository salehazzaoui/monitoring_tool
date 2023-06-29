package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.static_part.Component;
import models.static_part.Configuration;

import java.util.List;


public class NewComponentController {

    @FXML
    private ChoiceBox<String> configurationsChoice;
    @FXML
    private TextField nameComp;
    @FXML
    private TextField memoryConsComp;
    @FXML
    private TextField nameComposite;
    @FXML
    private ListView<String> childrenComp;

    public ChoiceBox<String> getConfigurationsChoice() {
        return configurationsChoice;
    }

    public Component addComponent(Configuration configuration){
         int memoryConsummation;
         String name = nameComp.getText();
         String mc = memoryConsComp.getText();
         if (name.isEmpty() || mc.isEmpty()){
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Validation Error");
             alert.setContentText("The name must be not empty.");
             alert.show();
             return null;
         }
        try {
            memoryConsummation = Integer.parseInt(mc);
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText(e.getMessage());
            alert.show();
            return null;
        }
         Component component = new Component(name.trim(), memoryConsummation, configuration);
         configuration.components.add(component);

         return component;
    }

    public Component addCompositeComponent(List<Component> components, Configuration configuration){
        String name = nameComposite.getText();
        int memoryConsummation = components.stream().reduce(0, (total, component) -> total + component.getMemoryConsummation(), Integer::sum);
        if (name.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setContentText("The name must be not empty.");
            alert.show();
            return null;
        }
        Component component = new Component(name.trim(), memoryConsummation, configuration);

        configuration.components.add(component);

        return component;
    }

    public TextField getNameComposite() {
        return nameComposite;
    }

    public ListView<String> getChildrenComp() {
        return childrenComp;
    }

    public TextField getNameComp() {
        return nameComp;
    }

    public void initNameComp(String name) {
        this.nameComp.setText(name);
    }

    public TextField getMemoryConsComp() {
        return memoryConsComp;
    }

    public void initMemoryConsComp(String memoryConsComp) {
        this.memoryConsComp.setText(memoryConsComp);
    }
}
