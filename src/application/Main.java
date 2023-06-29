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
import javafx.stage.StageStyle;

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
    }
  }



