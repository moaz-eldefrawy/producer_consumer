package GUI;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.Machine;
import model.MachineBuilder;
import model.Product;
import model.Queue;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetMachinesTest {
    @Test
    void test(){
        Pane canvas = new Pane();

        MachineGUI machineGUI_1 = new MachineGUI(0,0);
        MachineGUI machineGUI_2 = new MachineGUI(0,0);

        QueueGUI queueGUI_1 = new QueueGUI(0,0);
        QueueGUI queueGUI_2 = new QueueGUI(0,0);

        Arrow q1_m1 = new Arrow(queueGUI_1, machineGUI_1);
        Arrow q1_m2 = new Arrow(queueGUI_1, machineGUI_2);
        Arrow m1_q2 = new Arrow(machineGUI_1, queueGUI_2);
        Arrow m2_q2 = new Arrow(machineGUI_2, queueGUI_2);

        canvas.getChildren().add(machineGUI_1);
        canvas.getChildren().add(machineGUI_2);
        canvas.getChildren().add(queueGUI_1);
        canvas.getChildren().add(queueGUI_2);

        canvas.getChildren().add(q1_m1);
        canvas.getChildren().add(q1_m2);
        canvas.getChildren().add(m1_q2);
        canvas.getChildren().add(m2_q2);

        /**
         *          |->  m1  -|
         *     q1  -|         |-> q2
         *          |->  m2  -|
         */

        ArrayList<Machine> machines = getMachines(canvas);

        assertEquals(machines.get(0).getDestination(), machines.get(1).getDestination());
        for (int i = 0;i < machines.get(0).getSources().length;i++){
            assertTrue(machines.get(0).getSources()[i].equals(
                    machines.get(1).getSources()[i]));

            assertTrue(machines.get(0).getSources()[i] ==
                                machines.get(1).getSources()[i]);
        }



        //This part requires internalList of queues to be visible
        // so I can check that putting a product from a machine will be visible to the
        // other machine
        Product pr = new Product(new Color(0,0,0,0));
        //machines.get(0).getDestination().internal.add(pr);
        //assertTrue(machines.get(1).getDestination().internal.contains(pr));


        //machines.get(0).getSources()[0].internal.add(pr);
        //assertTrue(machines.get(1).getSources()[0].internal.contains(pr));

        //machines.get(1).getSources()[0].internal.remove();
        //assertTrue(machines.get(0).getSources()[0].internal.isEmpty());
    }

    public ArrayList<Machine> getMachines(Pane canvas){
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


        for (Node node: canvas.getChildren()){
            if (node.getClass() != Arrow.class)
                continue;
            Arrow arrow = (Arrow) node;
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



        ArrayList<Machine> machines = new ArrayList<>();
        for (MachineBuilder builder: machineInstances.values()){
            machines.add(builder.getResult());
        }

        // Printing test
        System.out.println("Queues: ");
        for (Queue q : queueInstances.values())
            System.out.println(q.toString());
        System.out.println("Machines: ");
        for (Machine machine : machines){
            System.out.println(machine.toString());
            System.out.println("Sources : " + machine.getSources().toString());
            System.out.println("Destination : " + machine.getDestination().toString());
        }
        System.out.println("=---------------------------------=");



        return machines;
    }

}
