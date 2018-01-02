package uy.com.agm.gaston.persistencia.impljpa;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import uy.com.agm.gaston.modelo.Ingreso;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IIngresoDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class IngresoDaoImpl extends GenericDaoImpl<Ingreso, Integer> implements IIngresoDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public IngresoDaoImpl() {
		super(Ingreso.class);
	}

	@Override
	public List<Ingreso> encontrarTodos(Integer idNucleoFamiliar) throws DaoException {
		List<Ingreso> result = null;
		try {
			TypedQuery<Ingreso> query = em.createNamedQuery("Ingreso.findByNucleoFamiliar", Ingreso.class);
			result = query.setParameter("idNucleoFamiliar", idNucleoFamiliar).getResultList();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}

	// Retorna todos los ingresos i tal que fechaIni < i.fecha <= fechaFin
	@Override
	public List<Ingreso> encontrarTodos(Integer idNucleoFamiliar, Date fechaIni, Date fechaFin) throws DaoException {
		List<Ingreso> result = null;
		try {
			TypedQuery<Ingreso> query = em.createNamedQuery("Ingreso.findByFecha", Ingreso.class);
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