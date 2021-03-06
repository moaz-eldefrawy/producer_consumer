package GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Shape;
import model.Machine;
import model.Product;
import model.Queue;

import javax.crypto.Mac;
import javax.xml.transform.Source;
import java.beans.EventHandler;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

public class NavBar extends HBox {
    double iconsSize = 50;
    public SimulationCanvas simulationCanvas;
    DraggableShape curr;

    HashSet<Machine> machines = new HashSet<>();

    public NavBar(){
        super();

        /*  Machine Button */
        CustomButton addMachineBtn = new CustomButton("machine.png");
        /*addMachineBtn.setOnMouseDragged(e -> {
            simulationCanvas.onShapeDragged(e, curr);
        });*/
        addMachineBtn.setOnMousePressed(e -> {
            MachineGUI m = new MachineGUI(e.getSceneX(), e.getSceneY());
            simulationCanvas.addDraggableShapeToCanvas(m);
            curr = m;
        });




        /*  Queue Button */
        CustomButton addQueueBtn = new CustomButton("queue.png");
        /*addQueueBtn.setOnMouseDragged(e -> {
            simulationCanvas.onShapeDragged(e, curr);
        });*/
        addQueueBtn.setOnMousePressed(e -> {
            QueueGUI m = new QueueGUI(e.getSceneX(), e.getSceneY());
            simulationCanvas.addDraggableShapeToCanvas(m);
            simulationCanvas.canvas.getChildren().add(m.text);
            curr = m;
        });


        /*  Play Button  */
        CustomButton playButton = new CustomButton("play.png");
        playButton.setOnMouseClicked(e -> {
            Graph graph = simulationCanvas.getMachines();
            playGraph(graph);

            //System.out.println(Arrays.toString(simulationCanvas.getMachines().toArray()));
        });

        /*  Replay Button  */
        CustomButton rePlayButton = new CustomButton("replay.png");
        rePlayButton.setOnMouseClicked(e -> {

            Graph graph = simulationCanvas.replaySimulation();
            /*System.out.println("Replay Clicked");
            for(Machine machine: machines){
                machine.stop();
                machine.startReplay();
            }*/
        });

        /*  Composing the HBox   */
        setBackground(new Background(new BackgroundFill(gradient(), CornerRadii.EMPTY, Insets.EMPTY)));
        setAlignment(Pos.CENTER_LEFT);


    }

    void playGraph(Graph graph){
        int count = Thread.activeCount();
        System.out.println("currently active threads = " + count);

        for(Machine machine: machines){
            machine.stop();
        }
        machines = graph.machines;
        Iterator<Machine> machine = machines.iterator();
        while(machine.hasNext())
        {
            Machine currMachine = machine.next();
            new Thread(currMachine).start();
        }
    }


    Paint gradient(){
        Stop[] stops = new Stop[] {
                new Stop(0, Color.LIGHTBLUE),
                new Stop(1, Color.LIGHTCYAN)
        };
        return new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
    }


    private class CustomButton extends Button {
        /**
         *
         * @param imagePath: The button will have this image as its graphic.
         *                   The size is controlled by the "iconSize"
         *
         * The constructor sets the image specified in the path, sets the margin, and adds itself to the "box"
         */
        CustomButton(String imagePath){
            super();
            HBox.setMargin(this, new Insets(10, 10, 10, 10));


            //Icon
            ImageView machine_iv = new ImageView(imagePath);
            machine_iv.setFitHeight(iconsSize);
            machine_iv.setPreserveRatio(true);
            this.setGraphic(machine_iv);
            NavBar.this.getChildren().add(this);
        }

    }
}
