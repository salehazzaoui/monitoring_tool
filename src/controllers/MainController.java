package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import models.Configuration;
import utils.DraggableMaker;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TreeView<String> mTreeView;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab mainConf;

    private DraggableMaker draggableMaker;

    private TreeItem<String> rootItem;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //draggableMaker = new DraggableMaker();
        //draggableMaker.makeDraggable(circle);
        rootItem = new TreeItem<>("Project");
        TreeItem<String> item = new TreeItem<>("Main Configuration");
        rootItem.getChildren().add(item);
        mTreeView.setRoot(rootItem);
    }

    public void addNewConf(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../views/newConfiguration.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("OK");
            NewConfController controller = fxmlLoader.getController();
            if (controller.addConf() != null){
                Configuration configuration =  controller.addConf();

                // add conf to the TreeView
                TreeItem<String> item = new TreeItem<>(configuration.getName());
                rootItem.getChildren().add(item);

                // create a new tab
                Tab tab = new Tab();
                tab.setText(configuration.getName());

                ScrollPane scrollPane = new ScrollPane();
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setPrefWidth(scrollPane.getPrefWidth());
                anchorPane.setPrefHeight(scrollPane.getPrefHeight());
                scrollPane.setContent(anchorPane);

                tab.setContent(scrollPane);

                tabPane.getTabs().add(tab);

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setContentText("The name must be not empty.");
                alert.show();
            }
        }else {
            System.out.println("CANCEL");
        }
    }

    public void addNewComponent(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../views/newConfiguration.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("OK");
            NewConfController controller = fxmlLoader.getController();
            if (controller.addConf() != null){
                Configuration configuration =  controller.addConf();

                // add conf to the TreeView
                TreeItem<String> item = new TreeItem<>(configuration.getName());
                rootItem.getChildren().add(item);

                // create a new tab
                Tab tab = new Tab();
                tab.setText(configuration.getName());

                ScrollPane scrollPane = new ScrollPane();
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setPrefWidth(scrollPane.getPrefWidth());
                anchorPane.setPrefHeight(scrollPane.getPrefHeight());
                scrollPane.setContent(anchorPane);

                tab.setContent(scrollPane);

                tabPane.getTabs().add(tab);

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setContentText("The name must be not empty.");
                alert.show();
            }
        }else {
            System.out.println("CANCEL");
        }
    }
}
