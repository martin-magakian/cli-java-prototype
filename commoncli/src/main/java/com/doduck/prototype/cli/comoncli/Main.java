package com.doduck.prototype.cli.comoncli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.*;

public class Main {

	public static void main(String[] args) {
		Options options = new Options();
		options.addOption("myCmd", "myCommand", false, "will run myCommand()" );
		options.addOption("helloW", "helloWorld", true, "display hello world the number of time specify" );
		
		try{
			CommandLine line = new BasicParser().parse( options, args );
			
			if( line.hasOption( "myCommand" ) ) {
				myCommand();
			}
			
			if(line.hasOption("helloWorld")){
				String repeat = line.getOptionValue("helloWorld");
				Integer repeatInt = new Integer(repeat);
				for(int i =0; i<repeatInt; i++){
					System.out.println( "Hello world !");
				}
			}
			
		}catch( ParseException exp ) {
		    System.out.println( "Unexpected exception:" + exp.getMessage() );
		}
	}

	public static void myCommand(){
		System.out.println("myCommand() just get call");
	}
	
}
