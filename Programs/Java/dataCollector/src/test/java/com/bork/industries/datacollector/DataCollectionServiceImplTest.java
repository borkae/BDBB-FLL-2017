package com.bork.industries.datacollector;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createControl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertNotNull;

import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;

import com.bork.industries.collector.meter.MeterCollector;
import com.bork.industries.collector.meter.types.MeterReading;
import com.bork.industries.collector.sensor.SensorCollector;
import com.bork.industries.collector.sensor.types.SensorReading;
import com.bork.industries.publisher.DataPublisher;
import com.bork.industries.service.DataCollectionService;
import com.bork.industries.service.DataCollectionServiceImpl;
import com.bork.industries.service.types.HouseData;

public class DataCollectionServiceImplTest {
	private DataCollectionService dataCollectionService;

	private IMocksControl mocksControl;
	private DataPublisher mockDataPublisher;
	private MeterCollector mockMeterCollector;
	private SensorCollector mockSensorCollector;

	@Before
	public void setup() {
		mocksControl = createControl();
		mockDataPublisher = mocksControl.createMock(DataPublisher.class);
		mockMeterCollector = mocksControl.createMock(MeterCollector.class);
		mockSensorCollector = mocksControl.createMock(SensorCollector.class);

		dataCollectionService = new DataCollectionServiceImpl(mockDataPublisher, mockMeterCollector, mockSensorCollector);
	}

	@Test
	public void setupOnly_collectorNotNull() {
		assertNotNull(dataCollectionService);
	}

	@Test
	public void assembleAndPublishData_happyPath_verifyMockMethodsCalled() {
		expect(mockMeterCollector.read()).andReturn(new MeterReading());
		expect(mockSensorCollector.read()).andReturn(new SensorReading());
		mockDataPublisher.publish(anyObject(HouseData.class));
		expectLastCall();

		mocksControl.replay();
		dataCollectionService.assembleAndPublish();
		mocksControl.verify();
	}
}
