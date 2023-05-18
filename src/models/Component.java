package models;

import utils.TypePort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Component extends ArchitecturalElement implements Serializable {
    private String name;
    private int memoryConsummation;
    private Configuration configuration;
    public ArrayList<Component> components = new ArrayList<>();
    public ArrayList<Port> ports = new ArrayList<>();
    public ArrayList<Method> methods = new ArrayList<>();
    private ComponentConstraint constraint;
    private Csp globalCsp;

    private double width;
    private double height;
    private double X;
    private double Y;

    public Component(String name, int memoryConsummation, Configuration configuration) {
        this.name = name;
        this.memoryConsummation = memoryConsummation;
        this.configuration = configuration;
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

    public List<Port> getPortsIn() {
        return ports.stream().filter(port -> port.getType().equals(TypePort.IN)).toList();
    }

    public List<Port> getPortsOut() {
        return ports.stream().filter(port -> port.getType().equals(TypePort.OUT)).toList();
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

    public ArrayList<Component> getComponents() {
        return components;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public ComponentConstraint getConstraint() {
        return constraint;
    }

    public void setConstraint(ComponentConstraint constraint) {
        this.constraint = constraint;
    }

    public int getMemoryConsummation() {
        return memoryConsummation;
    }

    public void setMemoryConsummation(int memoryConsummation) {
        this.memoryConsummation = memoryConsummation;
    }

    public ArrayList<Method> getMethods() {
        return methods;
    }

    public Csp getGlobalCsp() {
        return globalCsp;
    }

    public void setGlobalCsp(Csp globalCsp) {
        this.globalCsp = globalCsp;
    }
}
