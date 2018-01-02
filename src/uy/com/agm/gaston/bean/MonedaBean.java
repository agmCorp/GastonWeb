package uy.com.agm.gaston.bean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.Moneda;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IMonedaServicesLocal;
import uy.com.agm.gaston.soporte.enumerados.Modo;
import uy.com.agm.gaston.soporte.util.AppHelper;

@ManagedBean
@SessionScoped
public class MonedaBean implements Serializable {
	@EJB
	private IMonedaServicesLocal ms;
	@Inject
	private Logger logger;

	private static final long serialVersionUID = 1L;

	private Moneda monedaSeleccionada;
	private Moneda nuevaMoneda;
	private Modo modo;

	// Filtros de la grilla
	private List<Moneda> filtroMonedas;

	public void resetDataTable() throws IOException {
		// Se eliminan los filtros (bug de PF)
		filtroMonedas = null;
	}

	private void initNuevaMoneda() {
		nuevaMoneda = new Moneda();
	}

	public List<Moneda> getMonedas() {
		List<Moneda> result = null;
		try {
			result = ms.encontrarTodos();
		} catch (NegocioException ne) {
			logger.error("Error en método getMonedas", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public void almacenarMoneda() {
		try {
			Integer id = nuevaMoneda.getId();
			if (esModoEdicion()) {
				ms.guardar(nuevaMoneda);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Moneda actualizada",
								"Se ha actualizado la información del moneda '" + nuevaMoneda.getDescripcion() + "'."));
			} else {
				if (esModoCreacion()) {
					if (ms.encontrar(id) == null) {
						ms.nuevaMoneda(nuevaMoneda);
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Nueva moneda", "Se ha creado la moneda '" + nuevaMoneda.getDescripcion() + "'."));
						initNuevaMoneda();
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Error", "Ya existe una moneda con identificador " + id + "."));
					}
				}
			}
		} catch (NegocioException ne) {
			logger.error("Error en método almacenarMoneda", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public String editarMoneda(Moneda moneda) {
		nuevaMoneda = moneda;
		this.modo = Modo.EDICION;
		return "EDITAR";
	}

	public String crearMoneda() {
		initNuevaMoneda();
		this.modo = Modo.CREACION;
		return "CREAR";
	}

	public String visualizarMoneda(Moneda moneda) {
		monedaSeleccionada = moneda;
		this.modo = Modo.VISUALIZACION;
		return "VISUALIZAR";
	}

	public String preVisualizarMoneda(Moneda moneda) {
		monedaSeleccionada = moneda;
		this.modo = Modo.ELIMINACION;
		return "VISUALIZAR";
	}

	public String eliminarMoneda(Moneda moneda) {
		String outcome = "";
		try {
			ms.eliminar(moneda);
			monedaSeleccionada = null;
			outcome = "ELIMINAR";
		} catch (NegocioException ne) {
			logger.error("Error en método eliminarMoneda", ne);
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

	public boolean filtrarPorArbitraje(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		if (value == null) {
			return false;
		}
		return ((Comparable<BigDecimal>) (BigDecimal) value).compareTo(new BigDecimal(filterText)) >= 0;
	}

	public Moneda getMoneda() {
		return monedaSeleccionada;
	}

	public List<Moneda> getFiltroMonedas() {
		return filtroMonedas;
	}

	public void setFiltroMonedas(List<Moneda> filtroMonedas) {
		this.filtroMonedas = filtroMonedas;
	}

	public Moneda getNuevaMoneda() {
		return nuevaMoneda;
	}

	public void setNuevaMoneda(Moneda nuevaMoneda) {
		this.nuevaMoneda = nuevaMoneda;
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
