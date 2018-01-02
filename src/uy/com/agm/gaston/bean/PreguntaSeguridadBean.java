package uy.com.agm.gaston.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.PreguntaSeguridad;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IPreguntaSeguridadServicesLocal;
import uy.com.agm.gaston.soporte.enumerados.Modo;
import uy.com.agm.gaston.soporte.util.AppHelper;

@ManagedBean
@SessionScoped
public class PreguntaSeguridadBean implements Serializable {
	@EJB
	private IPreguntaSeguridadServicesLocal ps;
	@Inject
	private Logger logger;

	private static final long serialVersionUID = 1L;

	private PreguntaSeguridad preguntaSeguridadSeleccionada;
	private PreguntaSeguridad nuevaPreguntaSeguridad;
	private Modo modo;

	// Filtros de la grilla
	private List<PreguntaSeguridad> filtroPreguntasSeguridad;

	public void resetDataTable() throws IOException {
		// Se eliminan los filtros (bug de PF)
		filtroPreguntasSeguridad = null;
	}

	private void initNuevaPreguntaSeguridad() {
		nuevaPreguntaSeguridad = new PreguntaSeguridad();
	}

	public List<PreguntaSeguridad> getPreguntasSeguridad() {
		List<PreguntaSeguridad> result = null;
		try {
			result = ps.encontrarTodos();
		} catch (NegocioException ne) {
			logger.error("Error en método getPreguntasSeguridad", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public void almacenarPreguntaSeguridad() {
		try {
			if (esModoEdicion()) {
				ps.guardar(nuevaPreguntaSeguridad);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "PreguntaSeguridad actualizada",
								"Se ha actualizado la información del preguntaSeguridad '"
										+ nuevaPreguntaSeguridad.getPregunta() + "'."));
			} else {
				if (esModoCreacion()) {
					ps.nuevaPreguntaSeguridad(nuevaPreguntaSeguridad);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Nueva preguntaSeguridad",
							"Se ha creado la preguntaSeguridad '" + nuevaPreguntaSeguridad.getPregunta() + "'."));
					initNuevaPreguntaSeguridad();
				}
			}
		} catch (NegocioException ne) {
			logger.error("Error en método almacenarPreguntaSeguridad", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public String editarPreguntaSeguridad(PreguntaSeguridad preguntaSeguridad) {
		nuevaPreguntaSeguridad = preguntaSeguridad;
		this.modo = Modo.EDICION;
		return "EDITAR";
	}

	public String crearPreguntaSeguridad() {
		initNuevaPreguntaSeguridad();
		this.modo = Modo.CREACION;
		return "CREAR";
	}

	public String visualizarPreguntaSeguridad(PreguntaSeguridad preguntaSeguridad) {
		preguntaSeguridadSeleccionada = preguntaSeguridad;
		this.modo = Modo.VISUALIZACION;
		return "VISUALIZAR";
	}

	public String preVisualizarPreguntaSeguridad(PreguntaSeguridad preguntaSeguridad) {
		preguntaSeguridadSeleccionada = preguntaSeguridad;
		this.modo = Modo.ELIMINACION;
		return "VISUALIZAR";
	}

	public String eliminarPreguntaSeguridad(PreguntaSeguridad preguntaSeguridad) {
		String outcome = "";
		try {
			ps.eliminar(preguntaSeguridad);
			preguntaSeguridadSeleccionada = null;
			outcome = "ELIMINAR";
		} catch (NegocioException ne) {
			logger.error("Error en método eliminarPreguntaSeguridad", ne);
			AppHelper.errorNegocioMensaje();
		}
		return outcome;
	}

	public PreguntaSeguridad getPreguntaSeguridad() {
		return preguntaSeguridadSeleccionada;
	}

	public List<PreguntaSeguridad> getFiltroPreguntasSeguridad() {
		return filtroPreguntasSeguridad;
	}

	public void setFiltroPreguntasSeguridad(List<PreguntaSeguridad> filtroPreguntasSeguridad) {
		this.filtroPreguntasSeguridad = filtroPreguntasSeguridad;
	}

	public PreguntaSeguridad getNuevaPreguntaSeguridad() {
		return nuevaPreguntaSeguridad;
	}

	public void setNuevaPreguntaSeguridad(PreguntaSeguridad nuevaPreguntaSeguridad) {
		this.nuevaPreguntaSeguridad = nuevaPreguntaSeguridad;
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
