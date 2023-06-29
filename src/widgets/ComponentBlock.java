package widgets;

import controllers.AddNewPort;
import controllers.NewComponentController;
import controllers.NewGlobalCspController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import models.*;
import models.dynamic_part.Csp;
import models.static_part.Component;
import models.static_part.Configuration;
import models.static_part.Port;
import utils.TypePort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComponentBlock extends Pane implements EventHandler<MouseEvent> {
    private Component component;
    private Label header = new Label();
    private Label nameC = new Label();
    private ArrayList<Arrow> connectorsArrow = new ArrayList<>();

    private double POS_Y_RIGHT = 0;
    private double POS_Y_LEFT = 0;
    private double POS_X_TOP = 0;
    private double POS_X_BOTTOM = 0;
    private int count = 0;

    private double mouseAnchorX;
    private double mouseAnchorY;

    private ContextMenu contextMenu;

    public ComponentBlock(Component component){
        this.component = component;
        component.setComponentBlock(this);
        this.setPrefWidth(component.getWidth());
        this.setPrefHeight(component.getHeight());
        this.setStyle(component.getColor());
        this.setBorder(Border.stroke(Color.BLACK));
        this.getChildren().add(this.updateHeader("<< Component >>"));
        this.updateName(component.getName());
        this.setLayoutX(component.getX());
        this.setLayoutY(component.getY());
        this.setOnMouseClicked((mouseEvent) -> handle(mouseEvent));

        this.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });
        this.setOnMouseDragged(mouseEvent -> {
            this.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
            this.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
            this.component.setX(this.getLayoutX());
            this.component.setY(this.getLayoutY());
            this.getConnectorsArrow().forEach(arrow -> {
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

            });
        });

        this.contextMenu = new ContextMenu();

        // to delete this
        MenuItem delete = new MenuItem("delete");
        delete.setOnAction(ActionEvent -> {
            Configuration configuration = component.getConfiguration();
            AnchorPane anchorPane = (AnchorPane) this.getParent();
            for (int i = 0; i < this.connectorsArrow.size(); i++){
                configuration.getConnectors().remove(this.connectorsArrow.get(i).getConnector());
                anchorPane.getChildren().remove(this.connectorsArrow.get(i));
            }
            configuration.removeComponent(component);
            anchorPane.getChildren().remove(this);
        });

        MenuItem modify = new MenuItem("Modify");
        modify.setOnAction(ActionEvent -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            //dialog.initOwner(mainPane.getScene().getWindow());
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
            Model model = Model.getInstance();
            model.configurations.forEach(configuration -> {
                configurationsChoice.getItems().add(configuration.getName());
            });
            controller.initNameComp(this.component.getName());
            controller.initMemoryConsComp(String.valueOf(this.component.getMemoryConsummation()));
            configurationsChoice.getSelectionModel().select(this.component.getConfiguration().getName());
            configurationsChoice.setDisable(true);

            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                System.out.println("OK");
                String confName = configurationsChoice.getSelectionModel().getSelectedItem();
                try {
                    if(controller.getNameComp().getText().isEmpty() || controller.getMemoryConsComp().getText().isEmpty() || confName == null){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Validation Error");
                        alert.setContentText("Fill all field!");
                        alert.show();
                        return;
                    }
                    Configuration conf = model.getConfByName(confName);
                    this.component.setConfiguration(conf);
                    this.component.setName(controller.getNameComp().getText());
                    this.component.setMemoryConsummation(Integer.parseInt(controller.getMemoryConsComp().getText()));
                    this.updateName(this.component.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("CANCEL");
            }
        });

        MenuItem globalCsp = new MenuItem("Add Global CSP");
        globalCsp.setOnAction(ActionEvent -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            //dialog.initOwner(mainPane.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../views/newGlobalCsp.fxml"));
            try{
                dialog.getDialogPane().setContent(fxmlLoader.load());
            }catch (Exception e){
                e.printStackTrace();
                return;
            }

            NewGlobalCspController controller = fxmlLoader.getController();
            Csp globalCspExp = component.getGlobalCsp();
            if(globalCspExp != null){
                controller.initNameTextField(globalCspExp.getName());
                controller.initExpressionTextField(globalCspExp.getExpression());
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                System.out.println("OK");
                controller.addGlobalCsp(component);
            }else {
                System.out.println("CANCEL");
            }
        });

        this.contextMenu.getItems().addAll(delete, modify, globalCsp);
    }

    public Label updateHeader(String header){
        this.header.setText(header);
        this.header.setPrefWidth(this.getPrefWidth());
        this.header.setAlignment(Pos.CENTER);
        this.header.setStyle("-fx-text-fill: white;");
        return this.header;
    }

    public void updateName(String name){
        this.getChildren().remove(this.nameC);
        this.nameC.setText(name);
        this.nameC.setPrefWidth(this.getPrefWidth());
        this.nameC.setPrefHeight(this.getPrefHeight() - this.header.getPrefHeight());
        this.nameC.setAlignment(Pos.CENTER);
        this.nameC.setStyle("-fx-text-fill: white;");
        this.getChildren().add(this.nameC);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                System.out.println("Double clicked");
                Dialog<ButtonType> dialog = new Dialog<>();
                //dialog.initOwner(mainPane.getScene().getWindow());
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

                // fill type choice box
                ChoiceBox<String> typePort = controller.getTypePortChoice();
                typePort.getItems().addAll(List.of(String.valueOf(TypePort.IN), String.valueOf(TypePort.OUT)));

                // fill position choice box
                ChoiceBox<String> position = controller.getPositionChoice();
                position.getItems().addAll(List.of("TOP", "RIGHT", "BOTTOM", "LEFT"));

                Optional<ButtonType> result = dialog.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK){
                    System.out.println("OK");
                    String portType = typePort.getSelectionModel().getSelectedItem();
                    String positionVal = position.getSelectionModel().getSelectedItem();

                    Port port = controller.addPort(portType, this.component);
                    // Build port element
                    PortBlock portBlock = new PortBlock(port.getName().charAt(0)+""+String.valueOf(this.count++), port);
                    portBlock.setId(component.getName()+"-"+port.getName());
                    this.getChildren().add(portBlock);
                    switch (positionVal){
                        case "RIGHT":
                            portBlock.setLayoutX(this.getWidth() - portBlock.getPrefWidth());
                            POS_Y_RIGHT = POS_Y_RIGHT + 10;
                            portBlock.setLayoutY(POS_Y_RIGHT);
                            POS_Y_RIGHT = POS_Y_RIGHT + 12;
                            port.setX(portBlock.getLayoutX());
                            port.setY(portBlock.getLayoutY());
                            break;
                        case "LEFT":
                            portBlock.setLayoutX(0 - portBlock.getMinWidth());
                            POS_Y_LEFT = POS_Y_LEFT + 10;
                            portBlock.setLayoutY(POS_Y_LEFT);
                            POS_Y_LEFT = POS_Y_LEFT + 12;
                            port.setX(portBlock.getLayoutX());
                            port.setY(portBlock.getLayoutY());
                            break;
                        case "TOP":
                            POS_X_TOP = POS_X_TOP + 10;
                            portBlock.setLayoutX(POS_X_TOP);
                            POS_X_TOP = POS_X_TOP + 12;
                            portBlock.setLayoutY(0 - portBlock.getMinHeight());
                            port.setX(portBlock.getLayoutX());
                            port.setY(portBlock.getLayoutY());
                            break;
                        case "BOTTOM":
                            POS_X_BOTTOM = POS_X_BOTTOM + 10;
                            portBlock.setLayoutX(POS_X_BOTTOM);
                            POS_X_BOTTOM = POS_X_BOTTOM + 12;
                            portBlock.setLayoutY(this.getHeight() - portBlock.getPrefHeight());
                            port.setX(portBlock.getLayoutX());
                            port.setY(portBlock.getLayoutY());
                            break;
                    }
                }else {
                    System.out.println("CANCEL");
                }
            }
        }else if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
            if(contextMenu.isShowing()) contextMenu.hide();
            this.setOnContextMenuRequested(event -> {
                contextMenu.show(this, event.getScreenX(), event.getScreenY());
            });
        }
    }

    public ArrayList<Arrow> getConnectorsArrow() {
        return connectorsArrow;
    }

    public void setConnectorsArrow(ArrayList<Arrow> connectorsArrow) {
        this.connectorsArrow = connectorsArrow;
    }

    public void addConnectorArrow(Arrow connector){
        connectorsArrow.add(connector);
    }

    public Component getComponent() {
        return component;
    }
}
