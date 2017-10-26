package com.bork.industries.collector.sensor;

import org.apache.commons.lang3.StringUtils;

import com.bork.industries.ApplicationOptions;

public class SensorCollectorFactory {
	public static SensorCollector create(ApplicationOptions applicationOptions) {
		if (StringUtils.equalsAnyIgnoreCase(applicationOptions.getSensorBus(), "I2C")) {
			if (StringUtils.isBlank(applicationOptions.getI2cSensorAddress())) {
				System.err.println("I2C Slave Address as Hexdecimal is required for I2C Sensor bus.");
			} else {
				try {
					int address = Integer.parseInt(applicationOptions.getI2cSensorAddress(), 16);
					return new I2cSensorCollector(address);
				} catch (NumberFormatException e) {
					System.err.println("I2C Slave Address as Hexdecimal is required for I2C Sensor bus.");
				}
			}
		}

		if (StringUtils.equalsAnyIgnoreCase(applicationOptions.getSensorBus(), "STUB")) {
			return new StubSensorCollector(72.0, 55.0, 512, 600);
		}

		return null;
	}
}
