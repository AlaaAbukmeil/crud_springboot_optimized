Script:
docker compose down                         
rm -rf ${HOME}/energybox/postgres
docker compose up -d 
mvn clean compile
mvn spring-boot:run


Good Practices:
    1. Use DTO instead of exposing JPA entities directly
    2. Global exception handler
    3. Paging data
    4. Validate request body
    5. Lazy loading. Understanding goal of query



Test API:

1. POST sensors/random
    Populates the database with 5 gateways and 1000 sensors. Each sensor is assigned to a gateway and up to three sensor types
2. GET sensors/
   Retrieves all sensors. Paging & sorting is supported. Point out lazy loading
3. GET gateways/
    Retrieves all gateways. Lazy loading
4. GET sensors/types
    Retrieves all sensor types.
5. POST gateways/
   Creates a gateway. Follows convention with return status
6. GET sensors/gateway/1
    Show all sensors assigned to gateway with id 1. Duplicate page to compare after deleting gateway 1
    Try to with id 7, which will show a custom error message
7. GET sensors/unassigned
    Show all sensors without an assigned gateway with a suggested gateway. At this point, it will show none since there is no sensors without a gateway
8. DELETE gateways/1
    Delete gateway with id 1 and unassign all sensors assigned to gateway 1
    Call the api again, it will print a custom error message
9. GET sensors/unassigned
    Show all sensors that were previously assigned to gateway 1. It will also show best gateway to assign them to based on x & y coordinates.
10. POST sensors
    Create a new sensor and assign it to gateway 2. 
    try with invalid type
11. try with gateway 10
11. POST sensors/1001/assign
    Assign the newly created sensor to gateway 6
12. GET sensors/gateway/6
    Show that the newly created sensor got reassigned
13. GET sensors/type/temperature
    Show all sensors that have a temperature feature.
    Try with type mold, it will print a custom error message
14. GET gateways/with-sensors/temperatue
    Show all gateways that have sensors with a certain features
15. GET sensors/readings
    Show all last readings of all sensors. Pick a sensor ID with two features
16. GET sensors/readings/sensor/{sensorId}
    Show all last reading of a sensor ID. There is binary tree on sensor_id for faster look ups. duplicate page to compare later
17. POST sensors/readings/sensor/{sensorId}
    Send a new message. remove readingTime
18. GET sensors/readings/sensor/{sensorId}
    Show all last reading of a sensor ID
    
