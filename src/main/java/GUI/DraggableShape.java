package GUI;

import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.paint.Paint;

public interface DraggableShape {
    public DoubleProperty getXProperty();
    public void setXProperty(double x);
    public void setYProperty(double y);
    public DoubleProperty getYProperty();
    public void setShapeStroke(Paint paint);
    public void addConnection(Arrow arrow);
    public ObservableList<Arrow> getConnections();
}
