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

import uy.com.agm.gaston.modelo.Propiedad;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IAlertaGastoExcesivoTimerServices;
import uy.com.agm.gaston.negocio.interfaces.IPropiedadServicesLocal;
import uy.com.agm.gaston.soporte.enumerados.Modo;
import uy.com.agm.gaston.soporte.util.AppHelper;

@ManagedBean
@SessionScoped
public class PropiedadBean implements Serializable {
	@EJB
	private IPropiedadServicesLocal ps;
	@EJB
	private IAlertaGastoExcesivoTimerServices as;
	@Inject
	private Logger logger;

	private static final long serialVersionUID = 1L;

	private Propiedad propiedadSeleccionada;
	private Propiedad nuevaPropiedad;
	private Modo modo;

	// Filtros de la grilla
	private List<Propiedad> filtroPropiedades;

	public void resetDataTable() throws IOException {
		// Se eliminan los filtros (bug de PF)
		filtroPropiedades = null;
	}

	private void initNuevaPropiedad() {
		nuevaPropiedad = new Propiedad();
	}

	public List<Propiedad> getPropiedades() {
		List<Propiedad> result = null;
		try {
			result = ps.encontrarTodos();
		} catch (NegocioException ne) {
			logger.error("Error en método getPropiedades", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public void almacenarPropiedad() {
		try {
			String clave = nuevaPropiedad.getClave();
			if (esModoEdicion()) {
				ps.guardar(nuevaPropiedad);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Propiedad actualizada",
								"Se ha actualizado la información del propiedad '" + nuevaPropiedad.getClave() + "'."));
			} else {
				if (esModoCreacion()) {
					if (ps.encontrar(clave) == null) {
						ps.nuevaPropiedad(nuevaPropiedad);
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Nueva propiedad", "Se ha creado la propiedad '" + nuevaPropiedad.getClave() + "'."));
						initNuevaPropiedad();
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Error", "Ya existe una propiedad con clave " + clave + "."));
					}
				}
			}

			// Clave fija para controlar el ciclo del timer.
			if (clave.equals(as.getClaveMinutosAlertaGastoExcesivo())) {
				as.resetTimer();
			}

		} catch (NegocioException ne) {
			logger.error("Error en método almacenarPropiedad", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public String editarPropiedad(Propiedad propiedad) {
		nuevaPropiedad = propiedad;
		this.modo = Modo.EDICION;
		return "EDITAR";
	}

	public String crearPropiedad() {
		initNuevaPropiedad();
		this.modo = Modo.CREACION;
		return "CREAR";
	}

	public String visualizarPropiedad(Propiedad propiedad) {
		propiedadSeleccionada = propiedad;
		this.modo = Modo.VISUALIZACION;
		return "VISUALIZAR";
	}

	public String preVisualizarPropiedad(Propiedad propiedad) {
		propiedadSeleccionada = propiedad;
		this.modo = Modo.ELIMINACION;
		return "VISUALIZAR";
	}

	public String eliminarPropiedad(Propiedad propiedad) {
		try {
			ps.eliminar(propiedad);
			propiedadSeleccionada = null;
		} catch (NegocioException ne) {
			logger.error("Error en método eliminarPropiedad", ne);
			AppHelper.errorNegocioMensaje();
		}
		return "ELIMINAR";
	}

	public Propiedad getPropiedad() {
		return propiedadSeleccionada;
	}

	public List<Propiedad> getFiltroPropiedades() {
		return filtroPropiedades;
	}

	public void setFiltroPropiedades(List<Propiedad> filtroPropiedades) {
		this.filtroPropiedades = filtroPropiedades;
	}

	public Propiedad getNuevaPropiedad() {
		return nuevaPropiedad;
	}

	public void setNuevaPropiedad(Propiedad nuevaPropiedad) {
		this.nuevaPropiedad = nuevaPropiedad;
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
