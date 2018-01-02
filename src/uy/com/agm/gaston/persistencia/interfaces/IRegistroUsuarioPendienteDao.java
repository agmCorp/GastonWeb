package uy.com.agm.gaston.persistencia.interfaces;

import uy.com.agm.gaston.modelo.RegistroUsuarioPendiente;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;

public interface IRegistroUsuarioPendienteDao extends IGenericDao<RegistroUsuarioPendiente, Integer> {
	public RegistroUsuarioPendiente encontrar(String idActivacion) throws DaoException;

	public RegistroUsuarioPendiente encontrarRegistroDeUsuario(Integer idUsuario) throws DaoException;
}
