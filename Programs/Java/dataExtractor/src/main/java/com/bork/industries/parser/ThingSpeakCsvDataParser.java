package com.bork.industries.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ThingSpeakCsvDataParser implements DataParser {
	private static final String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss zzz";

	@Override
	public Map<ZonedDateTime, Long> parseToHour(InputStream inputStream, boolean includePartialHour) {
		return process(inputStream, includePartialHour, ChronoUnit.HOURS);
	}

	@Override
	public Map<ZonedDateTime, Long> parseToHour(InputStream inputStream) {
		return process(inputStream, true, ChronoUnit.HOURS);
	}

	@Override
	public Map<ZonedDateTime, Long> parseToDate(InputStream inputStream, boolean includePartialDate) {
		return process(inputStream, includePartialDate, ChronoUnit.DAYS);
	}

	@Override
	public Map<ZonedDateTime, Long> parseToDate(InputStream inputStream) {
		return process(inputStream, true, ChronoUnit.DAYS);
	}

	
	private static Map<ZonedDateTime, Long> process(InputStream inputStream, boolean includePartials, TemporalUnit temporalUnit) {
		LineEvaluator lineEvaluator = new LineEvaluator(includePartials, temporalUnit);
		Map<ZonedDateTime, Long> result = new TreeMap<>();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			String line = reader.readLine(); // read and dump the first line

			while ((line = reader.readLine()) != null) {
				String[] parts = StringUtils.splitPreserveAllTokens(line, ',');

				if (parts.length != 10) {
					System.err.println("Only found " + parts.length + " parts.");
					throw new IllegalStateException("Bad String: " + line);
				}

				ZonedDateTime entryDateTime = ZonedDateTime.parse(parts[0], DateTimeFormatter.ofPattern(DATE_FORMAT_STRING));
				Double reading = NumberUtils.createDouble(StringUtils.trimToNull(parts[3]));

				LineEntry lineEntry = lineEvaluator.evaluate(entryDateTime, reading);
				if (lineEntry != null) {
					result.put(lineEntry.getEntryDateTime(), lineEntry.getReading());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		LineEntry lastLineEntry = lineEvaluator.lastEntry();
		if (lastLineEntry != null) {
			result.put(lastLineEntry.getEntryDateTime(), lastLineEntry.getReading());
		}

		return result;
	}

}
