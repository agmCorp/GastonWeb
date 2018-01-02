package uy.com.agm.gaston.persistencia.impljpa;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import uy.com.agm.gaston.modelo.GrupoJaas;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IGrupoJaasDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class GrupoJaasDaoImpl extends GenericDaoImpl<GrupoJaas, Integer> implements IGrupoJaasDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public GrupoJaasDaoImpl() {
		super(GrupoJaas.class);
	}

	@Override
	public GrupoJaas encontrar(String nombre) throws DaoException {
		GrupoJaas grupoJaas = null;
		try {
			TypedQuery<GrupoJaas> query = em.createNamedQuery("GrupoJaas.findByName", GrupoJaas.class);
			grupoJaas = query.setParameter("nombre", nombre).getSingleResult();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return grupoJaas;
	}

	@Override
	public void agregarIntegrante(Integer idGrupoJaas, Usuario usuario) throws DaoException {
		try {
			GrupoJaas grupoJaas = encontrar(idGrupoJaas);
			grupoJaas.getIntegrantes().add(usuario);
			guardar(grupoJaas);
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
	}
}