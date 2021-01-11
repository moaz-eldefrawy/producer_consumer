package model;

public class Machine implements Runnable{
    private Queue[] in;
    private Queue out;
    private String colour;
    private long time;
    private Product currentProduct;
    private volatile boolean stop = false;

    public Machine(Queue[] in, Queue out, long time, String colour){
        this.in = in;
        this.out = out;
        this.time = time;
        this.colour = colour;
    }

    private void processProduct(){
        try{
            Thread.sleep(time);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        out.enqueue(currentProduct);
        currentProduct = null;
    }

    public boolean isReady(){
        return currentProduct == null;
    }

    public synchronized boolean putProduct(Product product){
        if(isReady()){
            currentProduct = product;
            return true;
        }
        return false;
    }

    public synchronized void register(){
        for(Queue q : in){
            q.registerMachine(this);
        }
        while(currentProduct == null){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        processProduct();
    }

    public void stop(){
        this.stop = true;
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
