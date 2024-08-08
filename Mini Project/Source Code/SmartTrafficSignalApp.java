package com.mycompany.smarttrafficsignal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SmartTrafficSignalApp extends Application {
    private List<TrafficData> trafficDataList = new ArrayList<>();
    private TrafficSignalOptimizer optimizer = new TrafficSignalOptimizer();
    private TextArea reportArea = new TextArea();
    private boolean firstSet = true;
    private Random random = new Random();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Smart Traffic Signal App");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField intersectionNameField = new TextField();
        intersectionNameField.setPromptText("Enter intersection name");

        TextField vehicleCountField = new TextField();
        vehicleCountField.setPromptText("Enter vehicle count");

        TextField averageSpeedField = new TextField();
        averageSpeedField.setPromptText("Enter average speed (km/h)");

        Button addDataButton = new Button("Add Data");
        addDataButton.setOnAction(e -> {
            handleAddData(intersectionNameField, vehicleCountField, averageSpeedField);
        });

        Button visualizeButton = new Button("Visualize");
        visualizeButton.setOnAction(e -> {
            handleVisualize();
        });

        Button emergencyButton = new Button("Handle Emergency");
        emergencyButton.setOnAction(e -> {
            handleEmergency();
        });

        TextField changeTimeField = new TextField();
        changeTimeField.setPromptText("Enter signal change time (s)");
        Button changeTimeButton = new Button("Change Time");
        changeTimeButton.setOnAction(e -> {
            int time = Integer.parseInt(changeTimeField.getText());
            optimizer.setSignalChangeTime(time);
        });

        HBox changeTimeBox = new HBox(10, changeTimeField, changeTimeButton);
        changeTimeBox.setPadding(new Insets(10, 0, 0, 0));

        root.getChildren().addAll(intersectionNameField, vehicleCountField, averageSpeedField, addDataButton, visualizeButton, emergencyButton, changeTimeBox, reportArea);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Automatically generate random values for subsequent sets without user clicking visualize every time
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(optimizer.getSignalChangeTime() * 4), event -> {
            handleVisualize();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void handleAddData(TextField intersectionNameField, TextField vehicleCountField, TextField averageSpeedField) {
        String intersectionName = intersectionNameField.getText();
        int vehicleCount = Integer.parseInt(vehicleCountField.getText());
        double averageSpeed = Double.parseDouble(averageSpeedField.getText());

        if (trafficDataList.size() < 4) {
            trafficDataList.add(new TrafficData(intersectionName, vehicleCount, averageSpeed));
            intersectionNameField.clear();
            vehicleCountField.clear();
            averageSpeedField.clear();
        } else {
            System.out.println("Maximum of 4 intersections allowed.");
        }
    }

    private void handleVisualize() {
        if (firstSet) {
            optimizer.optimizeTrafficSignals(trafficDataList);
            generateReport(trafficDataList);
            firstSet = false;
        } else {
            List<TrafficData> newTrafficDataList = generateRandomTrafficData(trafficDataList);
            optimizer.optimizeTrafficSignals(newTrafficDataList);
            generateReport(newTrafficDataList);
        }
    }

    private void handleEmergency() {
        optimizer.handleEmergency(); // Toggle emergency mode
    }

    private List<TrafficData> generateRandomTrafficData(List<TrafficData> baseData) {
        List<TrafficData> newTrafficDataList = new ArrayList<>();
        for (TrafficData data : baseData) {
            int vehicleCount = random.nextInt(100) + 1;
            double averageSpeed = random.nextDouble() * 50;
            newTrafficDataList.add(new TrafficData(data.getIntersectionName(), vehicleCount, averageSpeed));
        }
        return newTrafficDataList;
    }

    private void generateReport(List<TrafficData> trafficDataList) {
        StringBuilder report = new StringBuilder();
        report.append("---------------------------------------------------------------\n");
        report.append("| Intersection Name    | Vehicle Count | Average Speed (km/h) |\n");
        report.append("---------------------------------------------------------------\n");

        for (TrafficData data : trafficDataList) {
            report.append(String.format("| %-20s | %-13d | %-21.2f |\n",
                    data.getIntersectionName(), data.getVehicleCount(), data.getAverageSpeed()));
        }

        report.append("---------------------------------------------------------------\n");
        reportArea.setText(report.toString());
    }
}
