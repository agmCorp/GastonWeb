package uy.com.agm.gaston.soporte.util;

public class GmailBuilder {
	String login;
	String personal;
	String password;
	String to;
	String cc;
	String bcc;
	String subject;
	String body;
	String smtpHost;
	String socketFactoryClass;
	String socketFactoryPort;
	String smtpAuth;
	String smtpPort;

	public static GmailBuilder create() {
		return new GmailBuilder();
	}

	public GmailBuilder withLogin(String login) {
		this.login = login;
		return this;
	}

	public GmailBuilder withPersonal(String personal) {
		this.personal = personal;
		return this;
	}

	public GmailBuilder withPassword(String password) {
		this.password = password;
		return this;
	}

	public GmailBuilder withTo(String to) {
		this.to = to;
		return this;
	}

	public GmailBuilder withCc(String cc) {
		this.cc = cc;
		return this;
	}

	public GmailBuilder withBcc(String bcc) {
		this.bcc = bcc;
		return this;
	}

	public GmailBuilder withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public GmailBuilder withBody(String body) {
		this.body = body;
		return this;
	}

	public GmailBuilder withSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
		return this;
	}

	public GmailBuilder withSocketFactoryClass(String socketFactoryClass) {
		this.socketFactoryClass = socketFactoryClass;
		return this;
	}

	public GmailBuilder withSocketFactoryPort(String socketFactoryPort) {
		this.socketFactoryPort = socketFactoryPort;
		return this;
	}

	public GmailBuilder withSmtpAuth(String smtpAuth) {
		this.smtpAuth = smtpAuth;
		return this;
	}

	public GmailBuilder withSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
		return this;
	}

	public Gmail build() {
		return new Gmail(this);
	}
}
