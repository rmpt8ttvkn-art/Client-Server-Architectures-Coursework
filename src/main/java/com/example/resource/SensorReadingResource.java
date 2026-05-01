/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.resource;

/**
 *
 * @author nimra
 */
import com.example.dao.MockDatabase;
import com.example.model.Sensor;
import com.example.model.SensorReading;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import com.example.exception.SensorUnavailableException;
import com.example.exception.DataNotFoundException;

public class SensorReadingResource {

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getAllReadings() {
        List<SensorReading> readings = MockDatabase.SENSOR_READINGS.get(sensorId);

        if (readings == null) {
            return new ArrayList<>();
        }

        return readings;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SensorReading addReading(SensorReading reading) {

        Sensor sensor = MockDatabase.SENSORS.get(sensorId);

        if (sensor == null) {
            throw new DataNotFoundException("Sensor with ID " + sensorId + " not found.");
        }

        if (reading == null) {
            throw new RuntimeException("Reading data is required.");
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is currently under maintenance and cannot accept readings.");
        }

        List<SensorReading> readings = MockDatabase.SENSOR_READINGS.get(sensorId);

        if (readings == null) {
            readings = new ArrayList<>();
            MockDatabase.SENSOR_READINGS.put(sensorId, readings);
        }

        sensor.setCurrentValue(reading.getValue());
        readings.add(reading);

        return reading;
    }
}
