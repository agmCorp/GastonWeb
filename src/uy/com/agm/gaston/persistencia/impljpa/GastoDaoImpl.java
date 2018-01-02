package uy.com.agm.gaston.persistencia.impljpa;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import uy.com.agm.gaston.modelo.Gasto;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IGastoDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class GastoDaoImpl extends GenericDaoImpl<Gasto, Integer> implements IGastoDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public GastoDaoImpl() {
		super(Gasto.class);
	}

	@Override
	public List<Gasto> encontrarTodos(Integer idNucleoFamiliar) throws DaoException {
		List<Gasto> result = null;
		try {
			TypedQuery<Gasto> query = em.createNamedQuery("Gasto.findByNucleoFamiliar", Gasto.class);
			result = query.setParameter("idNucleoFamiliar", idNucleoFamiliar).getResultList();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}

	// Retorna todos los gastos g tal que fechaIni < g.fecha <= fechaFin
	@Override
	public List<Gasto> encontrarTodos(Integer idNucleoFamiliar, Date fechaIni, Date fechaFin) throws DaoException {
		List<Gasto> result = null;
		try {
			TypedQuery<Gasto> query = em.createNamedQuery("Gasto.findByFecha", Gasto.class);
			query.setParameter("idNucleoFamiliar", idNucleoFamiliar);
			query.setParameter("fechaIni", fechaIni);
			query.setParameter("fechaFin", fechaFin);
			result = query.getResultList();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}
}