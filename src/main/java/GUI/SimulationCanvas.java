package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.ArrayList;


public class SimulationCanvas {

    @FXML
    Pane canvas;
    @FXML
    NavBar navbar;
    DraggableShape source, destination;
    Arrow selectedArrow;
    ContextMenu canvasMenu;
    ArrayList<Arrow> allConnections = new ArrayList<Arrow>();
    double[] relativeContextMenuCoord;


    private void initCanvasMenu() {
        canvasMenu = new ContextMenu();
        relativeContextMenuCoord = new double[2];
        MenuItem addMachineMenuItem = new MenuItem("Add Machine");
        MenuItem addQueueMenuItem = new MenuItem("Add Queue");
        MenuItem addConnectionMenuItem = new MenuItem("Add Connection");
        MenuItem deleteShapeMenuItem = new MenuItem("Delete");

        addMachineMenuItem.setOnAction(e -> {
            MachineGUI m = new MachineGUI(relativeContextMenuCoord[0], relativeContextMenuCoord[1]);
            addDraggableShapeToCanvas(m);
        });




        addQueueMenuItem.setOnAction(e -> {
            QueueGUI q = new QueueGUI(relativeContextMenuCoord[0], relativeContextMenuCoord[1]);
            addDraggableShapeToCanvas(q);
        });

        addConnectionMenuItem.setOnAction(e -> {
            if(source != null && destination != null &&
                    !(source instanceof QueueGUI && destination instanceof QueueGUI) &&
                    !(source instanceof MachineGUI && destination instanceof MachineGUI)
            ) {
                if(source instanceof MachineGUI) {
                    for(Arrow a: allConnections)
                        if(a.source == source)
                            return;
                }
                Arrow arrow = new Arrow(source, destination);
                allConnections.add(arrow);
                canvas.getChildren().add(arrow);
                clearSelection(null);
            }
        });

        deleteShapeMenuItem.setOnAction(e -> {
            if(source != null) {
                canvas.getChildren().remove(source);
                ArrayList<Arrow> toBeDeleted = new ArrayList<Arrow>();
                for (Arrow arrow: allConnections) {
                    if(arrow.source == source || arrow.destination == source) {
                        canvas.getChildren().remove(arrow);
                        toBeDeleted.add(arrow);
                    }
                }
                allConnections.removeAll(toBeDeleted);
                for(Arrow a : allConnections)
                    System.out.println(a.toString());
                clearSelection(null);
            }
        });


        canvasMenu.getItems().addAll(addMachineMenuItem, addQueueMenuItem, addConnectionMenuItem, deleteShapeMenuItem);
    }


    public SimulationCanvas() {
        initCanvasMenu();
    }

    @FXML
    public void initialize() {
        navbar.simulationCanvas = this;
    }


    public void onShapeSelected(MouseEvent mouseEvent, DraggableShape m) {
        System.out.println("Child");
        if(source == null) {
            source = m;
            m.setShapeStroke(Color.AQUA);
        }
        else if (source == m)
            return;
        else if(destination == null) {
            destination = m;
            m.setShapeStroke(Color.TOMATO);
        }
        else {
            destination.setShapeStroke(null);
            destination = m;
            m.setShapeStroke(Color.TOMATO);
        }
        mouseEvent.consume();
    }

    public void showCanvasContextMenu(ContextMenuEvent contextMenuEvent) {
        canvasMenu.show(canvas, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());//show with 3 args takes coords relative to screen
        relativeContextMenuCoord[0] = contextMenuEvent.getX();
        relativeContextMenuCoord[1] = contextMenuEvent.getY();
    }

    public void onShapeDragged(MouseEvent mouseEvent, DraggableShape s) {
        if(mouseEvent.getButton() == MouseButton.PRIMARY) {
            s.setXProperty(mouseEvent.getX());
            System.out.println("Layout: " + s.getXProperty());
            System.out.println("Mouse: " + mouseEvent.getX());
            s.setYProperty(mouseEvent.getY());
        }
    }


    public void clearSelection(MouseEvent mouseEvent) {
        if(mouseEvent == null || mouseEvent.getButton() == MouseButton.PRIMARY) {
            System.out.println("Clear Selection");
            if (source != null)
                source.setShapeStroke(null);
            if (destination != null)
                destination.setShapeStroke(null);
            source = null;
            destination = null;
        }
    }


    public <S extends Shape & DraggableShape> void addDraggableShapeToCanvas(S shape){
        if (!canvas.getChildren().contains(shape)) {
            shape.setOnMouseClicked(mouseEvent -> onShapeSelected(mouseEvent, shape));
            shape.setOnMouseDragged(mouseEvent -> onShapeDragged(mouseEvent, shape));
            canvas.getChildren().add(shape);
        }
    }
}
