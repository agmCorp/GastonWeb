package uy.com.agm.gaston.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.Gasto;
import uy.com.agm.gaston.modelo.Ingreso;
import uy.com.agm.gaston.modelo.Moneda;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IGastoServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IIngresoServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesLocal;
import uy.com.agm.gaston.soporte.util.AppHelper;
import uy.com.agm.gaston.soporte.util.DateHelper;
import uy.com.agm.gaston.soporte.util.MonedaHelper;
import uy.com.agm.gaston.soporte.util.Reporte;
import uy.com.agm.gaston.soporte.util.ReporteLinea;

@ManagedBean
@ViewScoped
public class ReporteBean implements Serializable {
	@EJB
	private IGastoServicesLocal gs;
	@EJB
	private IIngresoServicesLocal is;
	@EJB
	private IUsuarioServicesLocal us;
	@Inject
	private Logger logger;
	// Inyección
	@ManagedProperty(value = "#{nucleoFamiliarBean.nucleoFamiliar}")
	private NucleoFamiliar nucleoFamiliarSeleccionado;
	private Reporte reporte;
	private static final long serialVersionUID = 1L;

	@PostConstruct
	private void init() {
		initNuevoReporte();
	}

	private void initNuevoReporte() {
		reporte = new Reporte();
		reporte.setFechaIni(DateHelper.getHoy());
		reporte.setFechaFin(DateHelper.getHoy());
		reporte.setNucleoFamiliar(nucleoFamiliarSeleccionado);
	}

	public String nombreCompletoResponsable(Usuario responsable) {
		return us.getNombreCompleto(responsable.getNombre(), responsable.getApellido());
	}

	public void generarReporteDeGastos() {
		Date fechaIni, fechaFin;
		List<Gasto> gastos;
		List<Ingreso> ingresos;
		Moneda nucleoFamiliarMoneda;
		int cantGastos, cantIngresos, max, i;
		BigDecimal totalGastos, totalIngresos;
		Gasto gasto;
		Ingreso ingreso;
		ReporteLinea reporteLinea;
		List<ReporteLinea> reporteLineas;

		try {
			fechaIni = DateHelper.getYesterday(reporte.getFechaIni());
			fechaFin = reporte.getFechaFin();
			gastos = gs.encontrarTodos(nucleoFamiliarSeleccionado.getId(), fechaIni, fechaFin);
			ingresos = is.encontrarTodos(nucleoFamiliarSeleccionado.getId(), fechaIni, fechaFin);
			nucleoFamiliarMoneda = nucleoFamiliarSeleccionado.getMoneda();

			cantGastos = gastos.size();
			cantIngresos = ingresos.size();
			max = (cantGastos > cantIngresos) ? cantGastos : cantIngresos;
			totalGastos = BigDecimal.ZERO;
			totalIngresos = BigDecimal.ZERO;
			reporteLineas = new ArrayList<>();
			for (i = 0; i < max; i++) {
				if (i < cantGastos) {
					gasto = gastos.get(i);
					totalGastos = totalGastos.add(MonedaHelper.convertirMontoAMonedaDestino(gasto.getMonto(),
							gasto.getMoneda(), nucleoFamiliarMoneda));
				} else {
					gasto = null;
				}
				if (i < cantIngresos) {
					ingreso = ingresos.get(i);
					totalIngresos = totalIngresos.add(MonedaHelper.convertirMontoAMonedaDestino(ingreso.getMonto(),
							ingreso.getMoneda(), nucleoFamiliarMoneda));
				} else {
					ingreso = null;
				}
				reporteLinea = new ReporteLinea();
				reporteLinea.setGasto(gasto);
				reporteLinea.setIngreso(ingreso);
				reporteLineas.add(reporteLinea);
			}
			reporte.setReporteLineas(reporteLineas);
			reporte.setTotalGastos(totalGastos);
			reporte.setTotalIngresos(totalIngresos);
			reporte.setSaldo(totalIngresos.subtract(totalGastos));
		} catch (NegocioException ne) {
			logger.error("Error en método obtenerReporteDeGastos", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public String formatBigDecimal(BigDecimal bd) {
		return MonedaHelper.formatBigDecimal(bd);
	}

	public Reporte getReporte() {
		return reporte;
	}

	public void setReporte(Reporte reporte) {
		this.reporte = reporte;
	}

	public NucleoFamiliar getNucleoFamiliar() {
		return nucleoFamiliarSeleccionado;
	}

	public NucleoFamiliar getNucleoFamiliarSeleccionado() {
		return nucleoFamiliarSeleccionado;
	}

	public void setNucleoFamiliarSeleccionado(NucleoFamiliar nucleoFamiliarSeleccionado) {
		this.nucleoFamiliarSeleccionado = nucleoFamiliarSeleccionado;
	}
}
