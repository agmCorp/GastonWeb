package uy.com.agm.gaston.negocio.interfaces;

import java.util.List;

import javax.ejb.Local;

import uy.com.agm.gaston.modelo.Alerta;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Local
public interface IAlertaServicesLocal {
	public List<Alerta> encontrarTodos(String emailUsuario) throws NegocioException;

	public void eliminar(Alerta alerta) throws NegocioException;

	public void nuevaAlertaVencimiento(Alerta alerta, Long diffMillis) throws NegocioException;
}
