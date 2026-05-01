/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dao;

/**
 *
 * @author nimra
 */
import com.example.model.Room;
import com.example.model.Sensor;
import com.example.model.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockDatabase {

    public static final Map<String, Room> ROOMS = new HashMap<>();
    public static final Map<String, Sensor> SENSORS = new HashMap<>();
    public static final Map<String, List<SensorReading>> SENSOR_READINGS = new HashMap<>();

    static {
        Room room1 = new Room("LIB-301", "Library Quiet Study", 40);
        Room room2 = new Room("ENG-201", "Engineering Lab", 25);

        ROOMS.put(room1.getId(), room1);
        ROOMS.put(room2.getId(), room2);

        Sensor sensor1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        Sensor sensor2 = new Sensor("CO2-001", "CO2", "ACTIVE", 420.0, "ENG-201");
        Sensor sensor3 = new Sensor("MAINT-001", "Temperature", "MAINTENANCE", 0.0, "LIB-301");

        SENSORS.put(sensor1.getId(), sensor1);
        SENSORS.put(sensor2.getId(), sensor2);
        SENSORS.put(sensor3.getId(), sensor3);

        room1.getSensorIds().add(sensor1.getId());
        room2.getSensorIds().add(sensor2.getId());
        room1.getSensorIds().add(sensor3.getId());

        SENSOR_READINGS.put(sensor1.getId(), new ArrayList<SensorReading>());
        SENSOR_READINGS.put(sensor2.getId(), new ArrayList<SensorReading>());
        SENSOR_READINGS.put(sensor3.getId(), new ArrayList<SensorReading>());
    }

    private MockDatabase() {
    }
}