package com.doduck.prototype.cli.clajr;

import com.doduck.prototype.cli.clajr.CLAJR.HelpNeededException;
import com.doduck.prototype.cli.clajr.CLAJR.ParseException;

public class Main {

	public static void main(String[] args) throws Throwable{
		CLAJR.parse(args, new Action());
    }
}