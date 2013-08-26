package com.doduck.prototype.cli.joptsimple;

import com.sun.tools.javac.util.List;

import joptsimple.OptionParser;
import joptsimple.OptionSet;


public class Main {

	
	public static void main(String[] args) {
		OptionParser p = new OptionParser("fc:q::");
		p.accepts("ftp" );
		p.accepts("user").requiredIf("ftp").withRequiredArg();
		p.accepts("pass").requiredIf("ftp").withRequiredArg();
		
		OptionSet opt = p.parse(args);
		if(opt.has("f")){
			System.out.println("-f exist");
			System.out.println("-f with : "+ opt.valueOf("f"));
		}
		if(opt.has( "c" )){
			System.out.println("-c exist with:"+opt.valueOf("c"));
		}
		
		if(opt.has("ftp")){
			// we know username and password
			// existe if -ftp is set
			String user = (String) opt.valueOf("user");
			String pwd = (String) opt.valueOf("pass");
			System.out.println("user:"+user+" pass:"+pwd);
		}
    }
}