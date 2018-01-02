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

import uy.com.agm.gaston.modelo.ClasificacionIngreso;
import uy.com.agm.gaston.modelo.Ingreso;
import uy.com.agm.gaston.modelo.Moneda;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IClasificacionIngresoServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IIngresoServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IMonedaServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesLocal;
import uy.com.agm.gaston.soporte.enumerados.Modo;
import uy.com.agm.gaston.soporte.util.AppHelper;
import uy.com.agm.gaston.soporte.util.DateHelper;

@ManagedBean
@SessionScoped
public class IngresoBean implements Serializable {
	@EJB
	private IIngresoServicesLocal is;
	@EJB
	private IUsuarioServicesLocal us;
	@EJB
	private IMonedaServicesLocal ms;
	@EJB
	private IClasificacionIngresoServicesLocal cs;
	@Inject
	private Logger logger;

	private static final long serialVersionUID = 1L;

	private NucleoFamiliar nucleoFamiliarSeleccionado;
	private Ingreso ingresoSeleccionado;
	private Ingreso nuevoIngreso;
	private Modo modo;

	// Filtros de la grilla
	private List<Ingreso> filtroIngresos;

	private List<Moneda> monedas;
	private List<ClasificacionIngreso> clasificacionesIngreso;

	@PostConstruct
	private void init() {
		try {
			monedas = ms.encontrarTodos();
			clasificacionesIngreso = cs.encontrarTodos();
		} catch (NegocioException ne) {
			logger.error("Error en método init", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public void resetDataTable() throws IOException {
		// Se eliminan los filtros (bug de PF)
		filtroIngresos = null;
	}

	private void initNuevoIngreso() {
		nuevoIngreso = new Ingreso();
		nuevoIngreso.setFecha(DateHelper.getHoy());
		nuevoIngreso.setNucleoFamiliar(nucleoFamiliarSeleccionado);
		nuevoIngreso.setMoneda(new Moneda());
		nuevoIngreso.setClasificacion(new ClasificacionIngreso());
	}

	public String visualizarIngresos(NucleoFamiliar nucleoFamiliar) {
		this.nucleoFamiliarSeleccionado = nucleoFamiliar;
		return "VISUALIZAR";
	}

	public List<Ingreso> getIngresos() {
		List<Ingreso> result = null;
		try {
			result = is.obtenerIngresos(nucleoFamiliarSeleccionado);
		} catch (NegocioException ne) {
			logger.error("Error en método getIngresos", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public void almacenarIngreso(String email) {
		try {
			nuevoIngreso.setMoneda(ms.encontrar(nuevoIngreso.getMoneda().getId()));
			nuevoIngreso.setClasificacion(cs.encontrar(nuevoIngreso.getClasificacion().getId()));
			Date fechaUltimoCierre = nuevoIngreso.getNucleoFamiliar().getFechaUltimoCierre();
			// Si el núcleo familiar no tiene fecha de último cierre,
			// se toma como fecha de último cierre el día anterior a su fecha de
			// creación.
			if (fechaUltimoCierre == null) {
				fechaUltimoCierre = DateHelper.getYesterday(nuevoIngreso.getNucleoFamiliar().getFechaCreacion());
			}
			Date fechaIngreso = nuevoIngreso.getFecha();
			String summary;

			// No se permite ingresar ni modificar un ingreso con fecha anterior
			// o igual a la fecha del último cierre.
			if (fechaIngreso.after(fechaUltimoCierre)) {
				if (esModoEdicion()) {
					is.guardar(nuevoIngreso);
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Ingreso actualizado",
									"Se ha actualizado la información del ingreso '" + nuevoIngreso.getDescripcion()
											+ "' vinculado al núcleo familiar '"
											+ nucleoFamiliarSeleccionado.getNombre() + "'."));
				} else {
					if (esModoCreacion()) {
						nuevoIngreso.setUsuario(us.encontrar(email));
						is.nuevoIngreso(nuevoIngreso);
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_INFO, "Nuevo ingreso",
										"Se ha vinculado el ingreso '" + nuevoIngreso.getDescripcion()
												+ "' al núcleo familiar '" + nucleoFamiliarSeleccionado.getNombre()
												+ "'."));
						initNuevoIngreso();
					}
				}
			} else {
				summary = "Error";
				if (esModoEdicion()) {
					summary += " al actualizar el ingreso";
				} else {
					if (esModoCreacion()) {
						summary += " al crear el ingreso";
					}
				}
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						summary,
						"La fecha del ingreso debe ser posterior a " + DateHelper.formatDate(fechaUltimoCierre) + "."));
			}
		} catch (NegocioException ne) {
			logger.error("Error en método almacenarIngreso", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public String editarIngreso(Ingreso ingreso) {
		nuevoIngreso = ingreso;
		this.modo = Modo.EDICION;
		return "EDITAR";
	}

	public String crearIngreso() {
		initNuevoIngreso();
		this.modo = Modo.CREACION;
		return "CREAR";
	}

	public String visualizarIngreso(Ingreso ingreso) {
		ingresoSeleccionado = ingreso;
		this.modo = Modo.VISUALIZACION;
		return "VISUALIZAR";
	}

	public String preVisualizarIngreso(Ingreso ingreso) {
		ingresoSeleccionado = ingreso;
		this.modo = Modo.ELIMINACION;
		return "VISUALIZAR";
	}

	public String eliminarIngreso(Ingreso ingreso) {
		try {
			is.eliminar(ingreso);
			ingresoSeleccionado = null;
		} catch (NegocioException ne) {
			logger.error("Error en método eliminarIngreso", ne);
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

	public Ingreso getIngreso() {
		return ingresoSeleccionado;
	}

	public List<Ingreso> getFiltroIngresos() {
		return filtroIngresos;
	}

	public void setFiltroIngresos(List<Ingreso> filtroIngresos) {
		this.filtroIngresos = filtroIngresos;
	}

	public List<Moneda> getMonedas() {
		return monedas;
	}

	public void setMonedas(List<Moneda> monedas) {
		this.monedas = monedas;
	}

	public List<ClasificacionIngreso> getClasificacionesIngreso() {
		return clasificacionesIngreso;
	}

	public void setClasificacionesIngreso(List<ClasificacionIngreso> clasificacionesIngreso) {
		this.clasificacionesIngreso = clasificacionesIngreso;
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

	public Ingreso getNuevoIngreso() {
		return nuevoIngreso;
	}

	public void setNuevoIngreso(Ingreso nuevoIngreso) {
		this.nuevoIngreso = nuevoIngreso;
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
