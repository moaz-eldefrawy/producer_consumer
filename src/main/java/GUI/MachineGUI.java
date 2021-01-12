package GUI;

import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class MachineGUI extends Rectangle implements DraggableShape {
    public ObservableList<Arrow> connections = FXCollections.observableArrayList();

    public MachineGUI(double x, double y) {
        setX(x);
        setY(y);
        setWidth(100);
        setHeight(100);
        translateXProperty().bind(widthProperty().divide(-2));
        translateYProperty().bind(heightProperty().divide(-2));
        setFill(Color.GREEN);

        setOnDragDetected(mouseEvent -> {
            toFront();
        });


        setOnMouseEntered(mouseEvent -> {
            getScene().setCursor(Cursor.HAND);
        });

        setOnMouseExited(mouseEvent -> {
            getScene().setCursor(Cursor.DEFAULT);
        });

    }

    @Override
    public DoubleProperty getXProperty() {
        return xProperty();
    }

    @Override
    public void setXProperty(double x) {
        setX(x - 50);
    }

    @Override
    public DoubleProperty getYProperty() {
        return yProperty();
    }

    @Override
    public void setYProperty(double y) {
        setY(y - 50);
    }

    @Override
    public void setShapeStroke(Paint paint) {
        setStroke(paint);
        setStrokeWidth(3);
    }

    @Override
    public void addConnection(Arrow arrow) {
        connections.add(arrow);
    }

    @Override
    public ObservableList getConnections() {
        return connections;
    }
}
