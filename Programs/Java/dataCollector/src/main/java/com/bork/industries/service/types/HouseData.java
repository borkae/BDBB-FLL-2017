package com.bork.industries.service.types;

import com.bork.industries.collector.meter.types.MeterReading;
import com.bork.industries.collector.sensor.types.SensorReading;

public class HouseData {
	private MeterReading meterReading;
	private SensorReading sensorReading;

	public HouseData() {
		this.meterReading = null;
		this.sensorReading = null;
	}

	public HouseData(MeterReading meterReading, SensorReading sensorReading) {
		this.meterReading = meterReading;
		this.sensorReading = sensorReading;
	}

	public MeterReading getMeterReading() {
		return meterReading;
	}

	public void setMeterReading(MeterReading meterReading) {
		this.meterReading = meterReading;
	}

	public SensorReading getSensorReading() {
		return sensorReading;
	}

	public void setSensorReading(SensorReading sensorReading) {
		this.sensorReading = sensorReading;
	}
}
