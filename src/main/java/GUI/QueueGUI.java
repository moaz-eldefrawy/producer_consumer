package GUI;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class QueueGUI extends Circle implements DraggableShape {
    public Text text = new Text("0");

    public QueueGUI(double x, double y) {
        setCenterX(x);
        setCenterY(y);

        setRadius(50);
        text.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 15));
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
    public synchronized DoubleProperty getXProperty() {
        return centerXProperty();
    }

    @Override
    public synchronized void setXProperty(double x) {
        setCenterX(x);
    }

    @Override
    public synchronized DoubleProperty getYProperty() {
        return centerYProperty();
    }

    @Override
    public synchronized void setYProperty(double y) {
        setCenterY(y);
    }

    @Override
    public synchronized void setShapeStroke(Paint paint) {
        setStroke(paint);
        setStrokeWidth(3);
    }

    public void setText(String newtext){
        Runnable updater = new Runnable() {
            @Override
            public void run() {
                System.out.println("QueueGUI::setText" + text);;
                text.setText(newtext);
            }
        };


        Platform.runLater(updater);


    }


    @Override
    public synchronized Text getText() {
        return this.text;
    }
}
