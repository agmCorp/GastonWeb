package uy.com.agm.gaston.persistencia.impljpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import uy.com.agm.gaston.modelo.RegistroUsuarioPendiente;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IRegistroUsuarioPendienteDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class RegistroUsuarioPendienteDaoImpl extends GenericDaoImpl<RegistroUsuarioPendiente, Integer>
		implements IRegistroUsuarioPendienteDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public RegistroUsuarioPendienteDaoImpl() {
		super(RegistroUsuarioPendiente.class);
	}

	@Override
	public RegistroUsuarioPendiente encontrar(String idActivacion) throws DaoException {
		RegistroUsuarioPendiente result = null;
		try {
			TypedQuery<RegistroUsuarioPendiente> query = em
					.createNamedQuery("RegistroUsuarioPendiente.findByIdActivacion", RegistroUsuarioPendiente.class);
			List<RegistroUsuarioPendiente> registrosUsuarioPendiente = query.setParameter("idActivacion", idActivacion)
					.getResultList();
			if (registrosUsuarioPendiente.size() > 0) {
				result = registrosUsuarioPendiente.get(0);
			}
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}

	@Override
	public RegistroUsuarioPendiente encontrarRegistroDeUsuario(Integer idUsuario) throws DaoException {
		RegistroUsuarioPendiente result = null;
		try {
			TypedQuery<RegistroUsuarioPendiente> query = em.createNamedQuery("RegistroUsuarioPendiente.findByIdUsuario",
					RegistroUsuarioPendiente.class);
			List<RegistroUsuarioPendiente> registrosUsuarioPendiente = query.setParameter("idUsuario", idUsuario)
					.getResultList();
			if (registrosUsuarioPendiente.size() > 0) {
				result = registrosUsuarioPendiente.get(0);
			}
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}
}