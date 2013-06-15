package com.doduck.prototype.cli.jcommando;

public class MyCLI extends Cli{

	private boolean isSendByFTP;
	private long debugLevel = 0;
	private boolean isSFtp;
	private String host;
	private String login;
	private String password;
	
	private boolean isSendEmail;
	private String smtp;

	@Override
	public void setSendFtp() {
		this.isSendByFTP = true;
	}

	@Override
	public void setDebugLever(long debugLever) {
		this.debugLevel = debugLever;
	}
	
	@Override
	public void setIsFtp() {
		this.isSFtp = false;
	}

	@Override
	public void setIsSftp() {
		this.isSFtp = true;
	}
	
	@Override
	public void setHost(String host) {
		this.host = host;
	}
	
	@Override
	public void setLogin(String login) {
		this.login = login;
	}
	
	@Override
	public void setPwd(String pwd) {
		this.password = pwd;
	}



	
	@Override
	public void doExecute() {
		if(isSendByFTP){
			String protocol = this.isSFtp ? "SFTP" : "FTP";
			
			System.out.println("send a file by FTP");
			System.out.println("over the protocole "+protocol);
			System.out.println("host:"+this.host);
			System.out.println("login:"+this.login);
			System.out.println("password:"+this.password);
			
		}else if(isSendEmail){
			System.out.println("send a file by Email");
			System.out.println("smtp:"+this.smtp);
			System.out.println("login:"+this.login);
			System.out.println("password:"+this.password);
		}
	}



	@Override
	public void setSendEmail() {
		this.isSendEmail = true;
	}



	@Override
	public void setFtp() {
		this.isSFtp = false;
	}





	@Override
	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}






}
