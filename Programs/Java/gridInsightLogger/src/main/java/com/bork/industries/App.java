package com.bork.industries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

import com.fazecast.jSerialComm.SerialPort;

public class App {
	private static final String LIST_PORTS_OPTION_NAME = "listPorts";
	private static final String PORT_OPTION_NAME = "port";

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
				port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 10*1000, 0);
				port.setBaudRate(115200);
				displayData(port);
				port.closePort();
			} else {
				usage(options);
			}

		} catch (ParseException exp) {
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}
	}

	private static void displayData(SerialPort port) {
		boolean done = false;
		InputStream inputStream = port.getInputStream();
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

			String line;

			try {
				do {
					line = bufferedReader.readLine();

					System.out.println(line);
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
