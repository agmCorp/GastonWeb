package uy.com.agm.gaston.soporte.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import uy.com.agm.gaston.modelo.NucleoFamiliar;

public class Reporte {
	private NucleoFamiliar nucleoFamiliar;
	private Date fechaIni;
	private Date fechaFin;
	private List<ReporteLinea> reporteLineas;
	private BigDecimal totalGastos;
	private BigDecimal totalIngresos;
	private BigDecimal saldo;

	public NucleoFamiliar getNucleoFamiliar() {
		return nucleoFamiliar;
	}

	public void setNucleoFamiliar(NucleoFamiliar nucleoFamiliar) {
		this.nucleoFamiliar = nucleoFamiliar;
	}

	public Date getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public List<ReporteLinea> getReporteLineas() {
		return reporteLineas;
	}

	public void setReporteLineas(List<ReporteLinea> reporteLineas) {
		this.reporteLineas = reporteLineas;
	}

	public BigDecimal getTotalGastos() {
		return totalGastos;
	}

	public void setTotalGastos(BigDecimal totalGastos) {
		this.totalGastos = totalGastos;
	}

	public BigDecimal getTotalIngresos() {
		return totalIngresos;
	}

	public void setTotalIngresos(BigDecimal totalIngresos) {
		this.totalIngresos = totalIngresos;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

}