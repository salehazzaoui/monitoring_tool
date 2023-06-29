package controllers;

import com.uppaal.model.core2.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import models.*;
import models.dynamic_part.*;
import models.static_part.*;
import org.w3c.dom.NodeList;
import utils.*;
import widgets.Arrow;
import widgets.ArrowDelegation;
import widgets.ComponentBlock;
import widgets.PortBlock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class IndexController implements Initializable {
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

    @FXML
    private MenuItem saveItem;

    @FXML
    private AnchorPane mainConfElement;

    private DraggableMaker draggableMaker;

    private TreeItem<String> rootItem;

    public Model model;

    private String DarkStylePath = "C:\\SALEH\\m2 il\\PFE_2023_MONITORING_TOOL\\src\\assets\\css\\style.css";
    private boolean openUppaal = true;

    private double mouseAnchorX;
    private double mouseAnchorY;

    private ArrayList<Arrow> connectorBlocks = new ArrayList<>();
    private Arrow connectorBlock;
    private int count = 0;
    private ArrayList<PortBlock> portOutBlocks = new ArrayList<>();
    private ArrayList<PortBlock> portInBlocks = new ArrayList<>();

    private StringBuilder globalDeclaration;
    private ArrayList<String> systemDeclaration;
    private ArrayList<Port> portsDone;
    private ArrayList<Component> componentsDone;
    private ArrayList<Location> initialLocations;
    private String locationName;
    private List<Location> locations;

    private int countReceive = 0, countSend = 0, y = 0;;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = Model.getInstance();
        draggableMaker = new DraggableMaker();
        rootItem = new TreeItem<>("Project");
        TreeItem<String> item = new TreeItem<>("Main Configuration");
        rootItem.getChildren().add(item);
        mTreeView.setRoot(rootItem);
        model.isDarkProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                File style = new File(DarkStylePath);
                try {
                    if(t1){
                        mainPane.getStylesheets().add(style.toURI().toURL().toExternalForm());
                        mainConfElement.setStyle("-fx-background-color: #3E3E40;");
                    }else {
                        mainPane.getStylesheets().remove(style.toURI().toURL().toExternalForm());
                        mainConfElement.setStyle("-fx-background-color: white;");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });
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
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_LEFT);
            Button closeButton = new Button("x");
            closeButton.setStyle(
                    "-fx-background-color: red;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 10; " +
                    "-fx-min-width: 15; " +
                    "-fx-min-height: 15; " +
                    "-fx-max-width: 15; " +
                    "-fx-max-height: 15;" +
                    "-fx-font-size: 8px;" +
                    "-fx-padding: 0;"
            );
            closeButton.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Remove Configuration");
                alert.setContentText("Are you sure you want to remove this configuration?");

                ButtonType buttonTypeYes = new ButtonType("Yes");
                ButtonType buttonTypeNo = new ButtonType("No");

                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                alert.showAndWait().ifPresent(res -> {
                    if (res == buttonTypeYes) {
                        model.configurations.removeIf(conf -> conf.getName().equals(tab.getText()));
                        rootItem.getChildren().removeIf(it -> it.getValue().equals(tab.getText()));
                        TabPane tabPane = tab.getTabPane();
                        tabPane.getTabs().remove(tab);
                    }
                });
            });
            hbox.getChildren().addAll(closeButton);
            tab.setGraphic(hbox);

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            AnchorPane anchorPane = new AnchorPane();
            model.isDarkProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    if(t1){
                        anchorPane.setStyle("-fx-background-color: #3E3E40;");
                    }else {
                        anchorPane.setStyle("-fx-background-color: white;");
                    }
                }
            });
            anchorPane.setPrefWidth(scrollPane.getPrefWidth());
            anchorPane.setPrefHeight(scrollPane.getPrefHeight());
            scrollPane.setContent(anchorPane);

            Configuration mainConfiguration =  model.configurations.get(0);
            ArrayList<Component> components = mainConfiguration.getComponents();
            for (Component component : components) {
                Component component1 = new Component(component.getName(), component.getMemoryConsummation(), configuration);
                component1.setX(component.getX());
                component1.setY(component.getY());
                component1.setGlobalCsp(component.getGlobalCsp());
                component1.setConstraint(component.getConstraint());
                component1.getMethods().addAll(component.getMethods());
                ArrayList<Component> components1 = configuration.getComponents();
                components1.add(component1);
                ComponentBlock componentBlock = new ComponentBlock(component1);
                anchorPane.getChildren().add(componentBlock);
                for (Port port : component.getPorts()) {
                    Port port1 = new Port(port.getName(), component1);
                    port1.setType(port.getType());
                    port1.setCspExpression(port.getCspExpression());
                    port1.setX(port.getX());
                    port1.setY(port.getY());
                    component1.ports.add(port);

                    PortBlock portBlock = new PortBlock(port1.getName().charAt(0)+""+String.valueOf(count++), port1);
                    portBlock.setId(component1.getName()+"-"+port1.getName());
                    componentBlock.getChildren().add(portBlock);
                    portBlock.setLayoutX(port1.getX());
                    portBlock.setLayoutY(port1.getY());

                }
            }

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

    public void addNewComponentComposite(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../views/newComponentComposite.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        NewComponentController controller = fxmlLoader.getController();
        ListView<String> componentChoice = controller.getChildrenComp();
        componentChoice.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        model = Model.getInstance();
        String currentConfName = tabPane.getSelectionModel().getSelectedItem().getText();
        Configuration currentConf = model.configurations
                .stream()
                .filter(configuration -> configuration.getName().equals(currentConfName)).toList().get(0);
        currentConf.getComponents().forEach(component -> {
            componentChoice.getItems().add(component.getName());
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("Ok");
            Tab tap = tabPane.getTabs().stream().filter(tab -> tab.getText().trim().equals(currentConfName.trim())).toList().get(0);
            ScrollPane scrollPane = (ScrollPane) tap.getContent();
            AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();
            List<Component> components = new ArrayList<>();
            componentChoice.getSelectionModel().getSelectedItems().forEach(item -> {
                try {
                    components.add(currentConf.getCompByName(item.trim()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            int x = components.stream().mapToInt(Component::getRoundedX).min().getAsInt() - 50;
            int y = components.stream().mapToInt(Component::getRoundedY).min().getAsInt() - 50 ;
            int width = components.stream().mapToInt(Component::getRoundedX).max().getAsInt() - x + 180;
            int height = components.stream().mapToInt(Component::getRoundedY).max().getAsInt() - y + 160;
            System.out.println("x = "+x+", y = "+y+", width = "+width+", height = "+height);
            // Build the Component element
            Component component =  controller.addCompositeComponent(components, currentConf);
            component.setX(x);
            component.setY(y);
            component.setWidth(width);
            component.setHeight(height);
            component.setColor("-fx-background-color: #ffeb9e;");
            component.addAllToComponents(components);
            ComponentBlock componentBox = new ComponentBlock(component);

            // connector
            components.forEach(component1 -> {
                ArrayList<Arrow> arrows = new ArrayList<>();
                component1.getComponentBlock().getConnectorsArrow().forEach(arrow -> {
                    if(!components.contains(arrow.getComponentDes()) || !components.contains(arrow.getComponentSrc())){
                        currentConf.connectors.remove(arrow.getConnector());
                        anchorPane.getChildren().remove(arrow);
                    }else {
                        arrows.add(arrow);
                    }
                });
                component1.getComponentBlock().setConnectorsArrow(arrows);
            });


            // Add component element
            anchorPane.getChildren().add(componentBox);

            components.forEach(component1 -> {
                ComponentBlock componentBlock = component1.getComponentBlock();
                componentBlock.setOnMouseDragged(ActionEvent -> {});
                componentBlock.setLayoutX(componentBlock.getLayoutX() - componentBox.getLayoutX());
                componentBlock.setLayoutY(componentBlock.getLayoutY() - componentBox.getLayoutY());
                component1.setX(componentBlock.getLayoutX());
                component1.setY(componentBlock.getLayoutY());

                componentBlock.getConnectorsArrow().forEach(arrow -> {
                    Component componentSrc = arrow.getComponentSrc();
                    Component componentDes = arrow.getComponentDes();

                    double startX = componentSrc.getX() + arrow.getPortBlockOut().getLayoutX() + arrow.getPortBlockOut().getWidth() / 2;
                    double startY = componentSrc.getY() + arrow.getPortBlockOut().getLayoutY() + arrow.getPortBlockOut().getHeight() / 2;
                    double endX = componentDes.getX() + arrow.getPortBlockIn().getLayoutX() + arrow.getPortBlockIn().getWidth() / 2;
                    double endY = componentDes.getY() + arrow.getPortBlockIn().getLayoutY() + arrow.getPortBlockIn().getHeight() / 2;

                    arrow.setStartX(startX);
                    arrow.setStartY(startY);
                    arrow.setEndX(endX);
                    arrow.setEndY(endY);
                    if (!componentBox.getChildren().contains(arrow)){
                        componentBox.getChildren().add(arrow);
                    }
                });

                componentBox.getChildren().add(componentBlock);
            });

        }else {
            System.out.println("CANCEL");
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

        portInChoice.setOnAction(ActionEvent -> {
            Port portIn =  portsIn.stream().filter(port -> port.getName().equals(portInChoice.getValue())).toList().get(0);
            controller.initCspInField(portIn.getCspExpression().getExpression());
        });

        portOutChoice.setOnAction(ActionEvent -> {
            Port portOut =  portsOut.stream().filter(port -> port.getName().equals(portOutChoice.getValue())).toList().get(0);
            controller.initCspOutField(portOut.getCspExpression().getExpression());
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
            portIn.setConnector(connector);
            portOut.setConnector(connector);

            ScrollPane scrollPane = (ScrollPane) tabPane.getSelectionModel().getSelectedItem().getContent();
            AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();

            PortBlock portOutBlock = (PortBlock) mainPane.lookup("#"+srcComp+"-"+portOutName);
            this.portOutBlocks.add(portOutBlock);
            PortBlock portInBlock = (PortBlock) mainPane.lookup("#"+desComp+"-"+portInName);
            this.portInBlocks.add(portInBlock);

            this.connectorBlock = new Arrow(portOutBlock, portInBlock, portOut, portIn, componentSrc, componentDes);

            ComponentBlock componentBlockIn = (ComponentBlock) portInBlock.getParent();
            componentBlockIn.addConnectorArrow(this.connectorBlock);

            ComponentBlock componentBlockOut = (ComponentBlock) portOutBlock.getParent();
            componentBlockOut.addConnectorArrow(this.connectorBlock);

            this.connectorBlock.setConnector(connector);
            anchorPane.getChildren().add(this.connectorBlock);

        }else {
            System.out.println("CANCEL");
        }
    }

    public void addNewConnectorDelegation(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../views/newConnectorDelegation.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        NewConnectorController controller = fxmlLoader.getController();
        ChoiceBox<String> compositeChoice = controller.getCompositeChoice();
        ChoiceBox<String> comportsChoice = controller.getComportsChoice();
        ChoiceBox<String> delegatedPortChoice = controller.getDelegatedPortChoice();

        model = Model.getInstance();
        String currentConfName = tabPane.getSelectionModel().getSelectedItem().getText();
        Configuration currentConf = model.configurations
                .stream()
                .filter(configuration -> configuration.getName().equals(currentConfName)).toList().get(0);

        List<Component> compositeComponents = currentConf.getComponents().stream().filter(component -> component.getComponents().size() > 0 ).toList();
        compositeComponents.forEach(component -> {
            compositeChoice.getItems().add(component.getName());
        });

        compositeChoice.setOnAction(ActionEvent -> {
            Component component =  compositeComponents.stream().filter(component1 -> component1.getName().equals(compositeChoice.getValue())).toList().get(0);
            component.getPorts().forEach(port -> {
                comportsChoice.getItems().add(port.getName());
            });
            ArrayList<Component> children = component.getComponents();
            children.forEach(child -> {
                child.getPorts().forEach(port -> {
                    delegatedPortChoice.getItems().add(port.getName());
                });
            });
        });

        comportsChoice.setOnAction(ActionEvent -> {
            Component component =  compositeComponents.stream().filter(component1 -> component1.getName().equals(compositeChoice.getValue())).toList().get(0);
            Port port =  component.getPorts().stream().filter(port1 -> port1.getName().equals(comportsChoice.getValue())).toList().get(0);
            controller.initCspCompPField(port.getCspExpression().getExpression());
        });

        delegatedPortChoice.setOnAction(ActionEvent -> {
            Component component =  compositeComponents.stream().filter(component1 -> component1.getName().equals(compositeChoice.getValue())).toList().get(0);
            ArrayList<Port> ports = new ArrayList<>();
            component.getComponents().forEach(component1 -> {
                ports.addAll(component1.getPorts());
            });
            Port port =  ports.stream().filter(port1 -> port1.getName().equals(delegatedPortChoice.getValue())).toList().get(0);
            controller.initCspDelegationPField(port.getCspExpression().getExpression());
        });


        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("OK");
            String srcCompName = compositeChoice.getSelectionModel().getSelectedItem();
            String portCompositeName = comportsChoice.getSelectionModel().getSelectedItem();
            String portDelegatedName = delegatedPortChoice.getSelectionModel().getSelectedItem();

            try {
                Component compositeComponent =  compositeComponents.stream().filter(component1 -> component1.getName().equals(compositeChoice.getValue())).toList().get(0);
                ArrayList<Port> ports = new ArrayList<>();
                compositeComponent.getComponents().forEach(component1 -> {
                    ports.addAll(component1.getPorts());
                });
                Port portComposite =  compositeComponent.getPorts().stream().filter(port -> port.getName().equals(portCompositeName)).toList().get(0);
                Port portDelegation =  ports.stream().filter(port -> port.getName().equals(portDelegatedName)).toList().get(0);

                Component componentSrc = currentConf.getCompByName(srcCompName);
                Component componentDes = portDelegation.getComponent();

                Connector connector =  controller.addDelegationConnector(portComposite, portDelegation);
                if (connector == null) return;
                currentConf.connectors.add(connector);
                portComposite.setConnector(connector);
                portDelegation.setConnector(connector);

                ScrollPane scrollPane = (ScrollPane) tabPane.getSelectionModel().getSelectedItem().getContent();
                AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();

                PortBlock portOutBlock = (PortBlock) mainPane.lookup("#"+srcCompName+"-"+portCompositeName);
                this.portOutBlocks.add(portOutBlock);
                PortBlock portInBlock = (PortBlock) mainPane.lookup("#"+componentDes.getName()+"-"+portDelegatedName);
                this.portInBlocks.add(portInBlock);

                ArrowDelegation arrow = new ArrowDelegation(portOutBlock, portInBlock, portComposite, portDelegation, componentSrc, componentDes);

                ComponentBlock componentBlockIn = (ComponentBlock) portInBlock.getParent();
                //componentBlockIn.addConnectorArrow(this.connectorBlock);

                ComponentBlock componentBlockOut = (ComponentBlock) portOutBlock.getParent();
                //componentBlockOut.addConnectorArrow(this.connectorBlock);

                //this.connectorBlock.setConnector(connector);
                arrow.setConnector(connector);
                compositeComponent.getComponentBlock().getChildren().add(arrow);
                //anchorPane.getChildren().add(this.connectorBlock);
            } catch (Exception e) {
                e.printStackTrace();
            }

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
        ObservableList<Method> methods = FXCollections.observableArrayList();
        currentConf.getComponents().forEach(component -> {
            methods.addAll(component.getMethods());
        });

        controller.getNameCol().setCellValueFactory(new PropertyValueFactory<Method, String>("name"));
        controller.getTimeCol().setCellValueFactory(new PropertyValueFactory<Method, String>("executionTime"));
        controller.getComponentCol().setCellValueFactory(new PropertyValueFactory<Method, String>("componentName"));

        controller.getTable().setItems(methods);

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
        componentChoice.setOnAction(ActionEvent -> {
            String componentName = componentChoice.getSelectionModel().getSelectedItem();
            try {
                Component component = currentConf.getCompByName(componentName);
                if(component.getConstraint() != null){
                    controller.setCompTimeField(String.valueOf(component.getConstraint().getTime()));
                    controller.setCompMemoryField(String.valueOf(component.getConstraint().getMemory()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        connectorChoice.setOnAction(ActionEvent -> {
            String connectorName = connectorChoice.getSelectionModel().getSelectedItem();
            try {
                Connector connector = currentConf.getConnectorByName(connectorName);
                if(connector.getConstraint() != null){
                    controller.setConcTimeField(String.valueOf(connector.getConstraint().getTime()));
                    controller.setConcBandField(String.valueOf(connector.getConstraint().getBandwidth()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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

    public void saveFile(ActionEvent actionEvent) {
        model = Model.getInstance();
        String currentConfName = tabPane.getSelectionModel().getSelectedItem().getText();
        Configuration currentConf = model.configurations
                .stream()
                .filter(configuration -> configuration.getName().equals(currentConfName)).toList().get(0);
        //Creating a File chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        //Adding action on the menu item
        File file = fileChooser.showSaveDialog(mainPane.getScene().getWindow());
        if(file != null){
            this.saveContentToFile(currentConf, file);
        }
    }

    public boolean checkStructural(ActionEvent actionEvent) {
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

        String isValidXML = XMLValidator.validateXMLSchema("model.xsd", "architecture.xml");
        if (isValidXML.equals("valid")){
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
            message.append("\n");
            message.append("Error : "+isValidXML);
            resTextArea.setText(String.valueOf(message));
            resTextArea.setStyle("-fx-text-fill: red;");
        }
        return isValidXML.equals("valid");
    }

    /**
     * TODO: Not completed yet
     * */
    public void checkBehavioral(ActionEvent actionEvent) {
        boolean isValidStruct = this.checkStructural(actionEvent);
        if (!isValidStruct){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("The structure is not valid.");
            alert.show();
            return;
        }
        Document doc = new Document(new PrototypeDocument());
        this.y = 0;
        systemDeclaration = new ArrayList<>();
        globalDeclaration = new StringBuilder();
        globalDeclaration.append("int band = 0, consummation = 0;\n\nclock t;\n");
        // get current configuration
        model = Model.getInstance();
        String currentConfName = tabPane.getSelectionModel().getSelectedItem().getText();
        Configuration currentConf = model.configurations
                .stream()
                .filter(configuration -> configuration.getName().equals(currentConfName)).toList().get(0);

        if(currentConf.getComponents().size() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No component!");
            alert.show();
            return;
        }

        ArrayList<Component> components = currentConf.getComponents();
        for (Component component: components) {
            if(component.getGlobalCsp() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No global CSP on "+ component.getName() +" component!");
                alert.show();
                return;
            }
            if(component.getConstraint() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No constraint on "+ component.getName() +" component!");
                alert.show();
                return;
            }
            if(component.getMethods().size() == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No methods on "+ component.getName() +" component!");
                alert.show();
                return;
            }
            BinaryTree<String> root = new BinaryTree<>(null);
            String expression = component.getGlobalCsp().getExpression();
            BinaryTree<String> componentTree = Automata.getComplexTree(root, expression);
            componentTree.printTree();
            Template template = doc.createTemplate();
            template.setProperty("name", component.getName());
            systemDeclaration.add(component.getName());
            doc.insert(template, null);
            count = 0;
            Location l0 = Uppaal.addLocation(template, null, null, this.y, 0);
            this.y = y+250;
            l0.setProperty("init", true);
            locations = new ArrayList<>();
            buildUppaalAutomata(componentTree, template, l0, component);
            this.y = 0;
        }

        List<Connector> connectors = currentConf.getConnectors();
        for (Connector connector: connectors) {
            if(connector.getConnectorCsp() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No CSP expression on "+ connector.getName() +" connector!");
                alert.show();
                return;
            }
            if(connector.getConstraint() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No constraint on "+ connector.getName() +" connector!");
                alert.show();
                return;
            }
            BinaryTree<String> root = new BinaryTree<>(null);
            String expression = connector.getConnectorCsp().getExpression();
            BinaryTree<String> componentTree = Automata.getComplexTree(root, expression);
            componentTree.printTree();
            Template template = doc.createTemplate();
            template.setProperty("name", connector.getName());
            systemDeclaration.add(connector.getName());
            doc.insert(template, null);
            count = 0;
            Location l0 = Uppaal.addLocation(template, null, null, this.y, 0);
            this.y = y+250;
            l0.setProperty("init", true);
            locations = new ArrayList<>();
            buildUppaalAutomata(componentTree, template, l0, connector);
            this.y = 0;
        }

        // add global variables:
        doc.setProperty("declaration", String.valueOf(globalDeclaration));
        // add system variables:
        StringBuilder sv = new StringBuilder();
        int c = 0;
        for (String var:
                systemDeclaration) {
            sv.append(var+""+c+"="+var+"();\n");
            c++;
        }
        sv.append("system ");
        c = 0;
        for (int j = 0, systemDeclarationSize = systemDeclaration.size() - 1; j < systemDeclarationSize; j++) {
            String var = systemDeclaration.get(j);
            sv.append(var+""+c+ ",");
            c++;
        }
        sv.append(systemDeclaration.get(systemDeclaration.size() - 1)+""+c+";");
        doc.setProperty("system", String.valueOf(sv));

        int occurrence = components.size() * connectors.size();
        UppaalRunner uppaalRunner = new UppaalRunner(doc, occurrence);
        Thread thread = new Thread(uppaalRunner);
        thread.start();

        try {
            doc.save("uppaal.xml");
            if(this.openUppaal){
                String uppaalPath = EV.uppaalPath;
                String modelPath = EV.modelPath;
                String command = "\""+uppaalPath+"\" \""+new File(modelPath).getAbsolutePath()+"\"";
                Process process = Runtime.getRuntime().exec(command);
            }
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    private void buildUppaalAutomata(BinaryTree<String> tree, Template template, Location l0, ArchitecturalElement element) {
        locations.add(l0);
        if (element instanceof Component){
            if(tree.getLeft() != null){
                String left = tree.getLeft().getNode();
                Uppaal.setLabel(l0, Uppaal.LKind.name, "L"+this.count, l0.getX()-7, l0.getY()+15);
                count++;
                if(tree.getNode().equals("->")){
                    if(tree.getRight() == null) return;
                    if(template.getPropertyValue("name").equals(tree.getRight().getNode())){
                        Location locationInit = locations.stream().filter(location -> location.getPropertyValue("init")).toList().get(0);
                        if (locations.size() > 1){
                            Edge e = left.contains("?") ? Uppaal.addEdge(template, l0, locationInit, "t <"+((Component) element).getConstraint().getTime(), left.split("[!\\\\?]")[1]+"?", "t = "+((Component) element).getSumTime()+", consummation = consummation-"+(locations.size() - 1) * ((Component) element).getMemoryConsummation())
                                    : Uppaal.addEdge(template, l0, locationInit, "t <"+((Component) element).getConstraint().getTime(), left.split("[!\\\\?]")[1]+"!", "t = "+((Component) element).getSumTime()+", consummation = consummation-"+(locations.size() - 1) * ((Component) element).getMemoryConsummation());
                            Nail nail = e.createNail();
                            nail.setProperty("x", 102);
                            nail.setProperty("y", 34);
                            e.insert(nail, null);
                        }else {
                            /*Edge e = left.contains("?") ? Uppaal.addEdge(template, l0, locationInit, "t <"+((Component) element).getConstraint().getTime(), left.split("[!\\\\?]")[1]+"?", "t = "+((Component) element).getSumTime())
                                    : Uppaal.addEdge(template, l0, locationInit, "t <"+((Component) element).getConstraint().getTime(), left.split("[!\\\\?]")[1]+"!", "t = "+((Component) element).getSumTime());*/
                            Edge e = left.contains("?") ? Uppaal.addEdge(template, l0, locationInit, null, left.split("[!\\\\?]")[1]+"?", null)
                                    : Uppaal.addEdge(template, l0, locationInit, null, left.split("[!\\\\?]")[1]+"!", null);
                            Nail nail = e.createNail();
                            nail.setProperty("x", 102);
                            nail.setProperty("y", 34);
                            e.insert(nail, null);
                            Nail nail1 = e.createNail();
                            nail1.setProperty("x", 80);
                            nail1.setProperty("y", 15);
                            e.insert(nail1, null);
                        }

                        if (!globalDeclaration.toString().contains("chan " + left.split("[!\\\\?]")[1])){
                            globalDeclaration.append("chan " + left.split("[!\\\\?]")[1] + ";\n");
                        }
                    }else if(!tree.getRight().getNode().equals("STOP") || !tree.getRight().getNode().equals("SKIP")) {
                        Location l1 = Uppaal.addLocation(template, null, null, this.y, 0);
                        Edge e = left.contains("?") ? Uppaal.addEdge(template, l0, l1, "consummation <"+((Component) element).getConstraint().getMemory(), left.split("[!\\\\?]")[1]+"?", "t = "+((Component) element).getSumTime()+", consummation = consummation+"+((Component) element).getMemoryConsummation())
                                : Uppaal.addEdge(template, l0, l1, "consummation <"+((Component) element).getConstraint().getMemory(), left.split("[!\\\\?]")[1]+"!", "t = "+((Component) element).getSumTime()+", consummation = consummation+"+((Component) element).getMemoryConsummation());
                        this.y = y+250;
                        if (!globalDeclaration.toString().contains("chan " + left.split("[!\\\\?]")[1])){
                            globalDeclaration.append("chan " + left.split("[!\\\\?]")[1] + ";\n");
                        }
                        if (tree.getRight() != null){
                            buildUppaalAutomata(tree.getRight(), template, l1, element);
                        }
                    }
                }else if(tree.getNode().equals("|||")){
                    //
                }else if(tree.getNode().equals("[]")){
                    if (tree.getRight() != null){
                        if(Automata.isOperator(tree.getRight().getNode())){
                            buildUppaalAutomata(tree.getRight(), template, l0, element);
                        }
                    }
                    if (tree.getLeft() != null) {
                        if (Automata.isOperator(tree.getLeft().getNode())) {
                            buildUppaalAutomata(tree.getLeft(), template, l0, element);
                        }
                    }
                }
            }
        }else if (element instanceof Connector){
            if(tree.getLeft() != null){
                String left = tree.getLeft().getNode();
                Uppaal.setLabel(l0, Uppaal.LKind.name, "L"+this.count, l0.getX()-7, l0.getY()+15);
                count++;
                if(tree.getNode().equals("->")){
                    if(tree.getRight() == null) return;
                    if(template.getPropertyValue("name").equals(tree.getRight().getNode())){
                        Location locationInit = locations.stream().filter(location -> location.getPropertyValue("init")).toList().get(0);
                        Edge e = left.contains("?") ? Uppaal.addEdge(template, l0, locationInit, "band <"+((Connector) element).getConstraint().getBandwidth(), left.split("[!\\\\?]")[1]+"?", "band = band+"+((Connector) element).getBandwidth())
                                : Uppaal.addEdge(template, l0, locationInit, "band <"+((Connector) element).getConstraint().getBandwidth(), left.split("[!\\\\?]")[1]+"!", "band = band - "+((Connector) element).getBandwidth());
                        Nail nail = e.createNail();
                        nail.setProperty("x", 102);
                        nail.setProperty("y", 34);
                        e.insert(nail, null);
                        if(locations.size() == 1){
                            Nail nail1 = e.createNail();
                            nail1.setProperty("x", 80);
                            nail1.setProperty("y", 15);
                            e.insert(nail1, null);
                        }
                        if (!globalDeclaration.toString().contains("chan " + left.split("[!\\\\?]")[1])){
                            globalDeclaration.append("chan " + left.split("[!\\\\?]")[1] + ";\n");
                        }
                    }else if(!tree.getRight().getNode().equals("STOP") || !tree.getRight().getNode().equals("SKIP")) {
                        Location l1 = Uppaal.addLocation(template, null, null, this.y, 0);
                        Edge e = left.contains("?") ? Uppaal.addEdge(template, l0, l1, "band <"+((Connector) element).getConstraint().getBandwidth(), left.split("[!\\\\?]")[1]+"?", "band = band+"+((Connector) element).getBandwidth())
                                : Uppaal.addEdge(template, l0, l1, "band <"+((Connector) element).getConstraint().getBandwidth(), left.split("[!\\\\?]")[1]+"!", "band = band+"+((Connector) element).getBandwidth());
                        this.y = y+250;
                        if (!globalDeclaration.toString().contains("chan " + left.split("[!\\\\?]")[1])){
                            globalDeclaration.append("chan " + left.split("[!\\\\?]")[1] + ";\n");
                        }
                        if (tree.getRight() != null){
                            buildUppaalAutomata(tree.getRight(), template, l1, element);
                        }
                    }
                }else if(tree.getNode().equals("|||")){
                    //
                }else if(tree.getNode().equals("[]")){
                    if (tree.getRight() != null){
                        if(Automata.isOperator(tree.getRight().getNode())){
                            buildUppaalAutomata(tree.getRight(), template, l0, element);
                        }
                    }
                    if (tree.getLeft() != null) {
                        if (Automata.isOperator(tree.getLeft().getNode())) {
                            buildUppaalAutomata(tree.getLeft(), template, l0, element);
                        }
                    }
                }
            }
        }
    }

    private void saveContentToFile(Configuration configuration, File file) {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(file));

            br.write("<Configuration name=\""+configuration.getName()+"\">"); br.newLine();
            br.write("<Components>"); br.newLine();
            for (Component component:
                    configuration.getComponents()) {
                br.write("<Component memory=\""+component.getMemoryConsummation()+"\" x=\""+component.getX()+"\"  y=\""+component.getY()+"\">"); br.newLine();
                br.write("<name>"+ component.getName() +"</name>"); br.newLine();
                br.write("<csp>"+ component.getGlobalCsp().getExpression() +"</csp>"); br.newLine();
                br.write("<Methods>"); br.newLine();
                for (Method method : component.getMethods()){
                    br.write("<Method time=\""+method.getExecutionTime()+"\">"); br.newLine();
                    br.write("<name>"+ method.getName() +"</name>"); br.newLine();
                    br.write("</Method>"); br.newLine();
                }
                br.write("</Methods>"); br.newLine();
                br.write("<Ports>"); br.newLine();
                for (Port port:
                        component.getPorts()) {
                    br.write("<Port x=\""+port.getX()+"\" y=\""+port.getY()+"\">"); br.newLine();
                    br.write("<name>"+ port.getName() +"</name>"); br.newLine();
                    br.write("<type>"+ port.getType() +"</type>"); br.newLine();
                    br.write("<csp>"+ port.getCspExpression().getExpression() +"</csp>"); br.newLine();
                    br.write("</Port>"); br.newLine();
                }
                br.write("</Ports>"); br.newLine();
                if(component.getConstraint() != null){
                    br.write("<Constraint>"); br.newLine();
                    br.write("<time>"+ component.getConstraint().getTime() +"</time>"); br.newLine();
                    br.write("<memory>"+ component.getConstraint().getMemory() +"</memory>"); br.newLine();
                    br.write("</Constraint>"); br.newLine();
                }
                br.write("</Component>"); br.newLine();
            }
            br.write("</Components>"); br.newLine();
            br.write("<Connectors>"); br.newLine();
            for (Connector connector:
                    configuration.getConnectors()) {
                br.write("<Connector band=\""+connector.getBandwidth()+"\">"); br.newLine();
                br.write("<name>"+ connector.getName() +"</name>"); br.newLine();
                br.write("<csp>"+ connector.getConnectorCsp().getExpression() +"</csp>"); br.newLine();
                br.write("<Port_In>"); br.newLine();
                br.write("<name>"+ connector.getPortIn().getName() +"</name>"); br.newLine();
                br.write("<type>"+ connector.getPortIn().getType() +"</type>"); br.newLine();
                br.write("</Port_In>"); br.newLine();
                br.write("<Port_Out>"); br.newLine();
                br.write("<name>"+ connector.getPortOut().getName() +"</name>"); br.newLine();
                br.write("<type>"+ connector.getPortOut().getType() +"</type>"); br.newLine();
                br.write("</Port_Out>"); br.newLine();
                if(connector.getConstraint() != null){
                    br.write("<Constraint>"); br.newLine();
                    br.write("<time>"+ connector.getConstraint().getTime() +"</time>"); br.newLine();
                    br.write("<band>"+ connector.getConstraint().getBandwidth() +"</band>"); br.newLine();
                    br.write("</Constraint>"); br.newLine();
                }
                br.write("</Connector>"); br.newLine();
            }
            br.write("</Connectors>"); br.newLine();
            br.write("</Configuration>"); br.newLine();

            br.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void openFile(ActionEvent actionEvent) {
        model = Model.getInstance();
        String currentConfName = tabPane.getSelectionModel().getSelectedItem().getText();
        Configuration currentConf = model.configurations
                .stream()
                .filter(configuration -> configuration.getName().equals(currentConfName)).toList().get(0);
        //Creating a File chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        //Adding action on the menu item
        File file = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        if(file != null){
            this.loadContentFromFile(currentConf, file);
        }
    }

    private void loadContentFromFile(Configuration currentConf, File file){
        ScrollPane scrollPane = (ScrollPane) mainConf.getContent();
        AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(file);
            org.w3c.dom.Element configuration = doc.getDocumentElement();
            String configName = configuration.getAttribute("name");
            if(configName.equals(currentConf.getName())){
                org.w3c.dom.Element componentsElement = (org.w3c.dom.Element) configuration.getElementsByTagName("Components").item(0);
                NodeList componentList = componentsElement.getElementsByTagName("Component");
                List<Component> components = new ArrayList<>();
                for (int i = 0; i < componentList.getLength(); i++) {
                    org.w3c.dom.Element componentElement = (org.w3c.dom.Element) componentList.item(i);
                    int memory = Integer.parseInt(componentElement.getAttribute("memory"));
                    double x = Double.parseDouble(componentElement.getAttribute("x"));
                    double y = Double.parseDouble(componentElement.getAttribute("y"));
                    String name = componentElement.getElementsByTagName("name").item(0).getTextContent();
                    String globalCsp = componentElement.getElementsByTagName("csp").item(0).getTextContent();
                    Component component = new Component(name, memory, currentConf);
                    component.setX(x);
                    component.setY(y);
                    component.setGlobalCsp(new Csp(name, globalCsp));

                    ComponentBlock componentBlock = new ComponentBlock(component);
                    anchorPane.getChildren().add(componentBlock);

                    rootItem.getChildren().get(0).getChildren().add(new TreeItem<>(component.getName()));

                    org.w3c.dom.Element methodsElement = (org.w3c.dom.Element) componentElement.getElementsByTagName("Methods").item(0);
                    NodeList methodList = methodsElement.getElementsByTagName("Method");
                    for (int j = 0; j < methodList.getLength(); j++) {
                        org.w3c.dom.Element methodElement = (org.w3c.dom.Element) methodList.item(j);
                        int methodTime = Integer.parseInt(methodElement.getAttribute("time"));
                        String methodName = methodElement.getElementsByTagName("name").item(0).getTextContent();
                        Method method = new Method(methodName, methodTime, component.getName());
                        component.methods.add(method);
                    }

                    org.w3c.dom.Element portsElement = (org.w3c.dom.Element) componentElement.getElementsByTagName("Ports").item(0);
                    NodeList portList = portsElement.getElementsByTagName("Port");
                    int count = 0;
                    for (int k = 0; k < portList.getLength(); k++) {
                        org.w3c.dom.Element portElement = (org.w3c.dom.Element) portList.item(k);
                        String portName = portElement.getElementsByTagName("name").item(0).getTextContent();
                        String portType = portElement.getElementsByTagName("type").item(0).getTextContent();
                        String csp = portElement.getElementsByTagName("csp").item(0).getTextContent();
                        x = Double.parseDouble(portElement.getAttribute("x"));
                        y = Double.parseDouble(portElement.getAttribute("y"));
                        Port port = new Port(portName, component);
                        port.setType(TypePort.valueOf(portType));
                        port.setCspExpression(new Csp(portName, csp));
                        port.setX(x);
                        port.setY(y);
                        component.ports.add(port);

                        PortBlock portBlock = new PortBlock(portName.charAt(0)+""+String.valueOf(count++), port);
                        portBlock.setId(component.getName()+"-"+port.getName());
                        componentBlock.getChildren().add(portBlock);
                        portBlock.setLayoutX(port.getX());
                        portBlock.setLayoutY(port.getY());

                    }

                    NodeList constraintList = componentElement.getElementsByTagName("Constraint");
                    for (int j = 0; j < constraintList.getLength(); j++) {
                        org.w3c.dom.Element constraintElement = (org.w3c.dom.Element) constraintList.item(j);
                        int constraintTime = Integer.parseInt(constraintElement.getElementsByTagName("time").item(0).getTextContent());
                        int constraintMemory = Integer.parseInt(constraintElement.getElementsByTagName("memory").item(0).getTextContent());
                        ComponentConstraint constraint = new ComponentConstraint(constraintTime, constraintMemory);
                        component.setConstraint(constraint);
                    }
                    components.add(component);
                }
                currentConf.setComponents((ArrayList<Component>) components);

                org.w3c.dom.Element connectorsElement = (org.w3c.dom.Element) configuration.getElementsByTagName("Connectors").item(0);
                NodeList connectorList = connectorsElement.getElementsByTagName("Connector");
                for (int i = 0; i < connectorList.getLength(); i++) {
                    org.w3c.dom.Element connectorElement = (org.w3c.dom.Element) connectorList.item(i);
                    int band = Integer.parseInt(connectorElement.getAttribute("band"));
                    String connectorName = connectorElement.getElementsByTagName("name").item(0).getTextContent();
                    String connectorCsp = connectorElement.getElementsByTagName("csp").item(0).getTextContent();
                    NodeList portInList = connectorElement.getElementsByTagName("Port_In");
                    Port portIn = null;
                    for (int j = 0; j < portInList.getLength(); j++) {
                        org.w3c.dom.Element portInElement = (org.w3c.dom.Element) portInList.item(j);
                        String portInName = portInElement.getElementsByTagName("name").item(0).getTextContent();
                        for (Component component : components) {
                            ArrayList<Port> ports = component.ports;
                            portIn = ports.stream().filter(port -> port.getName().equals(portInName) && port.getType().equals(TypePort.IN)).toList().size() > 0 ?
                                     ports.stream().filter(port -> port.getName().equals(portInName) && port.getType().equals(TypePort.IN)).toList().get(0) : null;
                            if (portIn != null) break;
                        }
                    }

                    NodeList portOutList = connectorElement.getElementsByTagName("Port_Out");
                    Port portOut = null;
                    for (int j = 0; j < portOutList.getLength(); j++) {
                        org.w3c.dom.Element portOutElement = (org.w3c.dom.Element) portOutList.item(j);
                        String portOutName = portOutElement.getElementsByTagName("name").item(0).getTextContent();
                        for (Component component : components) {
                            portOut = component.ports.stream().filter(port -> port.getName().equals(portOutName) && port.getType().equals(TypePort.OUT)).toList().size() > 0 ?
                                    component.ports.stream().filter(port -> port.getName().equals(portOutName) && port.getType().equals(TypePort.OUT)).toList().get(0) : null;
                            if (portOut != null) break;
                        }
                    }
                    Connector connector = new Connector(connectorName, portIn, portOut, band);
                    connector.setConnectorCsp(new Csp(connectorName, connectorCsp));
                    PortBlock portInBlock = portIn.getPortBlock();
                    PortBlock portOutBlock = portOut.getPortBlock();
                    assert portOut != null;
                    assert portIn != null;
                    Arrow arrow = new Arrow(portOutBlock,
                                            portInBlock,
                                            portOut,
                                            portIn,
                                            portOut.getComponent(),
                                            portIn.getComponent());
                    ComponentBlock componentBlockIn = (ComponentBlock) portInBlock.getParent();
                    componentBlockIn.addConnectorArrow(arrow);
                    ComponentBlock componentBlockOut = (ComponentBlock) portOutBlock.getParent();
                    componentBlockOut.addConnectorArrow(arrow);
                    arrow.setConnector(connector);
                    anchorPane.getChildren().add(arrow);

                    NodeList constraintList = connectorElement.getElementsByTagName("Constraint");
                    for (int j = 0; j < constraintList.getLength(); j++) {
                        org.w3c.dom.Element constraintElement = (org.w3c.dom.Element) constraintList.item(j);
                        int constraintTime = Integer.parseInt(constraintElement.getElementsByTagName("time").item(0).getTextContent());
                        int constraintBand = Integer.parseInt(constraintElement.getElementsByTagName("band").item(0).getTextContent());
                        ConnectorConstraint constraint = new ConnectorConstraint(constraintTime, constraintBand);
                        connector.setConstraint(constraint);
                    }

                    currentConf.connectors.add(connector);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runAutomata(ActionEvent actionEvent) {
        model = Model.getInstance();
        String currentConfName = tabPane.getSelectionModel().getSelectedItem().getText();
        Configuration currentConf = model.configurations
                .stream()
                .filter(configuration -> configuration.getName().equals(currentConfName)).toList().get(0);
        UppaalTrace trace = new UppaalTrace(resTextArea);
        Monitor monitor = new Monitor(resTextArea, currentConf);
        Thread thread1 = new Thread(trace);
        Thread thread2 = new Thread(monitor);

        this.openUppaal = false;
        this.checkBehavioral(actionEvent);
        this.openUppaal = true;
        resTextArea.clear();
        resTextArea.setStyle("-fx-text-fill: black;");
        System.out.println("Start Monitoring---------------------------------------------------------------------------");
        thread1.start();
        thread2.start();
    }

    public void setDarkTheme(ActionEvent actionEvent) {
        model.setDark(true);
    }

    public void setLightTheme(ActionEvent actionEvent) {
        model.setDark(false);
    }
}
