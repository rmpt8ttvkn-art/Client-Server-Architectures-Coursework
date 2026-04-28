# Client-Server-Architectures-Coursework
# Smart Campus API

## Overview
This project implements a RESTful API for managing rooms, sensors, and sensor readings using JAX-RS. It follows REST principles and uses in-memory data structures (HashMap and ArrayList).

## How to Run
1. Open in NetBeans
2. Clean and Build
3. Run project
4. Base URL:
http://localhost:8080/StudentApiDAO/rest

## Endpoints

### Rooms
GET /rooms  
POST /rooms  
GET /rooms/{id}  
DELETE /rooms/{id}

### Sensors
GET /sensors  
GET /sensors?type=CO2  
POST /sensors  
GET /sensors/{id}

### Readings
GET /sensors/{id}/readings  
POST /sensors/{id}/readings  

### 1. Get all rooms
curl http://localhost:8080/StudentApiDAO/rest/rooms

### 2. Get one room
curl http://localhost:8080/StudentApiDAO/rest/rooms/LIB-301

### 3. Create a new room
curl -X POST http://localhost:8080/StudentApiDAO/rest/rooms \
-H "Content-Type: application/json" \
-d '{"id":"SCI-101","name":"Science Room","capacity":50,"sensorIds":[]}'

### 4. Try deleting a room with sensors - returns 409
curl -X DELETE http://localhost:8080/StudentApiDAO/rest/rooms/LIB-301

### 5. Get all sensors
curl http://localhost:8080/StudentApiDAO/rest/sensors

### 6. Filter sensors by type
curl "http://localhost:8080/StudentApiDAO/rest/sensors?type=CO2"

### 7. Create a sensor with invalid room - returns 422
curl -X POST http://localhost:8080/StudentApiDAO/rest/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEST-001","type":"CO2","status":"ACTIVE","currentValue":10,"roomId":"FAKE-999"}'

### 8. Get readings for a sensor
curl http://localhost:8080/StudentApiDAO/rest/sensors/TEMP-001/readings

### 9. Add reading to a sensor
curl -X POST http://localhost:8080/StudentApiDAO/rest/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"id":"R1","timestamp":1710000000000,"value":23.5}'

### 10. Empty body test - returns clean 500 JSON
curl -X POST http://localhost:8080/StudentApiDAO/rest/sensors/TEMP-001/readings \
-H "Content-Type: application/json"

## Report Answers

### Lifecycle
JAX-RS resource classes are normally stateless, so they are initialized per request. That is to say that for each incoming HTTP request, there will be a new class instance created. Due to this stateless nature, we have to place our common data (rooms and sensors etc.) into some sort of static data structure such as HashMap or ArrayList. In doing so, we can ensure the data remains active from one request to another and avoid both data being lost and concurrency problems.

### IDs vs Objects
When you only return IDs it uses less network bandwidth and makes things run faster especially when you have a lot of data to work with. But returning full objects gives more information and cuts down on the number of API calls the client has to make. Therefore depending on the situation a balance needs to be found.

### DELETE
The DELETE operation is idempotent because sending the same DELETE request more than once will always give the same result. If the room is there it is deleted. If it doesn't exist the system sends back a 404 response but nothing else happens in the system.

### @Consumes
The @Consumes(MediaType.The APPLICATION_JSON annotation tells JAX-RS to expect JSON data as input. If a client sends data in a format that the server can't understand (like text/plain or XML) JAX-RS will give a 415 Unsupported Media Type error.

### QueryParam
Query parameters are better for filtering because they let you filter in a flexible and optional way without changing the structure of the resource path. @QueryParam keeps the URI clean and lets you use more than one filter. Putting filters in the path on the other hand makes it less flexible.

### Sub-resource
By putting logic into separate classes the sub-resource locator pattern makes code more modular. This makes things less complicated and keeps the code easy to work with, especially for big APIs with nested resources.

### 422 vs 404
HTTP 422 is better because the request is valid but the data inside it is wrong. A 404 error would mean that the endpoint doesn't exist but a 422 error correctly shows that the validation failed.

### Security
Exposing stack traces is a security risk because it shows how the system works on the inside, like class names, file paths, and the structure of the system. This information can be used by attackers to take advantage of weaknesses. So a general error response must be sent back instead.

### Filters
Using filters is better because they keep logging logic in one place and stop it from happening twice. Filters automatically catch all requests and responses which makes the system cleaner and easier to maintain. This means that you don't have to add logging code to every resource method.
