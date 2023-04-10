package utils;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class DraggableMaker {
    private double mouseAnchorX;
    private double mouseAnchorY;

    public double x;
    public double y;

    public void makeDraggable(Node node){

        node.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });

        node.setOnMouseDragged(mouseEvent -> {
            node.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
            node.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
            this.x = node.getLayoutX();
            this.y = node.getLayoutY();
        });
    }

    // make a node movable by dragging it around with the mouse.
    public void enableDrag(Node node) {
        final Delta dragDelta = new Delta();
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = mouseEvent.getX();
                dragDelta.y = mouseEvent.getY();
                node.getScene().setCursor(Cursor.MOVE);
            }
        });
        node.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                node.getScene().setCursor(Cursor.HAND);
            }
        });
        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                double newX = mouseEvent.getSceneX() - dragDelta.x;
                node.setLayoutX(newX);
                /*if (newX > 0 && newX < node.getScene().getWidth()) {
                    //new Center(node).centerX.set(newX); //setCenterX(newX);
                    node.setLayoutX(newX);
                }*/
                double newY = mouseEvent.getSceneY() - dragDelta.y;
                node.setLayoutY(newY);
                /*if (newY > 0 && newY < node.getScene().getHeight()) {
                    //new Center(node).centerY.set(newY); //setCenterY(newY);
                    node.setLayoutY(newY);
                }*/
            }
        });
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    node.getScene().setCursor(Cursor.HAND);
                }
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    node.getScene().setCursor(Cursor.DEFAULT);
                }
            }
        });
    }

    // records relative x and y co-ordinates.
    private class Delta { double x, y; }

    class Center {
        private ReadOnlyDoubleWrapper centerX = new ReadOnlyDoubleWrapper();
        private ReadOnlyDoubleWrapper centerY = new ReadOnlyDoubleWrapper();

        public Center(Node node) {
            calcCenter(node.getBoundsInParent());
            node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
                @Override public void changed(
                        ObservableValue<? extends Bounds> observableValue,
                        Bounds oldBounds,
                        Bounds bounds
                ) {
                    calcCenter(bounds);
                }
            });
        }

        private void calcCenter(Bounds bounds) {
            centerX.set(bounds.getMinX() + bounds.getWidth()  / 2);
            centerY.set(bounds.getMinY() + bounds.getHeight() / 2);
        }

        ReadOnlyDoubleProperty centerXProperty() {
            return centerX.getReadOnlyProperty();
        }

        ReadOnlyDoubleProperty centerYProperty() {
            return centerY.getReadOnlyProperty();
        }
    }
}
