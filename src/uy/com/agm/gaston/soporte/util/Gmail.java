package uy.com.agm.gaston.soporte.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Gmail {
	private Properties mailServerProperties;
	private Session getMailSession;
	private MimeMessage generateMailMessage;

	private String login;
	private String personal;
	private String password;
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String body;
	private String smtpHost;
	private String socketFactoryClass;
	private String socketFactoryPort;
	private String smtpAuth;
	private String smtpPort;

	public Gmail(GmailBuilder builder) {
		this.login = builder.login;
		this.personal = builder.personal;
		this.password = builder.password;
		this.to = builder.to;
		this.cc = builder.cc;
		this.bcc = builder.bcc;
		this.subject = builder.subject;
		this.body = builder.body;
		this.smtpHost = builder.smtpHost;
		this.socketFactoryClass = builder.socketFactoryClass;
		this.socketFactoryPort = builder.socketFactoryPort;
		this.smtpAuth = builder.smtpAuth;
		this.smtpPort = builder.smtpPort;
	}

	public Boolean send() throws AddressException, MessagingException, UnsupportedEncodingException {
		Boolean success = false;

		// Propiedades de la conexión
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.host", smtpHost);
		mailServerProperties.put("mail.smtp.socketFactory.port", socketFactoryPort);
		mailServerProperties.put("mail.smtp.socketFactory.class", socketFactoryClass);
		mailServerProperties.put("mail.smtp.auth", smtpAuth);
		mailServerProperties.put("mail.smtp.port", smtpPort);

		// Autenticación
		if (login == null || password == null) {
			return success;
		}

		getMailSession = Session.getDefaultInstance(mailServerProperties, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(login, password);
			}
		});

		// Enviar email
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.setFrom(new InternetAddress(login, personal));
		if (to != null) {
			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		}
		if (cc != null) {
			generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
		}
		if (bcc != null) {
			generateMailMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
		}
		generateMailMessage.setSubject(subject);
		generateMailMessage.setContent(body, "text/html");
		javax.mail.Transport.send(generateMailMessage);
		return true;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public String getTo() {
		return to;
	}

	public String getCc() {
		return cc;
	}

	public String getBcc() {
		return bcc;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public String getSocketFactoryClass() {
		return socketFactoryClass;
	}

	public String getSocketFactoryPort() {
		return socketFactoryPort;
	}

	public String getSmtpAuth() {
		return smtpAuth;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

}