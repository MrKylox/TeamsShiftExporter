package edu.ITSolutions.Export;



import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Teams-Shifts Hub");
        VBox main_vbox = new VBox();
        main_vbox.setPadding( new Insets( 10 ));
        main_vbox.setBackground(
            new Background(
                new BackgroundFill(Color.BLACK, new CornerRadii(0), new Insets(0))
            ));

        Pane p = new Pane();

        Arc largeArc = new Arc(0, 0, 100, 100, 270, 90);
        largeArc.setFill(Color.web("0x59291E"));
        largeArc.setType(ArcType.ROUND);

        Arc backgroundArc = new Arc(0, 0, 160, 160, 270, 90);
        backgroundArc.setFill( Color.web("0xD96F32"));
        backgroundArc.setType( ArcType.ROUND);

        Arc smArc1 = new Arc(0, 160, 30, 30, 270, 180);
        smArc1.setFill(Color.web("0xF2A444"));
        smArc1.setType(ArcType.ROUND);

        Circle smCircle = new Circle(
            160/Math.sqrt(2.0), 160/Math.sqrt(2.0), 30, Color.web("0xF2A444")
        );

        Arc smArc2 = new Arc(160, 0, 30, 30, 180, 180);
        smArc2.setFill(Color.web("0xF2A444"));
        smArc2.setType(ArcType.ROUND);

        //568.20: X, 320.20:Y
        Arc medArc = new Arc(568-20, 320-20, 60, 60, 90, 90);
        medArc.setFill(Color.web("0xD9583B"));
        medArc.setType(ArcType.ROUND);

        p.getChildren().addAll(backgroundArc, largeArc, smArc1, smCircle, smArc2, medArc);

        main_vbox.getChildren().add(p);

        Scene scene = new Scene(main_vbox);
        scene.setFill(Color.BLACK);

        primaryStage.setTitle("Pane App");
        primaryStage.setScene(scene);
        primaryStage.setWidth(568);
        primaryStage.setHeight(320);
        primaryStage.setMaximized(true);
        primaryStage.show();
        
    }



    public static void main(String[] args) {
        launch(args);
    }
}
