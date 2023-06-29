package models.dynamic_part;

import com.uppaal.engine.Engine;
import com.uppaal.model.core2.Document;
import com.uppaal.model.system.UppaalSystem;
import com.uppaal.model.system.symbolic.SymbolicTrace;
import com.uppaal.model.system.symbolic.SymbolicTransition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

public class UppaalRunner implements Runnable{
    private Document doc;
    private int occurrence;

    public UppaalRunner(Document doc, int occurrence){
        this.doc = doc;
        this.occurrence = occurrence;
    }

    @Override
    public void run() {
        try {
            Engine engine = Uppaal.connectToEngine();
            UppaalSystem uppaalSystem = Uppaal.compile(engine, doc);
            File file = new File("trace.txt");
            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            SymbolicTrace trace = Uppaal.symbolicSimulationInFile(engine, uppaalSystem, br, occurrence);
            if (trace == null) {
                System.out.println("(null trace)");
                return;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
