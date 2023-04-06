package models;

import utils.TypePort;

public class Port {
    private String name;
    private TypePort type;

    public Port(String name){
        this.name = name;
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
}

