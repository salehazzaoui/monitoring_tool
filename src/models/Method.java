package models;

public class Method {
    private String name;
    private int executionTime;
    private Component component;

    public Method(String name, int executionTime, Component component) {
        this.name = name;
        this.executionTime = executionTime;
        this.component = component;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }
}
