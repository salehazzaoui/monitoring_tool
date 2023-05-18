package application;

import csp.CSPProcessAlgebraBaseListener;
import csp.CSPProcessAlgebraLexer;
import csp.CSPProcessAlgebraListener;
import csp.CSPProcessAlgebraParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;;
import javafx.stage.Stage;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/main.fxml"));
        primaryStage.setTitle("Monitoring Tool");
        primaryStage.getIcons().add(new Image("assets/images/icon.png"));
        //primaryStage.setMaximized(true);
        primaryStage.setMinWidth(860);
        primaryStage.setMinHeight(610);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        /*
        // create the input stream
        //ANTLRInputStream input = new ANTLRInputStream("process P { a!x -> b!y -> skip }");
        CharStream stream = CharStreams.fromString("P = a!x -> b!y -> skip");

        // create the lexer
        CSPProcessAlgebraLexer lexer = new CSPProcessAlgebraLexer(stream);

        // create the token stream
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create the parser
        CSPProcessAlgebraParser parser = new CSPProcessAlgebraParser(tokens);

        // parse the input and get the AST
        CSPProcessAlgebraParser.ProcessContext processContext = parser.process();

        // print the AST
        System.out.println(processContext.toStringTree(parser));

        // get the list of events from the process
        List<CSPProcessAlgebraParser.EventContext> events = processContext.statements().statement()
                .stream()
                .filter(statement -> statement.event() != null)
                .map(CSPProcessAlgebraParser.StatementContext::event)
                .collect(Collectors.toList());

        // print out the events
        for (CSPProcessAlgebraParser.EventContext event : events) {
            System.out.println(event.getText());
        }

        ParseTreeWalker walker = new ParseTreeWalker();
        CSPProcessAlgebraListener listener = new CSPProcessAlgebraBaseListener();
        walker.walk(listener, processContext);*/
    }
  }



