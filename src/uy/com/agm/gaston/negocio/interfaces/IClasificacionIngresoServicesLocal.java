package uy.com.agm.gaston.negocio.interfaces;

import java.util.List;

import javax.ejb.Local;

import uy.com.agm.gaston.modelo.ClasificacionIngreso;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Local
public interface IClasificacionIngresoServicesLocal {
	public ClasificacionIngreso encontrar(Integer idClasificacionIngreso) throws NegocioException;

	public List<ClasificacionIngreso> encontrarTodos() throws NegocioException;

	public ClasificacionIngreso nuevaClasificacionIngreso(ClasificacionIngreso clasificacionIngreso)
			throws NegocioException;

	public void eliminar(ClasificacionIngreso clasificacionIngreso) throws NegocioException;

	public void guardar(ClasificacionIngreso clasificacionIngreso) throws NegocioException;
}
