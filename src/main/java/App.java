import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;


public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("adel shakal");

        Group layout = new Group();
        for (int i = 0; i < 5; i++) {
            Rectangle r = new Rectangle();
            r.setY(i * 110);
            r.setWidth(100);
            r.setHeight(100);
            r.setTranslateX(50);
            r.setTranslateY(50);
            r.setFill(Color.RED);
            r.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    r.setX(mouseEvent.getX() - );
                    r.setY(mouseEvent.getY() - );
                }
            });
            layout.getChildren().add(r);
        }
        Scene scene = new Scene(layout, 1920, 1080);
        stage.setScene(scene);
        stage.show();

        stage.show();
    }}
