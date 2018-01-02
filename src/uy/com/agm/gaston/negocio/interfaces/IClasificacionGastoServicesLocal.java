package uy.com.agm.gaston.negocio.interfaces;

import java.util.List;

import javax.ejb.Local;

import uy.com.agm.gaston.modelo.ClasificacionGasto;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Local
public interface IClasificacionGastoServicesLocal {
	public ClasificacionGasto encontrar(Integer idClasificacionGasto) throws NegocioException;

	public List<ClasificacionGasto> encontrarTodos() throws NegocioException;

	public ClasificacionGasto nuevaClasificacionGasto(ClasificacionGasto clasificacionGasto) throws NegocioException;

	public void eliminar(ClasificacionGasto clasificacionGasto) throws NegocioException;

	public void guardar(ClasificacionGasto clasificacionGasto) throws NegocioException;
}
