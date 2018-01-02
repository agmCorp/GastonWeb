package uy.com.agm.gaston.negocio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import uy.com.agm.gaston.modelo.ClasificacionIngreso;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IClasificacionIngresoServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IClasificacionIngresoServicesRemote;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IClasificacionIngresoDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ClasificacionIngresoServices
		implements IClasificacionIngresoServicesRemote, IClasificacionIngresoServicesLocal {
	@EJB
	private IClasificacionIngresoDao clasificacionIngresoDao;
	private static final String ERROR_MSG = "Error en negocio " + ClasificacionIngresoServices.class.getName();

	@Override
	public ClasificacionIngreso encontrar(Integer idClasificacionIngreso) throws NegocioException {
		ClasificacionIngreso result = null;
		try {
			result = clasificacionIngresoDao.encontrar(idClasificacionIngreso);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public List<ClasificacionIngreso> encontrarTodos() throws NegocioException {
		List<ClasificacionIngreso> result = null;
		;
		try {
			result = clasificacionIngresoDao.encontrarTodos();
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public ClasificacionIngreso nuevaClasificacionIngreso(ClasificacionIngreso clasificacionIngreso)
			throws NegocioException {
		ClasificacionIngreso result = null;
		try {
			result = clasificacionIngresoDao.crear(clasificacionIngreso);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public void eliminar(ClasificacionIngreso clasificacionIngreso) throws NegocioException {
		try {
			clasificacionIngresoDao.eliminar(clasificacionIngreso);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public void guardar(ClasificacionIngreso clasificacionIngreso) throws NegocioException {
		try {
			clasificacionIngresoDao.guardar(clasificacionIngreso);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}
}