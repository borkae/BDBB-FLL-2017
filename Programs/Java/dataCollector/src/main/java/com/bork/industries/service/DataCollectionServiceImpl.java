package com.bork.industries.service;

import com.bork.industries.collector.meter.MeterCollector;
import com.bork.industries.collector.sensor.SensorCollector;
import com.bork.industries.publisher.DataPublisher;
import com.bork.industries.service.types.HouseData;

public class DataCollectionServiceImpl implements DataCollectionService {
	private DataPublisher dataPublisher;
	private MeterCollector meterCollector;
	private SensorCollector sensorCollector;

	public DataCollectionServiceImpl(DataPublisher dataPublisher, MeterCollector meterCollector, SensorCollector sensorCollector) {
		this.dataPublisher = dataPublisher;
		this.meterCollector = meterCollector;
		this.sensorCollector = sensorCollector;
	}

	@Override
	public void assembleAndPublish() {
		HouseData houseData = new HouseData();

		// meterCollector.read() is a blocking call and sets the pace of the method. An OV-1 implementation is a read() every 4 seconds.
		houseData.setMeterReading(meterCollector.read());
		houseData.setSensorReading(sensorCollector.read());

		dataPublisher.publish(houseData);
	}

	@Override
	public void continuousAssembleAndPublish(CompletionCallback callback) {
		while (!callback.isDone()) {
			assembleAndPublish();
		}
	}

}
