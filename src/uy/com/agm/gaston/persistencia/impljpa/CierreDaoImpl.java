package uy.com.agm.gaston.persistencia.impljpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import uy.com.agm.gaston.modelo.Cierre;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.ICierreDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class CierreDaoImpl extends GenericDaoImpl<Cierre, Integer> implements ICierreDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public CierreDaoImpl() {
		super(Cierre.class);
	}

	@Override
	public List<Cierre> encontrarTodos(Integer idNucleoFamiliar) throws DaoException {
		List<Cierre> result = null;
		try {
			TypedQuery<Cierre> query = em.createNamedQuery("Cierre.findByNucleoFamiliar", Cierre.class);
			result = query.setParameter("idNucleoFamiliar", idNucleoFamiliar).getResultList();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}
}