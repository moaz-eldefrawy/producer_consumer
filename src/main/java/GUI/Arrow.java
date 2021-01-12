package GUI;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Arrow extends Group {

    private Polyline arrow = new Polyline();
    private Polyline head = new Polyline();


    private SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty x2 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y2 = new SimpleDoubleProperty();




    public Arrow(DraggableShape s1, DraggableShape s2)
    {
        super();
        this.x1.set(s1.getXProperty().get());
        this.y1.set(s1.getYProperty().get());
        this.x2.set(s2.getXProperty().get());
        this.y2.set(s2.getYProperty().get());

        this.getChildren().addAll(arrow, head);

        this.x1.addListener(e -> updatePoints());
        this.y1.addListener(e -> updatePoints());
        this.x2.addListener(e -> updatePoints());
        this.y2.addListener(e -> updatePoints());

        updatePoints();


        x1.bind(s1.getXProperty());
        y1.bind(s1.getYProperty());

        x2.bind(s2.getXProperty());
        y2.bind(s2.getYProperty());

        s1.addConnection(this);
        s2.addConnection(this);

    }



    private void updatePoints() {
        double theta = Math.atan2(y2.get() - y1.get(), x2.get() - x1.get());
        double x2Offset = x2.get()-Math.cos(theta)*80;
        double y2Offset = y2.get()-Math.sin(theta)*80;

        arrow.getPoints().setAll(x1.get() + Math.cos(theta)*80, y1.get() + Math.sin(theta)*80, x2Offset, y2Offset);
        head.getPoints().setAll(x2Offset-Math.cos(theta+0.5)*20, y2Offset-Math.sin(theta+0.5)*20, x2Offset, y2Offset,
                x2Offset-Math.cos(theta-0.5)*20, y2Offset-Math.sin(theta-0.5)*20);

    }



}
