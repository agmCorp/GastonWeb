package uy.com.agm.gaston.persistencia.impljpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import uy.com.agm.gaston.modelo.Solicitud;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.ISolicitudDao;
import uy.com.agm.gaston.soporte.enumerados.Estado;
import uy.com.agm.gaston.soporte.enumerados.TipoDeSolicitud;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class SolicitudDaoImpl extends GenericDaoImpl<Solicitud, Integer> implements ISolicitudDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public SolicitudDaoImpl() {
		super(Solicitud.class);
	}

	@Override
	public Solicitud obtenerSolicitud(Estado estado, TipoDeSolicitud tipoDeSolicitud, Integer idNucleoFamiliar,
			String emailSolicitante) throws DaoException {
		Solicitud result = null;
		try {
			TypedQuery<Solicitud> query = em.createNamedQuery("Solicitud.find", Solicitud.class);
			query.setParameter("estado", estado);
			query.setParameter("tipo", tipoDeSolicitud);
			query.setParameter("email", emailSolicitante);
			query.setParameter("idNucleoFamiliar", idNucleoFamiliar);
			List<Solicitud> solicitudes = query.getResultList();
			if (solicitudes.size() >= 1) {
				result = solicitudes.get(0);
			}
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}

	@Override
	public List<Solicitud> encontrarTodos(Integer idNucleoFamiliar) throws DaoException {
		List<Solicitud> result = null;
		;
		try {
			TypedQuery<Solicitud> query = em.createNamedQuery("Solicitud.findByNucleoFamiliar", Solicitud.class);
			query.setParameter("idNucleoFamiliar", idNucleoFamiliar);
			result = query.getResultList();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}
}