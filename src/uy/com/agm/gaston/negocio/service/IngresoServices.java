package uy.com.agm.gaston.negocio.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import uy.com.agm.gaston.modelo.Ingreso;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IIngresoServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IIngresoServicesRemote;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IIngresoDao;
import uy.com.agm.gaston.soporte.util.DateHelper;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class IngresoServices implements IIngresoServicesRemote, IIngresoServicesLocal {
	@EJB
	private IIngresoDao ingresoDao;
	private static final String ERROR_MSG = "Error en negocio " + IngresoServices.class.getName();

	@Override
	public List<Ingreso> obtenerIngresos(NucleoFamiliar nucleoFamiliar) throws NegocioException {
		List<Ingreso> result = null;
		try {
			result = ingresoDao.encontrarTodos(nucleoFamiliar.getId());
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public Ingreso nuevoIngreso(Ingreso ingreso) throws NegocioException {
		Ingreso result = null;
		try {
			Date fechaUltimoCierre = ingreso.getNucleoFamiliar().getFechaUltimoCierre();
			// Si el núcleo familiar no tiene fecha de último cierre,
			// se toma como fecha de último cierre el día anterior a su fecha de
			// creación.
			if (fechaUltimoCierre == null) {
				fechaUltimoCierre = DateHelper.getYesterday(ingreso.getNucleoFamiliar().getFechaCreacion());
			}
			Date fechaIngreso = ingreso.getFecha();
			if (fechaIngreso.after(fechaUltimoCierre)) {
				result = ingresoDao.crear(ingreso);
			} else {
				throw new NegocioException("No se puede crear el ingreso. La fecha del ingreso (" + fechaIngreso
						+ ") debe ser posterior a la fecha del último cierre de su núcleo familiar ("
						+ fechaUltimoCierre + ")");
			}
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public void eliminar(Ingreso ingreso) throws NegocioException {
		try {
			if (ingreso.getCierre() != null) {
				throw new NegocioException(
						"No se puede eliminar el ingreso. El ingreso no puede estar incluido en un cierre");
			} else {
				ingresoDao.eliminar(ingreso);
			}
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public void guardar(Ingreso ingreso) throws NegocioException {
		try {
			if (ingreso.getCierre() != null) {
				throw new NegocioException(
						"No se puede guardar el ingreso. El ingreso no puede estar incluido en un cierre");
			} else {
				ingresoDao.guardar(ingreso);
			}
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public List<Ingreso> encontrarTodos(Integer idNucleoFamiliar, Date fechaIni, Date fechaFin)
			throws NegocioException {
		List<Ingreso> result = null;
		try {
			result = ingresoDao.encontrarTodos(idNucleoFamiliar, fechaIni, fechaFin);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}
}