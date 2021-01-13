package model;

import GUI.DraggableShape;
import GUI.QueueGUI;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.locks.Lock;


public class Queue{
    private final java.util.Queue<Product> internal;
    private final java.util.Queue<Machine> waitingList;
    //Lock enQLock; On second thoughts, i'll use the built in queues..
    // Lock deQLock;
    public QueueGUI queueGUI;

    public Queue(){
        internal = new LinkedBlockingQueue<>();
        waitingList = new LinkedBlockingQueue<>();
    }
    public Queue(int m){
        internal = new LinkedBlockingQueue<>();
        waitingList = new ArrayBlockingQueue<>(m);
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
    }

    /**pulls product from queue*/
    public Product dequeue(){
        return internal.poll();
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

}
