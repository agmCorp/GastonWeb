package uy.com.agm.gaston.negocio.interfaces;

import javax.ejb.Local;

import uy.com.agm.gaston.modelo.RegistroUsuarioPendiente;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Local
public interface IRegistroUsuarioPendienteServicesLocal {
	public RegistroUsuarioPendiente encontrar(Integer idUsuario) throws NegocioException;

	public RegistroUsuarioPendiente encontrar(String idActivacion) throws NegocioException;
}
