package agh.cs.gui;

import agh.cs.map.utils.Vector2d;
import agh.cs.mapEntity.IMapElement;
import agh.cs.mapEntity.eatable.Grass;
import agh.cs.mapEntity.movable.Herbivore;
import agh.cs.mapEntity.movable.Scavenger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.NonNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static agh.cs.gui.EvolutionFrame.PIXEL_SIZE;

public class EntityImageView extends ImageView {

    private final IMapElement element;

    public EntityImageView(IMapElement element) throws FileNotFoundException {
        super(EntityImageView.getElementImage(element));
        this.element = element;

        Vector2d position = element.getPosition();

        setX(position.x * PIXEL_SIZE);
        setY(position.y * PIXEL_SIZE);
        setFitHeight(PIXEL_SIZE);
        setFitWidth(PIXEL_SIZE);
        setPreserveRatio(true);
    }

    public void changePosition(@NonNull Vector2d position) {
        /*
        TranslateTransition tt = new TranslateTransition(Duration.millis(1000.0/FREQUENCY/4), this);
        tt.setToX(position.x * PIXEL_SIZE);
        tt.setToY(position.y * PIXEL_SIZE);
        tt.play();*/
        this.relocate(position.x * PIXEL_SIZE,position.y * PIXEL_SIZE);

    }

    private static final String imgScavengerSource = "src/main/resources/scavengerColor.png";
    private static final String imgHerbivoreSource = "src/main/resources/herbivoreColor.png";
    private static final String imgGrassSource = "src/main/resources/grassColor.png";
    private static final String imgCarrionSource = "src/main/resources/carrionColor.png";


    private static Image getElementImage(IMapElement element) throws FileNotFoundException {

        String thisImgSource;
        if (element instanceof Scavenger)
            thisImgSource = imgScavengerSource;
        else if (element instanceof Herbivore)
            thisImgSource = imgHerbivoreSource;
        else if (element instanceof Grass)
            thisImgSource = imgGrassSource;
        else
            thisImgSource = imgCarrionSource;

        return new Image(new FileInputStream(thisImgSource));
    }

    IMapElement getElement() {
        return element;
    }

    @Override
    public String toString() {
        return element.getClass().toString();
    }
}
