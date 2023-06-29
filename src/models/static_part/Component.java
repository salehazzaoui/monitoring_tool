package models.static_part;

import models.dynamic_part.Csp;
import models.dynamic_part.ComponentConstraint;
import utils.TypePort;
import widgets.ComponentBlock;

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
    private String color;

    private ComponentBlock componentBlock;

    public Component(String name, int memoryConsummation, Configuration configuration) {
        this.name = name;
        this.memoryConsummation = memoryConsummation;
        this.configuration = configuration;
        this.height = 100;
        this.width = 120;
        this.X = 0;
        this.Y = 0;
        this.color = "-fx-background-color: #ff8000;";
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

    public int getSumTime(){
        return this.methods.stream().reduce(0, (totalTime, method) -> totalTime + method.getExecutionTime(), Integer::sum);
    }

    public int getMemoryConstraint(){
        return constraint.getMemory();
    }

    public int getRoundedX(){
        return (int) Math.ceil(this.getX());
    }

    public int getRoundedY(){
        return (int) Math.ceil(this.getY());
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ComponentBlock getComponentBlock() {
        return componentBlock;
    }

    public void setComponentBlock(ComponentBlock componentBlock) {
        this.componentBlock = componentBlock;
    }

    public void addToComponents(Component component) {
        this.components.add(component);
    }

    public void addAllToComponents(List<Component> components) {
        this.components.addAll(components);
    }
}
