package com.bork.industries.parser;

import static org.junit.Assert.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.Test;

public class LineEvaluatorTest {
	private LineEvaluator lineEvaluator;

	@Before
	public void setup() {
		lineEvaluator = new LineEvaluator(true, ChronoUnit.HOURS);
	}

	@Test
	public void evaluate_firstEntry_returnsNull() {
		ZonedDateTime entryTime = ZonedDateTime.of(2017, 11, 6, 13, 01, 02, 0, ZoneId.of("GMT"));
		Double meterReading = 1000000.0;
		LineEntry result = lineEvaluator.evaluate(entryTime, meterReading);

		assertNull(result);
	}
	
	@Test
	public void evaulate_twoEntriesSameHour_returnsNull() {
		ZonedDateTime entryTime = ZonedDateTime.of(2017, 11, 6, 13, 01, 02, 0, ZoneId.of("GMT"));
		Double meterReading = 1000000.0;
		LineEntry firstEntry = lineEvaluator.evaluate(entryTime, meterReading);
		assertNull(firstEntry);
		
		LineEntry secondEntry = lineEvaluator.evaluate(entryTime.plusMinutes(3), meterReading + 10);
		assertNull(secondEntry);
	}
	
	@Test
	public void evaulate_twoEntriesNextHour_returnsFirstHourWithDifference() {
		ZonedDateTime entryTime = ZonedDateTime.of(2017, 11, 6, 13, 01, 02, 0, ZoneId.of("GMT"));
		Double meterReading = 1000000.0;
		
		LineEntry firstEntry = lineEvaluator.evaluate(entryTime, meterReading);
		assertNull(firstEntry);
		
		LineEntry secondEntry = lineEvaluator.evaluate(entryTime.plusHours(1), meterReading + 10);
		assertNotNull(secondEntry);
		assertEquals(entryTime.truncatedTo(ChronoUnit.HOURS), secondEntry.getEntryDateTime());
		assertEquals(10, secondEntry.getReading());
		assertFalse(secondEntry.isCompleteOnFront());
		assertTrue(secondEntry.isCompleteOnBack());
	}
	
	@Test
	public void evaulate_threeEntriesNextHour_returnsFirstHourWithDifference() {
		ZonedDateTime entryTime = ZonedDateTime.of(2017, 11, 6, 13, 01, 02, 0, ZoneId.of("GMT"));
		Double meterReading = 1000000.0;
		
		LineEntry firstEntry = lineEvaluator.evaluate(entryTime, meterReading);
		assertNull(firstEntry);
		
		LineEntry secondEntry = lineEvaluator.evaluate(entryTime.plusHours(1), meterReading + 10);
		assertNotNull(secondEntry);
		
		LineEntry thirdEntry = lineEvaluator.evaluate(entryTime.plusHours(2), meterReading + 30);
		assertNotNull(thirdEntry);
		
		assertEquals(entryTime.plusHours(1).truncatedTo(ChronoUnit.HOURS), thirdEntry.getEntryDateTime());
		assertEquals(20, thirdEntry.getReading());
		assertTrue(thirdEntry.isCompleteOnFront());
		assertTrue(thirdEntry.isCompleteOnBack());
	}
	
	@Test
	public void lastEntry_twoEntriesSameHour_returnsHourWithDifference() {
		ZonedDateTime entryTime = ZonedDateTime.of(2017, 11, 6, 13, 01, 02, 0, ZoneId.of("GMT"));
		Double meterReading = 1000000.0;
		LineEntry firstEntry = lineEvaluator.evaluate(entryTime, meterReading);
		assertNull(firstEntry);
		
		LineEntry secondEntry = lineEvaluator.evaluate(entryTime.plusMinutes(3), meterReading + 10);
		assertNull(secondEntry);

		LineEntry finalEntry = lineEvaluator.lastEntry();
		assertEquals(entryTime.truncatedTo(ChronoUnit.HOURS), finalEntry.getEntryDateTime());
		assertEquals(10, finalEntry.getReading());
		assertFalse(finalEntry.isCompleteOnFront());
		assertFalse(finalEntry.isCompleteOnBack());
	}
}
