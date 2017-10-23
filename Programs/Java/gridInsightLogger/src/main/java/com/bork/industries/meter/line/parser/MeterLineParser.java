package com.bork.industries.meter.line.parser;

import com.bork.industries.meter.line.parser.types.MeterReading;

public interface MeterLineParser {
	MeterReading determineMeterReading(String meterReadingLine);
}
