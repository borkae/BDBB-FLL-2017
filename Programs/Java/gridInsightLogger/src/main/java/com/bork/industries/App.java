package com.bork.industries;

import java.io.*;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.bork.industries.meter.line.parser.*;
import com.bork.industries.meter.line.parser.types.MeterReading;
import com.fazecast.jSerialComm.SerialPort;

public class App {
	private static final String LIST_PORTS_OPTION_NAME = "listPorts";
	private static final String PORT_OPTION_NAME = "port";

	private static RestOperations restOperations = new RestTemplate();
	private static MeterLineParser meterLineParser = new MeterLineParserStringUtilsImpl();

	public static void main(String[] args) {
		Options options = createOptions();
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine line = parser.parse(options, args);
			ApplicationOptions appOptions = parseToApplicationOptions(line);

			if (appOptions.isListPorts()) {
				System.out.println("Listing known ports...");
				SerialPort[] ports = SerialPort.getCommPorts();
				for (SerialPort serialPort : ports) {
					System.out.println("  " + serialPort.getSystemPortName());
				}
			} else if (StringUtils.isNotBlank(appOptions.getCommPort())) {
				SerialPort port = SerialPort.getCommPort(appOptions.getCommPort());
				port.openPort();
				port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 10 * 1000, 0);
				port.setBaudRate(115200);
				listenToPort(port);
				port.closePort();
			} else {
				usage(options);
			}

		} catch (ParseException exp) {
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}
	}

	private static void listenToPort(SerialPort port) {
		boolean done = false;
		InputStream inputStream = port.getInputStream();
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

			String line;

			try {
				do {
					line = bufferedReader.readLine();

					System.out.println(line);

					MeterReading meterReading = meterLineParser.determineMeterReading(line);

					//@formatter:off
					UriComponentsBuilder builder = UriComponentsBuilder
							.fromHttpUrl("https://api.thingspek.com/update")
							.queryParam("api_key", "W7BI5H4Z0CMJAUNU")
							.queryParam("field1", meterReading.getMeterId())
							.queryParam("field2", String.valueOf(meterReading.getCurrentGallons()));
					//@formatter:on

					HttpEntity<?> entity = new HttpEntity<>(null);
					try {
						restOperations.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
					} catch (Exception e) {
						System.out.println("Could not send data to ThingSpeak.");
						e.printStackTrace();
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

		return retval;
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

		return options;
	}

}
