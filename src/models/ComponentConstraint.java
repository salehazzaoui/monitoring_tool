package models;

public class ComponentConstraint extends Constraint{
    private int time;
    private int memory;

    public ComponentConstraint(int time, int memory) {
        this.time = time;
        this.memory = memory;
    }
}
