package uy.com.agm.gaston.persistencia.interfaces;

import uy.com.agm.gaston.modelo.Propiedad;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;

public interface IPropiedadDao extends IGenericDao<Propiedad, Integer> {
	public Propiedad encontrar(String clave) throws DaoException;
}
