package models.static_part;

import models.dynamic_part.Csp;
import utils.TypePort;
import widgets.PortBlock;

import java.io.Serializable;

public class Port extends ArchitecturalElement implements Serializable {
    private String name;
    private TypePort type;
    private Component component;
    private Connector connector;
    private Csp cspExpression;
    private Csp cspExpressionModify;
    private PortBlock portBlock;

    private double width;
    private double height;
    private double x;
    private double y;

    public Port(String name, Component component){
        this.name = name;
        this.component = component;
        this.x = 0;
        this.y = 0;
        this.width = 15;
        this.height = 15;
    }

    public String getName() {
        return name;
    }

    public TypePort getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(TypePort type) {
        this.type = type;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Csp getCspExpression() {
        return cspExpression;
    }

    public void setCspExpression(Csp cspExpression) {
        this.cspExpression = cspExpression;
    }

    public Csp getCspExpressionModify() {
        return cspExpressionModify;
    }

    public void setCspExpressionModify(Csp cspExpressionModify) {
        this.cspExpressionModify = cspExpressionModify;
    }

    public PortBlock getPortBlock() {
        return portBlock;
    }

    public void setPortBlock(PortBlock portBlock) {
        this.portBlock = portBlock;
    }
}

