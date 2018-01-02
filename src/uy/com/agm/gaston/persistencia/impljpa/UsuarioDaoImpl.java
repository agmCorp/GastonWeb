package uy.com.agm.gaston.persistencia.impljpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import uy.com.agm.gaston.modelo.GrupoJaas;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IUsuarioDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class UsuarioDaoImpl extends GenericDaoImpl<Usuario, Integer> implements IUsuarioDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public UsuarioDaoImpl() {
		super(Usuario.class);
	}

	@Override
	public Usuario encontrar(String email) throws DaoException {
		Usuario result = null;
		try {
			TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByEmail", Usuario.class);
			List<Usuario> usuarios = query.setParameter("email", email).getResultList();
			if (usuarios.size() > 0) {
				result = usuarios.get(0);
			}
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}

	@Override
	public List<GrupoJaas> obtenerGruposJaas(String email) throws DaoException {
		List<GrupoJaas> result = null;
		try {
			Usuario usuario = encontrar(email);
			result = usuario.getGruposJaas();
			// Obliga a cargar lista (lazy)
			result.size();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}
}