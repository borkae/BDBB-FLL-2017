package com.bork.industries.collector.sensor;

import java.io.Closeable;

import com.bork.industries.collector.sensor.types.SensorReading;

public interface SensorCollector extends Closeable{
	SensorReading read(); 
}
