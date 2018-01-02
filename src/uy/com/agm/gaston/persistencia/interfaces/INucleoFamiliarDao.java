package uy.com.agm.gaston.persistencia.interfaces;

import java.util.List;

import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;

public interface INucleoFamiliarDao extends IGenericDao<NucleoFamiliar, Integer> {
	public List<NucleoFamiliar> obtenerNucleosFamiliaresDeIntegrante(String email) throws DaoException;

	public List<Usuario> obtenerIntegrantes(Integer idNucleoFamiliar) throws DaoException;

	public void agregarIntegrante(Integer idNucleoFamiliar, Usuario usuario) throws DaoException;

	public void eliminarIntegrante(Integer idNucleoFamiliar, Usuario integrante) throws DaoException;

	public List<NucleoFamiliar> obtenerNucleosFamiliaresNoAdministrados(String emailAdministrador) throws DaoException;

	public List<NucleoFamiliar> obtenerNucleosFamiliaresAdministrados(String emailAdministrador) throws DaoException;
}
