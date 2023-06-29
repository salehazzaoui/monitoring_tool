package models.dynamic_part;

public class ComponentConstraint extends Constraint {
    private int time;
    private int memory;

    public ComponentConstraint(int time, int memory) {
        this.time = time;
        this.memory = memory;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }
}
