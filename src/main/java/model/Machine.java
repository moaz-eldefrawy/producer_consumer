package model;

import GUI.MachineGUI;
import javafx.scene.paint.Color;

import java.util.concurrent.LinkedBlockingQueue;

public class Machine implements Runnable{
    private final Queue[] in;
    private final Queue out;
    private final Color colour;
    private final long time;
    private Thread machineThread;
    private Product currentProduct;
    private volatile boolean stop;
    private volatile boolean replay;
    private long simStart;
    public MachineGUI machineGUI;
    private java.util.Queue<Memento> log;

    /**for GUI based simultaion*/
    public Machine(Queue[] in, Queue out, long time, Color colour, MachineGUI machineGUI) {
        this(in,out,time,colour);
        this.machineGUI = machineGUI;
        machineGUI.setFill(colour);
    }
    /**for testing*/
    public Machine(Queue[] in, Queue out, long time, Color colour) {
        this.in = in;
        this.out = out;
        this.time = time;
        this.colour = colour;
        machineGUI = new MachineGUI(0,0);
        init();
    }

    private static class Memento{
        Color nextState;
        long timestamp;
        Memento(Color nextState, long timestamp){
            this.nextState = nextState;
            this.timestamp = timestamp;
        }
    }

    /**processes current product and forwards it to next queue*/
    private void processProduct(){
        report();
        try{
            Thread.sleep(time);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        out.enqueue(currentProduct);
        machineGUI.setFill(Color.WHITE);
        try{
            Thread.sleep(100);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        machineGUI.setFill(Color.GREEN);
        currentProduct = null;
        report();
    }

    /**reports event to GUI and stores it in log*/
    private void report(){
        Color nextState = getColour();
        System.out.println(nextState.toString());
        System.out.println(machineGUI);
        machineGUI.setFill(nextState);
        log.offer(new Memento(nextState,System.currentTimeMillis() - simStart));
    }

    /**pops events from logs and displays them*/
    private void replay(){
        long currentReplayStamp = 0, tempTime;
        int logSize = log.size()/2;
        machineGUI.setFill(colour);
        out.resetForReplay();
        for (Queue q: in){
            q.resetForReplay();
        }
        try {
            for(int i = 0; i < logSize; i++){
                Memento memento = log.remove(); //event: processing next product
                tempTime = memento.timestamp - currentReplayStamp;
                System.out.println("sleep time = " + tempTime);
                Thread.sleep(tempTime); //sleep till its time comes
                currentReplayStamp = memento.timestamp; //ready for pushing event
                log.offer(memento); //if we need to replay again

                for(Queue q : in){ //refresh input queues
                    q.replay();
                }
                machineGUI.setFill(memento.nextState);//change colour

                memento = log.remove(); //event: pushing the product to out
                tempTime = memento.timestamp - currentReplayStamp;
                Thread.sleep(tempTime);//sleep till its time comes
                currentReplayStamp = memento.timestamp;//ready for next product
                log.offer(memento); //if we need to replay again

                out.replay();
                machineGUI.setFill(memento.nextState);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void init(){
        log = new LinkedBlockingQueue<>();
        stop = false;
        replay = false;
        simStart = System.currentTimeMillis();
    }

    /**
     * @return true if ready to process (no product currently being processed)*/
    public boolean isReady(){
        return currentProduct == null;
    }

    /**sets the current product to be processed
     * @return true if ready to process and received product, false otherwise*/
    public synchronized boolean putProduct(Product product){
        if(isReady()){
            currentProduct = product;
            return true;
        }
        return false;
    }

    /**
     * registers this machine to all the input queues it is connected to*/
    public synchronized void register(){
        for(Queue q : in){
            if(!q.registerMachine(this)){ //if this queue got a product in the meantime
                putProduct(q.dequeue()); //accept it
                break;
            }
        }
        while(currentProduct == null){
            try {
                this.wait();
            } catch (InterruptedException e) {
                if(!stop && !replay) //the interrupt was not caused by stop method nor startReplay
                    e.printStackTrace();
                else
                    return;
            }
        }
        processProduct();
    }


    public Color getColour(){
        if(currentProduct == null){
            return this.colour;
        }else{
            return currentProduct.colour;
        }
    }

    /**for testing*/
    public String printLog(){
        Memento[] temp = log.toArray(Memento[]::new);
        StringBuilder sb = new StringBuilder();
        sb.append(machineThread.getName());
        sb.append(": ");
        for(Memento m : temp){
            sb.append(m.nextState);
            sb.append(", ");
        }
        return sb.toString();
    }

    public Queue[] getSources (){
        return in;
    }
    public Queue getDestination (){
        return out;
    }

    /*control methods*/

    /**
     * safe way to stop the machine:
     * stops only if no product is currently being processed*/
    public void stop(){
        this.stop = true;
        machineThread.interrupt();
    }

    /**stops the current simulation and replays it*/
    public void startReplay(){
        stop = false;
        replay = true;
        machineThread.interrupt();
    }

    /**restarts the simulation from scratch with the same machines and queues*/
    public void restart(){
        init();
    }

    /**runs a single simulation and can replay it*/
    public void run(){
        machineThread = Thread.currentThread();
        while (!stop && !replay){
            boolean found = false;
            for (Queue q : in){
                currentProduct = q.dequeue();
                if(currentProduct != null){
                    found = true;
                    break;
                }
            }
            if(found){
                processProduct();
            }else{ //no input queue had a product
                register();
            }
        }

        if(stop) return;

        replay();
    }

}
