package com.bork.industries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.bork.industries.collector.meter.MeterCollector;
import com.bork.industries.collector.meter.MeterCollectorFactory;
import com.bork.industries.collector.meter.line.parser.MeterLineParser;
import com.bork.industries.collector.meter.line.parser.MeterLineParserStringUtilsImpl;
import com.bork.industries.collector.meter.types.MeterReading;
import com.bork.industries.collector.sensor.SensorCollector;
import com.bork.industries.collector.sensor.SensorCollectorFactory;
import com.bork.industries.publisher.DataPublisher;
import com.bork.industries.publisher.DataPublisherFactory;
import com.bork.industries.service.CompletionCallback;
import com.bork.industries.service.DataCollectionService;
import com.bork.industries.service.DataCollectionServiceImpl;
import com.fazecast.jSerialComm.SerialPort;

public class App {
	private static final String LIST_PORTS_OPTION_NAME = "listPorts";
	private static final String PORT_OPTION_NAME = "port";
	private static final String METER_ID_OPTION_NAME = "meterId";
	private static final String PUBLISH_INTERVAL_OPTION_NAME = "publishInterval";

	private static RestOperations restOperations = new RestTemplate();
	private static MeterLineParser meterLineParser = new MeterLineParserStringUtilsImpl();

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Options options = createOptions();
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine line = parser.parse(options, args);
			ApplicationOptions appOptions = parseToApplicationOptions(line);

			if (appOptions.isListPorts()) {
				listPorts();
			} else {

				MeterCollector meterCollector = MeterCollectorFactory.create(appOptions);
				SensorCollector sensorCollector = SensorCollectorFactory.create(appOptions);
				DataPublisher publisher = DataPublisherFactory.create(appOptions);

				if (meterCollector == null || sensorCollector == null || publisher == null) {
					usage(options);
				} else {
					DataCollectionService service = new DataCollectionServiceImpl(publisher, meterCollector, sensorCollector);

					service.continuousAssembleAndPublish(new CompletionCallback() {
						@Override
						public boolean isDone() {
							return false;
						}
					});

					try {
						meterCollector.close();
						sensorCollector.close();
						publisher.close();
					} catch (IOException e) {
						System.err.println("Application interrupted.");
						e.printStackTrace();
					}
				}
			}
		} catch (ParseException exp) {
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}
	}

	private static void listPorts() {
		System.out.println("Listing known ports...");
		SerialPort[] ports = SerialPort.getCommPorts();
		for (SerialPort serialPort : ports) {
			System.out.println("  " + serialPort.getSystemPortName());
		}
		if (ports.length == 0) {
			System.out.println("  ...No Serial Ports found...");
		}
	}

	private static void listenToPort(SerialPort port, String meterId, int publishInterval) {
		boolean done = false;
		InputStream inputStream = port.getInputStream();
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

			String line;
			LocalDateTime lastRead = LocalDateTime.now().minus(publishInterval, ChronoUnit.MILLIS);

			try {
				do {
					line = bufferedReader.readLine();

					System.out.println(line);

					MeterReading meterReading = meterLineParser.determineMeterReading(line);

					if (StringUtils.equalsAnyIgnoreCase(meterReading.getMeterId(), meterId)) {
						if (lastRead.plus(publishInterval, ChronoUnit.MILLIS).isBefore(LocalDateTime.now())) {
							//@formatter:off
							UriComponentsBuilder builder = UriComponentsBuilder
									.fromHttpUrl("https://api.thingspeak.com/update")
									.queryParam("api_key", "W7BI5H4Z0CMJAUNU")
									.queryParam("field1", meterReading.getMeterId())
									.queryParam("field2", String.valueOf(meterReading.getCurrentGallons()));
							//@formatter:on

							HttpEntity<?> entity = new HttpEntity<>(null);
							try {
								restOperations.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
								lastRead = LocalDateTime.now();

								System.out.println("Published to ThinkSpeak.");
							} catch (Exception e) {
								System.out.println("Could not send data to ThingSpeak.");
								e.printStackTrace();
							}
						}
					}
				} while (!done);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private static ApplicationOptions parseToApplicationOptions(CommandLine line) {
		ApplicationOptions retval = new ApplicationOptions();

		retval.setListPorts(line.hasOption(LIST_PORTS_OPTION_NAME));
		retval.setCommPort(line.getOptionValue(PORT_OPTION_NAME));
		retval.setMeterId(line.getOptionValue(METER_ID_OPTION_NAME));
		retval.setPublishInterval(converToPublishInterval(line.getOptionValue(PUBLISH_INTERVAL_OPTION_NAME, "15")));

		return retval;
	}

	private static int converToPublishInterval(String publishRateInSeconds) {
		int rateInSeconds = Integer.parseInt(publishRateInSeconds);
		return rateInSeconds * 1000; // ms
	}

	private static void usage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("gridInsightLogger", options);
	}

	private static Options createOptions() {
		Options options = new Options();

		Option listPorts = Option.builder("l").longOpt(LIST_PORTS_OPTION_NAME).desc("Lists available ports to the serial library.").build();
		options.addOption(listPorts);

		Option port = Option.builder("p").longOpt(PORT_OPTION_NAME).hasArg().argName("portName").desc("The port the grid insight is running on.").build();
		options.addOption(port);

		Option meterId = Option.builder("m").longOpt(METER_ID_OPTION_NAME).hasArg().argName("Id").desc("The meter id grid insight should filter readings to.").build();
		options.addOption(meterId);

		Option publishRate = Option.builder("r").longOpt(PUBLISH_INTERVAL_OPTION_NAME).hasArg().argName("seconds")
				.desc("The minimum time in seconds between reads to push the data to ThinkSpeak.  Default is 15 seconds.").build();
		options.addOption(publishRate);

		return options;
	}

}
