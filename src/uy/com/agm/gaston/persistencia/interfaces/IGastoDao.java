package uy.com.agm.gaston.persistencia.interfaces;

import java.util.Date;
import java.util.List;

import uy.com.agm.gaston.modelo.Gasto;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;

public interface IGastoDao extends IGenericDao<Gasto, Integer> {
	public List<Gasto> encontrarTodos(Integer idNucleoFamiliar) throws DaoException;

	// Retorna todos los gastos g tal que fechaIni < g.fecha <= fechaFin
	public List<Gasto> encontrarTodos(Integer idNucleoFamiliar, Date fechaIni, Date fechaFin) throws DaoException;
}
