package widgets;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import models.static_part.Component;
import models.static_part.Connector;
import models.static_part.Port;

public class Arrow extends Line {

    private double offsetX;
    private double offsetY;

    private Component componentSrc;
    private Component componentDes;
    private Port out;
    private Port in;
    private Connector connector;

    private PortBlock portBlockOut;
    private PortBlock portBlockIn;

    private double startX;
    private double startY;
    private double endX;
    private double endY;

    public Arrow(PortBlock portOut, PortBlock portIn, Port out, Port in, Component componentSrc, Component componentDes){
        this.componentSrc = componentSrc;
        this.componentDes = componentDes;
        this.out = out;
        this.in = in;
        this.portBlockIn = portIn;
        this.portBlockOut = portOut;

        this.setStrokeWidth(2);
        this.setStroke(Color.BLACK);

        startX = this.componentSrc.getX() + portOut.getLayoutX() + portOut.getWidth() / 2;
        startY = this.componentSrc.getY() + portOut.getLayoutY() + portOut.getHeight() / 2;
        endX = this.componentDes.getX() + portIn.getLayoutX() + portIn.getWidth() / 2;
        endY = this.componentDes.getY() + portIn.getLayoutY() + portIn.getHeight() / 2;

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

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public PortBlock getPortBlockOut() {
        return portBlockOut;
    }

    public void setPortBlockOut(PortBlock portBlockOut) {
        this.portBlockOut = portBlockOut;
    }

    public PortBlock getPortBlockIn() {
        return portBlockIn;
    }

    public void setPortBlockIn(PortBlock portBlockIn) {
        this.portBlockIn = portBlockIn;
    }
}
