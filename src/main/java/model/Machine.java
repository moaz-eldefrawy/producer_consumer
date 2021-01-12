package model;

public class Machine implements Runnable{
    private final Queue[] in;
    private final Queue out;
    private final String colour;
    private final long time;
    private Product currentProduct;
    private volatile boolean stop = false;

    public Machine(Queue[] in, Queue out, long time, String colour){
        this.in = in;
        this.out = out;
        this.time = time;
        this.colour = colour;
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
        currentProduct = null;
        report();
    }

    /**reports to main/ mediator any colour change
     * currently only a stub*/
    private void report(){//still needs proper synchronization, check with main/monitor
        System.out.print(Thread.currentThread().getName());
        System.out.print("'s colour is ");
        System.out.println(this.getColour());
    }

    /**
     * @return true if ready to process (no product currently being processed*/
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
                if(!stop) //the interrupt was not caused by stop method
                    e.printStackTrace();
                else
                    return;
            }
        }
        processProduct();
    }

    /**
     * safe way to stop the machine:
     * stops only if no product is currently being processed AND not waiting for any queue*/
    public synchronized void stop(){
        this.stop = true;
        Thread.currentThread().interrupt();
    }

    public void run(){
        while (!stop){
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

    }

    public String getColour(){
        if(currentProduct == null){
            return this.colour;
        }else{
            return currentProduct.colour;
        }
    }
}
