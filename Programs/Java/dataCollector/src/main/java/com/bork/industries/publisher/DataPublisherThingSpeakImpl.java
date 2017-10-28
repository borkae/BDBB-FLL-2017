package com.bork.industries.publisher;

import java.io.IOException;

import org.springframework.http.*;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.bork.industries.service.types.HouseData;

public class DataPublisherThingSpeakImpl implements DataPublisher {
	private static RestOperations restOperations = new RestTemplate();

	@Override
	public void publish(HouseData houseData) {
		//@formatter:off
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl("https://api.thingspeak.com/update")
				.queryParam("api_key", "W7BI5H4Z0CMJAUNU");
		
		if (houseData.getMeterReading() != null) {
			builder
				.queryParam("field1", houseData.getMeterReading().getMeterId())
				.queryParam("field2", String.valueOf(houseData.getMeterReading().getCurrentGallons()));
		}
				
		if (houseData.getSensorReading() != null) {
			builder
				.queryParam("field3", String.valueOf(houseData.getSensorReading().getTemperatureSensor1()))
				.queryParam("field4", String.valueOf(houseData.getSensorReading().getVibrationSensor1()))
				.queryParam("field5", String.valueOf(houseData.getSensorReading().isPipeOn1()))
				.queryParam("field6", String.valueOf(houseData.getSensorReading().getTemperatureSensor2()))
				.queryParam("field7", String.valueOf(houseData.getSensorReading().getVibrationSensor2()))
				.queryParam("field8", String.valueOf(houseData.getSensorReading().isPipeOn2()));
		}
				
		//@formatter:on

		HttpEntity<?> entity = new HttpEntity<>(null);
		try {
			restOperations.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);

			System.out.println("Published to ThinkSpeak.");
		} catch (Exception e) {
			System.out.println("Could not send data to ThingSpeak.");
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException {
		// Intentionally left blank
	}
}
