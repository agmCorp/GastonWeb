package uy.com.agm.gaston.negocio.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import uy.com.agm.gaston.modelo.Gasto;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IGastoServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IGastoServicesRemote;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IGastoDao;
import uy.com.agm.gaston.soporte.util.DateHelper;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class GastoServices implements IGastoServicesRemote, IGastoServicesLocal {
	@EJB
	private IGastoDao gastoDao;
	private static final String ERROR_MSG = "Error en negocio " + GastoServices.class.getName();

	@Override
	public List<Gasto> obtenerGastos(NucleoFamiliar nucleoFamiliar) throws NegocioException {
		List<Gasto> result = null;
		try {
			result = gastoDao.encontrarTodos(nucleoFamiliar.getId());
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public Gasto nuevoGasto(Gasto gasto) throws NegocioException {
		Gasto result = null;
		try {
			Date fechaUltimoCierre = gasto.getNucleoFamiliar().getFechaUltimoCierre();
			// Si el núcleo familiar no tiene fecha de último cierre,
			// se toma como fecha de último cierre el día anterior a su fecha de
			// creación.
			if (fechaUltimoCierre == null) {
				fechaUltimoCierre = DateHelper.getYesterday(gasto.getNucleoFamiliar().getFechaCreacion());
			}
			Date fechaGasto = gasto.getFecha();
			if (fechaGasto.after(fechaUltimoCierre)) {
				result = gastoDao.crear(gasto);
			} else {
				throw new NegocioException("No se puede crear el gasto. La fecha del gasto (" + fechaGasto
						+ ") debe ser posterior a la fecha del último cierre de su núcleo familiar ("
						+ fechaUltimoCierre + ")");
			}
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public void eliminar(Gasto gasto) throws NegocioException {
		try {
			if (gasto.getCierre() != null) {
				throw new NegocioException(
						"No se puede eliminar el gasto. El gasto no puede estar incluido en un cierre");
			} else {
				gastoDao.eliminar(gasto);
			}
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public void guardar(Gasto gasto) throws NegocioException {
		try {
			if (gasto.getCierre() != null) {
				throw new NegocioException(
						"No se puede guardar el gasto. El gasto no puede estar incluido en un cierre");
			} else {
				gastoDao.guardar(gasto);
			}
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public List<Gasto> encontrarTodos(Integer idNucleoFamiliar, Date fechaIni, Date fechaFin) throws NegocioException {
		List<Gasto> result = null;
		try {
			result = gastoDao.encontrarTodos(idNucleoFamiliar, fechaIni, fechaFin);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}
}