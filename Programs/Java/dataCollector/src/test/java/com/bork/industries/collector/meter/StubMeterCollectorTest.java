package com.bork.industries.collector.meter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;

import com.bork.industries.collector.meter.types.MeterReading;

public class StubMeterCollectorTest {
	private static final int DURATION = 500;
	private static final String METER_ID = "7654321";
	private MeterCollector meterCollector;

	@Before
	public void setup() {
		meterCollector = new StubMeterCollector(METER_ID, DURATION);
	}

	@Test
	public void read_staticValuesWith1SecInterval_verifyValuesAndCallDuration() {
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		MeterReading reading = meterCollector.read();
		stopWatch.stop();

		assertEquals(METER_ID, reading.getMeterId());
		assertEquals(StubMeterCollector.CURRENT_GALLONS, reading.getCurrentGallons(), 0.1);
		assertTrue(stopWatch.getTime() >= DURATION);
	}
}
