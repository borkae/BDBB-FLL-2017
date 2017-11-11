package com.bork.industries.parser;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Map;

public interface DataParser {
	Map<ZonedDateTime, Long> parseToHour(InputStream inputStream, boolean includePartialHour);

	Map<ZonedDateTime, Long> parseToHour(InputStream inputStream);

	Map<ZonedDateTime, Long> parseToDate(InputStream inputStream, boolean includePartialDate);

	Map<ZonedDateTime, Long> parseToDate(InputStream inputStream);
}
