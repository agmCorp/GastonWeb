package uy.com.agm.gaston.persistencia.impljpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.INucleoFamiliarDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class NucleoFamiliarDaoImpl extends GenericDaoImpl<NucleoFamiliar, Integer> implements INucleoFamiliarDao {
	@Inject
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public NucleoFamiliarDaoImpl() {
		super(NucleoFamiliar.class);
	}

	@Override
	public List<NucleoFamiliar> obtenerNucleosFamiliaresDeIntegrante(String email) throws DaoException {
		List<NucleoFamiliar> result = null;
		try {
			TypedQuery<NucleoFamiliar> query = em.createNamedQuery("NucleoFamiliar.integranteByEmail",
					NucleoFamiliar.class);
			result = query.setParameter("email", email).getResultList();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}

	@Override
	public List<Usuario> obtenerIntegrantes(Integer idNucleoFamiliar) throws DaoException {
		List<Usuario> result = null;
		try {
			NucleoFamiliar nucleoFamiliar = encontrar(idNucleoFamiliar);
			result = nucleoFamiliar.getIntegrantes();
			// Obliga a cargar lista (lazy)
			result.size();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}

	@Override
	public void agregarIntegrante(Integer idNucleoFamiliar, Usuario usuario) throws DaoException {
		try {
			NucleoFamiliar nucleoFamiliar = encontrar(idNucleoFamiliar);
			nucleoFamiliar.getIntegrantes().add(usuario);
			guardar(nucleoFamiliar);
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
	}

	@Override
	public void eliminarIntegrante(Integer idNucleoFamiliar, Usuario integrante) throws DaoException {
		try {
			NucleoFamiliar nucleoFamiliar = encontrar(idNucleoFamiliar);

			int indice = -1;
			List<Usuario> integrantes = nucleoFamiliar.getIntegrantes();
			for (int i = 0; i < integrantes.size(); i++) {
				if (integrantes.get(i).getId().equals(integrante.getId())) {
					indice = i;
					break;
				}
			}
			integrantes.remove(indice);
			guardar(nucleoFamiliar);
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
	}

	@Override
	public List<NucleoFamiliar> obtenerNucleosFamiliaresNoAdministrados(String emailAdministrador) throws DaoException {
		List<NucleoFamiliar> result = null;
		try {
			TypedQuery<NucleoFamiliar> query = em.createNamedQuery("NucleoFamiliar.noAdministradosByEmail",
					NucleoFamiliar.class);
			result = query.setParameter("emailAdministrador", emailAdministrador).getResultList();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}

	@Override
	public List<NucleoFamiliar> obtenerNucleosFamiliaresAdministrados(String emailAdministrador) throws DaoException {
		List<NucleoFamiliar> result = null;
		try {
			TypedQuery<NucleoFamiliar> query = em.createNamedQuery("NucleoFamiliar.administradosByEmail",
					NucleoFamiliar.class);
			result = query.setParameter("emailAdministrador", emailAdministrador).getResultList();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}
}