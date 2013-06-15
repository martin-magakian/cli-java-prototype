package com.doduck.prototype.cli.jcommender;

import com.beust.jcommander.Parameter;

public class UploaderCommand {

	@Parameter(names = "-debug", description = "Debug mode")
	private boolean debug = false;

	@Parameter(names = "-file", description = "file to send", required = true)
	private String file = null;
	
	@Parameter(names = "-srv", description = "server to send the file to", required = true)
	private String srv = null;

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getSrv() {
		return srv;
	}

	public void setSrv(String srv) {
		this.srv = srv;
	}
	

}
