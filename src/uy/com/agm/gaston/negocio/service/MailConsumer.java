package uy.com.agm.gaston.negocio.service;

import java.io.UnsupportedEncodingException;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.mail.MessagingException;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.Mail;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IMailDao;
import uy.com.agm.gaston.persistencia.interfaces.IPropiedadDao;
import uy.com.agm.gaston.soporte.util.DateHelper;
import uy.com.agm.gaston.soporte.util.GmailBuilder;
import uy.com.agm.gaston.soporte.util.MailSimple;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:jboss/exported/jms/queue/mail"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class MailConsumer implements MessageListener {
	@EJB
	private IMailDao mailDao;
	@EJB
	private IPropiedadDao propiedadDao;
	@Inject
	private Logger logger;

	private static final String ERROR_MSG = "Error en negocio " + MailServices.class.getName();
	private static final String KEY_LOGIN_ACCOUNT = "loginAccount";
	private static final String KEY_PERSONAL = "personal";
	private static final String KEY_LOGIN_ACCOUNT_PASSWORD = "loginAccountPassword";
	private static final String KEY_SMTP_HOST = "smtpHost";
	private static final String KEY_SOCKET_FACTORY_CLASS = "socketFactoryClass";
	private static final String KEY_SOCKET_FACTORY_PORT = "socketFactoryPort";
	private static final String KEY_SMTP_AUTH = "smtpAuth";
	private static final String KEY_SMTP_PORT = "smtpPort";

	@Override
	public void onMessage(Message message) {
		try {
			// Consume el objeto de la cola "queue/mail" y lo envía por mail.
			ObjectMessage objectMessage = (ObjectMessage) message;
			MailSimple mailSimple = (MailSimple) objectMessage.getObject();
			enviarMail(mailSimple);
		} catch (Exception e) {
			logger.error("Error en método onMessage", e);
		}
	}

	private void enviarMail(MailSimple mailSimple) throws NegocioException {
		Boolean result;
		String login, personal, password, smtpHost, socketFactoryClass, socketFactoryPort, smtpAuth, smtpPort, para,
				asunto, mensaje;

		try {
			login = propiedadDao.encontrar(KEY_LOGIN_ACCOUNT).getValor();
			personal = propiedadDao.encontrar(KEY_PERSONAL).getValor();
			password = propiedadDao.encontrar(KEY_LOGIN_ACCOUNT_PASSWORD).getValor();
			smtpHost = propiedadDao.encontrar(KEY_SMTP_HOST).getValor();
			socketFactoryClass = propiedadDao.encontrar(KEY_SOCKET_FACTORY_CLASS).getValor();
			socketFactoryPort = propiedadDao.encontrar(KEY_SOCKET_FACTORY_PORT).getValor();
			smtpAuth = propiedadDao.encontrar(KEY_SMTP_AUTH).getValor();
			smtpPort = propiedadDao.encontrar(KEY_SMTP_PORT).getValor();
			para = mailSimple.getPara();
			asunto = mailSimple.getAsunto();
			mensaje = mailSimple.getMensaje();
			try {
				result = GmailBuilder.create().withLogin(login).withPersonal(personal).withPassword(password)
						.withSmtpHost(smtpHost).withSocketFactoryClass(socketFactoryClass)
						.withSocketFactoryPort(socketFactoryPort).withSmtpAuth(smtpAuth).withSmtpPort(smtpPort)
						.withTo(para).withSubject(asunto).withBody(mensaje).build().send();
			} catch (MessagingException | UnsupportedEncodingException me) {
				logger.error("Error en método enviarMail", me);
				result = false;
			}
			// El mail se almacena siempre
			Mail mail = new Mail();
			mail.setAsunto(asunto);
			mail.setDe(login);
			mail.setPara(para);
			mail.setMensaje(mensaje);
			mail.setTimestamp(DateHelper.getHoy());
			mail.setEstado(result ? "Envío exitoso" : "Error durante el envío");
			mailDao.crear(mail);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}
}
