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
import com.example.model.Room;
import com.example.model.Sensor;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import com.example.exception.LinkedResourceNotFoundException;
import com.example.exception.DataNotFoundException;

@Path("/sensors")
public class SensorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getAllSensors(@QueryParam("type") String type) {

        List<Sensor> sensors = new ArrayList<>(MockDatabase.SENSORS.values());

        if (type == null || type.isEmpty()) {
            return sensors;
        }

        List<Sensor> filtered = new ArrayList<>();

        for (Sensor s : sensors) {
            if (s.getType().equalsIgnoreCase(type)) {
                filtered.add(s);
            }
        }

        return filtered;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Sensor addSensor(Sensor sensor) {

        Room room = MockDatabase.ROOMS.get(sensor.getRoomId());

        if (room == null) {
            throw new LinkedResourceNotFoundException("Room with ID " + sensor.getRoomId() + " does not exist.");
        }

        MockDatabase.SENSORS.put(sensor.getId(), sensor);

        room.getSensorIds().add(sensor.getId());

        MockDatabase.SENSOR_READINGS.put(sensor.getId(), new ArrayList<>());

        return sensor;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Sensor getSensor(@PathParam("id") String id) {

        Sensor sensor = MockDatabase.SENSORS.get(id);

        if (sensor == null) {
            throw new DataNotFoundException("Sensor with ID " + id + " not found.");
        }

        return sensor;
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadings(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
