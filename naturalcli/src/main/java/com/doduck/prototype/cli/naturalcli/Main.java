package com.doduck.prototype.cli.naturalcli;

import java.util.HashSet;
import java.util.Set;

import org.naturalcli.Command;
import org.naturalcli.ExecutionException;
import org.naturalcli.ICommandExecutor;
import org.naturalcli.InvalidSyntaxException;
import org.naturalcli.NaturalCLI;
import org.naturalcli.ParseResult;


public class Main {

	public static void main(String[] args) throws ExecutionException, InvalidSyntaxException {
		
		ICommandExecutor sendFile = new ICommandExecutor ()
        {
           public void execute(ParseResult pr) throws ExecutionException 
           {
        	   String file = (String) pr.getParameterValue(0);
        	   String server = (String) pr.getParameterValue(1);
        	   String userName = (String) pr.getParameterValue(2);
        	   String msg = (String) pr.getParameterValue(3);
        	   
        	   // TODO send the file to the server...
        	   
        	   System.out.println("the file '"+file+"' was sent sucessfully"); 
        	   System.out.println("to '"+server+"' and the user '"+userName+"'");
        	   System.out.println("get notify with the message '"+msg+"'");
           }
        };
		
		Command cmd = new Command("send file <file:string> to <server:string> notify <username:string> with <msg:string>",
			 "Send a file to a server with notification message", 
			 sendFile);

		
	    Set<Command> cs = new HashSet<Command>();
	    cs.add(cmd);
	    new NaturalCLI(cs).execute(args);
    }
	
}
