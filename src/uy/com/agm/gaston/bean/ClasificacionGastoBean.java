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

import uy.com.agm.gaston.modelo.ClasificacionGasto;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IClasificacionGastoServicesLocal;
import uy.com.agm.gaston.soporte.enumerados.Modo;
import uy.com.agm.gaston.soporte.util.AppHelper;

@ManagedBean
@SessionScoped
public class ClasificacionGastoBean implements Serializable {
	@EJB
	private IClasificacionGastoServicesLocal cs;
	@Inject
	private Logger logger;

	private static final long serialVersionUID = 1L;

	private ClasificacionGasto clasificacionGastoSeleccionada;
	private ClasificacionGasto nuevaClasificacionGasto;
	private Modo modo;

	// Filtros de la grilla
	private List<ClasificacionGasto> filtroClasificacionGastos;

	public void resetDataTable() throws IOException {
		// Se eliminan los filtros (bug de PF)
		filtroClasificacionGastos = null;
	}

	private void initNuevaClasificacionGasto() {
		nuevaClasificacionGasto = new ClasificacionGasto();
	}

	public List<ClasificacionGasto> getClasificacionGastos() {
		List<ClasificacionGasto> result = null;
		try {
			result = cs.encontrarTodos();
		} catch (NegocioException ne) {
			logger.error("Error en método getClasificacionGastos", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public void almacenarClasificacionGasto() {
		try {
			Integer id = nuevaClasificacionGasto.getId();
			if (esModoEdicion()) {
				cs.guardar(nuevaClasificacionGasto);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Clasificación de gasto actualizada",
								"Se ha actualizado la información de la clasificación de gasto '"
										+ nuevaClasificacionGasto.getDescripcion() + "'."));
			} else {
				if (esModoCreacion()) {
					if (cs.encontrar(id) == null) {
						cs.nuevaClasificacionGasto(nuevaClasificacionGasto);
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_INFO, "Nueva clasificación de gasto",
										"Se ha creado la clasificación de gasto '"
												+ nuevaClasificacionGasto.getDescripcion() + "'."));
						initNuevaClasificacionGasto();
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Error", "Ya existe una clasificación de gasto con identificador " + id + "."));

					}
				}
			}
		} catch (NegocioException ne) {
			logger.error("Error en método almacenarClasificacionGasto", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public String editarClasificacionGasto(ClasificacionGasto clasificacionGasto) {
		nuevaClasificacionGasto = clasificacionGasto;
		this.modo = Modo.EDICION;
		return "EDITAR";
	}

	public String crearClasificacionGasto() {
		initNuevaClasificacionGasto();
		this.modo = Modo.CREACION;
		return "CREAR";
	}

	public String visualizarClasificacionGasto(ClasificacionGasto clasificacionGasto) {
		clasificacionGastoSeleccionada = clasificacionGasto;
		this.modo = Modo.VISUALIZACION;
		return "VISUALIZAR";
	}

	public String preVisualizarClasificacionGasto(ClasificacionGasto clasificacionGasto) {
		clasificacionGastoSeleccionada = clasificacionGasto;
		this.modo = Modo.ELIMINACION;
		return "VISUALIZAR";
	}

	public String eliminarClasificacionGasto(ClasificacionGasto clasificacionGasto) {
		String outcome = "";
		try {
			cs.eliminar(clasificacionGasto);
			clasificacionGastoSeleccionada = null;
			outcome = "ELIMINAR";
		} catch (NegocioException ne) {
			logger.error("Error en método eliminarClasificacionGasto", ne);
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

	public ClasificacionGasto getClasificacionGasto() {
		return clasificacionGastoSeleccionada;
	}

	public List<ClasificacionGasto> getFiltroClasificacionGastos() {
		return filtroClasificacionGastos;
	}

	public void setFiltroClasificacionGastos(List<ClasificacionGasto> filtroClasificacionGastos) {
		this.filtroClasificacionGastos = filtroClasificacionGastos;
	}

	public ClasificacionGasto getNuevaClasificacionGasto() {
		return nuevaClasificacionGasto;
	}

	public void setNuevaClasificacionGasto(ClasificacionGasto nuevaClasificacionGasto) {
		this.nuevaClasificacionGasto = nuevaClasificacionGasto;
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
