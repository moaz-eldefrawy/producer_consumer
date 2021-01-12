package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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


    private void initCanvasMenu() {
        canvasMenu = new ContextMenu();
        MenuItem addMachine = new MenuItem("Add Machine");
        MenuItem addQueue = new MenuItem("Add Queue");
        MenuItem addConnection = new MenuItem("Add Connection");
        MenuItem delete = new MenuItem("Delete");

        addMachine.setOnAction(e -> {
            MachineGUI m = new MachineGUI(canvasMenu.getX(), canvasMenu.getY());
            m.setOnMouseClicked(mouseEvent -> onShapeSelected(mouseEvent, m));
            m.setOnMouseDragged(mouseEvent -> onShapeDragged(mouseEvent, m));
            canvas.getChildren().add(m);
        });

        addQueue.setOnAction(e -> {
            QueueGUI q = new QueueGUI(canvasMenu.getX(), canvasMenu.getY());
            q.setOnMouseClicked(mouseEvent -> onShapeSelected(mouseEvent, q));
            q.setOnMouseDragged(mouseEvent -> onShapeDragged(mouseEvent, q));
            canvas.getChildren().add(q);

        });

        addConnection.setOnAction(e -> {
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

        delete.setOnAction(e -> {
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


        canvasMenu.getItems().addAll(addMachine, addQueue, addConnection, delete);
    }


    public SimulationCanvas() {
        initCanvasMenu();
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
        canvasMenu.show(canvas, contextMenuEvent.getX(), contextMenuEvent.getY());
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
}
