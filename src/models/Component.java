package models;

import java.util.ArrayList;

public class Component {
    private String name;
    private double width;
    private double height;
    private double X;
    private double Y;

    public ArrayList<Port> ports = new ArrayList<>();

    public Component(String name) {
        this.name = name;
        height = 100;
        width = 120;
    }

    public String getName() {
        return name;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public ArrayList<Port> getPorts() {
        return ports;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }
}
