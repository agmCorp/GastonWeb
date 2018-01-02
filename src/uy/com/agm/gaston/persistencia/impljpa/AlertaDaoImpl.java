package uy.com.agm.gaston.persistencia.impljpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import uy.com.agm.gaston.modelo.Alerta;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IAlertaDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AlertaDaoImpl extends GenericDaoImpl<Alerta, Integer> implements IAlertaDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public AlertaDaoImpl() {
		super(Alerta.class);
	}

	@Override
	public List<Alerta> encontrarTodos(String emailUsuario) throws DaoException {
		List<Alerta> result = null;
		try {
			TypedQuery<Alerta> query = em.createNamedQuery("Alerta.findByEmail", Alerta.class);
			result = query.setParameter("email", emailUsuario).getResultList();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}
}
