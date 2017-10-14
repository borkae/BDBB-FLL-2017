package com.bork.industries;

public class ApplicationOptions {
	private String commPort;
	private boolean listPorts;

	public String getCommPort() {
		return commPort;
	}

	public void setCommPort(String commPort) {
		this.commPort = commPort;
	}

	public boolean isListPorts() {
		return listPorts;
	}

	public void setListPorts(boolean listPorts) {
		this.listPorts = listPorts;
	}
}
