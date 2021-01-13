package GUI;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import model.*;

import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


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
        //System.out.println("Child");
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
            /*System.out.println("Layout: " + s.getXProperty());
            System.out.println("Mouse: " + mouseEvent.getX());*/
            s.setYProperty(mouseEvent.getY());
        }
    }


    public void clearSelection(MouseEvent mouseEvent) {
        if(mouseEvent == null || mouseEvent.getButton() == MouseButton.PRIMARY) {
            //System.out.println("Clear Selection");
            if (source != null)
                source.setShapeStroke(null);
            if (destination != null)
                destination.setShapeStroke(null);
            source = null;
            destination = null;
        }
    }


    public <S extends Shape & DraggableShape> void addDraggableShapeToCanvas(S shape){
        shape.setOnMouseClicked(mouseEvent -> onShapeSelected(mouseEvent, shape));
        shape.setOnMouseDragged(mouseEvent -> onShapeDragged(mouseEvent, shape));
        canvas.getChildren().add(shape);
    }

    public HashSet<Machine> getMachines(){
        HashMap<DraggableShape, Queue> queueInstances = new HashMap<>();
        HashMap<DraggableShape, MachineBuilder> machineInstances = new HashMap<>();

        //create the queue instances from the QueueGui instances
        //create the MachineBuilder instances from the MachineGUI instances
        for (Node node: canvas.getChildren()){
            //System.out.println("Class = " + node.getClass());
            if (node.getClass() == QueueGUI.class) {
                //System.out.println("it is a queue!");
                queueInstances.put((DraggableShape) node, new Queue());
            }
            else if (node.getClass() == MachineGUI.class) {
                //System.out.println("it is a Machine!");
                machineInstances.put((DraggableShape) node, new MachineBuilder());
            }
        }


        for (Arrow arrow: allConnections){
            //System.out.println("Arrow contecting from " + arrow.source.hashCode() + ", to " + arrow.destination.hashCode());
            //The source is a machine
            if (arrow.source.getClass() == MachineGUI.class) {
                //Get the MachineBuilder instance from the machines HashMap
                //Set the destination to be the Queue instance from the HashMap
                machineInstances.get(arrow.source).setDestination(queueInstances.get(arrow.destination));
            }
            else
                machineInstances.get(arrow.destination).addSource(queueInstances.get(arrow.source));
        }

        // Printing test
        System.out.println("Queues: ");
        for (Queue q : queueInstances.values())
            System.out.println(q.toString());
        System.out.println("Machines: ");
        for (MachineBuilder machine : machineInstances.values()){
            System.out.println(machine.toString());
            System.out.println("Sources : " + machine.sourceQueues.toString());
            System.out.println("Destination : " + machine.destinationQueue.toString());
        }
        System.out.println("=---------------------------------=");

        HashSet<Machine> machines = new HashSet<>();
        for (MachineBuilder builder: machineInstances.values()){
            machines.add(builder.getResult());
        }



        return machines;
    }
}
