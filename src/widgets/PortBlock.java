package widgets;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import models.static_part.Port;
import utils.TypePort;

public class PortBlock extends HBox {
    private Label name = new Label();

    public  PortBlock(String name, Port port){
        this.setMinSize(15, 15);
        if (port.getType().equals(TypePort.IN)) {
            this.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
        } else {
            this.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        }
        this.getChildren().add(this.updateName(name));
        port.setPortBlock(this);
    }

    public Label updateName(String name){
        this.name.setText(name);
        this.name.setTextFill(Color.WHITE);
        return this.name;
    }
}
