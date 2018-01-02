package uy.com.agm.gaston.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;

import uy.com.agm.gaston.modelo.Alerta;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IAlertaServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.INucleoFamiliarServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesLocal;
import uy.com.agm.gaston.soporte.enumerados.Modo;
import uy.com.agm.gaston.soporte.util.AppHelper;
import uy.com.agm.gaston.soporte.util.DateHelper;

@ManagedBean
@SessionScoped
public class AlertaBean implements Serializable {
	@EJB
	private IAlertaServicesLocal as;
	@EJB
	private IUsuarioServicesLocal us;
	@EJB
	private INucleoFamiliarServicesLocal nfs;
	@Inject
	private Logger logger;

	private static final long serialVersionUID = 1L;
	private static final Integer SEGUNDOS_HOLGURA = 120;

	private NucleoFamiliar nucleoFamiliarSeleccionado;
	private Alerta alertaSeleccionada;
	private Alerta nuevaAlerta;
	private Modo modo;

	// Filtros de la grilla
	private List<Alerta> filtroAlertas;

	public void resetDataTable() throws IOException {
		// Se eliminan los filtros (bug de PF)
		filtroAlertas = null;
	}

	private void initNuevaAlerta() {
		nucleoFamiliarSeleccionado = new NucleoFamiliar();
		nuevaAlerta = new Alerta();
		nuevaAlerta.setTimestamp(new Date());
	}

	public List<Alerta> obtenerAlertas(String email) {
		List<Alerta> result = null;
		try {
			result = as.encontrarTodos(email);
		} catch (NegocioException ne) {
			logger.error("Error en método obtenerAlertas", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public void almacenarAlerta() {
		Date hoy;
		Long diffMillis;
		List<Usuario> integrantes;
		Integer idNucleoFamiliar;

		try {
			if (esModoCreacion()) {
				hoy = DateHelper.getHoy();
				diffMillis = DateHelper.diffMillis(hoy, nuevaAlerta.getTimestamp());
				if (diffMillis > SEGUNDOS_HOLGURA) {
					idNucleoFamiliar = nucleoFamiliarSeleccionado.getId();
					integrantes = nfs.obtenerIntegrantes(idNucleoFamiliar);
					for (Usuario integrante : integrantes) {
						nuevaAlerta.setId(AppHelper.getUniqueId());
						nuevaAlerta.setUsuario(integrante);
						as.nuevaAlertaVencimiento(nuevaAlerta, diffMillis);
					}
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Nueva alerta",
									"Se ha creado la alerta '" + nuevaAlerta.getDescripcion()
											+ "' para los integrantes de '"
											+ nfs.encontrar(idNucleoFamiliar).getNombre() + "'."));
					initNuevaAlerta();
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nueva alerta",
									"Debe seleccionar una fecha y hora posterior a "
											+ DateHelper.formatDateTime(DateHelper.addSeconds(hoy, SEGUNDOS_HOLGURA))
											+ "."));
				}
			}
		} catch (NegocioException ne) {
			logger.error("Error en método almacenarAlerta", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public String parseAlerta(String mensaje) {
		return Jsoup.parse(mensaje).text();
	}

	public String crearAlerta() {
		initNuevaAlerta();
		this.modo = Modo.CREACION;
		return "CREAR";
	}

	public String visualizarAlerta(Alerta alerta) {
		alertaSeleccionada = alerta;
		this.modo = Modo.VISUALIZACION;
		return "VISUALIZAR";
	}

	public String preVisualizarAlerta(Alerta alerta) {
		alertaSeleccionada = alerta;
		this.modo = Modo.ELIMINACION;
		return "VISUALIZAR";
	}

	public String eliminarAlerta(Alerta alerta) {
		try {
			as.eliminar(alerta);
			alertaSeleccionada = null;
		} catch (NegocioException ne) {
			logger.error("Error en método eliminarAlerta", ne);
			AppHelper.errorNegocioMensaje();
		}
		return "ELIMINAR";
	}

	public String nombreCompletoResponsable(Usuario responsable) {
		return us.getNombreCompleto(responsable.getNombre(), responsable.getApellido());
	}

	public Alerta getAlerta() {
		return alertaSeleccionada;
	}

	public List<Alerta> getFiltroAlertas() {
		return filtroAlertas;
	}

	public void setFiltroAlertas(List<Alerta> filtroAlertas) {
		this.filtroAlertas = filtroAlertas;
	}

	public Alerta getNuevaAlerta() {
		return nuevaAlerta;
	}

	public void setNuevaAlerta(Alerta nuevaAlerta) {
		this.nuevaAlerta = nuevaAlerta;
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

	public NucleoFamiliar getNucleoFamiliar() {
		return nucleoFamiliarSeleccionado;
	}
}
