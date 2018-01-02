package uy.com.agm.gaston.persistencia.interfaces;

import java.util.List;

import uy.com.agm.gaston.modelo.Alerta;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;

public interface IAlertaDao extends IGenericDao<Alerta, Integer> {
	public List<Alerta> encontrarTodos(String emailUsuario) throws DaoException;
}
