package uy.com.agm.gaston.soporte.util;

import uy.com.agm.gaston.modelo.Gasto;
import uy.com.agm.gaston.modelo.Ingreso;

public class ReporteLinea {
	private Gasto gasto;
	private Ingreso ingreso;

	public Gasto getGasto() {
		return gasto;
	}

	public void setGasto(Gasto gasto) {
		this.gasto = gasto;
	}

	public Ingreso getIngreso() {
		return ingreso;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

}
