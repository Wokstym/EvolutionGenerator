package agh.cs.gui;

import agh.cs.map.Map;
import agh.cs.utils.DayOperator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Timer;
import java.util.TimerTask;

public class EvolutionFrame extends Application {

    static final int PIXEL_SIZE = 48;
    static final int FREQUENCY = 15;

    public static Map map;
    @Setter
    private DayOperator dayOperator;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        JSONObject json = (JSONObject) new JSONParser().parse(new FileReader("src/main/resources/parameters.json"));
        Map map = new Map(
                ((Long) json.get("mapWidth")).intValue(),
                ((Long) json.get("mapHeight")).intValue(),
                ((Long) json.get("startEnergy")).intValue(),
                ((Long) json.get("moveEnergy")).intValue(),
                ((Long) json.get("plantEnergy")).intValue(),
                ((Long) json.get("carrionEnergy")).intValue(),
                ((Long) json.get("jungleRatio")).intValue(),
                ((Long) json.get("nrOfStartScavenger")).intValue() > 0);

        Pane rootNode = new Pane();
        EvolutionScene eScene = new EvolutionScene(rootNode, PIXEL_SIZE * map.upperRightMap.x, PIXEL_SIZE * map.upperRightMap.y, map.lowerLeftJungle, map.upperRightJungle);

        map.addPropertyChangeListener(eScene);
        this.dayOperator = new DayOperator(
                map,
                ((Long) json.get("nrOfStartHerbivores")).intValue(),
                ((Long) json.get("nrOfStartScavenger")).intValue(),
                ((Long) json.get("startEnergy")).intValue(),
                ((Long) json.get("moveEnergy")).intValue()
        );
        stage.setTitle("Evolution");
        stage.setScene(eScene);
        stage.show();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    dayOperator.nextDay();
                });
            }
        }, 10000, 1000 / FREQUENCY);
    }
}


