package models;

import utils.TypePort;

import java.io.Serializable;

public class Port extends ArchitecturalElement implements Serializable {
    private String name;
    private TypePort type;
    private Component component;
    private Connector connector;
    private Csp cspExpression;
    private Csp cspExpressionModify;

    private double width;
    private double height;
    private double X;
    private double Y;

    public Port(String name, Component component, Csp csp){
        this.name = name;
        this.component = component;
        this.cspExpression = csp;
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
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
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
}

