package utils;

import com.uppaal.engine.CannotEvaluateException;
import com.uppaal.engine.Engine;
import com.uppaal.engine.EngineException;
import com.uppaal.engine.EngineStub;
import com.uppaal.engine.Parser;
import com.uppaal.engine.Problem;
import com.uppaal.engine.QueryFeedback;
import com.uppaal.engine.QueryResult;
import com.uppaal.model.core2.Data2D;
import com.uppaal.model.core2.DataSet2D;
import com.uppaal.model.core2.Document;
import com.uppaal.model.core2.Edge;
import com.uppaal.model.core2.Location;
import com.uppaal.model.core2.Property;
import com.uppaal.model.core2.PrototypeDocument;
import com.uppaal.model.core2.Query;
import com.uppaal.model.core2.QueryData;
import com.uppaal.model.core2.Template;
import com.uppaal.model.io2.UXMLResolver;
import com.uppaal.model.io2.XMLReader;
import com.uppaal.model.system.SystemEdge;
import com.uppaal.model.system.SystemLocation;
import com.uppaal.model.system.symbolic.SymbolicState;
import com.uppaal.model.system.symbolic.SymbolicTransition;
import com.uppaal.model.system.symbolic.SymbolicTrace;
import com.uppaal.model.system.concrete.Limit;
import com.uppaal.model.system.concrete.ConcreteVariable;
import com.uppaal.model.system.concrete.ConcreteState;
import com.uppaal.model.system.concrete.ConcreteTransitionRecord;
import com.uppaal.model.system.concrete.ConcreteTrace;
import com.uppaal.model.system.UppaalSystem;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.geom.Point2D;

public class Uppaal {
    /**
     * Valid kinds of labels on locations.
     */
    public enum LKind {
        name, init, urgent, committed, invariant, exponentialrate, comments
    };
    /**
     * Valid kinds of labels on edges.
     */
    public enum EKind {
        select, guard, synchronisation, assignment, comments
    };
    /**
     * Sets a label on a location.
     * @param l the location on which the label is going to be attached
     * @param kind a kind of the label
     * @param value the label value (either boolean or String)
     * @param x the x coordinate of the label
     * @param y the y coordinate of the label
     */
    public static void setLabel(Location l, LKind kind, Object value, int x, int y) {
        l.setProperty(kind.name(), value);
        Property p = l.getProperty(kind.name());
        p.setProperty("x", x);
        p.setProperty("y", y);
    }
    /**
     * Adds a location to a template.
     * @param t the template
     * @param name a name for the new location
     * @param exprate an expression for an exponential rate
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     * @return the new location instance
     */
    public static Location addLocation(Template t, String name, String exprate,
                                       int x, int y)
    {
        Location l = t.createLocation();
        t.insert(l, null);
        l.setProperty("x", x);
        l.setProperty("y", y);
        if (name != null)
            setLabel(l, LKind.name, name, x, y-28);
        if (exprate != null)
            setLabel(l, LKind.exponentialrate, exprate, x, y-28-12);
        return l;
    }
    /**
     * Sets a label on an edge.
     * @param e the edge
     * @param kind the kind of the label
     * @param value the content of the label
     * @param x the x coordinate of the label
     * @param y the y coordinate of the label
     */
    public static void setLabel(Edge e, EKind kind, String value, int x, int y) {
        e.setProperty(kind.name(), value);
        Property p = e.getProperty(kind.name());
        p.setProperty("x", x);
        p.setProperty("y", y);
    }
    /**
     * Adds an edge to the template
     * @param t the template where the edge belongs
     * @param source the source location
     * @param target the target location
     * @param guard guard expression
     * @param sync synchronization expression
     * @param update update expression
     * @return
     */
    public static Edge addEdge(Template t, Location source, Location target,
                               String guard, String sync, String update)
    {
        Edge e = t.createEdge();
        t.insert(e, null);
        e.setSource(source);
        e.setTarget(target);
        int x = (source.getX()+target.getX())/2;
        int y = (source.getY()+target.getY())/2;
        if (guard != null) {
            setLabel(e, EKind.guard, guard, x-15, y-28);
        }
        if (sync != null) {
            setLabel(e, EKind.synchronisation, sync, x-15, y-14);
        }
        if (update != null) {
            setLabel(e, EKind.assignment, update, x-15, y);
        }
        return e;
    }

    public static void print(UppaalSystem sys, SymbolicState s) {
        System.out.print("(");
        boolean first = true;
        for (SystemLocation l: s.getLocations()) {
            if (first) first=false; else System.out.print(", ");
            System.out.print(l.getName());
        }
        int val[] = s.getVariableValues();
        for (int i=0; i<sys.getNoOfVariables(); ++i) {
            System.out.print(", ");
            System.out.print(sys.getVariableName(i)+"="+val[i]);
        }
        List<String> constraints = new ArrayList<>();
        s.getPolyhedron().getAllConstraints(constraints);
        for (String cs : constraints) {
            System.out.print(", ");
            System.out.print(cs);
        }
        System.out.println(")");
    }

    static final BigDecimal zero = BigDecimal.valueOf(0);

    public static void print(UppaalSystem sys, ConcreteState s) {
        System.out.print("(");
        boolean first = true;
        for (SystemLocation l: s.getLocations()) {
            if (first) first=false; else System.out.print(", ");
            System.out.print(l.getName());
        }
        Limit limit = s.getInvariant();
        if (!limit.isUnbounded()) {
            if (limit.isStrict())
                System.out.print(", limit<");
            else
                System.out.print(", limit≤");
            System.out.print(limit.getDoubleValue());
        }
        ConcreteVariable val[] = s.getCVariables();
        int vars = sys.getNoOfVariables();
        for (int i=0; i<vars; ++i) {
            System.out.print(", ");
            System.out.print(sys.getVariableName(i)+"="+val[i].getValue(zero));
        }
        for (int i=0; i<sys.getNoOfClocks(); ++i) {
            System.out.print(", ");
            System.out.print(sys.getClockName(i)+"="+val[i+vars].getValue(zero));
            System.out.print(", ");
            System.out.print(sys.getClockName(i)+"'="+val[i+vars].getRate());
        }
        System.out.println(")");
    }

    public static Document createSampleModel()
    {
        // create a new Uppaal model with default properties:
        Document doc = new Document(new PrototypeDocument());
        // add global variables:
        doc.setProperty("declaration", "int v;\n\nclock x,y,z;");
        // add a TA template:
        Template t = doc.createTemplate(); doc.insert(t, null);
        t.setProperty("name", "Experiment");
        // the template has initial location:
        Location l0 = addLocation(t, "L0", "1", 0, 0);
        l0.setProperty("init", true);
        // add another location to the right:
        Location l1 = addLocation(t, "L1", null, 150, 0);
        setLabel(l1, LKind.invariant, "x<=10", l1.getX()-7, l1.getY()+10);
        // add another location below to the right:
        Location l2 = addLocation(t, "L2", null, 150, 150);
        setLabel(l2, LKind.invariant, "y<=20", l2.getX()-7, l2.getY()+10);
        // add another location below:
        Location l3 = addLocation(t, "L3", "1", 0, 150);
        // add another location below:
        Location lf = addLocation(t, "Final", null, -150, 150);
        // create an edge L0->L1 with an update
        Edge e = addEdge(t, l0, l1, "v<2", null, "v=1,\nx=0");
        e.setProperty(EKind.comments.name(), "Execute L0->L1 with v=1");
        // create some more edges:
        addEdge(t, l1, l2, "x>=5", null, "v=2,\ny=0");
        addEdge(t, l2, l3, "y>=10", null, "v=3,\nz=0");
        addEdge(t, l3, l0, null, null, "v=4");
        addEdge(t, l3, lf, null, null, "v=5");
        // add system declaration:
        doc.setProperty("system",
                "Exp1=Experiment();\n"+
                        "Exp2=Experiment();\n\n"+
                        "system Exp1, Exp2;");
        return doc;
    }

    public static Document loadModel(String location) throws IOException
    {
        try {
            // try URL scheme (useful to fetch from Internet):
            return new PrototypeDocument().load(new URL(location));
        } catch (MalformedURLException ex) {
            // not URL, retry as it were a local filepath:
            return new PrototypeDocument().load(new URL("file", null, location));
        }
    }

    public static Engine connectToEngine() throws EngineException, IOException
    {
        String os = System.getProperty("os.name");
        String here = System.getProperty("user.dir");
        String path = null;
        if ("Linux".equals(os)) {
            path = here+"/bin-Linux/server";
        } else if ("Mac OS X".equals(os)) {
            path = here+"/bin-Darwin/server";
        } else if ("Windows".equals(os)) {
            path = here+"\\bin-Windows\\server.exe";
        } else {
            System.err.println("Unknown operating system.");
            System.exit(1);
        }
        Engine engine = new Engine();
        engine.setServerPath(path);
        engine.setServerHost("localhost");
        engine.setConnectionMode(EngineStub.BOTH);
        engine.connect();
        return engine;
    }

    public static UppaalSystem compile(Engine engine, Document doc)
            throws EngineException, IOException
    {
        // compile the model into system:
        ArrayList<Problem> problems = new ArrayList<>();
        UppaalSystem sys = engine.getSystem(doc, problems);
        if (!problems.isEmpty()) {
            boolean fatal = false;
            System.out.println("There are problems with the document:");
            for (Problem p : problems) {
                System.out.println(p.toString());
                if (!"warning".equals(p.getType())) { // ignore warnings
                    fatal = true;
                }
            }
            if (fatal) {
                System.exit(1);
            }
        }
        return sys;
    }

    public static void printTrace(UppaalSystem sys, SymbolicTrace trace)
    {
        if (trace == null) {
            System.out.println("(null trace)");
            return;
        }
        Iterator<SymbolicTransition> it = trace.iterator();
        print(sys, it.next().getTarget());
        while (it.hasNext()) {
            SymbolicTransition tr = it.next();
            if (tr.getSize()==0) {
                // no edges, something special (like "deadlock" or initial state):
                System.out.println(tr.getEdgeDescription());
            } else {
                // one or more edges involved, print them:
                boolean first = true;
                for (SystemEdge e: tr.getEdges()) {
                    if (first) first = false; else System.out.print(", ");
                    System.out.print(e.getProcessName()+": "
                            + e.getEdge().getSource().getPropertyValue("name")
                            + " \u2192 "
                            + e.getEdge().getTarget().getPropertyValue("name"));
                }
            }
            System.out.println();
            print(sys, tr.getTarget());
        }
        System.out.println();
    }

    public static void printTrace(UppaalSystem sys, ConcreteTrace trace)
    {
        if (trace == null) {
            System.out.println("(null trace)");
            return;
        }
        Iterator<ConcreteTransitionRecord> it = trace.iterator();
        print(sys, it.next().getTarget());
        while (it.hasNext()) {
            ConcreteTransitionRecord tr = it.next();
            if (tr.getSize()==0) {
                // no edges, something special (like "deadlock" or initial state):
                System.out.println(tr.getTransitionDescription());
            } else {
                // one or more edges involved, print them:
                boolean first = true;
                for (SystemEdge e: tr.getEdges()) {
                    if (first) first = false; else System.out.print(", ");
                    System.out.print(e.getProcessName()+": "
                            + e.getEdge().getSource().getPropertyValue("name")
                            + " \u2192 "
                            + e.getEdge().getTarget().getPropertyValue("name"));
                }
            }
            System.out.println();
            print(sys, tr.getTarget());
        }
        System.out.println();
    }

    static public void print(QueryData data) {
        for (String title: data.getDataTitles()) {
            DataSet2D plot = data.getData(title);
            System.out.println("Plot \""+plot.getTitle()+
                    "\" showing \"" + plot.getYLabel() +
                    "\" over \"" + plot.getXLabel()+"\"");
            for (Data2D traj: plot) {
                System.out.print("Trajectory " + traj.getTitle()+":");
                for (Point2D.Double p: traj)
                    System.out.print(" ("+p.x+","+p.y+")");
                System.out.println();
            }
        }
    }

    public static SymbolicTrace symbolicSimulation(Engine engine,
                                                   UppaalSystem sys)
            throws EngineException, IOException, CannotEvaluateException
    {
        SymbolicTrace trace = new SymbolicTrace();
        // compute the initial state:
        SymbolicState state = engine.getInitialState(sys);
        // add the initial transition to the trace:
        trace.add(new SymbolicTransition(null, null, state));
        while (state != null) {
            print(sys, state);
            // compute the successors (including "deadlock"):
            ArrayList<SymbolicTransition> trans = engine.getTransitions(sys, state);
            // select a random transition:
            int n = (int)Math.floor(Math.random()*trans.size());
            SymbolicTransition tr = trans.get(n);
            // check the number of edges involved:
            if (tr.getSize()==0) {
                // no edges, something special (like "deadlock"):
                System.out.print(tr.getEdgeDescription());
            } else {
                // one or more edges involved, print them:
                boolean first = true;
                for (SystemEdge e: tr.getEdges()) {
                    if (first) first = false; else System.out.print(", ");
                    System.out.print(e.getProcessName()+": "
                            + e.getEdge().getSource().getPropertyValue("name")
                            + " \u2192 "
                            + e.getEdge().getTarget().getPropertyValue("name"));
                }
            }
            System.out.println();
            // jump to a successor state (null in case of deadlock):
            state = tr.getTarget();
            // if successfull, add the transition to the trace:
            if (state != null)
                trace.add(tr);
        }
        return trace;
    }

    public static void saveXTRFile(SymbolicTrace trace, String file)
            throws IOException
    {
		/* BNF for the XTR format just in case
		   (it may change, thus don't rely on it)
		   <XTRFomat>  := <state> ( <state> <transition> ".\n" )* ".\n"
		   <state>     := <locations> ".\n" <polyhedron> ".\n" <variables> ".\n"
		   <locations> := ( <locationId> "\n" )*
		   <polyhedron> := ( <constraint> ".\n" )*
		   <constraint> := <clockId> "\n" clockId "\n" bound "\n"
		   <variables> := ( <varValue> "\n" )*
		   <transition> := ( <processId> <edgeId> )* ".\n"
		*/
        FileWriter out = new FileWriter(file);
        Iterator<SymbolicTransition> it = trace.iterator();
        it.next().getTarget().writeXTRFormat(out);
        while (it.hasNext()) {
            it.next().writeXTRFormat(out);
        }
        out.write(".\n");
        out.close();
    }
}
