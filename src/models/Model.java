package models;

import java.util.ArrayList;

public class Model {
    private static Model instance;
    public ArrayList<Configuration> configurations;

    private Model(){
        this.configurations = new ArrayList<>();
        this.configurations.add(new Configuration("Main Configuration"));
    }

    public static synchronized Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public Configuration getConfByName(String name) throws Exception {
        int i = 0;
        while (!configurations.get(i).getName().trim().equals(name.trim())){
            i++;
        }
        if (i < configurations.size()){
            return configurations.get(i);
        }else {
            throw new Exception("not match name");
        }
    }
}
