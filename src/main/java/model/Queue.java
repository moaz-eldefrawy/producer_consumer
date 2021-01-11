package model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.locks.Lock;

public class Queue {
    private final java.util.Queue<Product> internal;
    private final java.util.Queue<Machine> waitingList;
    //Lock enQLock; On second thoughts, i'll use the built in queues..
   // Lock deQLock;

    public Queue(){
        internal = new LinkedBlockingQueue<>();
        waitingList = new LinkedBlockingQueue<>();
    }
    public Queue(int m){
        internal = new LinkedBlockingQueue<>();
        waitingList = new ArrayBlockingQueue<>(m);
    }

    public void enqueue(Product product){
        boolean done = false;
        while (!waitingList.isEmpty()){
            Machine m = waitingList.poll();
            synchronized (m){
                if(m.putProduct(product)){
                    m.notify();
                    done = true;
                }
            }
        }
        if(!done){
            internal.offer(product);
        }
    }

    public Product dequeue(){
        return internal.poll();
    }

    public boolean isEmpty(){
        return internal.isEmpty();
    }

    public void registerMachine(Machine m){
        waitingList.offer(m);
    }

    public int size(){
        return internal.size();
    }
}
