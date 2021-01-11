package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MachineTest {
    /*@Test
    public void testUnbelievablyHappyCase(){ //1 machine, 2 queues, one product
        Queue q_in = new Queue();
        Queue q_out = new Queue();
        Product p1 = new Product("blue");
        Machine m1 = new Machine(new Queue[]{q_in}, q_out, 1000, "red");
        Thread t1 = new Thread(m1, "Machine 1");
        q_in.enqueue(p1);

        assertEquals(1,q_in.size());
        assertEquals(0,q_out.size());
        assertTrue(m1.isReady());

        t1.start();

        try {
            Thread.sleep(200);
            assertEquals("blue", m1.getColour());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("red", m1.getColour());
    }

    @Test
    public void testQuiteHappyCase(){ //1 machine, 2 queues, many products
        Queue q_in = new Queue();
        Queue q_out = new Queue();
        Machine m1 = new Machine(new Queue[]{q_in}, q_out, 1000, "red");
        Thread t1 = new Thread(m1, "Machine 1");

        q_in.enqueue(new Product("blue"));
        q_in.enqueue(new Product("purple"));
        q_in.enqueue(new Product("grey"));
        q_in.enqueue(new Product("orange"));
        q_in.enqueue(new Product("turquoise"));

        assertEquals(5,q_in.size());
        assertEquals(0,q_out.size());
        assertTrue(m1.isReady());

        t1.start();

        try {
            Thread.sleep(200);
            assertEquals("blue", m1.getColour());
            assertEquals(0,q_out.size());
            Thread.sleep(1000);
            assertEquals("purple", m1.getColour());
            assertEquals(1,q_out.size());
            Thread.sleep(1000);
            assertEquals("grey", m1.getColour());
            assertEquals(2,q_out.size());
            Thread.sleep(3000);
            q_in.enqueue(new Product("gold"));
            Thread.sleep(200);
            assertEquals("gold", m1.getColour());
            assertEquals(5,q_out.size());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("red", m1.getColour());
    }

    @Test
    public void testQuiteSadCase(){ //1 machine, 3 queues, many products
        Queue[] q_in = new Queue[]{new Queue(), new Queue()};
        Queue q_out = new Queue();
        Machine m1 = new Machine(q_in, q_out, 1000, "red");
        Thread t1 = new Thread(m1, "Machine 1");
        q_in[0].enqueue(new Product("blue"));
        q_in[0].enqueue(new Product("purple"));
        q_in[1].enqueue(new Product("grey"));
        q_in[1].enqueue(new Product("orange"));
        q_in[1].enqueue(new Product("turquoise"));

        assertTrue(m1.isReady());

        t1.start();

        try {
            for(long i = 900; i < 6000; i+=900){
                System.out.println(m1.getColour()); //this thing here is funny because
                // it shows that Machine cannot be polled for colour,
                // it should register the new colour itself to the main/mediator class
                Thread.sleep(i);
            }
            q_in[0].enqueue(new Product("gold"));
            q_in[1].enqueue(new Product("white"));
            Thread.sleep(900);
            System.out.println(m1.getColour());
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("red", m1.getColour());
        assertTrue(q_in[0].isEmpty());
        assertTrue(q_in[1].isEmpty());
        assertEquals(7, q_out.size());
    }*/
    
}