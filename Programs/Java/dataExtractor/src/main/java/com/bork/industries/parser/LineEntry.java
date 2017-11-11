package com.bork.industries.parser;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;

import lombok.Data;
import lombok.NonNull;

public @Data class LineEntry {
	@NonNull private TemporalUnit temporalUnit;
	private boolean completeOnFront;
	private boolean completeOnBack;
	private ZonedDateTime entryDateTime;
	private long reading;
}
