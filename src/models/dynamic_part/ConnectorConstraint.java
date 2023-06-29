package models.dynamic_part;

public class ConnectorConstraint extends Constraint{
    private int time;
    private int bandwidth;

    public ConnectorConstraint(int time, int bandwidth) {
        this.time = time;
        this.bandwidth = bandwidth;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }
}
