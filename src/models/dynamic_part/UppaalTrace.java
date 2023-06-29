package models.dynamic_part;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class UppaalTrace implements Runnable{
    private TextArea resTextArea;
    private boolean stopRequested = false;

    public UppaalTrace(TextArea textArea){
        resTextArea = textArea;
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
        File file = new File("trace.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        synchronized (resTextArea){
            try {
                while (!stopRequested){
                    String line;
                    while ((line = br.readLine()) != null) {
                        resTextArea.appendText(line+"\n");
                        Thread.sleep(300);
                        resTextArea.notify();
                        System.out.println("Trace is waiting");
                        resTextArea.wait();
                        System.out.println("Trace notified");
                    }
                    br.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
