package models;

public class ConnectorConstraint extends Constraint{
    private int time;
    private int bandwidth;

    public ConnectorConstraint(int time, int bandwidth) {
        this.time = time;
        this.bandwidth = bandwidth;
    }
}
