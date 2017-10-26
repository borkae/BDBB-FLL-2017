package com.bork.industries.collector.meter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.bork.industries.collector.meter.types.MeterReading;

@Ignore("Test has hardware dependencies.")
public class Ov1MeterCollectorTest {
	private MeterCollector meterCollector;

	@Before
	public void setup() {
		meterCollector = new Ov1MeterCollector("COM7", "70112229");
	}

	@After
	public void tearDown() throws IOException {
		meterCollector.close();
	}

	@Test
	public void read_givenConnectedHardwareAndPlatform_verifyReadFromMeter() {
		MeterReading meterReading = meterCollector.read();

		assertNotNull(meterReading);
		assertEquals("70112229", meterReading.getMeterId());
		assertTrue(meterReading.getCurrentGallons() > 1);
	}
}
