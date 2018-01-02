package uy.com.agm.gaston.negocio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import uy.com.agm.gaston.modelo.Moneda;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IMonedaServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IMonedaServicesRemote;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IMonedaDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MonedaServices implements IMonedaServicesRemote, IMonedaServicesLocal {
	@EJB
	private IMonedaDao monedaDao;
	private static final String ERROR_MSG = "Error en negocio " + MonedaServices.class.getName();

	@Override
	public Moneda encontrar(Integer idMoneda) throws NegocioException {
		Moneda result = null;
		;
		try {
			result = monedaDao.encontrar(idMoneda);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public List<Moneda> encontrarTodos() throws NegocioException {
		List<Moneda> result = null;
		;
		try {
			result = monedaDao.encontrarTodos();
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public Moneda nuevaMoneda(Moneda moneda) throws NegocioException {
		Moneda result = null;
		try {
			result = monedaDao.crear(moneda);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public void eliminar(Moneda moneda) throws NegocioException {
		try {
			monedaDao.eliminar(moneda);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public void guardar(Moneda moneda) throws NegocioException {
		try {
			monedaDao.guardar(moneda);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}
}
