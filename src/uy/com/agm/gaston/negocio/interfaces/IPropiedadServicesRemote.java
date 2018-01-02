package uy.com.agm.gaston.negocio.interfaces;

import java.util.List;

import javax.ejb.Remote;

import uy.com.agm.gaston.modelo.Propiedad;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Remote
public interface IPropiedadServicesRemote {
	public Propiedad encontrar(Integer idPropiedad) throws NegocioException;

	public Propiedad encontrar(String clave) throws NegocioException;

	public List<Propiedad> encontrarTodos() throws NegocioException;

	public Propiedad nuevaPropiedad(Propiedad propiedad) throws NegocioException;

	public void eliminar(Propiedad propiedad) throws NegocioException;

	public void guardar(Propiedad propiedad) throws NegocioException;
}
