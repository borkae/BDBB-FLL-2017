package com.bork.industries.meter.line.parser.types;

public class MeterReading {
	private String meterId;
	private double currentGallons;

	public String getMeterId() {
		return meterId;
	}

	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}

	public double getCurrentGallons() {
		return currentGallons;
	}

	public void setCurrentGallons(double currentGallons) {
		this.currentGallons = currentGallons;
	}
}
