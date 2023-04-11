package models;

import java.io.Serializable;
import java.util.ArrayList;

public class Configuration extends ArchitecturalElement implements Serializable {
    private String name;
    public ArrayList<Configuration> configurations = new ArrayList<>();
    public ArrayList<Component> components = new ArrayList<>();
    public ArrayList<Connector> connectors = new ArrayList<>();

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

    public Component getCompByName(String name) throws Exception {
        int i = 0;
        while (!components.get(i).getName().trim().equals(name.trim())){
            i++;
        }
        if (i < components.size()){
            return components.get(i);
        }else {
            throw new Exception("not match name");
        }
    }

    public Connector getConnectorByName(String name) throws Exception {
        int i = 0;
        while (!connectors.get(i).getName().trim().equals(name.trim())){
            i++;
        }
        if (i < connectors.size()){
            return connectors.get(i);
        }else {
            throw new Exception("not match name");
        }
    }

    public ArrayList<Connector> getConnectors() {
        return connectors;
    }

    public ArrayList<Configuration> getConfigurations() {
        return configurations;
    }
}
