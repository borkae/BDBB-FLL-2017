package com.bork.industries;

public class ApplicationOptions {
	private String commPort;
	private boolean listPorts;
	private String meterId;
	private int publishInterval;

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

	public String getMeterId() {
		return meterId;
	}

	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}

	public int getPublishInterval() {
		return publishInterval;
	}

	public void setPublishInterval(int publishInterval) {
		this.publishInterval = publishInterval;
	}
}
