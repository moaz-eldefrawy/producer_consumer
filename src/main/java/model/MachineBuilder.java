package model;

import GUI.MachineGUI;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MachineBuilder{
    public Queue destinationQueue = null;
    public ArrayList<Queue> sourceQueues = new ArrayList<>();

    public MachineBuilder(){}

    public void addSource(Queue q){
        sourceQueues.add(q);
    }

    public void setDestination(Queue q){
        destinationQueue = q;
    }

    public Machine getResult(){



        Queue[] in = new Queue[sourceQueues.size()];
        for (int i = 0;i < sourceQueues.size();i++) {
            in[i] = sourceQueues.get(i);

            System.out.println("equals " + in[i].equals(sourceQueues.get(i)));
            System.out.println("HashCode " + (in[i].hashCode() == sourceQueues.get(i).hashCode()));
        }

        //TODO generate time and color
        Random r = new Random();
        return new Machine(in , destinationQueue, r.nextLong(), Color.color(r.nextDouble(),r.nextDouble(),r.nextDouble()));
    }
}
