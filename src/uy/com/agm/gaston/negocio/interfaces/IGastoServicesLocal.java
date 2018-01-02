package uy.com.agm.gaston.negocio.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import uy.com.agm.gaston.modelo.Gasto;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Local
public interface IGastoServicesLocal {
	public List<Gasto> obtenerGastos(NucleoFamiliar nucleoFamiliar) throws NegocioException;

	public Gasto nuevoGasto(Gasto gasto) throws NegocioException;

	public void eliminar(Gasto gasto) throws NegocioException;

	public void guardar(Gasto gasto) throws NegocioException;

	// Retorna todos los gastos g tal que fechaIni < g.fecha <= fechaFin
	public List<Gasto> encontrarTodos(Integer idNucleoFamiliar, Date fechaIni, Date fechaFin) throws NegocioException;
}
