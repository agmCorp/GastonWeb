package uy.com.agm.gaston.negocio.interfaces;

import java.util.List;

import javax.ejb.Local;

import uy.com.agm.gaston.modelo.Solicitud;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Local
public interface ISolicitudServicesLocal {
	public Solicitud obtenerSolicitudPendienteIngreso(Integer idNucleoFamiliar, String emailSolicitante)
			throws NegocioException;

	public List<Solicitud> obtenerSolicitudesAdministrador(String emailAdministrador) throws NegocioException;

	public Solicitud generarSolicitudPendienteIngreso(Integer idNucleoFamiliar, String emailSolicitante)
			throws NegocioException;

	public List<Solicitud> obtenerSolicitudes(Integer idNucleoFamiliar) throws NegocioException;

	public void eliminar(Solicitud solicitud) throws NegocioException;

	public Solicitud obtenerSolicitudPendienteEgreso(Integer idNucleoFamiliar, String emailSolicitante)
			throws NegocioException;

	public Solicitud generarSolicitudPendienteEgreso(Integer idNucleoFamiliar, String emailSolicitante)
			throws NegocioException;

	public void aprobarSolicitudPendiente(Solicitud solicitud) throws NegocioException;
}
