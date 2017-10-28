package com.bork.industries.collector.sensor.types;

public class SensorReading {
	private double temperatureSensor1;
	private int vibrationSensor1;
	private boolean pipeOn1;
	private double temperatureSensor2;
	private int vibrationSensor2;
	private boolean pipeOn2;

	public double getTemperatureSensor1() {
		return temperatureSensor1;
	}

	public void setTemperatureSensor1(double temperatureSensor1) {
		this.temperatureSensor1 = temperatureSensor1;
	}

	public int getVibrationSensor1() {
		return vibrationSensor1;
	}

	public void setVibrationSensor1(int vibrationSensor1) {
		this.vibrationSensor1 = vibrationSensor1;
	}

	public boolean isPipeOn1() {
		return pipeOn1;
	}

	public void setPipeOn1(boolean pipeOn1) {
		this.pipeOn1 = pipeOn1;
	}

	public double getTemperatureSensor2() {
		return temperatureSensor2;
	}

	public void setTemperatureSensor2(double temperatureSensor2) {
		this.temperatureSensor2 = temperatureSensor2;
	}

	public int getVibrationSensor2() {
		return vibrationSensor2;
	}

	public void setVibrationSensor2(int vibrationSensor2) {
		this.vibrationSensor2 = vibrationSensor2;
	}

	public boolean isPipeOn2() {
		return pipeOn2;
	}

	public void setPipeOn2(boolean pipeOn2) {
		this.pipeOn2 = pipeOn2;
	}
}
