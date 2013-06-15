package com.doduck.prototype.cli.jcommando;


public class Main {

	public static void main(String[] args) {
		MyCLI cli = new MyCLI();
		cli.parse(args);
    }
}