package uy.com.agm.gaston.negocio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import uy.com.agm.gaston.modelo.PreguntaSeguridad;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IPreguntaSeguridadServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IPreguntaSeguridadServicesRemote;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IPreguntaSeguridadDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PreguntaSeguridadServices implements IPreguntaSeguridadServicesRemote, IPreguntaSeguridadServicesLocal {
	@EJB
	private IPreguntaSeguridadDao preguntaSeguridadDao;
	private static final String ERROR_MSG = "Error en negocio " + PreguntaSeguridadServices.class.getName();

	@Override
	public List<PreguntaSeguridad> encontrarTodos() throws NegocioException {
		List<PreguntaSeguridad> result = null;
		;
		try {
			result = preguntaSeguridadDao.encontrarTodos();
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public PreguntaSeguridad encontrar(Integer idPreguntaSeguridad) throws NegocioException {
		PreguntaSeguridad result = null;
		;
		try {
			result = preguntaSeguridadDao.encontrar(idPreguntaSeguridad);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public PreguntaSeguridad nuevaPreguntaSeguridad(PreguntaSeguridad preguntaSeguridad) throws NegocioException {
		PreguntaSeguridad result = null;
		try {
			result = preguntaSeguridadDao.crear(preguntaSeguridad);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public void eliminar(PreguntaSeguridad preguntaSeguridad) throws NegocioException {
		try {
			preguntaSeguridadDao.eliminar(preguntaSeguridad);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public void guardar(PreguntaSeguridad preguntaSeguridad) throws NegocioException {
		try {
			preguntaSeguridadDao.guardar(preguntaSeguridad);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}
}
