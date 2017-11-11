package com.bork.industries;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class App {
	private static final String LIST_PORTS_OPTION_NAME = "listPorts";
	private static final String PORT_OPTION_NAME = "port";
	private static final String METER_ID_OPTION_NAME = "meterId";
	private static final String PUBLISH_INTERVAL_OPTION_NAME = "publishInterval";

	public static void main(String[] args) {
		Options options = createOptions();
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine line = parser.parse(options, args);
			ApplicationOptions appOptions = parseToApplicationOptions(line);

			if (false) {
				// nothing
			} else {
				usage(options);
			}

		} catch (ParseException exp) {
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
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
