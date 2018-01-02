package uy.com.agm.gaston.persistencia.impljpa;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import uy.com.agm.gaston.modelo.ClasificacionIngreso;
import uy.com.agm.gaston.persistencia.interfaces.IClasificacionIngresoDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ClasificacionIngresoDaoImpl extends GenericDaoImpl<ClasificacionIngreso, Integer>
		implements IClasificacionIngresoDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public ClasificacionIngresoDaoImpl() {
		super(ClasificacionIngreso.class);
	}
}