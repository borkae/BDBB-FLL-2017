package com.bork.industries.service;

public interface DataCollectionService {

	void assembleAndPublish();
	void continuousAssembleAndPublish(CompletionCallback callback);
}
