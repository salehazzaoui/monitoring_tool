package controllers;

import com.uppaal.model.core2.Document;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import models.*;
import utils.*;
import widgets.Arrow;
import widgets.ComponentBlock;
import widgets.PortBlock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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

    private double mouseAnchorX;
    private double mouseAnchorY;

    private ArrayList<Arrow> connectorBlocks = new ArrayList<>();
    private Arrow connectorBlock;
    private int count = 0;
    private ArrayList<PortBlock> portOutBlocks = new ArrayList<>();
    private ArrayList<PortBlock> portInBlocks = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        draggableMaker = new DraggableMaker();
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
            configurationsChoice.getItems().add(configuration.getName());
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("OK");
            String confName = configurationsChoice.getSelectionModel().getSelectedItem();
            try {
                Configuration conf = model.getConfByName(confName);
                Component component = controller.addComponent(conf);
                if (component == null) return;

                // Build the Component element
                ComponentBlock componentBox = new ComponentBlock(component);
                // Make it draggable
                //draggableMaker.makeDraggable(componentBox);
                //draggableMaker.enableDrag(componentBox);
                componentBox.setOnMousePressed(mouseEvent -> {
                    mouseAnchorX = mouseEvent.getX();
                    mouseAnchorY = mouseEvent.getY();
                });

                componentBox.setOnMouseDragged(mouseEvent -> {
                    componentBox.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
                    componentBox.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
                    component.setX(componentBox.getLayoutX());
                    component.setY(componentBox.getLayoutY());
                });

                // Add component element
                Tab tap = tabPane.getTabs().stream().filter(tab -> tab.getText().trim().equals(confName.trim())).toList().get(0);
                ScrollPane scrollPane = (ScrollPane) tap.getContent();
                AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();
                anchorPane.getChildren().add(componentBox);

                // Add it to the TreeView
                TreeItem<String> item = rootItem.getChildren().stream().filter(it -> it.getValue().trim().equals(confName.trim())).toList().get(0);
                item.getChildren().add(new TreeItem<>(component.getName()));

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
            BufferedWriter br = new BufferedWriter(new FileWriter(file));

            br.write("<Architecture>"); br.newLine();
            for (Configuration configuration:
                 configurations) {
                br.write("<Configuration>"); br.newLine();
                for (Component component:
                     configuration.getComponents()) {
                    br.write("<Component>"); br.newLine();
                    br.write("<name>"+ component.getName() +"</name>"); br.newLine();
                    for (Port port:
                         component.getPorts()) {
                        br.write("<Port>"); br.newLine();
                        br.write("<name>"+ port.getName() +"</name>"); br.newLine();
                        br.write("<type>"+ port.getType() +"</type>"); br.newLine();
                        br.write("</Port>"); br.newLine();
                    }
                    br.write("</Component>"); br.newLine();
                }
                for (Connector connector:
                     configuration.getConnectors()) {
                    br.write("<Connector>"); br.newLine();
                    br.write("<name>"+ connector.getName() +"</name>"); br.newLine();
                    br.write("<Port_In>"); br.newLine();
                    br.write("<name>"+ connector.getPortIn().getName() +"</name>"); br.newLine();
                    br.write("<type>"+ connector.getPortIn().getType() +"</type>"); br.newLine();
                    br.write("</Port_In>"); br.newLine();
                    br.write("<Port_Out>"); br.newLine();
                    br.write("<name>"+ connector.getPortOut().getName() +"</name>"); br.newLine();
                    br.write("<type>"+ connector.getPortOut().getType() +"</type>"); br.newLine();
                    br.write("</Port_Out>"); br.newLine();
                    br.write("</Connector>"); br.newLine();
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
        Document doc = Uppaal.createSampleModel();
        try {
            doc.save("uppaal.xml");
            String uppaalPath = EV.uppaalPath;
            String modelPath = EV.modelPath;
            String command = "\""+uppaalPath+"\" \""+new File(modelPath).getAbsolutePath()+"\"";
            Process process = Runtime.getRuntime().exec(command);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * TODO: fix bug with in the interface
     *
     * */
    public void addNewConnector(ActionEvent actionEvent) throws Exception {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../views/newConnector.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        NewConnectorController controller = fxmlLoader.getController();
        ChoiceBox<String> srcCompChoice = controller.getSrcCompChoice();
        ChoiceBox<String> desCompChoice = controller.getDesCompChoice();
        ChoiceBox<String> portOutChoice = controller.getPortOutChoice();
        ChoiceBox<String> portInChoice = controller.getPortInChoice();

        model = Model.getInstance();
        String currentConfName = tabPane.getSelectionModel().getSelectedItem().getText();
        Configuration currentConf = model.configurations
                .stream()
                .filter(configuration -> configuration.getName().equals(currentConfName)).toList().get(0);
        ArrayList<Port> portsIn = new ArrayList<>();
        ArrayList<Port> portsOut = new ArrayList<>();
        for (Component component:
                currentConf.getComponents()) {
            srcCompChoice.getItems().add(component.getName());
            desCompChoice.getItems().add(component.getName());
            component.getPorts().forEach(port -> {
                if (port.getType().equals(TypePort.OUT)) {
                    portsOut.add(port);
                } else {
                    portsIn.add(port);
                }
            });
        }

        srcCompChoice.setOnAction(ActionEvent -> {
            String compName = srcCompChoice.getValue();
            try {
                Component component = currentConf.getCompByName(compName);
                component.getPortsOut().forEach(port -> {
                    portOutChoice.getItems().add(port.getName());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        desCompChoice.setOnAction(ActionEvent -> {
            String compName = desCompChoice.getValue();
            try {
                Component component = currentConf.getCompByName(compName);
                component.getPortsIn().forEach(port -> {
                    portInChoice.getItems().add(port.getName());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("OK");
            String srcComp = srcCompChoice.getSelectionModel().getSelectedItem();
            String desComp = desCompChoice.getSelectionModel().getSelectedItem();
            String portOutName = portOutChoice.getSelectionModel().getSelectedItem();
            String portInName = portInChoice.getSelectionModel().getSelectedItem();

            Component componentSrc = currentConf.getCompByName(srcComp);
            Component componentDes = currentConf.getCompByName(desComp);

            Port portIn =  portsIn.stream().filter(port -> port.getName().equals(portInName)).toList().get(0);
            Port portOut =  portsOut.stream().filter(port -> port.getName().equals(portOutName)).toList().get(0);
            Connector connector =  controller.addConnector(portIn, portOut);
            if (connector == null) return;
            currentConf.connectors.add(connector);

            ScrollPane scrollPane = (ScrollPane) tabPane.getSelectionModel().getSelectedItem().getContent();
            AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();

            PortBlock portOutBlock = (PortBlock) mainPane.lookup("#"+srcComp+"-"+portOutName);
            this.portOutBlocks.add(portOutBlock);
            PortBlock portInBlock = (PortBlock) mainPane.lookup("#"+desComp+"-"+portInName);
            this.portInBlocks.add(portInBlock);

            System.out.println("componentSrc X = "+componentSrc.getX()+" Y = "+componentSrc.getY());
            System.out.println("componentDes X = "+componentDes.getX()+" Y = "+componentDes.getY());
            System.out.println("X = "+portOutBlock.getLayoutX()+" Y = "+portOutBlock.getLayoutY());
            System.out.println("X = "+portInBlock.getLayoutX()+" Y = "+portInBlock.getLayoutY());

            this.connectorBlock = new Arrow(portOutBlock, portInBlock, portOut, portIn, componentSrc, componentDes);

            ComponentBlock componentBlockIn = (ComponentBlock) portInBlock.getParent();
            componentBlockIn.addConnectorArrow(this.connectorBlock);
            componentBlockIn.setOnMouseDragged(mouseEvent -> {
                anchorPane.getChildren().removeIf(node -> node.equals(this.connectorBlock));
                this.connectorBlock = new Arrow(portOutBlock, portInBlock, portOut, portIn, componentSrc, componentBlockIn.getComponent());
                anchorPane.getChildren().add(this.connectorBlock);
            });

            ComponentBlock componentBlockOut = (ComponentBlock) portOutBlock.getParent();
            componentBlockOut.addConnectorArrow(this.connectorBlock);
            componentBlockOut.setOnMouseDragged(mouseEvent -> {
                anchorPane.getChildren().removeIf(node -> node.equals(this.connectorBlock));
                this.connectorBlock = new Arrow(portOutBlock, portInBlock, portOut, portIn, componentBlockOut.getComponent(), componentDes);
                anchorPane.getChildren().add(this.connectorBlock);
            });

            anchorPane.getChildren().add(this.connectorBlock);

        }else {
            System.out.println("CANCEL");
        }
    }

    public void addMethod(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../views/newMethod.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        NewMethodController controller = fxmlLoader.getController();
        ChoiceBox<String> componentChoice = controller.getComponent();
        model = Model.getInstance();
        String currentConfName = tabPane.getSelectionModel().getSelectedItem().getText();
        Configuration currentConf = model.configurations
                .stream()
                .filter(configuration -> configuration.getName().equals(currentConfName)).toList().get(0);
        for (Component component:
                currentConf.getComponents()) {
            componentChoice.getItems().add(component.getName());
        }

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("OK");
            String compName = componentChoice.getSelectionModel().getSelectedItem();
            Component component = null;
            try {
                component = currentConf.getCompByName(compName);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setContentText("All fields are required");
                alert.show();
                return;
            }
            if(controller.addMethod(component)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Method added successfully.");
                alert.show();
            }
        }else {
            System.out.println("CANCEL");
        }
    }

    public void addNonFunctionalConstraint(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../views/newConstraint.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        NewConstraintController controller = fxmlLoader.getController();
        TabPane tabPaneConstraint = controller.getTabPane();
        ChoiceBox<String> componentChoice = controller.getComponentChoice();
        ChoiceBox<String> connectorChoice = controller.getConnectorChoice();
        model = Model.getInstance();
        String currentConfName = tabPane.getSelectionModel().getSelectedItem().getText();
        Configuration currentConf = model.configurations
                .stream()
                .filter(configuration -> configuration.getName().equals(currentConfName)).toList().get(0);
        for (Component component:
                currentConf.getComponents()) {
            componentChoice.getItems().add(component.getName());
        }

        for (Connector connector:
                currentConf.getConnectors()) {
            connectorChoice.getItems().add(connector.getName());
        }

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("OK");
            if (tabPaneConstraint.getSelectionModel().getSelectedItem().getText().equals("Component")){
                String componentName = componentChoice.getSelectionModel().getSelectedItem();
                try {
                    Component component = currentConf.getCompByName(componentName);
                    this.addConstraintToComponent(controller, component);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            } else {
                String connectorName = connectorChoice.getSelectionModel().getSelectedItem();
                try {
                    Connector connector = currentConf.getConnectorByName(connectorName);
                    this.addConstraintToConnector(controller, connector);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Constraint added successfully.");
            alert.show();
        }else {
            System.out.println("CANCEL");
        }
    }

    private void addConstraintToComponent(NewConstraintController controller, Component component){
        controller.addComponentConstraint(component);
    }
    private void addConstraintToConnector(NewConstraintController controller, Connector connector){
        controller.addConnectorConstraint(connector);
    }
}
