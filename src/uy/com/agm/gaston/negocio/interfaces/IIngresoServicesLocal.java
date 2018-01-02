package uy.com.agm.gaston.negocio.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import uy.com.agm.gaston.modelo.Ingreso;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Local
public interface IIngresoServicesLocal {
	public List<Ingreso> obtenerIngresos(NucleoFamiliar nucleoFamiliar) throws NegocioException;

	public Ingreso nuevoIngreso(Ingreso ingreso) throws NegocioException;

	public void eliminar(Ingreso ingreso) throws NegocioException;

	public void guardar(Ingreso ingreso) throws NegocioException;

	// Retorna todos los ingresos i tal que fechaIni < i.fecha <= fechaFin
	public List<Ingreso> encontrarTodos(Integer idNucleoFamiliar, Date fechaIni, Date fechaFin) throws NegocioException;
}
