package uy.com.agm.gaston.persistencia.impljpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import uy.com.agm.gaston.modelo.Propiedad;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IPropiedadDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PropiedadDaoImpl extends GenericDaoImpl<Propiedad, Integer> implements IPropiedadDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public PropiedadDaoImpl() {
		super(Propiedad.class);
	}

	@Override
	public Propiedad encontrar(String clave) throws DaoException {
		Propiedad result = null;
		try {
			TypedQuery<Propiedad> query = em.createNamedQuery("Propiedad.findByClave", Propiedad.class);
			List<Propiedad> propiedades = query.setParameter("clave", clave).getResultList();
			if (propiedades.size() > 0) {
				result = propiedades.get(0);
			}
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}
}