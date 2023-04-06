package widgets;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import models.Port;
import utils.TypePort;

public class PortBlock extends HBox {
    private Label name = new Label();

    public  PortBlock(String name, Port port){
        this.setMinSize(15, 15);
        if (port.getType().equals(TypePort.IN)) {
            this.setStyle("-fx-background-color: blue;");
        } else {
            this.setStyle("-fx-background-color: red;");
        }
        this.getChildren().add(this.updateName(name));
    }

    public Label updateName(String name){
        this.name.setText(name);
        this.name.setTextFill(Color.WHITE);
        return this.name;
    }
}
