package models;

import java.io.Serializable;
import java.util.ArrayList;

public class Configuration extends ArchitecturalElement implements Serializable {
    private String name;
    public ArrayList<Component> components = new ArrayList<>();

    public Configuration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }
}
