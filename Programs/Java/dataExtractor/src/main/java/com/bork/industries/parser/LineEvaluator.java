package com.bork.industries.parser;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalUnit;

public class LineEvaluator {
	private boolean allowPartialUnits;
	private TemporalUnit temporalUnit;

	private ZonedDateTime currentBlock;
	private Double readingAtStartOfBlock;
	private Double latestReadingOfBlock;

	private ZonedDateTime previousBlock;

	public LineEvaluator(boolean allowPartialUnits, TemporalUnit temporalUnit) {
		this.allowPartialUnits = allowPartialUnits;
		this.temporalUnit = temporalUnit;
	}

	public LineEntry evaluate(ZonedDateTime entryTime, Double meterReading) {
		if(entryTime == null || meterReading == null) return null;

		ZonedDateTime truncatedDateTime = entryTime.truncatedTo(temporalUnit);
				
		if (currentBlock == null) {
			currentBlock = truncatedDateTime;
			readingAtStartOfBlock = meterReading;
			latestReadingOfBlock = meterReading;
			return null;
		}

		if (currentBlock.equals(truncatedDateTime)) {
			latestReadingOfBlock = meterReading;
			return null;
		}

		LineEntry entry = new LineEntry(temporalUnit);

		entry.setEntryDateTime(currentBlock);
		entry.setReading(Math.round(meterReading - readingAtStartOfBlock));

		entry.setCompleteOnFront(unitsAreAdjacent(previousBlock, currentBlock));
		entry.setCompleteOnBack(unitsAreAdjacent(currentBlock, truncatedDateTime));

		previousBlock = currentBlock;
		currentBlock = truncatedDateTime;
		readingAtStartOfBlock = meterReading;

		if (!allowPartialUnits && !(entry.isCompleteOnFront() && entry.isCompleteOnBack())) {
			return null;
		}
		
		return entry;
	}

	private boolean unitsAreAdjacent(ZonedDateTime firstBlock, ZonedDateTime secondBlock) {
		if (firstBlock == null)
			return false;
		if (secondBlock == null)
			return false;
		
		return firstBlock.plus(1, temporalUnit).equals(secondBlock);
	}

	public LineEntry lastEntry() {
		LineEntry entry = new LineEntry(temporalUnit);
		
		entry.setEntryDateTime(currentBlock);
		entry.setReading(Math.round(latestReadingOfBlock - readingAtStartOfBlock));

		entry.setCompleteOnFront(unitsAreAdjacent(previousBlock, currentBlock));
		entry.setCompleteOnBack(false);
		
		if(!allowPartialUnits) return null;
		
		return entry;
	}

}
