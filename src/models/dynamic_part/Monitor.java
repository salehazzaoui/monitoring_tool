package models.dynamic_part;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import models.static_part.Component;
import models.static_part.Configuration;
import models.static_part.Connector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Monitor implements Runnable {
    private TextArea resTextArea;
    private Configuration currentConf;
    private boolean stopRequested = false;

    public Monitor(TextArea textArea, Configuration currentConf) {
        resTextArea = textArea;
        this.currentConf = currentConf;
    }

    public void stopThread() {
        stopRequested = true;
    }

    public boolean isStopRequested() {
        return stopRequested;
    }

    public void setStopRequested(boolean stopRequested) {
        this.stopRequested = stopRequested;
    }

    @Override
    public void run() {
        synchronized (resTextArea) {
            try {
                while (!stopRequested) {
                    System.out.println("Monitor is waiting");
                    resTextArea.wait();
                    String trace = resTextArea.getText();
                    String[] traces = trace.split("\n");
                    String states = traces[traces.length - 1];
                    String transitions;
                    if (states.equals("not deadlock")){
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("No deadlock");
                            alert.setContentText("configuration is valid");
                            alert.show();
                            stopThread();
                        });
                    }
                    if (states.equals("deadlock")){
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Deadlock");
                            alert.setContentText("Configuration is invalid");
                            alert.show();
                            stopThread();
                        });
                    }
                    List<String> elementNames = new ArrayList<>();
                    if (states.contains("band") && states.contains("consummation") && traces.length > 1) {
                        transitions = traces[traces.length - 2];
                        String pattern0 = "\\b\\w+(?=\\d*:)";

                        Pattern regex0 = Pattern.compile(pattern0);
                        Matcher matcher0 = regex0.matcher(transitions);

                        while (matcher0.find()) {
                            String name = matcher0.group();
                            name = name.replaceAll("\\d+$", "");
                            elementNames.add(name);
                        }

                    }

                    System.out.println(elementNames);
                    if(!elementNames.isEmpty()){
                        List<Component> components = new ArrayList<>();
                        for (String name: elementNames) {
                             Component component = this.currentConf.getComponents()
                                    .stream()
                                    .filter(comp -> comp.getName().equals(name)).toList().size() > 0 ?
                                     this.currentConf.getComponents()
                                            .stream()
                                            .filter(comp -> comp.getName().equals(name)).toList().get(0) : null;
                            if(component != null){
                                components.add(component);
                            }
                        }

                        List<Connector> connectors = new ArrayList<>();
                        for (String name: elementNames) {
                            Connector connector = this.currentConf.getConnectors()
                                    .stream()
                                    .filter(conn -> conn.getName().equals(name)).toList().size() > 0 ?
                                    this.currentConf.getConnectors()
                                            .stream()
                                            .filter(conn -> conn.getName().equals(name)).toList().get(0) : null;
                            if(connector != null){
                                connectors.add(connector);
                            }
                        }

                        int consummationConstraint = components.stream().mapToInt(Component::getMemoryConstraint).min().getAsInt();
                        int bandConstraint = connectors.stream().mapToInt(Connector::getBandConstraint).min().getAsInt();

                        if (states.contains("band")) {
                            String regex = "band=(\\d+)";
                            Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                            Matcher matcher = pattern.matcher(states);
                            System.out.println("Start Band Matcher ---------------");
                            int i = 0;
                            String match = "";
                            while (matcher.find()) {
                                match = matcher.group(i);
                                i++;
                            }
                            System.out.println("Band : " + match.split("=")[1]);
                            System.out.println("End Band Matcher ---------------");
                            int band = Integer.parseInt(match.split("=")[1]);
                            if (band >= bandConstraint) {
                                Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Required a reconfiguration");
                                    alert.setContentText("There is a violation of bandwidth constraint");
                                    alert.show();
                                    stopThread(); // Stop the thread if alert is shown
                                });
                            }
                        }
                        if (states.contains("consummation")) {
                            String regex = "consummation=(\\d+)";
                            Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                            Matcher matcher = pattern.matcher(states);
                            System.out.println("Start Consummation Matcher ---------------");
                            int i = 0;
                            String match = "";
                            while (matcher.find()) {
                                match = matcher.group(i);
                                i++;
                            }
                            System.out.println("Consummation : " + match.split("=")[1]);
                            System.out.println("End Consummation Matcher ---------------");
                            int consummation = Integer.parseInt(match.split("=")[1]);
                            if (consummation >= consummationConstraint) {
                                Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Required a reconfiguration");
                                    alert.setContentText("There is a violation of memory consummation constraint");
                                    alert.show();
                                    stopThread(); // Stop the thread if alert is shown
                                });
                            }
                        }
                    }
                    resTextArea.notify();
                    System.out.println("Monitor notified");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
