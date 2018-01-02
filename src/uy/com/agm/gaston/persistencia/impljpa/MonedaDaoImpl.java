package uy.com.agm.gaston.persistencia.impljpa;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import uy.com.agm.gaston.modelo.Moneda;
import uy.com.agm.gaston.persistencia.interfaces.IMonedaDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class MonedaDaoImpl extends GenericDaoImpl<Moneda, Integer> implements IMonedaDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public MonedaDaoImpl() {
		super(Moneda.class);
	}
}