package models;

import java.io.Serializable;

public class Connector extends ArchitecturalElement implements Serializable {
    private String name;
    private int bandwidth;
    private Port portIn;
    private Port portOut;
    private ConnectorConstraint constraint;
    private Csp connectorCsp;

    public Connector(String name, Port portIn, Port portOut, int bandwidth) {
        this.name = name;
        this.portIn = portIn;
        this.portOut = portOut;
        this.bandwidth = bandwidth;
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

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public ConnectorConstraint getConstraint() {
        return constraint;
    }

    public void setConstraint(ConnectorConstraint constraint) {
        this.constraint = constraint;
    }

    public Csp getConnectorCsp() {
        return connectorCsp;
    }

    public void setConnectorCsp(Csp connectorCsp) {
        this.connectorCsp = connectorCsp;
    }
}
