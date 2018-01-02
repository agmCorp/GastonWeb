package uy.com.agm.gaston.persistencia.impljpa;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import uy.com.agm.gaston.modelo.PreguntaSeguridad;
import uy.com.agm.gaston.persistencia.interfaces.IPreguntaSeguridadDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PreguntaSeguridadDaoImpl extends GenericDaoImpl<PreguntaSeguridad, Integer>
		implements IPreguntaSeguridadDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public PreguntaSeguridadDaoImpl() {
		super(PreguntaSeguridad.class);
	}
}