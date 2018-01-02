package uy.com.agm.gaston.negocio.interfaces;

import java.util.List;

import javax.ejb.Local;

import uy.com.agm.gaston.modelo.Moneda;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Local
public interface INucleoFamiliarServicesLocal {
	public NucleoFamiliar nuevoNucleoFamiliar(String nombreNucleoFamiliar, Moneda moneda, String emailAdministrador)
			throws NegocioException;

	public void eliminar(NucleoFamiliar nucleoFamiliar) throws NegocioException;

	public List<NucleoFamiliar> obtenerNucleosFamiliares() throws NegocioException;

	public List<NucleoFamiliar> obtenerNucleosFamiliaresDeIntegrante(String emailIntegrante) throws NegocioException;

	public List<Usuario> obtenerIntegrantes(Integer idNucleoFamiliar) throws NegocioException;

	public List<NucleoFamiliar> obtenerNucleosFamiliaresNoAdministrados(String emailAdministrador)
			throws NegocioException;

	public List<NucleoFamiliar> obtenerNucleosFamiliaresAdministrados(String emailAdministrador)
			throws NegocioException;

	public void agregarIntegrante(Integer idNucleoFamiliar, Usuario usuario) throws NegocioException;

	public void eliminarIntegrante(Integer idNucleoFamiliar, Usuario integrante) throws NegocioException;

	public NucleoFamiliar encontrar(Integer idNucleoFamiliar) throws NegocioException;
}
