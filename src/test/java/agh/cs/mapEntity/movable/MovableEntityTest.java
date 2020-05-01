package agh.cs.mapEntity.movable;

import agh.cs.map.Map;
import agh.cs.map.utils.MapDirection;
import agh.cs.map.utils.Vector2d;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MovableEntityTest {



    @Test
    void rotate() {
        Herbivore h = new Herbivore(null,0, null, 0);
        MapDirection dir = h.getDir();
        h.rotate(1);
        dir =dir.shiftDirection(1);
        assertEquals(dir,h.getDir());
        h.rotate(7);
        assertEquals(dir.shiftDirection(7),h.getDir());

    }

    @Test
    void createMyChild() {
        JSONObject json = null;
        try {
            json = (JSONObject) new JSONParser().parse(new FileReader("src/main/resources/parameters.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        assertNotNull(json);
       // Map map = new Map((int) (long)json.get("mapWidth"), (int)(long)json.get("mapHeight"), (int)(long)json.get("startEnergy"), (int)(long)json.get("moveEnergy"), (int)(long)json.get("plantEnergy"), (int)(long)json.get("carrionEnergy"),  (int)(long)json.get("jungleRatio"));

/*        Herbivore h = new Herbivore(null,0, map, 0);
        Scavenger s = new Scavenger(null,0, map, 0);
        assertEquals(Scavenger.class, s.createMyChild(null, 0, s).getClass());
        assertEquals(Herbivore.class, h.createMyChild(null, 0, h).getClass());*/

    }


    @Test
    void decreaseEnergyBy() {
        Herbivore h = new Herbivore(null,10, null, 0);
        h.decreaseEnergyBy(5);
        assertEquals(5, h.energy);
    }


}