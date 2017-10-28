package com.bork.industries.collector.sensor;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;

import com.bork.industries.collector.sensor.types.SensorReading;
import com.pi4j.io.i2c.I2CDevice;

public class I2cSensorCollectorTest {
	private SensorCollector sensorCollector;
	
	private IMocksControl mocksControl;
	private I2CDevice mockI2cDevice;

	@Before
	public void setup() {
		mocksControl = createControl();
		mockI2cDevice = mocksControl.createMock(I2CDevice.class);
		
		sensorCollector = new I2cSensorCollector(mockI2cDevice);
	}

	@Test
	public void read_withMockDeviceReads_verifySensorReads() throws IOException {
		expect(mockI2cDevice.read(0x01)).andReturn(0x4E);
		expect(mockI2cDevice.read(0x02)).andReturn(0x20);
		expect(mockI2cDevice.read(0x04)).andReturn(0x75);
		expect(mockI2cDevice.read(0x05)).andReturn(0x30);
		expect(mockI2cDevice.read(0x07)).andReturn(0x03);
		expect(mockI2cDevice.read(0x08)).andReturn(0x84);
		expect(mockI2cDevice.read(0x09)).andReturn(0x03);
		expect(mockI2cDevice.read(0x0A)).andReturn(0x20);
		
		mocksControl.replay();
		SensorReading reading = sensorCollector.read();
		mocksControl.verify();
		
		assertEquals(20000.0, reading.getTemperatureSensor1(), 0.1);
		assertEquals(30000.0, reading.getTemperatureSensor2(), 0.1);
		assertEquals(900, reading.getVibrationSensor1());
		assertEquals(800, reading.getVibrationSensor2());
	}
}
