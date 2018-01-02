package uy.com.agm.gaston.persistencia.interfaces;

import uy.com.agm.gaston.modelo.GrupoJaas;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;

public interface IGrupoJaasDao extends IGenericDao<GrupoJaas, Integer> {
	public GrupoJaas encontrar(String nombre) throws DaoException;

	public void agregarIntegrante(Integer idGrupoJaas, Usuario usuario) throws DaoException;
}
