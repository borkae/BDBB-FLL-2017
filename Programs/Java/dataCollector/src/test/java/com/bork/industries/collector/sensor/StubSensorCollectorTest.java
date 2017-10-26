package com.bork.industries.collector.sensor;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.bork.industries.collector.sensor.types.SensorReading;

public class StubSensorCollectorTest {
	private static final double TEMP_SENSOR_1 = 72.0;
	private static final double TEMP_SENSOR_2 = 55.0;
	private static final int VIBRATION_SENSOR_1 = 512;
	private static final int VIBRATION_SENSOR_2 = 600;
	private SensorCollector sensorCollector;

	@Before
	public void setup() {
		sensorCollector = new StubSensorCollector(TEMP_SENSOR_1, TEMP_SENSOR_2, VIBRATION_SENSOR_1, VIBRATION_SENSOR_2);
	}

	@Test
	public void read_staticValues_verifyResults() {
		SensorReading reading = sensorCollector.read();

		assertEquals(TEMP_SENSOR_1, reading.getTempSensor1(), 0.1);
		assertEquals(TEMP_SENSOR_2, reading.getTempSensor2(), 0.1);
		assertEquals(VIBRATION_SENSOR_1, reading.getVibrationSensor1());
		assertEquals(VIBRATION_SENSOR_2, reading.getVibrationSensor2());
	}
}
