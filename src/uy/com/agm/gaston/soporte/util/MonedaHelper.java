package uy.com.agm.gaston.soporte.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import uy.com.agm.gaston.modelo.Moneda;

public class MonedaHelper {
	public static BigDecimal convertirMontoAMonedaDestino(BigDecimal montoMonedaOrigen, Moneda monedaOrigen,
			Moneda monedaDestino) {
		BigDecimal result;

		if (monedaOrigen.getId() != monedaDestino.getId()) {
			result = montoMonedaOrigen.divide(monedaOrigen.getArbitraje(), 2, RoundingMode.HALF_UP);
			result = result.multiply(monedaDestino.getArbitraje());
		} else {
			result = montoMonedaOrigen;
		}
		return result;
	}

	public static String formatBigDecimal(BigDecimal bd) {
		String result = "0.00";
		if (bd != null) {
			if (bd.compareTo(BigDecimal.ZERO) != 0) {
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				df.setMinimumFractionDigits(2);
				df.setGroupingUsed(true);
				result = df.format(bd);
			}
		}
		return result;
	}
}
