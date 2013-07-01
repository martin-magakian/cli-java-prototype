/*
 * THIS IS A GENERATED FILE.  DO NOT EDIT.
 *
 * JCommando (http://jcommando.sourceforge.net)
 */

package com.doduck.prototype.jcommando;

import org.jcommando.Command;
import org.jcommando.JCommandParser;
import org.jcommando.Option;
import org.jcommando.Grouping;
import org.jcommando.And;
import org.jcommando.Or;
import org.jcommando.Xor;
import org.jcommando.Not;

/**
 * JCommando generated parser class.
 */
public abstract class Cli extends JCommandParser
{
   /**
     * JCommando generated constructor.
     */
   public Cli()
   {
      Option sendFtp = new Option();
      sendFtp.setId("sendFtp");
      sendFtp.setShortMnemonic("sendFtp");
      sendFtp.setLongMnemonic("sendFtp");
      sendFtp.setDescription("send file by ftp");
      addOption(sendFtp);

      Option isFtp = new Option();
      isFtp.setId("isFtp");
      isFtp.setShortMnemonic("isFtp");
      isFtp.setLongMnemonic("isFtp");
      isFtp.setDescription("isFtp");
      addOption(isFtp);

      Option isSftp = new Option();
      isSftp.setId("isSftp");
      isSftp.setShortMnemonic("isSftp");
      isSftp.setLongMnemonic("isSftp");
      isSftp.setDescription("isSftp");
      addOption(isSftp);

      Option sendEmail = new Option();
      sendEmail.setId("sendEmail");
      sendEmail.setShortMnemonic("sendEmail");
      sendEmail.setLongMnemonic("sendEmail");
      sendEmail.setDescription("send file by email");
      addOption(sendEmail);

      Option debugLever = new Option();
      debugLever.setId("debugLever");
      debugLever.setShortMnemonic("d");
      debugLever.setLongMnemonic("debugLever");
      debugLever.setDescription("debug level");
      debugLever.setOptionType("long");
      debugLever.setMin(-9.223372036854776E18d);
      debugLever.setMax(9.223372036854776E18d);
      addOption(debugLever);

      Option smtp = new Option();
      smtp.setId("smtp");
      smtp.setShortMnemonic("smtp");
      smtp.setLongMnemonic("smtp");
      smtp.setDescription("smtp server url");
      smtp.setOptionType("String");
      addOption(smtp);

      Option login = new Option();
      login.setId("login");
      login.setShortMnemonic("login");
      login.setLongMnemonic("login");
      login.setDescription("login to connect");
      login.setOptionType("String");
      addOption(login);

      Option pwd = new Option();
      pwd.setId("pwd");
      pwd.setShortMnemonic("pwd");
      pwd.setLongMnemonic("pwd");
      pwd.setDescription("password to connect");
      pwd.setOptionType("String");
      addOption(pwd);

      Option ftp = new Option();
      ftp.setId("ftp");
      ftp.setShortMnemonic("ftp");
      ftp.setLongMnemonic("ftp");
      ftp.setDescription("is ftp connection");
      addOption(ftp);

      Option host = new Option();
      host.setId("host");
      host.setShortMnemonic("host");
      host.setLongMnemonic("host");
      host.setDescription("host server url");
      host.setOptionType("String");
      addOption(host);

      Command execute = new Command();
      execute.setName("commandless");
      execute.setId("execute");
      execute.addOption(sendFtp);
      execute.addOption(debugLever);
      execute.addOption(host);
      execute.addOption(isSftp);
      execute.addOption(pwd);
      execute.addOption(login);
      execute.addOption(smtp);
      execute.addOption(isFtp);
      execute.addOption(sendEmail);
      execute.setGrouping( createExecuteGrouping() );
      addCommand(execute);

   }

   /**
     * Called by parser to set the 'sendFtp' property.
     *
     */
   public abstract void setSendFtp();

   /**
     * Called by parser to set the 'isFtp' property.
     *
     */
   public abstract void setIsFtp();

   /**
     * Called by parser to set the 'isSftp' property.
     *
     */
   public abstract void setIsSftp();

   /**
     * Called by parser to set the 'sendEmail' property.
     *
     */
   public abstract void setSendEmail();

   /**
     * Called by parser to set the 'debugLever' property.
     *
     * @param debugLever the value to set.
     */
   public abstract void setDebugLever(long debugLever);

   /**
     * Called by parser to set the 'smtp' property.
     *
     * @param smtp the value to set.
     */
   public abstract void setSmtp(String smtp);

   /**
     * Called by parser to set the 'login' property.
     *
     * @param login the value to set.
     */
   public abstract void setLogin(String login);

   /**
     * Called by parser to set the 'pwd' property.
     *
     * @param pwd the value to set.
     */
   public abstract void setPwd(String pwd);

   /**
     * Called by parser to set the 'ftp' property.
     *
     */
   public abstract void setFtp();

   /**
     * Called by parser to set the 'host' property.
     *
     * @param host the value to set.
     */
   public abstract void setHost(String host);

   /**
     * Called by parser to perform the 'execute' command.
     *
     */
   public abstract void doExecute();

   /**
    * Generate the grouping for the 'execute' command.
    */
   private Grouping createExecuteGrouping()
   {
      Xor xor1 = new Xor();
      xor1.addOption(getOptionById("isSftp"));
      xor1.addOption(getOptionById("isFtp"));
      And and1 = new And();
      and1.getGroupings().add(xor1);
      and1.addOption(getOptionById("sendFtp"));
      and1.addOption(getOptionById("pwd"));
      and1.addOption(getOptionById("host"));
      and1.addOption(getOptionById("login"));
      And and2 = new And();
      and2.addOption(getOptionById("pwd"));
      and2.addOption(getOptionById("login"));
      and2.addOption(getOptionById("smtp"));
      and2.addOption(getOptionById("sendEmail"));
      Xor xor2 = new Xor();
      xor2.getGroupings().add(and2);
      xor2.getGroupings().add(and1);
      And and3 = new And();
      and3.getGroupings().add(xor2);
      and3.addOption(getOptionById("debugLever"));
      return and3;
   }
}
