package com.bork.industries.collector.meter.line.parser;

import com.bork.industries.collector.meter.types.MeterReading;

public interface MeterLineParser {
	MeterReading determineMeterReading(String meterReadingLine);
}
