package uy.com.agm.gaston.negocio.interfaces;

import java.util.List;

import javax.ejb.Local;

import uy.com.agm.gaston.modelo.PreguntaSeguridad;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Local
public interface IPreguntaSeguridadServicesLocal {
	public List<PreguntaSeguridad> encontrarTodos() throws NegocioException;

	public PreguntaSeguridad encontrar(Integer idPreguntaSeguridad) throws NegocioException;

	public PreguntaSeguridad nuevaPreguntaSeguridad(PreguntaSeguridad preguntaSeguridad) throws NegocioException;

	public void eliminar(PreguntaSeguridad preguntaSeguridad) throws NegocioException;

	public void guardar(PreguntaSeguridad preguntaSeguridad) throws NegocioException;
}
