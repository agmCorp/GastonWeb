package uy.com.agm.gaston.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.ClasificacionIngreso;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IClasificacionIngresoServicesLocal;
import uy.com.agm.gaston.soporte.enumerados.Modo;
import uy.com.agm.gaston.soporte.util.AppHelper;

@ManagedBean
@SessionScoped
public class ClasificacionIngresoBean implements Serializable {
	@EJB
	private IClasificacionIngresoServicesLocal cs;
	@Inject
	private Logger logger;

	private static final long serialVersionUID = 1L;

	private ClasificacionIngreso clasificacionIngresoSeleccionada;
	private ClasificacionIngreso nuevaClasificacionIngreso;
	private Modo modo;

	// Filtros de la grilla
	private List<ClasificacionIngreso> filtroClasificacionIngresos;

	public void resetDataTable() throws IOException {
		// Se eliminan los filtros (bug de PF)
		filtroClasificacionIngresos = null;
	}

	private void initNuevaClasificacionIngreso() {
		nuevaClasificacionIngreso = new ClasificacionIngreso();
	}

	public List<ClasificacionIngreso> getClasificacionIngresos() {
		List<ClasificacionIngreso> result = null;
		try {
			result = cs.encontrarTodos();
		} catch (NegocioException ne) {
			logger.error("Error en método getClasificacionIngresos", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public void almacenarClasificacionIngreso() {
		try {
			Integer id = nuevaClasificacionIngreso.getId();
			if (esModoEdicion()) {
				cs.guardar(nuevaClasificacionIngreso);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Clasificación de ingreso actualizada",
								"Se ha actualizado la información de la clasificación de ingreso '"
										+ nuevaClasificacionIngreso.getDescripcion() + "'."));
			} else {
				if (esModoCreacion()) {
					if (cs.encontrar(id) == null) {
						cs.nuevaClasificacionIngreso(nuevaClasificacionIngreso);
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_INFO, "Nueva clasificación de ingreso",
										"Se ha creado la clasificación de ingreso '"
												+ nuevaClasificacionIngreso.getDescripcion() + "'."));
						initNuevaClasificacionIngreso();
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Error", "Ya existe una clasificación de ingreso con identificador " + id + "."));

					}
				}
			}
		} catch (NegocioException ne) {
			logger.error("Error en método almacenarClasificacionIngreso", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public String editarClasificacionIngreso(ClasificacionIngreso clasificacionIngreso) {
		nuevaClasificacionIngreso = clasificacionIngreso;
		this.modo = Modo.EDICION;
		return "EDITAR";
	}

	public String crearClasificacionIngreso() {
		initNuevaClasificacionIngreso();
		this.modo = Modo.CREACION;
		return "CREAR";
	}

	public String visualizarClasificacionIngreso(ClasificacionIngreso clasificacionIngreso) {
		clasificacionIngresoSeleccionada = clasificacionIngreso;
		this.modo = Modo.VISUALIZACION;
		return "VISUALIZAR";
	}

	public String preVisualizarClasificacionIngreso(ClasificacionIngreso clasificacionIngreso) {
		clasificacionIngresoSeleccionada = clasificacionIngreso;
		this.modo = Modo.ELIMINACION;
		return "VISUALIZAR";
	}

	public String eliminarClasificacionIngreso(ClasificacionIngreso clasificacionIngreso) {
		String outcome = "";
		try {
			cs.eliminar(clasificacionIngreso);
			clasificacionIngresoSeleccionada = null;
			outcome = "ELIMINAR";
		} catch (NegocioException ne) {
			logger.error("Error en método eliminarClasificacionIngreso", ne);
			AppHelper.errorNegocioMensaje();
		}
		return outcome;
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

	public ClasificacionIngreso getClasificacionIngreso() {
		return clasificacionIngresoSeleccionada;
	}

	public List<ClasificacionIngreso> getFiltroClasificacionIngresos() {
		return filtroClasificacionIngresos;
	}

	public void setFiltroClasificacionIngresos(List<ClasificacionIngreso> filtroClasificacionIngresos) {
		this.filtroClasificacionIngresos = filtroClasificacionIngresos;
	}

	public ClasificacionIngreso getNuevaClasificacionIngreso() {
		return nuevaClasificacionIngreso;
	}

	public void setNuevaClasificacionIngreso(ClasificacionIngreso nuevaClasificacionIngreso) {
		this.nuevaClasificacionIngreso = nuevaClasificacionIngreso;
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
