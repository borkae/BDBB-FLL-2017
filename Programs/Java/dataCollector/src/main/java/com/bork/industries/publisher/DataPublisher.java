package com.bork.industries.publisher;

import java.io.Closeable;

import com.bork.industries.service.types.HouseData;

public interface DataPublisher extends Closeable {
	void publish(HouseData houseData);
}
