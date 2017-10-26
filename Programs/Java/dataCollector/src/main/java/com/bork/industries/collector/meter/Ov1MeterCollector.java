package com.bork.industries.collector.meter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import com.bork.industries.collector.meter.line.parser.MeterLineParser;
import com.bork.industries.collector.meter.line.parser.MeterLineParserStringUtilsImpl;
import com.bork.industries.collector.meter.types.MeterReading;
import com.fazecast.jSerialComm.SerialPort;

public class Ov1MeterCollector implements MeterCollector {
	// private String commPort;
	private String meterId;
	private MeterLineParser parser;
	private SerialPort port;
	private BufferedReader bufferedReader;

	public Ov1MeterCollector(String commPort, String meterId) {
		// this.commPort = commPort;
		this.meterId = meterId;
		this.parser = new MeterLineParserStringUtilsImpl();

		setupCommPort(commPort);
	}

	private void setupCommPort(String commPort) {
		port = SerialPort.getCommPort(commPort);
		port.openPort();
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 10 * 1000, 0);
		port.setBaudRate(115200);

		bufferedReader = new BufferedReader(new InputStreamReader(port.getInputStream()));
	}

	@Override
	public MeterReading read() {
		String line;

		try {
			line = bufferedReader.readLine();
			System.out.println(line);

			MeterReading meterReading = parser.determineMeterReading(line);

			if (StringUtils.equalsAnyIgnoreCase(meterReading.getMeterId(), meterId)) {
				return meterReading;
			}
		} catch (IOException e) {
			System.out.println("Meter Read Exception...");
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void close() throws IOException {
		if (bufferedReader != null) {
			bufferedReader.close();
		}
		port.closePort();
	}
}
