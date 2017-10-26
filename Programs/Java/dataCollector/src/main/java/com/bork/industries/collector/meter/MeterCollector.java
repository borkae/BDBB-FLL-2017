package com.bork.industries.collector.meter;

import java.io.Closeable;

import com.bork.industries.collector.meter.types.MeterReading;

public interface MeterCollector extends Closeable {
	MeterReading read();
}
