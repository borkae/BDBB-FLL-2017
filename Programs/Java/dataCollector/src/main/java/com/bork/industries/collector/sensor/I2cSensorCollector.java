package com.bork.industries.collector.sensor;

import java.io.IOException;

import com.bork.industries.collector.sensor.types.SensorReading;
import com.pi4j.io.i2c.*;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

/*-
 * Address	Register Description
 * 0x00 	Status Register
 * 0x01 	Temp Sensor 1 MSB
 * 0x02 	Temp Sensor 1 LSB
 * 0x03 	Temp Sensor 1 Faction
 * 0x04 	Temp Sensor 2 MSB
 * 0x05 	Temp Sensor 2 LSB
 * 0x06 	Temp Sensor 2 Faction
 * 0x07 	Vibration Sensor 1 MSB
 * 0x08 	Vibration Sensor 1 LSB
 * 0x09 	Vibration Sensor 2 MSB
 * 0x0A 	Vibration Sensor 2 LSB
 * 0x0B		Pipe 1 On
 * 0x0C		Pipe 2 On
 * 0x0D 	Mode Register
 */
public class I2cSensorCollector implements SensorCollector {
	private I2CDevice device;

	public I2cSensorCollector(int address) {
		try {
			I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_2);
			device = i2c.getDevice(address);
		} catch (IOException | UnsupportedBusNumberException e) {
			System.err.println("Not able to create I2C bus and device.");
			System.err.println(e.getMessage());
		}
	}

	public I2cSensorCollector(I2CDevice device) {
		this.device = device;
	}

	@Override
	public void close() {
		// Nothing here
	}

	@Override
	public SensorReading read() {
		try {
			int tempSens1Msb = device.read(0x01);
			int tempSens1Lsb = device.read(0x02);

			int tempSens2Msb = device.read(0x04);
			int tempSens2Lsb = device.read(0x05);

			int vibrSens1Msb = device.read(0x07);
			int vibrSens1Lsb = device.read(0x08);

			int vibrSens2Msb = device.read(0x09);
			int vibrSens2Lsb = device.read(0x0A);

			int pipeOneOn = device.read(0x0B);
			int pipeTwoOn = device.read(0x0C);

			SensorReading sensorReading = new SensorReading();

			sensorReading.setTemperatureSensor1(((0xFF & tempSens1Msb) << 8) | (0xFF & tempSens1Lsb));
			sensorReading.setTemperatureSensor2(((0xFF & tempSens2Msb) << 8) | (0xFF & tempSens2Lsb));

			sensorReading.setVibrationSensor1(((0xFF & vibrSens1Msb) << 8) | (0xFF & vibrSens1Lsb));
			sensorReading.setVibrationSensor2(((0xFF & vibrSens2Msb) << 8) | (0xFF & vibrSens2Lsb));

			sensorReading.setPipeOn1(pipeOneOn != 0);
			sensorReading.setPipeOn2(pipeTwoOn != 0);

			return sensorReading;
		} catch (IOException e) {
			System.err.println("Failed to read sensors.");
			System.err.println(e.getMessage());
		}

		return null;
	}

}
