package uy.com.agm.gaston.negocio.interfaces;

import java.util.List;

import javax.ejb.Local;

import uy.com.agm.gaston.modelo.Moneda;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Local
public interface IMonedaServicesLocal {
	public Moneda encontrar(Integer idMoneda) throws NegocioException;

	public List<Moneda> encontrarTodos() throws NegocioException;

	public Moneda nuevaMoneda(Moneda moneda) throws NegocioException;

	public void eliminar(Moneda moneda) throws NegocioException;

	public void guardar(Moneda moneda) throws NegocioException;
}
