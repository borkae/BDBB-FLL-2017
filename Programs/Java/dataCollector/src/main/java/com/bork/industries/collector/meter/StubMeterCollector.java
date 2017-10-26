package com.bork.industries.collector.meter;

import com.bork.industries.collector.meter.types.MeterReading;

public class StubMeterCollector implements MeterCollector {
	public static final double CURRENT_GALLONS = 100001.0;
	private String meterId;
	private int interval;

	public StubMeterCollector(String meterId, int interval) {
		this.meterId = meterId;
		this.interval = interval;
	}

	@Override
	public MeterReading read() {
		MeterReading reading = new MeterReading();

		reading.setMeterId(meterId);
		reading.setCurrentGallons(CURRENT_GALLONS);

		try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			// do nothing
		}

		return reading;
	}

	@Override
	public void close() {
		// nothing here
	}

}
