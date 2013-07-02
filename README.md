cli-java-prototype
==================
Exemple of code related to the article [CLI prototypes in Java](http://doduck.com/en/command-line-interface-in-java-api-overview/) <br />
or in French [Command Line Interface en Java](http://doduck.com/fr/command-line-interface-en-java/) <br />

This exemple will explore how to use the Command Line Interface (CLI) in Java.
We will try few API who solve the complexity of parsing the CLI:
* commoncli
* jopt-simple
* jcommando
* jcommander
* clajr

How to use
=========
The project is build with maven who make it easier to import with Eclipse.


Import project to Eclipse
---
First you will need to install maven on your system.
Then in the console go to the project folder and run the following maven command.
> cd /path/to/folder/cli-java-prototype/  <br />
> mvn eclipse:eclipse

The project is now set to be import in eclipse.
Open eclipse. Go to File > import... <br />
![Import CLI prototype into eclipse](/README_src/import.png "Import CLI prototype into eclipse")

Select Existing project into eclipse. Click Next. <br />
![Import CLI prototype finish](/README_src/import.png "Import CLI prototype finish")


CLI argument from eclipse
---
In order to "simulate" a your program to run from eclipse with CLI parameters, you need to create a Run or Debug Configuration.

![Create eclipse debug configuration 1](/README_src/createCLI_1.png "Create eclipse debug configuration 1")
![Create eclipse debug configuration 2](/README_src/createCLI_2.png "Create eclipse debug configuration 2")
![Create eclipse debug configuration 3](/README_src/createCLI_3.png "Create eclipse debug configuration 3")

Now hit the debug button.
 <br />

Your can retrive your debug configuration in: <br />
![run debug configuration](/README_src/runCLI.png "run debug configuration")


Compile into a jar
---
A executable jar need to be self-contained. It mean that all the class need to be embeded into on jar. <br />
It's also the case with third party jar (or need to be load explicitly by reflexion)

The maven pom.xml files are using maven-assembly-plugin who make it easier to merge all those compiled .jar class into a single executable jar with the command:
> mvn clean install <br />

You can run this command at the root of the project to compile all the projects. <br />
Or you can run this command within each sub-project to compile only this sub-project.

This command will create an executable into the "target" directory.<br />
For the sub-project "commoncli" you will find the executable jar in /cli-java-prototype/commoncli/target/commoncli-1-jar-with-dependencies.jar


Specify the Main() of your executable jar
---
When creating the executable jar you need to specify where is the Main class to run.<br />

Kind of the entry point using:

	public static void main(String[] args) {
	}

It also the maven-assembly-plugin who is in charge of specify this class to run from the CLI as the entry point.<br />
Look into the pom.xml file of each project. You will find something like:

	<archive>
		<manifest>
	    	<addClasspath>true</addClasspath>
	    	<mainClass>com.doduck.prototype.cli.jcommando.Main</mainClass>
		</manifest>
	</archive>

In this case the Main class is specify to be "com.doduck.prototype.cli.jcommando.Main"<br />

In reallity a jar file is just a zip file.<br />
By unziping it we can notice a /META-INF/MANIFEST.MF file who contain the address of the Main class

	Main-Class: com.doduck.prototype.cli.jcommando.Main

who will also find the compile .java file into .class files.


Run the jar
---
In the command line go to your executable jar into the /cli-java-prototype/commoncli/target/commoncli-1-jar-with-dependencies.jar directory for exemple.
Then execute the following command:
> java -jar commoncli-1-jar-with-dependencies.jar -myCmd -helloWorld 3

it should generate to following output in your console:
> myCommand() just get call <br />
> Hello world ! <br />
> Hello world ! <br />
> Hello world ! <br />


Running the CLI exemples
=========

Commoncli
---
Command:
> java -jar commoncli-1-jar-with-dependencies.jar -myCmd -helloWorld 3 <br />

Result:
> myCommand() just get call <br />
> Hello world ! <br />
> Hello world ! <br />
> Hello world ! <br />


Jcommando
---
Command:
> java -jar jcommando-1-jar-with-dependencies.jar -d 3 -sendEmail -smtp smtp.mysrv.com -login myLogin -pwd myPwd

Result:
> send a file by Email <br />
> smtp:smtp.mysrv.com <br />
> login:myLogin <br />
> password:myPwd <br />


Command:
> java -jar jcommando-1-jar-with-dependencies.jar -sendFtp -isSftp -host myhost.com -login myLogin -pwd mypassword -d 3

Result:
> send a file by FTP <br />
> over the protocole SFTP <br />
> host:myhost.com <br />
> login:myLogin <br />
> password:mypassword <br />

With JCommando you can create a xml file who define your command line. See /cli-java-prototype/jcommando/cli.xml<br />
This file will generate class implementation "Cli.java" when you run the Ant command:
> ant genCLI

Then you need to extend this class to overwrite the setters and the doExecute() function to process actions. See the Cli.java file.

JCommander
---
Command:
> java -jar jcommender-1-jar-with-dependencies.jar -file /path/to/file.txt -srv ftp.mySrv.com -debug

Result:
> sending file: /path/to/file.txt <br />
> to srv: ftp.mySrv.com <br />
> debug enable: true <br />

jopt-simple
---
Command:
> java -jar joptsimple-1-jar-with-dependencies.jar -ftp -user myUser -pass myPassword

Result:
> user:myUser pass:myPassword

Command:
> java -jar joptsimple-1-jar-with-dependencies.jar -f

Result:
> -f exist <br />
> -f with : null <br />

Command:
> java -jar joptsimple-1-jar-with-dependencies.jar -c foo

Result:
> -c exist with:foo

naturalcli
---
Command:
> java -jar naturalcli-1-jar-with-dependencies.jar send file /home/doduck/myFile.txt to ftp://ftpsrv/dir notify theBoss with "Please, see myFile.txt on the ftp"

Result:
> the file '/home/doduck/myFile.txt' was sent sucessfully <br />
> to 'ftp://ftpsrv/dir' and the user 'theBoss' <br />
> get notify with the message 'Please, see myFile.txt on the ftp' <br />


clajr
---
Command:
> java -jar clajr-1-jar-with-dependencies.jar -p ok -i

Result:
> let's print =>ok <br />
> Here is some information

Contact
=========
Developed by Martin Magakian<br />
dev.martin.magakian@gmail.com<br />
for [doduck java prototype](http://doduck.com/)

License
=========
MIT License (MIT)

![githalytics.com alpha](https://cruel-carlota.gopagoda.com/eea43c3685570137e157263e4e115619 "githalytics.com")]

