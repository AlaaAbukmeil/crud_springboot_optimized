CREATE SCHEMA IF NOT EXISTS devices;

CREATE TABLE devices.gateways (
  id   SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);


CREATE TABLE devices.sensors (
  id          SERIAL PRIMARY KEY,
  name        VARCHAR(100) NOT NULL,
  gateway_id  INTEGER REFERENCES devices.gateways(id) ON DELETE SET NULL,
  created_at  TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE devices.sensor_types (
  id   SERIAL PRIMARY KEY,
  type VARCHAR(50) UNIQUE NOT NULL
);

-- if sensor id gets deleted, remove all rows with sensor id. same with type but it wont happen
CREATE TABLE devices.sensorId_with_typeId (
  sensor_id      INTEGER REFERENCES devices.sensors(id) ON DELETE CASCADE,
  sensor_type_id INTEGER REFERENCES devices.sensor_types(id) ON DELETE CASCADE,
  PRIMARY KEY (sensor_id, sensor_type_id)
);

-- store only one row of sensor id & type as we only need last
CREATE TABLE devices.sensor_last_readings (
  id              SERIAL PRIMARY KEY,
  sensor_id       INTEGER REFERENCES devices.sensors(id) ON DELETE CASCADE,
  sensor_type_id  INTEGER REFERENCES devices.sensor_types(id) ON DELETE CASCADE,
  reading_time    TIMESTAMP WITH TIME ZONE NOT NULL,
  reading_value   NUMERIC NOT NULL,
  UNIQUE(sensor_id, sensor_type_id)
);


-- add sensor types
INSERT INTO devices.sensor_types(type) VALUES
  ('temperature'),
  ('humidity'),
  ('electricity'),
  ('pressure'),
  ('sound');