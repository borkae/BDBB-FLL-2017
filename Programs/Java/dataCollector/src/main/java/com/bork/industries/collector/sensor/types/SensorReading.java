package com.bork.industries.collector.sensor.types;

public class SensorReading {
	private double tempSensor1;
	private double tempSensor2;
	private int vibrationSensor1;
	private int vibrationSensor2;

	public double getTempSensor1() {
		return tempSensor1;
	}

	public void setTempSensor1(double tempSensor1) {
		this.tempSensor1 = tempSensor1;
	}

	public double getTempSensor2() {
		return tempSensor2;
	}

	public void setTempSensor2(double tempSensor2) {
		this.tempSensor2 = tempSensor2;
	}

	public int getVibrationSensor1() {
		return vibrationSensor1;
	}

	public void setVibrationSensor1(int vibrationSensor1) {
		this.vibrationSensor1 = vibrationSensor1;
	}

	public int getVibrationSensor2() {
		return vibrationSensor2;
	}

	public void setVibrationSensor2(int vibrationSensor2) {
		this.vibrationSensor2 = vibrationSensor2;
	}
}
