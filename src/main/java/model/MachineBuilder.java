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


    public MachineGUI machineGUI;

    public MachineBuilder(){}

    public void addSource(Queue q){
        sourceQueues.add(q);
    }

    public void setDestination(Queue q){
        destinationQueue = q;
    }

    public void setMachineGUI(MachineGUI machineGUI) {
        this.machineGUI = machineGUI;
    }

    public Machine getResult(){
        Queue[] in = new Queue[sourceQueues.size()];
        for (int i = 0;i < sourceQueues.size();i++) {
            in[i] = sourceQueues.get(i);
        }

        for (int i = 0;i < sourceQueues.size();i++) {
            System.out.println("equals " + in[i].equals(sourceQueues.get(i)));
            System.out.println("HashCode " + (in[i].hashCode() == sourceQueues.get(i).hashCode()));
            System.out.println("Identity " + (in[i] == sourceQueues.get(i)));
        }
        //TODO generate time and color
        Random r = new Random();
        final int millisecondsPerSecond = 1000;
        final int machineProcessingTime  = (r.nextInt(4)+1) * millisecondsPerSecond;

        Machine m = new Machine(in , destinationQueue, machineProcessingTime
                , Color.color(0,0.5,0));
        m.machineGUI = this.machineGUI;
        return m;
    }
}
