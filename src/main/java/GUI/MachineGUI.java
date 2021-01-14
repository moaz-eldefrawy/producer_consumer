package GUI;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class MachineGUI extends Rectangle implements DraggableShape {


    Color color;
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
    public synchronized DoubleProperty getXProperty() {
        return xProperty();
    }

    @Override
    public synchronized void setXProperty(double x) {
        setX(x - 50);
    }

    @Override
    public synchronized DoubleProperty getYProperty() {
        return yProperty();
    }

    @Override
    public synchronized void setYProperty(double y) {
        setY(y - 50);
    }

    @Override
    public synchronized void setShapeStroke(Paint paint) {
        setStroke(paint);
        setStrokeWidth(3);
    }

    @Override
    public synchronized Text getText() {
        return null;
    }

}
