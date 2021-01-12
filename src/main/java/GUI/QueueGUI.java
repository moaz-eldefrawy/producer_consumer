package GUI;

import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class QueueGUI extends Circle implements DraggableShape {

    public ObservableList<Arrow> connections = FXCollections.observableArrayList();

    public QueueGUI(double x, double y) {
        setCenterX(x);
        setCenterY(y);

        setRadius(50);

        setFill(Color.DARKGOLDENROD);

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
        return centerXProperty();
    }

    @Override
    public void setXProperty(double x) {
        setCenterX(x);
    }

    @Override
    public DoubleProperty getYProperty() {
        return centerYProperty();
    }

    @Override
    public void setYProperty(double y) {
        setCenterY(y);
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
