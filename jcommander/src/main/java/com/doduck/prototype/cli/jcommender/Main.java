package com.doduck.prototype.cli.jcommender;

import com.beust.jcommander.JCommander;


public class Main {

	public static void main(String[] args) {
		UploaderCommand uploader = new UploaderCommand();
		new JCommander(uploader, args);

		System.out.println("sending file: "+uploader.getFile());
		System.out.println("to srv: "+uploader.getSrv());
		System.out.println("debug enable: "+uploader.isDebug());
		
	}
	
}
