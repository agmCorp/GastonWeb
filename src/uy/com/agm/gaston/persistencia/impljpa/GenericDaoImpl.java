package uy.com.agm.gaston.persistencia.impljpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IGenericDao;

public abstract class GenericDaoImpl<T, K> implements IGenericDao<T, K> {

	private Class<T> entityClass;
	protected static final String ERROR_MSG = "Error en persistencia";

	public GenericDaoImpl(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected abstract EntityManager getEntityManager();

	@Override
	public T crear(T entity) throws DaoException {
		try {
			getEntityManager().persist(entity);
			// Obliga a sincronizar el contexto de persistencia con la base de
			// datos. De esta manera se pueden atrapar excepciones en caso de
			// error (flush no hace commit).
			getEntityManager().flush();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return entity;
	}

	@Override
	public T guardar(T entity) throws DaoException {
		try {
			getEntityManager().merge(entity);
			// Obliga a sincronizar el contexto de persistencia con la base de
			// datos. De esta manera se pueden atrapar excepciones en caso de
			// error (flush no hace commit).
			getEntityManager().flush();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return entity;
	}

	@Override
	public void eliminar(T entity) throws DaoException {
		try {
			getEntityManager().remove(getEntityManager().merge(entity));
			// Obliga a sincronizar el contexto de persistencia con la base de
			// datos. De esta manera se pueden atrapar excepciones en caso de
			// error (flush no hace commit).
			getEntityManager().flush();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
	}

	@Override
	public T encontrar(K id) throws DaoException {
		T result = null;
		try {
			result = getEntityManager().find(entityClass, id);
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}

	@Override
	public List<T> encontrarTodos() throws DaoException {
		List<T> result = null;
		try {
			CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
			cq.select(cq.from(entityClass));
			result = getEntityManager().createQuery(cq).getResultList();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}

	@Override
	public int contar() throws DaoException {
		int result = 0;
		try {
			CriteriaQuery<Long> cq = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
			Root<T> rt = cq.from(entityClass);
			cq.select(getEntityManager().getCriteriaBuilder().count(rt));
			Query q = getEntityManager().createQuery(cq);
			result = ((Long) q.getSingleResult()).intValue();
		} catch (Exception e) {
			throw new DaoException(ERROR_MSG, e);
		}
		return result;
	}
}