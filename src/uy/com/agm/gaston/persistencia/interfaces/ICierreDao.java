package uy.com.agm.gaston.persistencia.interfaces;

import java.util.List;

import uy.com.agm.gaston.modelo.Cierre;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;

public interface ICierreDao extends IGenericDao<Cierre, Integer> {
	public List<Cierre> encontrarTodos(Integer idNucleoFamiliar) throws DaoException;
}
