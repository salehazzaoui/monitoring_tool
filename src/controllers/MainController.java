package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import models.Component;
import models.Configuration;
import models.Model;
import utils.DraggableMaker;
import utils.XMLValidator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    @FXML
    private TreeView<String> mTreeView;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab mainConf;

    @FXML
    private TextArea resTextArea;

    private DraggableMaker draggableMaker;

    private TreeItem<String> rootItem;

    public Model model;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //draggableMaker = new DraggableMaker();
        //draggableMaker.makeDraggable(circle);
        rootItem = new TreeItem<>("Project");
        TreeItem<String> item = new TreeItem<>("Main Configuration");
        rootItem.getChildren().add(item);
        mTreeView.setRoot(rootItem);
    }

    public void addNewConf(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../views/newConfiguration.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("OK");
            NewConfController controller = fxmlLoader.getController();

            Configuration configuration =  controller.addConf();
            if(configuration == null) return;
            // add conf to the TreeView
            TreeItem<String> item = new TreeItem<>(configuration.getName());
            rootItem.getChildren().add(item);

            // create a new tab
            Tab tab = new Tab();
            tab.setText(configuration.getName());

            ScrollPane scrollPane = new ScrollPane();
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefWidth(scrollPane.getPrefWidth());
            anchorPane.setPrefHeight(scrollPane.getPrefHeight());
            scrollPane.setContent(anchorPane);

            tab.setContent(scrollPane);

            tabPane.getTabs().add(tab);

        }else {
            System.out.println("CANCEL");
        }
    }

    public void addNewComponent(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../views/newComponent.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        NewComponentController controller = fxmlLoader.getController();
        ChoiceBox<String> configurationsChoice = controller.getConfigurationsChoice();
        model = Model.getInstance();
        model.configurations.forEach(configuration -> {
            System.out.println(configuration.getName());
            configurationsChoice.getItems().add(configuration.getName());
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("OK");
            String confName = configurationsChoice.getSelectionModel().getSelectedItem();
            try {
                Configuration conf = model.getConfByName(confName);
                String nameComp = controller.addComponent(conf);
                if (nameComp == null) return;
                else nameComp = nameComp.trim();

                // Build the Component element
                VBox componentBox = new VBox();
                componentBox.setPrefWidth(120);
                componentBox.setPrefHeight(100);
                componentBox.setStyle("-fx-background-color: #F4CCC3;");

                Label header = new Label("<< Component >>");
                header.setPrefWidth(componentBox.getPrefWidth());
                header.setAlignment(Pos.CENTER);
                componentBox.getChildren().add(header);

                Label nameC = new Label(nameComp);
                nameC.setPrefWidth(componentBox.getPrefWidth());
                nameC.setPrefHeight(componentBox.getPrefHeight() - header.getPrefHeight());
                nameC.setAlignment(Pos.CENTER);
                componentBox.getChildren().add(nameC);

                // Make it draggable
                draggableMaker = new DraggableMaker();
                draggableMaker.makeDraggable(componentBox);


                // Add component element
                Tab tap = tabPane.getTabs().stream().filter(tab -> tab.getText().trim().equals(confName.trim())).toList().get(0);
                ScrollPane scrollPane = (ScrollPane) tap.getContent();
                AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();
                anchorPane.getChildren().add(componentBox);

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setContentText("the name of configuration doesn't match");
                alert.show();
            }
        }else {
            System.out.println("CANCEL");
        }
    }

    public void checkStructural(ActionEvent actionEvent) {
        model = Model.getInstance();
        ArrayList<Configuration> configurations = model.configurations;
        File file = new File("architecture.xml");
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(file, true));

            br.write("<Architecture>"); br.newLine();
            for (Configuration configuration:
                 configurations) {
                br.write("<Configuration>"); br.newLine();
                for (Component component:
                     configuration.components) {
                    br.write("<Component>"); br.newLine();
                    br.write("<name>"+ component.getName() +"</name>"); br.newLine();
                    br.write("</Component>"); br.newLine();
                }
                br.write("</Configuration>"); br.newLine();
            }
            br.write("</Architecture>"); br.newLine();

            br.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        boolean isValidXML = XMLValidator.validateXMLSchema("model.xsd", "architecture.xml");
        if (isValidXML){
            StringBuilder message = new StringBuilder();
            message.append("Result :");
            message.append("\n");
            message.append("The structural is valid.");
            resTextArea.setText(String.valueOf(message));
            resTextArea.setStyle("-fx-text-fill: green;");
        }else {
            StringBuilder message = new StringBuilder();
            message.append("Result :");
            message.append("\n");
            message.append("The structural is not valid.");
            resTextArea.setText(String.valueOf(message));
            resTextArea.setStyle("-fx-text-fill: red;");
        }
    }

    public void checkBehavioral(ActionEvent actionEvent) {
    }

    public void addPort(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../views/newPort.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        AddNewPort controller = fxmlLoader.getController();
        // fill component choice box
        ChoiceBox<String> compPort = controller.getCompPortChoice();
        model = Model.getInstance();
        ArrayList<ArrayList<Component>> components = new ArrayList<>();
        model.configurations.forEach(configuration -> {
            components.add(configuration.getComponents());
        });
        ArrayList<Component> components1 = (ArrayList<Component>) components.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        components1.forEach(component -> {
            System.out.println(component.getName());
            compPort.getItems().add(component.getName());
        });
        // fill component choice box
        ChoiceBox<String> typePort = controller.getTypePortChoice();
        typePort.getItems().addAll(List.of("Port In", "Port Out"));

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("OK");
        }else {
            System.out.println("CANCEL");
        }
    }
}
