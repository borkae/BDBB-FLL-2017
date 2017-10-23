package com.bork.industries.meter.line.parser;

import static org.junit.Assert.*;

import org.junit.*;

import com.bork.industries.meter.line.parser.types.MeterReading;

public class MeterLineParserStringUtilsImplTest {
	private MeterLineParser meterLineParser;

	private String meterReadingLine;
	private MeterReading meterReading;

	@Before
	public void setup() {
		meterLineParser = new MeterLineParserStringUtilsImpl();
	}

	@Test
	public void determineMeterReading_validInput_validValuesReturned() {
		meterReadingLine = "$UMBOM,70112229,1010458,8,0,130*7B";

		meterReading = meterLineParser.determineMeterReading(meterReadingLine);

		assertNotNull(meterReading);
		assertEquals("70112229", meterReading.getMeterId());
		assertEquals(1010458.0, meterReading.getCurrentGallons(), 0);
	}
}
