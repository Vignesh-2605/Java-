package com.mycompany.smarttrafficsignal;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TrafficSignalOptimizer {
    private TrafficSignal trafficSignalFrame;
    private List<TrafficData> trafficDataList = new ArrayList<>();
    private int currentIndex = 0;
    private int signalChangeTime = 10; // Default time in seconds
    private boolean emergencyActive = false;
    private Timeline timeline;

    public TrafficSignalOptimizer() {
        Platform.runLater(() -> {
            trafficSignalFrame = new TrafficSignal("Traffic Light");
            trafficSignalFrame.show();
        });
    }

    public void setSignalChangeTime(int seconds) {
        this.signalChangeTime = seconds;
    }

    public int getSignalChangeTime() {
        return signalChangeTime;
    }

    public void optimizeTrafficSignals(List<TrafficData> trafficDataList) {
        this.trafficDataList = new ArrayList<>(trafficDataList);
        this.trafficDataList.sort(Comparator.comparingInt(TrafficData::getVehicleCount).reversed());
        clearAllLights();
        setInitialColors();
        startVisualization();
    }

    private void setInitialColors() {
        for (int i = 0; i < trafficDataList.size(); i++) {
            TrafficData data = trafficDataList.get(i);
            if (i == 0) {
                updateTrafficSignalColor(data.getIntersectionName(), "green");
            } else if (i == 1) {
                updateTrafficSignalColor(data.getIntersectionName(), "yellow");
            } else {
                updateTrafficSignalColor(data.getIntersectionName(), "red");
            }
        }
    }

    private void startVisualization() {
        if (trafficDataList.isEmpty() || emergencyActive) {
            return;
        }

        stopVisualization(); // Stop previous timeline if running

        timeline = new Timeline(new KeyFrame(Duration.seconds(signalChangeTime), event -> {
            processNextIntersection();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void stopVisualization() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    private void processNextIntersection() {
        if (trafficDataList.isEmpty() || emergencyActive) {
            return;
        }

        TrafficData currentData = trafficDataList.get(currentIndex);
        updateTrafficSignalColor(currentData.getIntersectionName(), "red");

        currentIndex = (currentIndex + 1) % trafficDataList.size();
        TrafficData nextData = trafficDataList.get(currentIndex);
        updateTrafficSignalColor(nextData.getIntersectionName(), "green");

        int nextYellowIndex = (currentIndex + 1) % trafficDataList.size();
        TrafficData yellowData = trafficDataList.get(nextYellowIndex);
        updateTrafficSignalColor(yellowData.getIntersectionName(), "yellow");

        for (int i = 0; i < trafficDataList.size(); i++) {
            if (i != currentIndex && i != nextYellowIndex) {
                TrafficData redData = trafficDataList.get(i);
                updateTrafficSignalColor(redData.getIntersectionName(), "red");
            }
        }
    }

    public void handleEmergency() {
        if (emergencyActive) {
            emergencyActive = false;
            optimizeTrafficSignals(trafficDataList);
        } else {
            emergencyActive = true;
            turnAllLightsRed();
        }
    }

    private void turnAllLightsRed() {
        if (trafficSignalFrame != null) {
            Platform.runLater(() -> {
                for (TrafficData data : trafficDataList) {
                    trafficSignalFrame.updateTrafficLightPanel(data.getIntersectionName(), "red");
                }
            });
        }
    }

    public void generateReport(List<TrafficData> trafficDataList) {
        System.out.println("Generating traffic report...");
        System.out.println("---------------------------------------------------------------");
        System.out.println("| Intersection Name    | Vehicle Count | Average Speed (km/h) |");
        System.out.println("---------------------------------------------------------------");

        for (TrafficData data : trafficDataList) {
            System.out.printf("| %-20s | %-13d | %-21.2f |%n", 
                data.getIntersectionName(), data.getVehicleCount(), data.getAverageSpeed());
        }

        System.out.println("---------------------------------------------------------------");
    }

    private void updateTrafficSignalColor(String intersectionName, String color) {
        if (trafficSignalFrame != null) {
            Platform.runLater(() -> {
                trafficSignalFrame.updateTrafficLightPanel(intersectionName, color);
            });
        }
    }

    private void clearAllLights() {
        turnAllLightsRed();
    }

    void handleEmergency(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
