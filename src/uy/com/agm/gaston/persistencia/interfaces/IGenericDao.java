package uy.com.agm.gaston.persistencia.interfaces;

import java.util.List;

import uy.com.agm.gaston.persistencia.excepciones.DaoException;

public interface IGenericDao<T, K> {
	public T crear(T entity) throws DaoException;

	public T guardar(T entity) throws DaoException;

	public void eliminar(T entity) throws DaoException;

	public T encontrar(K id) throws DaoException;

	public List<T> encontrarTodos() throws DaoException;

	public int contar() throws DaoException;
}
