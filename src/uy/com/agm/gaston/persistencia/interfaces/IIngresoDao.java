package uy.com.agm.gaston.persistencia.interfaces;

import java.util.Date;
import java.util.List;

import uy.com.agm.gaston.modelo.Ingreso;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;

public interface IIngresoDao extends IGenericDao<Ingreso, Integer> {
	public List<Ingreso> encontrarTodos(Integer idNucleoFamiliar) throws DaoException;

	// Retorna todos los ingresos i tal que fechaIni < i.fecha <= fechaFin
	public List<Ingreso> encontrarTodos(Integer idNucleoFamiliar, Date fechaIni, Date fechaFin) throws DaoException;

}
