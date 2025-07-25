# Energybox Backend Coding Challenge

Please complete the assignment detailed below email me back with your code either zipped as an attachment or a link to where you're working on the code ( ie GitHub, bitbucket, gitlab etc. ) Please also suggest a time early next week when we can spend 45 minutes to an 1 hour reviewing your work.

You may be busy during normal business hours, so I am open to accommodate almost any time. If you have any questions or concerns, feel free to reach out to me via email (mugeesh.husain@energybox.com). I will try to reply as quickly as possible.

### Tech to Use

- Java
- Spring Boot
- Spring Data
- Maven
- PostgresDB
- docker-compose

### Getting Started
1. https://bitbucket.org/energybox/backend-coding-challenge-postgres/src/master/
2. fork the repository
3. docker compose up -d
4. check docker ps, see if postgresDB is running or not
5. run the application
6. http://localhost:8082/api/v1/swagger-ui.html

You'll need to set up a local PostgresDB instance. A Docker Compose file is included in the sample project for convenience.
Don't feel obligated to spend any more than 2-5 hours on this. You're welcome to spend more time if you're having fun, but you won't be judged on how much you complete.

The project POM file is purposefully bare. Feel free to add dependencies of your choosing.

---

Our company is rolling out a new backend service that will allow developers to create Sensors and Gateways in our system. A sensor can only be connected to one Gateway at a time, while a Gateway can have N sensors connected to it.

A relationship of type `ManyToOne`, `JoinColumn` should be created between sensor and gateway when a sensor is assigned to a given gateway.

Sensors should have a `type` attribute, and a sensor can have multiple types assigned to it.

Some examples of sensor types could be temperature, humidity, electricity.

Try to complete the user stories below as best you can, and feel free to add in any features you'd like to see that may not be detailed here. The stories are purposefully vague and open to interpretation.

### User Stories

- As a user, I'd like to query for all the existing sensors.
- As a user, I'd like to query for all gateways.
- As a user, I'd like to query all sensors assigned to an existing gateway.
- As a user, I'd like to create a new sensor.
- As a user, I'd like to create a new gateway.
- As a user, I'd like to assign a sensor to a given gateway.
- As a user, I'd like to query for sensors of a certain type.
- As a user, I'd like to query for a gateway that has electrical sensors assigned to it.
- Delete gateway
- Delete sensor
- Show unassigned sensors
- Add sensor type to a sensor
- Remove sensor type from a sensor

- Add sensor type
- Remove sensor type

- last reading of a sensor regardless of sensor type

- add a sensor reading update

**Bonus:**
- Sensors can have a `last_reading` for each sensor type. The `last_reading` consists of a timestamp and a value.
- As a user, I'd like to query all the existing last_readings of a given sensor.
- test case any method or service



* http://localhost:8082/api/v1/swagger-ui.html

### Endpoint Details

1. **Query for All Existing Sensors**
    ```
       Endpoint: GET /sensors
    ```
2. **Query for All Gateways**
    ```
       Endpoint: GET /gateways
    ``` 
3. **Query All Sensors Assigned to an Existing Gateway**
    ```
      Endpoint: GET /sensors/gateway/{gatewayId}
    ```
4. **Create a New Sensor**
    ```
    Endpoint: POST /sensors
    Body:
    {
      "types": ["temperature", "humidity"],
      "gateway": { "id": 1 }
    }
    ```

5. **Create a New Gateway**
    ```
    Endpoint: POST /gateways
    Body:
    {
      "name": "Gateway 1"
    }
    ```

6. **Assign a Sensor to a Given Gateway**
    ```
    Endpoint: PUT /sensors/{sensorId}/assign
    Body:
    {
    "gatewayId": 1
    }
    ```
 7. **Query for Sensors of a Certain Type**
    ```
    Endpoint: GET /sensors/type/{type}
    ```
8. **Query for a Gateway that Has Electrical Sensors Assigned to It**
   ```
   Endpoint: GET /gateways/with-sensors/electrical
   ```
   

Response 
