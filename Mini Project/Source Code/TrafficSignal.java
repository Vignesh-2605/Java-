package com.mycompany.smarttrafficsignal;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;

public class TrafficSignal extends Stage {
    private GridPane gridPane;
    private Map<String, TrafficLightPanel> trafficLightPanels;

    public TrafficSignal(String title) {
        setTitle(title);
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(gridPane, 600, 400); // Adjusted scene size to fit 4 columns
        setScene(scene);

        trafficLightPanels = new HashMap<>();
    }

    public void addTrafficLightPanel(String intersectionName, String color) {
        if (trafficLightPanels.containsKey(intersectionName)) {
            updateTrafficLightPanel(intersectionName, color);
        } else {
            VBox panel = new VBox(10);
            panel.setPadding(new Insets(10));
            panel.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
            panel.setAlignment(Pos.CENTER);

            TrafficLightPanel lightPanel = new TrafficLightPanel(color);
            lightPanel.setMinSize(100, 300);
            panel.getChildren().add(lightPanel);

            Label intersectionLabel = new Label(intersectionName + ": " + getColorText(color));
            panel.getChildren().add(intersectionLabel);

            int colIndex = trafficLightPanels.size() % 4;
            int rowIndex = trafficLightPanels.size() / 4;
            gridPane.add(panel, colIndex, rowIndex);

            trafficLightPanels.put(intersectionName, lightPanel);
        }
    }

    public void updateTrafficLightPanel(String intersectionName, String color) {
        if (trafficLightPanels.containsKey(intersectionName)) {
            TrafficLightPanel lightPanel = trafficLightPanels.get(intersectionName);
            lightPanel.updateLightColor(color);
            Label label = (Label) ((VBox) lightPanel.getParent()).getChildren().get(1);
            label.setText(intersectionName + ": " + getColorText(color));
        } else {
            addTrafficLightPanel(intersectionName, color);
        }
    }

    public void clearTrafficLightPanels() {
        Platform.runLater(() -> gridPane.getChildren().clear());
        trafficLightPanels.clear();
    }

    private String getColorText(String color) {
        switch (color.toLowerCase()) {
            case "red":
                return "Stop";
            case "yellow":
                return "Caution";
            case "green":
                return "Go";
            default:
                return "Unknown";
        }
    }
}
