package model;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MachineTest {
    @Test
    public void testUnbelievablyHappyCase(){ //1 machine, 2 queues, one product
        Queue q_in = new Queue();
        Queue q_out = new Queue();
        Product p1 = new Product(Color.BLUE);
        Machine m1 = new Machine(new Queue[]{q_in}, q_out, 1000, Color.RED);
        Thread t1 = new Thread(m1, "Machine 1");
        q_in.enqueue(p1);

        assertEquals(1,q_in.size());
        assertEquals(0,q_out.size());
        assertTrue(m1.isReady());

        t1.start();

        try {
            Thread.sleep(200);
            assertEquals(Color.BLUE, m1.getColour());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Color.RED, m1.getColour());
        System.out.println(m1.printLog());
        System.out.println(q_out.printLog("Q_out"));
        System.out.println(q_in.printLog("Q_in"));
    }

    @Test
    public void testQuiteHappyCase(){ //1 machine, 2 queues, many products
        Queue q_in = new Queue();
        Queue q_out = new Queue();
        Machine m1 = new Machine(new Queue[]{q_in}, q_out, 1000, Color.RED);
        Thread t1 = new Thread(m1, "Machine 1");

        q_in.enqueue(new Product(Color.BLUE));
        q_in.enqueue(new Product(Color.PURPLE));
        q_in.enqueue(new Product(Color.GREY));
        q_in.enqueue(new Product(Color.ORANGE));
        q_in.enqueue(new Product(Color.TURQUOISE));

        assertEquals(5,q_in.size());
        assertEquals(0,q_out.size());
        assertTrue(m1.isReady());

        t1.start();

        try {
            Thread.sleep(200);
            assertEquals(Color.BLUE, m1.getColour());
            assertEquals(0,q_out.size());
            Thread.sleep(1000);
            assertEquals(Color.PURPLE, m1.getColour());
            assertEquals(1,q_out.size());
            Thread.sleep(1000);
            assertEquals(Color.GREY, m1.getColour());
            assertEquals(2,q_out.size());
            Thread.sleep(3000);
            q_in.enqueue(new Product(Color.GOLD));
            Thread.sleep(200);
            assertEquals(Color.GOLD, m1.getColour());
            assertEquals(5,q_out.size());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Color.RED, m1.getColour());

        System.out.println(m1.printLog());
        System.out.println(q_out.printLog("Q_out"));
        System.out.println(q_in.printLog("Q_in"));
    }

    @Test
    public void testQuiteSadCase(){ //1 machine, 3 queues, many products
        Queue[] q_in = new Queue[]{new Queue(), new Queue()};
        Queue q_out = new Queue();
        Machine m1 = new Machine(q_in, q_out, 1000, Color.RED);
        Thread t1 = new Thread(m1, "Machine 1");
        q_in[0].enqueue(new Product(Color.BLUE));
        q_in[0].enqueue(new Product(Color.PURPLE));
        q_in[1].enqueue(new Product(Color.GREY));
        q_in[1].enqueue(new Product(Color.ORANGE));
        q_in[1].enqueue(new Product(Color.TURQUOISE));

        assertTrue(m1.isReady());

        t1.start();

        try {
            for(long i = 900; i < 6000; i+=900){
                System.out.println(m1.getColour()); //this thing here is funny because
                // it shows that Machine cannot be polled for colour,
                // it should register the new colour itself to the main/mediator class
                Thread.sleep(i);
            }
            q_in[0].enqueue(new Product(Color.GOLD));
            q_in[1].enqueue(new Product(Color.WHITE));
            Thread.sleep(900);
            System.out.println(m1.getColour());
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(Color.RED, m1.getColour());
        assertTrue(q_in[0].isEmpty());
        assertTrue(q_in[1].isEmpty());
        assertEquals(7, q_out.size());

        System.out.println(m1.printLog());
        System.out.println(q_out.printLog("Q_out"));
        System.out.println(q_in[0].printLog("Q_in_0"));
        System.out.println(q_in[1].printLog("Q_in_1"));
    }

    @Test
    public void testUnbelievablySadCase(){ //2 machines, 3 queues, many products
        Queue[] q_in = new Queue[]{new Queue(), new Queue()};
        Queue q_out = new Queue();
        Machine m1 = new Machine(q_in, q_out, 1000, Color.RED);
        Machine m2 = new Machine(q_in, q_out, 1500, Color.DARKRED);

        Thread t1 = new Thread(m1, "Machine 1");
        Thread t2 = new Thread(m2, "Machine 2");

        q_in[0].enqueue(new Product(Color.BLUE));
        q_in[0].enqueue(new Product(Color.PURPLE));
        q_in[1].enqueue(new Product(Color.GREY));
        q_in[1].enqueue(new Product(Color.ORANGE));
        q_in[1].enqueue(new Product(Color.TURQUOISE));

        assertTrue(m1.isReady());
        assertTrue(m2.isReady());

        t1.start();
        t2.start();

        try {
            Thread.sleep(5000);
            q_in[0].enqueue(new Product(Color.GOLD));
            q_in[1].enqueue(new Product(Color.WHITE));
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(q_in[0].isEmpty());
        assertTrue(q_in[1].isEmpty());
        assertEquals(7, q_out.size());

        System.out.println(m1.printLog());
        System.out.println(m2.printLog());
        System.out.println(q_out.printLog("Q_out"));
        System.out.println(q_in[0].printLog("Q_in_0"));
        System.out.println(q_in[1].printLog("Q_in_1"));

    }
}