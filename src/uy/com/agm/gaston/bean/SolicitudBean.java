package uy.com.agm.gaston.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.modelo.Solicitud;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.INucleoFamiliarServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.ISolicitudServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesLocal;
import uy.com.agm.gaston.soporte.enumerados.Estado;
import uy.com.agm.gaston.soporte.enumerados.Modo;
import uy.com.agm.gaston.soporte.enumerados.TipoDeSolicitud;
import uy.com.agm.gaston.soporte.util.AppHelper;

@ManagedBean
@SessionScoped
public class SolicitudBean implements Serializable {
	@EJB
	private INucleoFamiliarServicesLocal nfs;
	@EJB
	private ISolicitudServicesLocal ss;
	@EJB
	private IUsuarioServicesLocal us;
	@Inject
	private Logger logger;

	// Filtros de la grilla
	private List<Solicitud> filtroSolicitudes;
	private final static String[] tipos;
	private final static String[] estados;

	static {
		estados = new String[2];
		estados[0] = Estado.PENDIENTE.toString();
		estados[1] = Estado.CONFIRMADO.toString();

		tipos = new String[2];
		tipos[0] = TipoDeSolicitud.EGRESO.toString();
		tipos[1] = TipoDeSolicitud.INGRESO.toString();
	}

	private static final long serialVersionUID = 1L;
	private Solicitud solicitudSeleccionada;
	private Modo modo;

	public void resetDataTable() throws IOException {
		// Se eliminan los filtros (bug de PF)
		filtroSolicitudes = null;
	}

	public List<Solicitud> obtenerSolicitudesAdministrador(String emailAdministrador) {
		List<Solicitud> result = null;
		try {
			result = ss.obtenerSolicitudesAdministrador(emailAdministrador);
		} catch (NegocioException ne) {
			logger.error("Error en método obtenerSolicitudes", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public void solicitarIngreso(NucleoFamiliar nucleoFamiliar, String emailSolicitante) {
		try {
			Usuario administrador = nucleoFamiliar.getAdministrador();
			String nombreCompletoAdmnistrador = us.getNombreCompleto(administrador.getNombre(),
					administrador.getApellido());

			Solicitud solicitud = ss.obtenerSolicitudPendienteIngreso(nucleoFamiliar.getId(), emailSolicitante);
			if (solicitud != null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Ya existe la solicitud número '" + solicitud.getId() + "' pendiente de ingreso:",
								"Ya existe una solicitud para ingresar al núcleo familiar '"
										+ nucleoFamiliar.getNombre() + "' a ser aprobada por '"
										+ nombreCompletoAdmnistrador + "'."));
			} else {
				solicitud = ss.generarSolicitudPendienteIngreso(nucleoFamiliar.getId(), emailSolicitante);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Solicitud número '" + solicitud.getId() + "' en curso:",
								"Se ha generado una solicitud para ingresar al núcleo familiar '"
										+ nucleoFamiliar.getNombre() + "' a ser aprobada por '"
										+ nombreCompletoAdmnistrador + "'."));
			}
		} catch (NegocioException ne) {
			logger.error("Error en método solicitarIngreso", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public void solicitarEgreso(NucleoFamiliar nucleoFamiliar, String emailSolicitante) {
		try {
			Usuario administrador = nucleoFamiliar.getAdministrador();
			String nombreCompletoAdmnistrador = us.getNombreCompleto(administrador.getNombre(),
					administrador.getApellido());

			Solicitud solicitud = ss.obtenerSolicitudPendienteEgreso(nucleoFamiliar.getId(), emailSolicitante);
			if (solicitud != null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Ya existe la solicitud número '" + solicitud.getId() + "' pendiente de egreso:",
								"Ya existe una solicitud para abandonar el núcleo familiar '"
										+ nucleoFamiliar.getNombre() + "' a ser aprobada por '"
										+ nombreCompletoAdmnistrador + "'."));
			} else {
				solicitud = ss.generarSolicitudPendienteEgreso(nucleoFamiliar.getId(), emailSolicitante);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Solicitud número '" + solicitud.getId() + "' en curso:",
								"Se ha generado una solicitud para abandonar el núcleo familiar '"
										+ nucleoFamiliar.getNombre() + "' a ser aprobada por '"
										+ nombreCompletoAdmnistrador + "'."));
			}
		} catch (NegocioException ne) {
			logger.error("Error en método solicitarEgreso", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public String preVisualizarSolicitud(Solicitud solicitud) {
		solicitudSeleccionada = solicitud;
		this.modo = Modo.ELIMINACION;
		return "VISUALIZAR";
	}

	public String aprobarSolicitud(Solicitud solicitud) {
		solicitudSeleccionada = solicitud;
		this.modo = Modo.VISUALIZACION;
		return "VISUALIZAR";
	}

	public String obtenerNombreCompletoSolicitante(Usuario solicitante) {
		return us.getNombreCompleto(solicitante.getNombre(), solicitante.getApellido());
	}

	public String eliminarSolicitudPendiente(Solicitud solicitud) {
		try {
			ss.eliminar(solicitud);
		} catch (NegocioException ne) {
			logger.error("Error en método eliminarSolicitudPendiente", ne);
			AppHelper.errorNegocioMensaje();
		}
		return "ELIMINAR";
	}

	public String aprobarSolicitudPendiente(Solicitud solicitud) {
		try {
			ss.aprobarSolicitudPendiente(solicitud);
		} catch (NegocioException ne) {
			logger.error("Error en método aprobarSolicitudPendiente", ne);
			AppHelper.errorNegocioMensaje();
		}
		return "APROBAR";
	}

	public Boolean esPendiente(Solicitud solicitud) {
		return solicitud.getEstado().equals(Estado.PENDIENTE);
	}

	public boolean filtrarPorId(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		if (value == null) {
			return false;
		}
		return ((Comparable<Integer>) (Integer) value).compareTo(Integer.valueOf(filterText)) >= 0;
	}

	public Solicitud getSolicitud() {
		return solicitudSeleccionada;
	}

	public List<Solicitud> getFiltroSolicitudes() {
		return filtroSolicitudes;
	}

	public void setFiltroSolicitudes(List<Solicitud> filtroSolicitudes) {
		this.filtroSolicitudes = filtroSolicitudes;
	}

	public List<String> getTipos() {
		return Arrays.asList(tipos);
	}

	public List<String> getEstados() {
		return Arrays.asList(estados);
	}

	public Boolean esModoCreacion() {
		return this.modo == Modo.CREACION;
	}

	public Boolean esModoEdicion() {
		return this.modo == Modo.EDICION;
	}

	public Boolean esModoEliminacion() {
		return this.modo == Modo.ELIMINACION;
	}

	public Boolean esModoVisualizacion() {
		return this.modo == Modo.VISUALIZACION;
	}
}
