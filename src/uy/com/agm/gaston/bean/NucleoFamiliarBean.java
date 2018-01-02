package uy.com.agm.gaston.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.Moneda;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.ICierreServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IMonedaServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.INucleoFamiliarServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.ISolicitudServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesLocal;
import uy.com.agm.gaston.soporte.enumerados.Modo;
import uy.com.agm.gaston.soporte.util.AppHelper;
import uy.com.agm.gaston.soporte.util.DateHelper;

@ManagedBean
@SessionScoped
public class NucleoFamiliarBean implements Serializable {
	@EJB
	private INucleoFamiliarServicesLocal nfs;
	@EJB
	private ISolicitudServicesLocal ss;
	@EJB
	private IUsuarioServicesLocal us;
	@EJB
	private IMonedaServicesLocal ms;
	@EJB
	private ICierreServicesLocal cs;
	@Inject
	private Logger logger;

	private static final long serialVersionUID = 1L;

	private NucleoFamiliar nucleoFamiliarSeleccionado;
	private NucleoFamiliar nuevoNucleoFamiliar;
	private Modo modo;

	// Filtros de la grilla
	private List<NucleoFamiliar> filtroNucleosFamiliares;
	private List<Usuario> filtroIntegrantes;

	private List<Moneda> monedas;

	@PostConstruct
	private void init() {
		try {
			monedas = ms.encontrarTodos();
			initNuevoNucleoFamiliar();
		} catch (NegocioException ne) {
			logger.error("Error en método init", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public void resetDataTable() throws IOException {
		// Se eliminan los filtros (bug de PF)
		filtroNucleosFamiliares = null;
		filtroIntegrantes = null;
	}

	public String crearNucleoFamiliar() {
		initNuevoNucleoFamiliar();
		this.modo = Modo.CREACION;
		return "CREAR";
	}

	public void almacenarNucleoFamiliar(String email) {
		try {
			nuevoNucleoFamiliar = nfs.nuevoNucleoFamiliar(nuevoNucleoFamiliar.getNombre(),
					nuevoNucleoFamiliar.getMoneda(), email);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Nuevo núcleo familiar",
							"Se ha creado el núcleo familiar '" + nuevoNucleoFamiliar.getNombre() + "'."));
			initNuevoNucleoFamiliar();
		} catch (NegocioException ne) {
			logger.error("Error en método crearNucleoFamiliar", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	private void initNuevoNucleoFamiliar() {
		nuevoNucleoFamiliar = new NucleoFamiliar();
		nuevoNucleoFamiliar.setMoneda(new Moneda());
	}

	public List<NucleoFamiliar> getNucleosFamiliares() {
		List<NucleoFamiliar> result = null;
		try {
			result = nfs.obtenerNucleosFamiliares();
		} catch (NegocioException ne) {
			logger.error("Error en método getNucleosFamiliares", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public NucleoFamiliar getNucleoFamiliar() {
		return nucleoFamiliarSeleccionado;
	}

	public String visualizarNucleoFamiliar(NucleoFamiliar nucleoFamiliar) {
		nucleoFamiliarSeleccionado = nucleoFamiliar;
		this.modo = Modo.VISUALIZACION;
		return "VISUALIZAR";
	}

	public String preVisualizarNucleoFamiliar(NucleoFamiliar nucleoFamiliar) {
		nucleoFamiliarSeleccionado = nucleoFamiliar;
		this.modo = Modo.ELIMINACION;
		return "VISUALIZAR";
	}

	public String eliminarNucleoFamiliar() {
		try {
			nfs.eliminar(nucleoFamiliarSeleccionado);
			nucleoFamiliarSeleccionado = null;
		} catch (NegocioException ne) {
			logger.error("Error en método eliminarNucleoFamiliar", ne);
			AppHelper.errorNegocioMensaje();
		}
		return "ELIMINAR";
	}

	public Boolean esIntegrante(NucleoFamiliar nucleoFamiliar, String email) {
		Boolean result = false;
		try {
			List<Usuario> integrantes = nfs.obtenerIntegrantes(nucleoFamiliar.getId());
			for (Usuario u : integrantes) {
				if (u.getEmail().equals(email)) {
					result = true;
					break;
				}
			}
		} catch (NegocioException ne) {
			logger.error("Error en método esIntegrante", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public List<Usuario> obtenerIntegrantes() {
		List<Usuario> result = null;
		try {
			result = nfs.obtenerIntegrantes(nucleoFamiliarSeleccionado.getId());
		} catch (NegocioException ne) {
			logger.error("Error en método obtenerIntegrantes", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public String reporte(NucleoFamiliar nucleoFamiliar) {
		nucleoFamiliarSeleccionado = nucleoFamiliar;
		return "REPORTE";
	}

	public void contabilizarNucleoFamiliar(NucleoFamiliar nucleoFamiliar) {
		try {
			nucleoFamiliarSeleccionado = nucleoFamiliar;
			cs.nuevoCierre(nucleoFamiliarSeleccionado);
		} catch (NegocioException ne) {
			logger.error("Error en método contabilizarNucleoFamiliar", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public String nombreAdministrador(Usuario usuarioAdministrador) {
		return us.getNombreCompleto(usuarioAdministrador.getNombre(), usuarioAdministrador.getApellido());
	}

	public List<NucleoFamiliar> getFiltroNucleosFamiliares() {
		return filtroNucleosFamiliares;
	}

	public void setFiltroNucleosFamiliares(List<NucleoFamiliar> filtroNucleosFamiliares) {
		this.filtroNucleosFamiliares = filtroNucleosFamiliares;
	}

	public List<Usuario> getFiltroIntegrantes() {
		return filtroIntegrantes;
	}

	public void setFiltroIntegrantes(List<Usuario> filtroIntegrantes) {
		this.filtroIntegrantes = filtroIntegrantes;
	}

	public List<NucleoFamiliar> obtenerNucleosFamiliaresDeIntegrante(String emailIntegrante) {
		List<NucleoFamiliar> result = null;
		try {
			result = nfs.obtenerNucleosFamiliaresDeIntegrante(emailIntegrante);
		} catch (NegocioException ne) {
			logger.error("Error en método obtenerNucleosFamiliaresDeIntegrante", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public List<NucleoFamiliar> obtenerNucleosFamiliaresNoAdministrados(String emailAdministrador) {
		List<NucleoFamiliar> result = null;
		try {
			result = nfs.obtenerNucleosFamiliaresNoAdministrados(emailAdministrador);
		} catch (NegocioException ne) {
			logger.error("Error en método obtenerNucleosFamiliaresNoAdministrados", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public List<NucleoFamiliar> obtenerNucleosFamiliaresAdministrados(String emailAdministrador) {
		List<NucleoFamiliar> result = null;
		try {
			result = nfs.obtenerNucleosFamiliaresAdministrados(emailAdministrador);
		} catch (NegocioException ne) {
			logger.error("Error en método obtenerNucleosFamiliaresAdministrados", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public NucleoFamiliar getNuevoNucleoFamiliar() {
		return nuevoNucleoFamiliar;
	}

	public void setNuevoNucleoFamiliar(NucleoFamiliar nuevoNucleoFamiliar) {
		this.nuevoNucleoFamiliar = nuevoNucleoFamiliar;
	}

	public List<Moneda> getMonedas() {
		return monedas;
	}

	public void setMonedas(List<Moneda> monedas) {
		this.monedas = monedas;
	}

	public Boolean deshabilitarContabilizacion(NucleoFamiliar nucleoFamiliar) {
		Boolean result = false;
		Date fechaUltimoCierre = nucleoFamiliar.getFechaUltimoCierre();

		if (fechaUltimoCierre != null) {
			result = DateHelper.equals(fechaUltimoCierre, DateHelper.getHoy());
		}
		return result;
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
