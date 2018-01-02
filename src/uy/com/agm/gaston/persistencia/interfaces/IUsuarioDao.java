package uy.com.agm.gaston.persistencia.interfaces;

import java.util.List;

import uy.com.agm.gaston.modelo.GrupoJaas;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;

public interface IUsuarioDao extends IGenericDao<Usuario, Integer> {
	public Usuario encontrar(String email) throws DaoException;

	public List<GrupoJaas> obtenerGruposJaas(String email) throws DaoException;
}
