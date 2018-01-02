package uy.com.agm.gaston.bean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.ClasificacionGasto;
import uy.com.agm.gaston.modelo.Gasto;
import uy.com.agm.gaston.modelo.Moneda;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IClasificacionGastoServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IGastoServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IMonedaServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesLocal;
import uy.com.agm.gaston.soporte.enumerados.Modo;
import uy.com.agm.gaston.soporte.util.AppHelper;
import uy.com.agm.gaston.soporte.util.DateHelper;

@ManagedBean
@SessionScoped
public class GastoBean implements Serializable {
	@EJB
	private IGastoServicesLocal gs;
	@EJB
	private IUsuarioServicesLocal us;
	@EJB
	private IMonedaServicesLocal ms;
	@EJB
	private IClasificacionGastoServicesLocal cs;
	@Inject
	private Logger logger;

	private static final long serialVersionUID = 1L;

	private NucleoFamiliar nucleoFamiliarSeleccionado;
	private Gasto gastoSeleccionado;
	private Gasto nuevoGasto;
	private Modo modo;

	// Filtros de la grilla
	private List<Gasto> filtroGastos;

	private List<Moneda> monedas;
	private List<ClasificacionGasto> clasificacionesGasto;

	@PostConstruct
	private void init() {
		try {
			monedas = ms.encontrarTodos();
			clasificacionesGasto = cs.encontrarTodos();
		} catch (NegocioException ne) {
			logger.error("Error en método init", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public void resetDataTable() throws IOException {
		// Se eliminan los filtros (bug de PF)
		filtroGastos = null;
	}

	private void initNuevoGasto() {
		nuevoGasto = new Gasto();
		nuevoGasto.setFecha(DateHelper.getHoy());
		nuevoGasto.setNucleoFamiliar(nucleoFamiliarSeleccionado);
		nuevoGasto.setMoneda(new Moneda());
		nuevoGasto.setClasificacion(new ClasificacionGasto());
	}

	public String visualizarGastos(NucleoFamiliar nucleoFamiliar) {
		this.nucleoFamiliarSeleccionado = nucleoFamiliar;
		return "VISUALIZAR";
	}

	public List<Gasto> getGastos() {
		List<Gasto> result = null;
		try {
			result = gs.obtenerGastos(nucleoFamiliarSeleccionado);
		} catch (NegocioException ne) {
			logger.error("Error en método getGastos", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public void almacenarGasto(String email) {
		try {
			nuevoGasto.setMoneda(ms.encontrar(nuevoGasto.getMoneda().getId()));
			nuevoGasto.setClasificacion(cs.encontrar(nuevoGasto.getClasificacion().getId()));
			Date fechaUltimoCierre = nuevoGasto.getNucleoFamiliar().getFechaUltimoCierre();
			// Si el núcleo familiar no tiene fecha de último cierre,
			// se toma como fecha de último cierre el día anterior a su fecha de
			// creación.
			if (fechaUltimoCierre == null) {
				fechaUltimoCierre = DateHelper.getYesterday(nuevoGasto.getNucleoFamiliar().getFechaCreacion());
			}
			Date fechaGasto = nuevoGasto.getFecha();
			String summary;

			// No se permite ingresar ni modificar un gasto con fecha anterior o
			// igual a la fecha del último cierre.
			if (fechaGasto.after(fechaUltimoCierre)) {
				if (esModoEdicion()) {
					gs.guardar(nuevoGasto);
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Gasto actualizado",
									"Se ha actualizado la información del gasto '" + nuevoGasto.getDescripcion()
											+ "' vinculado al núcleo familiar '"
											+ nucleoFamiliarSeleccionado.getNombre() + "'."));
				} else {
					if (esModoCreacion()) {
						nuevoGasto.setUsuario(us.encontrar(email));
						gs.nuevoGasto(nuevoGasto);
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_INFO, "Nuevo gasto",
										"Se ha vinculado el gasto '" + nuevoGasto.getDescripcion()
												+ "' al núcleo familiar '" + nucleoFamiliarSeleccionado.getNombre()
												+ "'."));
						initNuevoGasto();
					}
				}
			} else {
				summary = "Error";
				if (esModoEdicion()) {
					summary += " al actualizar el gasto";
				} else {
					if (esModoCreacion()) {
						summary += " al crear el gasto";
					}
				}
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						summary,
						"La fecha del gasto debe ser posterior a " + DateHelper.formatDate(fechaUltimoCierre) + "."));
			}
		} catch (NegocioException ne) {
			logger.error("Error en método almacenarGasto", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public String editarGasto(Gasto gasto) {
		nuevoGasto = gasto;
		this.modo = Modo.EDICION;
		return "EDITAR";
	}

	public String crearGasto() {
		initNuevoGasto();
		this.modo = Modo.CREACION;
		return "CREAR";
	}

	public String visualizarGasto(Gasto gasto) {
		gastoSeleccionado = gasto;
		this.modo = Modo.VISUALIZACION;
		return "VISUALIZAR";
	}

	public String preVisualizarGasto(Gasto gasto) {
		gastoSeleccionado = gasto;
		this.modo = Modo.ELIMINACION;
		return "VISUALIZAR";
	}

	public String eliminarGasto(Gasto gasto) {
		try {
			gs.eliminar(gasto);
			gastoSeleccionado = null;
		} catch (NegocioException ne) {
			logger.error("Error en método eliminarGasto", ne);
			AppHelper.errorNegocioMensaje();
		}
		return "ELIMINAR";
	}

	public String nombreCompletoResponsable(Usuario responsable) {
		return us.getNombreCompleto(responsable.getNombre(), responsable.getApellido());
	}

	public NucleoFamiliar getNucleoFamiliar() {
		return nucleoFamiliarSeleccionado;
	}

	public Gasto getGasto() {
		return gastoSeleccionado;
	}

	public List<Gasto> getFiltroGastos() {
		return filtroGastos;
	}

	public void setFiltroGastos(List<Gasto> filtroGastos) {
		this.filtroGastos = filtroGastos;
	}

	public List<Moneda> getMonedas() {
		return monedas;
	}

	public void setMonedas(List<Moneda> monedas) {
		this.monedas = monedas;
	}

	public List<ClasificacionGasto> getClasificacionesGasto() {
		return clasificacionesGasto;
	}

	public void setClasificacionesGasto(List<ClasificacionGasto> clasificacionesGasto) {
		this.clasificacionesGasto = clasificacionesGasto;
	}

	public boolean filtrarPorMonto(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		if (value == null) {
			return false;
		}
		return ((Comparable<BigDecimal>) (BigDecimal) value).compareTo(new BigDecimal(filterText)) >= 0;
	}

	public Gasto getNuevoGasto() {
		return nuevoGasto;
	}

	public void setNuevoGasto(Gasto nuevoGasto) {
		this.nuevoGasto = nuevoGasto;
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
