package com.bork.industries.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ThingSpeakCsvDataParserTest {
	private DataParser dataParser;
	private InputStream inputStream;

	@Before
	public void setup() throws IOException {
		dataParser = new ThingSpeakCsvDataParser();

		inputStream = Files.newInputStream(Paths.get("src/test/resources/feeds.csv"), StandardOpenOption.READ);
	}

	@After
	public void tearDown() throws IOException {
		if (inputStream != null) {
			inputStream.close();
		}
	}

	@Test
	public void verifySetup() {
		assertNotNull(dataParser);
		assertNotNull(inputStream);
	}

	@Test
	public void parseToHour_givenSourceFile_expectedNumberOfRecords() {
		Map<ZonedDateTime, Long> actual = dataParser.parseToHour(inputStream);

		assertNotNull(actual);
		assertEquals(463, actual.size());

		outputMap(actual);
	}

	@Test
	public void parseToHour_givenSourceFileNoPartionals_expectedNumberOfRecords() {
		Map<ZonedDateTime, Long> actual = dataParser.parseToHour(inputStream, false);

		assertNotNull(actual);
		assertEquals(456, actual.size());

		outputMap(actual);
	}

	private static void outputMap(Map<ZonedDateTime, Long> actual) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		ZoneId centralTimeZone = ZoneId.of("America/Chicago");
		System.out.println("Date/Time,Reading");
		for (Entry<ZonedDateTime, Long> entry : actual.entrySet()) {
			LocalDateTime central = entry.getKey().withZoneSameInstant(centralTimeZone).toLocalDateTime();
			System.out.println(central.format(formatter) + "," + entry.getValue());
		}
	}

}
