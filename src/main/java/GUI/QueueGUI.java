package GUI;

import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class QueueGUI extends Circle implements DraggableShape {
    public Text text = new Text("fsdfdfsd");

    public QueueGUI(double x, double y) {
        setCenterX(x);
        setCenterY(y);

        setRadius(50);
        text.xProperty().bind(centerXProperty());
        text.yProperty().bind(centerYProperty());
        setFill(Color.DARKGOLDENROD);

        setOnDragDetected(mouseEvent -> {
            toFront();
            text.toFront();
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
    public Text getText() {
        return text;
    }
}
