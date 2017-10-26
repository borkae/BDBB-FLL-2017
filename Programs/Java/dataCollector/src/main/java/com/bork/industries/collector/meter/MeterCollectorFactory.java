package com.bork.industries.collector.meter;

import org.apache.commons.lang3.StringUtils;

import com.bork.industries.ApplicationOptions;

public class MeterCollectorFactory {
	public static MeterCollector create(ApplicationOptions applicationOptions) {
		boolean exitEarly = false;

		if (StringUtils.isBlank(applicationOptions.getMeterId())) {
			System.err.println("Meter Id is a required parameter.");
			exitEarly = true;
		}
		if (StringUtils.isBlank(applicationOptions.getCommPort())) {
			System.err.println("Comm Port is a required parameter.");
			exitEarly = true;
		}

		if (exitEarly) {
			return null;
		}

		if (StringUtils.equals("STUB", applicationOptions.getCommPort())) {
			return new StubMeterCollector(applicationOptions.getMeterId(), 4000);
		}

		return new Ov1MeterCollector(applicationOptions.getCommPort(), applicationOptions.getMeterId());
	}
}
