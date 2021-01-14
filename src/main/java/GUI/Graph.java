package GUI;

import model.Machine;
import model.Product;
import model.Queue;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Graph {
    public HashSet<Machine> machines;
    public ArrayList<Queue> startQueues;
    public ArrayList<Queue> queues;
    public HashMap<Queue,ArrayList<Product>> initProducts;
    public Integer numOfProducts;
    Graph(){
        machines = new HashSet<Machine>();
        startQueues = new ArrayList<Queue>();
        queues = new ArrayList<>();
        initProducts = new HashMap<Queue,ArrayList<Product>>();
    }

    public void emptyQueues(){
        for(Queue q: queues){
            q.clear();
        }
    }

    public void resetMachines(){
        for(Machine m : machines){
            m.restart();
        }
    }
}
