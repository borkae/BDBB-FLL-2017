package com.bork.industries.collector.sensor;

import com.bork.industries.collector.sensor.types.SensorReading;

public class StubSensorCollector implements SensorCollector {
	private SensorReading sensorReading;

	public StubSensorCollector(double tempSensor1, double tempSensor2, int vibrationSensor1, int vibrationSensor2) {
		sensorReading = new SensorReading();
		sensorReading.setTemperatureSensor1(tempSensor1);
		sensorReading.setTemperatureSensor2(tempSensor2);
		sensorReading.setVibrationSensor1(vibrationSensor1);
		sensorReading.setVibrationSensor2(vibrationSensor2);
	}

	@Override
	public void close() {
		// do nothing
	}

	@Override
	public SensorReading read() {
		return sensorReading;
	}
}
