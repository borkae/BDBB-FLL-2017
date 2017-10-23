package com.bork.industries.meter.line.parser;

import org.apache.commons.lang3.StringUtils;

import com.bork.industries.meter.line.parser.types.MeterReading;

public class MeterLineParserStringUtilsImpl implements MeterLineParser {
	@Override
	public MeterReading determineMeterReading(String meterReadingLine) {
		String[] splitMeterReadingLine = StringUtils.splitByWholeSeparator(meterReadingLine, ",");

		MeterReading meterReading = new MeterReading();
		meterReading.setMeterId(splitMeterReadingLine[1]);
		meterReading.setCurrentGallons(convertToDouble(splitMeterReadingLine[2]));

		return meterReading;
	}

	private static double convertToDouble(String stringValue) {
		return Double.valueOf(stringValue);
	}
}
