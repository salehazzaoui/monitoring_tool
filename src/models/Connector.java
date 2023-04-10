package models;

public class Connector {
    private String name;
    private Port portIn;
    private Port portOut;

    public Connector(String name, Port portIn, Port portOut) {
        this.name = name;
        this.portIn = portIn;
        this.portOut = portOut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Port getPortIn() {
        return portIn;
    }

    public void setPortIn(Port portIn) {
        this.portIn = portIn;
    }

    public Port getPortOut() {
        return portOut;
    }

    public void setPortOut(Port portOut) {
        this.portOut = portOut;
    }
}
