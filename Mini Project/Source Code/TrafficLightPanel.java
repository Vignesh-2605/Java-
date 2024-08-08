package com.mycompany.smarttrafficsignal;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class TrafficLightPanel extends VBox {
    private Rectangle redLight;
    private Rectangle yellowLight;
    private Rectangle greenLight;

    private Polygon straightArrow;
    private Polygon rightArrow;
    private Polygon leftArrow;

    public TrafficLightPanel(String lightColor) {
        createTrafficLight();
        updateLightColor(lightColor);
    }

    private void createTrafficLight() {
        setAlignment(Pos.CENTER);
        setSpacing(10);

        redLight = new Rectangle(50, 50, Color.GRAY);
        yellowLight = new Rectangle(50, 50, Color.GRAY);
        greenLight = new Rectangle(50, 50, Color.GRAY);

        // Create arrows
        straightArrow = createArrow(0);
        rightArrow = createArrow(90);
        leftArrow = createArrow(-90);

        getChildren().addAll(redLight, yellowLight, greenLight, straightArrow, rightArrow, leftArrow);
    }

    private Polygon createArrow(double angle) {
        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(0.0, 0.0, 10.0, 10.0, -10.0, 10.0);
        arrow.setRotate(angle);
        arrow.setFill(Color.GRAY);
        return arrow;
    }

    public void updateLightColor(String lightColor) {
        redLight.setFill(Color.GRAY);
        yellowLight.setFill(Color.GRAY);
        greenLight.setFill(Color.GRAY);

        straightArrow.setFill(Color.GRAY);
        rightArrow.setFill(Color.GRAY);
        leftArrow.setFill(Color.GRAY);

        switch (lightColor.toLowerCase()) {
            case "red":
                redLight.setFill(Color.RED);
                break;
            case "yellow":
                yellowLight.setFill(Color.YELLOW);
                break;
            case "green":
                greenLight.setFill(Color.GREEN);
                straightArrow.setFill(Color.GREEN);
                rightArrow.setFill(Color.GREEN);
                leftArrow.setFill(Color.GREEN);
                break;
        }
    }
}
