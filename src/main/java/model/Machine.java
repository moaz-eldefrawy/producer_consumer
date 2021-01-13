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
        log.offer(new Memento(nextState,simStart - System.currentTimeMillis()));
    }

    /**pops events from logs and displays them*/
    private void replay(){
        long currentReplayStamp = 0;
        try {
            while(!log.isEmpty()){
                Memento memento = log.remove(); //event: processing next product
                Thread.sleep(memento.timestamp - currentReplayStamp); //sleep till its time comes
                currentReplayStamp = memento.timestamp; //ready for pushing event

                for(Queue q : in){ //refresh input queues
                    q.replay();
                }
                machineGUI.setFill(memento.nextState);//change colour

                memento = log.remove(); //event: pushing the product to out
                Thread.sleep(memento.timestamp - currentReplayStamp);//sleep till its time comes
                currentReplayStamp = memento.timestamp;//ready for next product

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

    /**
     * safe way to stop the machine:
     * stops only if no product is currently being processed*/
    public void stop(){
        this.stop = true;
        machineThread.interrupt();
    }

    /**stops the current simulation and replays it*/
    public void startReplay(){
        replay = true;
        machineThread.interrupt();
    }

    /**runs a single simulation and can replay it*/
    public void run(){
        init();
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

    public Color getColour(){
        if(currentProduct == null){
            return this.colour;
        }else{
            return currentProduct.colour;
        }
    }

    /**for testing*/
    public String printLog(){
        return "";
    }

    public Queue[] getSources (){
        return in;
    }
    public Queue getDestination (){
        return out;
    }
}
