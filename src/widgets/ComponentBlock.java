package widgets;

import controllers.AddNewPort;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.Component;
import models.Model;
import models.Port;
import utils.TypePort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public ComponentBlock(Component component){
        this.component = component;
        this.setPrefWidth(component.getWidth());
        this.setPrefHeight(component.getHeight());
        this.setStyle("-fx-background-color: #F4CCC3;");
        this.getChildren().add(this.updateHeader("<< Component >>"));
        this.getChildren().add(this.updateName(component.getName()));
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
        });
    }

    public Label updateHeader(String header){
        this.header.setText(header);
        this.header.setPrefWidth(this.getPrefWidth());
        this.header.setAlignment(Pos.CENTER);
        return this.header;
    }

    public Label updateName(String name){
        this.nameC.setText(name);
        this.nameC.setPrefWidth(this.getPrefWidth());
        this.nameC.setPrefHeight(this.getPrefHeight() - this.header.getPrefHeight());
        this.nameC.setAlignment(Pos.CENTER);
        return this.nameC;
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
                    System.out.println(port.getType());
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
        }
    }

    public ArrayList<Arrow> getConnectorsArrow() {
        return connectorsArrow;
    }

    public void addConnectorArrow(Arrow connector){
        connectorsArrow.add(connector);
    }

    public Component getComponent() {
        return component;
    }
}
