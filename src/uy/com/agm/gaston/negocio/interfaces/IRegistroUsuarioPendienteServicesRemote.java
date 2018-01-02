package uy.com.agm.gaston.negocio.interfaces;

import javax.ejb.Remote;

import uy.com.agm.gaston.modelo.RegistroUsuarioPendiente;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Remote
public interface IRegistroUsuarioPendienteServicesRemote {
	public RegistroUsuarioPendiente encontrar(Integer idUsuario) throws NegocioException;

	public RegistroUsuarioPendiente encontrar(String idActivacion) throws NegocioException;
}
