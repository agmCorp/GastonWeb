package uy.com.agm.gaston.negocio.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import uy.com.agm.gaston.modelo.Cierre;
import uy.com.agm.gaston.modelo.Gasto;
import uy.com.agm.gaston.modelo.Ingreso;
import uy.com.agm.gaston.modelo.Moneda;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.ICierreServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.ICierreServicesRemote;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.ICierreDao;
import uy.com.agm.gaston.persistencia.interfaces.IGastoDao;
import uy.com.agm.gaston.persistencia.interfaces.IIngresoDao;
import uy.com.agm.gaston.persistencia.interfaces.IMonedaDao;
import uy.com.agm.gaston.persistencia.interfaces.INucleoFamiliarDao;
import uy.com.agm.gaston.soporte.util.DateHelper;
import uy.com.agm.gaston.soporte.util.MonedaHelper;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CierreServices implements ICierreServicesRemote, ICierreServicesLocal {
	@EJB
	private INucleoFamiliarDao nucleoFamiliarDao;
	@EJB
	private IGastoDao gastoDao;
	@EJB
	private IIngresoDao ingresoDao;
	@EJB
	private ICierreDao cierreDao;
	@EJB
	private IMonedaDao monedaDao;

	private static final String ERROR_MSG = "Error en negocio " + CierreServices.class.getName();

	@Override
	public Cierre nuevoCierre(NucleoFamiliar nucleoFamiliar) throws NegocioException {
		Cierre result = null;
		try {
			Date fechaIni = nucleoFamiliar.getFechaUltimoCierre();
			// Si el núcleo familiar no tiene fecha de último cierre,
			// se toma como fecha de último cierre el día anterior a su fecha de
			// creación.
			if (fechaIni == null) {
				fechaIni = DateHelper.getYesterday(nucleoFamiliar.getFechaCreacion());
			}
			Date fechaFin = DateHelper.getHoy();
			Integer idNucleoFamiliar = nucleoFamiliar.getId();

			List<Gasto> gastos = gastoDao.encontrarTodos(idNucleoFamiliar, fechaIni, fechaFin);
			List<Ingreso> ingresos = ingresoDao.encontrarTodos(idNucleoFamiliar, fechaIni, fechaFin);

			Cierre cierre = new Cierre();
			cierre.setNucleoFamiliar(nucleoFamiliar);
			cierre.setFecha(fechaFin);
			cierreDao.crear(cierre);
			Moneda nucleoFamiliarMoneda = nucleoFamiliar.getMoneda();

			BigDecimal totalGastos = BigDecimal.ZERO;
			for (Gasto gasto : gastos) {
				gasto.setCierre(cierre);
				totalGastos = totalGastos.add(MonedaHelper.convertirMontoAMonedaDestino(gasto.getMonto(),
						gasto.getMoneda(), nucleoFamiliarMoneda));
				gastoDao.guardar(gasto);
			}
			BigDecimal totalIngresos = BigDecimal.ZERO;
			for (Ingreso ingreso : ingresos) {
				ingreso.setCierre(cierre);
				totalIngresos = totalIngresos.add(MonedaHelper.convertirMontoAMonedaDestino(ingreso.getMonto(),
						ingreso.getMoneda(), nucleoFamiliarMoneda));
				ingresoDao.guardar(ingreso);
			}

			nucleoFamiliar.setFechaUltimoCierre(fechaFin);
			nucleoFamiliarDao.guardar(nucleoFamiliar);

			cierre.setTotalEgresos(totalGastos);
			cierre.setTotalIngresos(totalIngresos);
			cierre.setSaldo(totalIngresos.subtract(totalGastos));
			result = cierreDao.guardar(cierre);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public List<Cierre> obtenerCierres(NucleoFamiliar nucleoFamiliar) throws NegocioException {
		List<Cierre> result = null;
		try {
			result = cierreDao.encontrarTodos(nucleoFamiliar.getId());
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}
}