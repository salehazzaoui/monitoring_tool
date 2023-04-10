package widgets;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import models.Component;
import models.Port;

public class Arrow extends Line {

    private double offsetX;
    private double offsetY;

    private Component componentSrc;
    private Component componentDes;
    private Port out;
    private Port in;

    private double startX;
    private double startY;
    private double endX;
    private double endY;

    public Arrow(PortBlock portOut, PortBlock portIn, Port out, Port in, Component componentSrc, Component componentDes){
        this.componentSrc = componentSrc;
        this.componentDes = componentDes;
        this.out = out;
        this.in = in;
        /*this.startXProperty().bind(portOut.layoutXProperty().add(portOut.widthProperty().divide(2)));
        this.startYProperty().bind(portOut.layoutYProperty().add(portOut.heightProperty().divide(2)));
        this.endXProperty().bind(portIn.layoutXProperty().add(portIn.widthProperty().divide(2)));
        this.endYProperty().bind(portIn.layoutYProperty().add(portIn.heightProperty().divide(2)));
        this.setStrokeWidth(2);
        this.setStroke(Color.BLACK);

        this.setOnMousePressed((MouseEvent event) -> {
            offsetX = event.getX() - this.getStartX();
            offsetY = event.getY() - this.getStartY();
        });
        this.setOnMouseDragged((MouseEvent event) -> {
            this.setStartX(event.getX() - offsetX);
            this.setStartY(event.getY() - offsetY);
        });*/

        this.setStrokeWidth(2);
        this.setStroke(Color.BLACK);

        double startX = this.componentSrc.getX() + portOut.getLayoutX() + portOut.getWidth() / 2;
        double startY = this.componentSrc.getY() + portOut.getLayoutY() + portOut.getHeight() / 2;
        double endX = this.componentDes.getX() + portIn.getLayoutX() + portIn.getWidth() / 2;
        double endY = this.componentDes.getY() + portIn.getLayoutY() + portIn.getHeight() / 2;

        this.setStartX(startX);
        this.setStartY(startY);
        this.setEndX(endX);
        this.setEndY(endY);

    }

    public Component getComponentSrc() {
        return componentSrc;
    }

    public void setComponentSrc(Component componentSrc) {
        this.componentSrc = componentSrc;
    }

    public Component getComponentDes() {
        return componentDes;
    }

    public void setComponentDes(Component componentDes) {
        this.componentDes = componentDes;
    }

    public Port getOut() {
        return out;
    }

    public void setOut(Port out) {
        this.out = out;
    }

    public Port getIn() {
        return in;
    }

    public void setIn(Port in) {
        this.in = in;
    }
}
