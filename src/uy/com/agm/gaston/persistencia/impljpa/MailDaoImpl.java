package uy.com.agm.gaston.persistencia.impljpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import uy.com.agm.gaston.modelo.Mail;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IMailDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class MailDaoImpl extends GenericDaoImpl<Mail, Integer> implements IMailDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public MailDaoImpl() {
		super(Mail.class);
	}

	@Override
	public List<Mail> obtenerMails(String emailReceptor) throws DaoException {
		List<Mail> result = null;
		try {
			TypedQuery<Mail> query = em.createNamedQuery("Mail.findByReceptor", Mail.class);
			result = query.setParameter("para", emailReceptor).getResultList();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}
}