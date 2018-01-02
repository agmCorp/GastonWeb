package uy.com.agm.gaston.negocio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import uy.com.agm.gaston.modelo.ClasificacionGasto;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IClasificacionGastoServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IClasificacionGastoServicesRemote;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IClasificacionGastoDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ClasificacionGastoServices implements IClasificacionGastoServicesRemote, IClasificacionGastoServicesLocal {
	@EJB
	private IClasificacionGastoDao clasificacionGastoDao;
	private static final String ERROR_MSG = "Error en negocio " + ClasificacionGastoServices.class.getName();

	@Override
	public ClasificacionGasto encontrar(Integer idClasificacionGasto) throws NegocioException {
		ClasificacionGasto result = null;
		try {
			result = clasificacionGastoDao.encontrar(idClasificacionGasto);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public List<ClasificacionGasto> encontrarTodos() throws NegocioException {
		List<ClasificacionGasto> result = null;
		;
		try {
			result = clasificacionGastoDao.encontrarTodos();
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public ClasificacionGasto nuevaClasificacionGasto(ClasificacionGasto clasificacionGasto) throws NegocioException {
		ClasificacionGasto result = null;
		try {
			result = clasificacionGastoDao.crear(clasificacionGasto);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public void eliminar(ClasificacionGasto clasificacionGasto) throws NegocioException {
		try {
			clasificacionGastoDao.eliminar(clasificacionGasto);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public void guardar(ClasificacionGasto clasificacionGasto) throws NegocioException {
		try {
			clasificacionGastoDao.guardar(clasificacionGasto);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}
}
