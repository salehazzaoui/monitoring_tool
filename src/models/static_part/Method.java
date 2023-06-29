package models.static_part;

public class Method {
    private String name;
    private int executionTime;
    private String componentName;

    public Method(String name, int executionTime, String componentName) {
        this.name = name;
        this.executionTime = executionTime;
        this.componentName = componentName;
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

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
}
