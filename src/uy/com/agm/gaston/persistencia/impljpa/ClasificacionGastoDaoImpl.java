package uy.com.agm.gaston.persistencia.impljpa;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import uy.com.agm.gaston.modelo.ClasificacionGasto;
import uy.com.agm.gaston.persistencia.interfaces.IClasificacionGastoDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ClasificacionGastoDaoImpl extends GenericDaoImpl<ClasificacionGasto, Integer>
		implements IClasificacionGastoDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public ClasificacionGastoDaoImpl() {
		super(ClasificacionGasto.class);
	}
}