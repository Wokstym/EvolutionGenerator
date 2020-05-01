package agh.cs.gui;

import agh.cs.map.utils.Vector2d;
import agh.cs.mapEntity.IMapElement;
import agh.cs.mapEntity.eatable.EatableEntity;
import agh.cs.mapEntity.movable.Animal;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.SneakyThrows;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import static agh.cs.gui.EvolutionFrame.PIXEL_SIZE;

public class EvolutionScene extends Scene implements PropertyChangeListener {

    private Multimap<Vector2d, EntityImageView> animalsViews;
    private Multimap<Vector2d, EntityImageView> eatableViews;

    private Group group;


    EvolutionScene(Pane rootPane, double width, double height, Vector2d lowerLeftJungle, Vector2d upperRightJungle) {
        super(rootPane, width, height, Color.YELLOWGREEN);

        animalsViews = ArrayListMultimap.create();
        eatableViews = ArrayListMultimap.create();

        Rectangle rectangle = new Rectangle(PIXEL_SIZE*(upperRightJungle.x-lowerLeftJungle.x), PIXEL_SIZE*(upperRightJungle.y-lowerLeftJungle.y));
        rectangle.setX(lowerLeftJungle.x* PIXEL_SIZE);
        rectangle.setY(lowerLeftJungle.y*PIXEL_SIZE);
        rectangle.setFill(Color.GREEN);

        group = new Group();

        rootPane.getChildren().add(rectangle);
        rootPane.getChildren().add(group);

    }

    @Override
    @SneakyThrows
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("animal moved")) {

            Vector2d oldPos = (Vector2d) event.getOldValue();
            Vector2d newPos = (Vector2d) event.getNewValue();
            Animal movedAnimal = (Animal) event.getSource();

            Collection<EntityImageView> animalViewsAtOldPos = animalsViews.get(oldPos);

            animalViewsAtOldPos
                    .stream()
                    .filter(eImgV -> eImgV.getElement().equals(movedAnimal))
                    .findFirst()
                    .ifPresent(wantedEImgV -> {
                        wantedEImgV.changePosition(newPos);
                        animalsViews.remove(oldPos, wantedEImgV);
                        animalsViews.put(newPos, wantedEImgV);
                    });
        }

        if (event.getPropertyName().equals("animal created")) {

            Animal entity = (Animal) event.getNewValue();
            EntityImageView entityImageView = new EntityImageView(entity);

            animalsViews.put(entity.getPosition(), entityImageView);
            group.getChildren().add(entityImageView);
        }
        if (event.getPropertyName().equals("eatable added")) {

            EatableEntity entity = (EatableEntity) event.getNewValue();
            EntityImageView entityImageView = new EntityImageView(entity);

            eatableViews.put(entity.getPosition(), entityImageView);
            group.getChildren().add(entityImageView);
        }

        if (event.getPropertyName().equals("removed animal")) {

            Animal removedAnimal = (Animal) event.getOldValue();
            Collection<EntityImageView> animalsAtPos = animalsViews.get(removedAnimal.getPosition());

            animalsAtPos
                    .stream()
                    .filter(eImgV -> eImgV.getElement().equals(removedAnimal))
                    .findFirst()
                    .ifPresent(wantedEImgV -> {
                        group.getChildren().remove(wantedEImgV);
                        animalsViews.remove(removedAnimal.getPosition(), wantedEImgV);
                    });
        }
        if (event.getPropertyName().equals("deleted eatable")) {
            EatableEntity removedEatable = (EatableEntity) event.getNewValue();
            Collection<EntityImageView> animalsAtPos = eatableViews.get(removedEatable.getPosition());

            animalsAtPos
                    .stream()
                    .filter(eImgV-> eImgV.getElement().equals(removedEatable))
                    .findAny()
                    .ifPresent(wantedEImgV->{
                        group.getChildren().remove(wantedEImgV);
                        eatableViews.remove(removedEatable.getPosition(), wantedEImgV);
                    });
        }
    }
}
