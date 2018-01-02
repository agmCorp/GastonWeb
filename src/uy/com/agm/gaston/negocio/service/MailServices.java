package uy.com.agm.gaston.negocio.service;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

import uy.com.agm.gaston.modelo.Mail;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IMailServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IMailServicesRemote;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IMailDao;
import uy.com.agm.gaston.soporte.util.MailSimple;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MailServices implements IMailServicesRemote, IMailServicesLocal {
	@Inject
	@JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
	private JMSContext context;

	@Resource(mappedName = "java:jboss/exported/jms/queue/mail")
	private Queue queue;

	@EJB
	private IMailDao mailDao;

	private static final String ERROR_MSG = "Error en negocio " + MailServices.class.getName();

	@Override
	public void enviarMail(MailSimple mailSimple) throws NegocioException {
		try {
			// Agrega el mail a la cola "queue/mail" para su envío asincrónico.
			context.createProducer().send(queue, mailSimple);
		} catch (Exception e) {
			throw new NegocioException(ERROR_MSG, e);
		}
	}

	@Override
	public List<Mail> obtenerMails(String emailReceptor) throws NegocioException {
		List<Mail> result = null;
		try {
			result = mailDao.obtenerMails(emailReceptor);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public void eliminar(Mail mail) throws NegocioException {
		try {
			mailDao.eliminar(mail);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}
}
