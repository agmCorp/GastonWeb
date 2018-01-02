package uy.com.agm.gaston.bean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.Cierre;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.ICierreServicesLocal;
import uy.com.agm.gaston.soporte.util.AppHelper;

@ManagedBean
@SessionScoped
public class CierreBean implements Serializable {
	@EJB
	private ICierreServicesLocal cs;
	@Inject
	private Logger logger;

	private static final long serialVersionUID = 1L;

	private NucleoFamiliar nucleoFamiliarSeleccionado;
	private Cierre cierreSeleccionado;

	// Filtros de la grilla
	private List<Cierre> filtroCierres;

	public void resetDataTable() throws IOException {
		// Se eliminan los filtros (bug de PF)
		filtroCierres = null;
	}

	public String visualizarCierres(NucleoFamiliar nucleoFamiliar) {
		this.nucleoFamiliarSeleccionado = nucleoFamiliar;
		return "VISUALIZAR";
	}

	public List<Cierre> getCierres() {
		List<Cierre> result = null;
		try {
			result = cs.obtenerCierres(nucleoFamiliarSeleccionado);
		} catch (NegocioException ne) {
			logger.error("Error en método getCierres", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public String visualizarCierre(Cierre cierre) {
		cierreSeleccionado = cierre;
		return "VISUALIZAR";
	}

	public NucleoFamiliar getNucleoFamiliar() {
		return nucleoFamiliarSeleccionado;
	}

	public Cierre getCierre() {
		return cierreSeleccionado;
	}

	public List<Cierre> getFiltroCierres() {
		return filtroCierres;
	}

	public void setFiltroCierres(List<Cierre> filtroCierres) {
		this.filtroCierres = filtroCierres;
	}

	public boolean filtrarPorImporte(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		if (value == null) {
			return false;
		}
		return ((Comparable<BigDecimal>) (BigDecimal) value).compareTo(new BigDecimal(filterText)) >= 0;
	}
}
