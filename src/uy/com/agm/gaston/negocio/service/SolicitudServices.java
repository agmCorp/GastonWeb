package uy.com.agm.gaston.negocio.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.modelo.Solicitud;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.ISolicitudServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.ISolicitudServicesRemote;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.INucleoFamiliarDao;
import uy.com.agm.gaston.persistencia.interfaces.ISolicitudDao;
import uy.com.agm.gaston.persistencia.interfaces.IUsuarioDao;
import uy.com.agm.gaston.soporte.enumerados.Estado;
import uy.com.agm.gaston.soporte.enumerados.TipoDeSolicitud;
import uy.com.agm.gaston.soporte.util.DateHelper;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SolicitudServices implements ISolicitudServicesRemote, ISolicitudServicesLocal {
	@EJB
	private INucleoFamiliarDao nucleoFamiliarDao;
	@EJB
	private IUsuarioDao usuarioDao;
	@EJB
	private ISolicitudDao solicitudDao;
	private static final String ERROR_MSG = "Error en negocio " + SolicitudServices.class.getName();

	@Override
	public List<Solicitud> obtenerSolicitudesAdministrador(String emailAdministrador) throws NegocioException {
		ArrayList<Solicitud> solicitudes = new ArrayList<>();
		try {
			List<NucleoFamiliar> nucleosFamiliares = nucleoFamiliarDao
					.obtenerNucleosFamiliaresAdministrados(emailAdministrador);
			for (NucleoFamiliar nucleoFamiliar : nucleosFamiliares) {
				solicitudes.addAll(solicitudDao.encontrarTodos(nucleoFamiliar.getId()));
			}
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return solicitudes;
	}

	@Override
	public Solicitud obtenerSolicitudPendienteIngreso(Integer idNucleoFamiliar, String emailSolicitante)
			throws NegocioException {
		Solicitud result = null;
		try {
			result = solicitudDao.obtenerSolicitud(Estado.PENDIENTE, TipoDeSolicitud.INGRESO, idNucleoFamiliar,
					emailSolicitante);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public Solicitud generarSolicitudPendienteIngreso(Integer idNucleoFamiliar, String emailSolicitante)
			throws NegocioException {
		Solicitud result = null;
		try {
			Usuario solicitante = usuarioDao.encontrar(emailSolicitante);
			NucleoFamiliar nucleoFamiliar = nucleoFamiliarDao.encontrar(idNucleoFamiliar);
			Solicitud solicitud = new Solicitud();
			solicitud.setEstado(Estado.PENDIENTE);
			solicitud.setNucleoFamiliar(nucleoFamiliar);
			solicitud.setSolicitante(solicitante);
			solicitud.setTimestamp(DateHelper.getHoy());
			solicitud.setTipo(TipoDeSolicitud.INGRESO);
			result = solicitudDao.crear(solicitud);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public List<Solicitud> obtenerSolicitudes(Integer idNucleoFamiliar) throws NegocioException {
		List<Solicitud> result = null;
		try {
			result = solicitudDao.encontrarTodos(idNucleoFamiliar);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public void eliminar(Solicitud solicitud) throws NegocioException {
		try {
			solicitudDao.eliminar(solicitud);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public Solicitud obtenerSolicitudPendienteEgreso(Integer idNucleoFamiliar, String emailSolicitante)
			throws NegocioException {
		Solicitud result = null;
		try {
			result = solicitudDao.obtenerSolicitud(Estado.PENDIENTE, TipoDeSolicitud.EGRESO, idNucleoFamiliar,
					emailSolicitante);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public Solicitud generarSolicitudPendienteEgreso(Integer idNucleoFamiliar, String emailSolicitante)
			throws NegocioException {
		Solicitud result = null;
		try {
			Usuario solicitante = usuarioDao.encontrar(emailSolicitante);
			NucleoFamiliar nucleoFamiliar = nucleoFamiliarDao.encontrar(idNucleoFamiliar);
			Solicitud solicitud = new Solicitud();
			solicitud.setEstado(Estado.PENDIENTE);
			solicitud.setNucleoFamiliar(nucleoFamiliar);
			solicitud.setSolicitante(solicitante);
			solicitud.setTimestamp(DateHelper.getHoy());
			solicitud.setTipo(TipoDeSolicitud.EGRESO);
			result = solicitudDao.crear(solicitud);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	private void confirmar(Solicitud solicitud) throws NegocioException {
		try {
			solicitud.setEstado(Estado.CONFIRMADO);
			solicitudDao.guardar(solicitud);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public void aprobarSolicitudPendiente(Solicitud solicitud) throws NegocioException {
		try {
			Usuario solicitante = solicitud.getSolicitante();
			List<Usuario> integrantes = nucleoFamiliarDao.obtenerIntegrantes(solicitud.getNucleoFamiliar().getId());
			Boolean esIntegrante = esIntegrante(integrantes, solicitante);

			if (solicitud.getTipo().equals(TipoDeSolicitud.INGRESO)) {
				if (!esIntegrante) {
					nucleoFamiliarDao.agregarIntegrante(solicitud.getNucleoFamiliar().getId(), solicitante);
				}
			} else {
				if (solicitud.getTipo().equals(TipoDeSolicitud.EGRESO)) {
					if (esIntegrante) {
						nucleoFamiliarDao.eliminarIntegrante(solicitud.getNucleoFamiliar().getId(), solicitante);
					}
				}
			}
			confirmar(solicitud);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	private Boolean esIntegrante(List<Usuario> integrantes, Usuario usuario) {
		Boolean result = false;

		for (Usuario integrante : integrantes) {
			if (integrante.getEmail().equals(usuario.getEmail())) {
				result = true;
				break;
			}
		}
		return result;
	}
}
