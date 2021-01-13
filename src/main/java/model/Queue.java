package model;

import GUI.DraggableShape;
import GUI.MachineGUI;
import GUI.QueueGUI;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.locks.Lock;


public class Queue{
    private final java.util.Queue<Product> internal = new LinkedBlockingQueue<>();
    private final java.util.Queue<Machine> waitingList;
    private final java.util.Queue<String> log = new LinkedBlockingQueue<>();
    //Lock enQLock; On second thoughts, i'll use the built in queues..
    // Lock deQLock;
    public QueueGUI queueGUI;

    public Queue(){
        waitingList = new LinkedBlockingQueue<>();
        queueGUI = new QueueGUI(0,0); //TODO how in blazes is this supposed to be set?
    }
    public Queue(int m){
        waitingList = new ArrayBlockingQueue<>(m);
    }


    public synchronized void clear(){
        internal.clear();
        log.clear();
    }
    /**
     * adds product to this queue*/
    public synchronized void enqueue(Product product){
        boolean done = false;
        while (!waitingList.isEmpty()){
            Machine m = waitingList.poll();
            synchronized (m){
                if(m.putProduct(product)){
                    m.notify();
                    done = true;
                    break;
                }
            }
        }

        if(!done){
            internal.offer(product);
        }
        report();
    }

    /**pulls product from queue*/
    public Product dequeue(){
        Product ans = internal.poll();
        report();
        return ans;
    }

    /**reports to GUI and updates log*/
    private void report(){
        String nextState = Integer.toString(internal.size());
        queueGUI.setText(nextState);
        log.offer(nextState);
    }

    public void updateText(){
        queueGUI.setText(Integer.toString(internal.size()));
    }

    public boolean isEmpty(){
        return internal.isEmpty();
    }

    /**
     * if no products in the queue, register machine as ready
     * @return true if added to waiting list, false if queue already has a product*/
    public boolean registerMachine(Machine m){
        if(internal.isEmpty()){
            waitingList.offer(m);
            return true;
        }
        return false;
    }

    public int size(){
        return internal.size();
    }

    /**pops the next event and sends to GUI*/
    void replay(){
        String nextState = log.remove(); //should never be null, but idk
        queueGUI.setText(nextState);
        log.offer(nextState); //if we need to replay multiple times
    }

    public String printLog( String name ){
        String[] temp = log.toArray(String[]::new);
        StringBuilder sb = new StringBuilder(name);
        sb.append(": ");
        for(String s : temp){
            sb.append(s);
            sb.append(", ");
        }
        return sb.toString();
    }

    public void setQueueGUI(QueueGUI queueGUI) {
        this.queueGUI = queueGUI;
        report();
    }
}
