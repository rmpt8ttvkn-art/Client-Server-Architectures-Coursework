# Client-Server-Architectures-Coursework
# Smart Campus API

## Overview
This project has implemented a restful api for managing rooms, sensors and sensor readings using jax-RS. As stated above the project follows all REST principles and uses an in memory data structure (HashMap & ArrayList).

## How to Run
1. Open in NetBeans
2. Clean and Build
3. Run project
4. Base URL:
http://localhost:8080/StudentApiDAO/api/v1

## API Discovery Endpoint

GET /api/v1  
This endpoint returns metadata about the API including version, contact information, and available resources.

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

## Sample curl Commands

### 1. Get all rooms
curl http://localhost:8080/StudentApiDAO/api/v1/rooms

### 2. Get one room
curl http://localhost:8080/StudentApiDAO/api/v1/rooms/LIB-301

### 3. Create a new room
curl -X POST http://localhost:8080/StudentApiDAO/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"SCI-101","name":"Science Room","capacity":50,"sensorIds":[]}'

### 4. Try deleting a room with sensors - returns 409
curl -X DELETE http://localhost:8080/StudentApiDAO/api/v1/rooms/LIB-301

### 5. Get all sensors
curl http://localhost:8080/StudentApiDAO/api/v1/sensors

### 6. Filter sensors by type
curl "http://localhost:8080/StudentApiDAO/api/v1/sensors?type=CO2"

### 7. Create a sensor with invalid room - returns 422
curl -X POST http://localhost:8080/StudentApiDAO/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEST-001","type":"CO2","status":"ACTIVE","currentValue":10,"roomId":"FAKE-999"}'

### 8. Get readings for a sensor
curl http://localhost:8080/StudentApiDAO/api/v1/sensors/TEMP-001/readings

### 9. Add reading to a sensor
curl -X POST http://localhost:8080/StudentApiDAO/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"id":"R1","timestamp":1710000000000,"value":23.5}'

### 10. Empty body test - returns clean 500 JSON
curl -X POST http://localhost:8080/StudentApiDAO/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json"

## Report Answers

### Lifecycle
Since jax-RS resource classes are typically stateless the jax-RS resource class will be instantiated once per request. This instantiation of the resource class does not persist through the lifecycle of the application. To allow the persistence of common data (rooms/sensors etc.) throughout the lifetime of the application the data needs to be stored within a static data structure i.e. HashMap/ArrayList.

### IDs vs Objects
Returning ID's only will save network bandwidth and improve performance, however returning full objects will provide more information to the client and reduce the number of calls the client makes to your api. Both approaches have their use cases therefore a balance must be found depending on your specific needs.

### DELETE
Delete operations are idempotent because sending the same delete request more than once will always produce the same result. If the room exists it is deleted. If the room does not exist a 404 response is returned by the system however nothing else happens within the system.

### @Consumes
The @consumes(mediatype.APPLICATION_JSON) annotation informs jax-RS that it should expect json data as input. If a client sends data in any format other than what the server can understand (i.e. Text/plain or XML), jax-RS will return a 415 unsupported Media type error.

### QueryParam
Query parameters are better for filtering because they allow clients to filter data in a flexible and optional way without changing the structure of the resource path. Using query parameter annotations (@queryparam) keeps the uri clean and allows clients to apply multiple filters at one time. Applying filters to the path instead would limit flexibility.

### Sub-resource
Separating logic into separate classes increases code modularity through the sub-resource locator pattern. Modularizing code reduces complexity and improves maintainability especially when working with large scale APIs that contain nested resources.

### 422 vs 404
Http 422 status codes was preferred over 404 status codes because while 404 indicates that a valid url does not exist, a 422 status code correctly indicate that the validation failed even though the request was valid.

### Security
Exposing stack traces creates security risks because it reveals how the system internally works which includes class names, file paths and system layout. Attackers can leverage this internal knowledge to take advantage of weaknesses. Therefore a general error message must be returned by the system rather than expose its inner workings.

### Filters
Filters provide cleaner code then logging directly from each resource method because all requests and responses can automatically be caught by a single point where logging logic resides, removing duplication.
