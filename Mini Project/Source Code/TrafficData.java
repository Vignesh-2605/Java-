package com.mycompany.smarttrafficsignal;

public class TrafficData {
    private String intersectionName;
    private int vehicleCount;
    private double averageSpeed;

    public TrafficData(String intersectionName, int vehicleCount, double averageSpeed) {
        this.intersectionName = intersectionName;
        this.vehicleCount = vehicleCount;
        this.averageSpeed = averageSpeed;
    }

    public String getIntersectionName() {
        return intersectionName;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }
}
