package GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.*;

public class MainWindow extends Group {
    double iconsSize = 50;
    HBox box = new HBox();


    public MainWindow(){
        super();

        /*  Machine Button */
        CustomButton addMachineBtn = new CustomButton("machine.png");
        addMachineBtn.setOnMouseClicked(e -> System.out.println("Machine Clicked"));

        /*  Queue Button */
        CustomButton addQueueBtn = new CustomButton("queue.png");
        addQueueBtn.setOnMouseClicked(e -> System.out.println("Queue Clicked"));

        /*  Play Button  */
        CustomButton playButton = new CustomButton("play.png");
        playButton.setOnMouseClicked(e -> System.out.println("Start Clicked"));

        /*  Replay Button  */
        CustomButton rePlayButton = new CustomButton("replay.png");
        rePlayButton.setOnMouseClicked(e -> System.out.println("Replay Clicked"));

        /*  Composing the HBox   */
        box.setBackground(new Background(new BackgroundFill(gradient(), CornerRadii.EMPTY, Insets.EMPTY)));
        box.setAlignment(Pos.CENTER_LEFT);

        getChildren().add(box);
    }

    Paint gradient(){
        Stop[] stops = new Stop[] {
                new Stop(0, Color.LIGHTBLUE),
                new Stop(1, Color.LIGHTCYAN)
        };
        return new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
    }


    private class CustomButton extends Button{
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

            box.getChildren().add(this);
        }

    }
}
