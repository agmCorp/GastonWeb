package uy.com.agm.gaston.negocio.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import uy.com.agm.gaston.modelo.Propiedad;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IPropiedadServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IPropiedadServicesRemote;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IPropiedadDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PropiedadServices implements IPropiedadServicesRemote, IPropiedadServicesLocal {
	@EJB
	private IPropiedadDao propiedadDao;
	private static final String ERROR_MSG = "Error en negocio " + PropiedadServices.class.getName();

	@Override
	public Propiedad encontrar(Integer idPropiedad) throws NegocioException {
		Propiedad result = null;
		;
		try {
			result = propiedadDao.encontrar(idPropiedad);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public List<Propiedad> encontrarTodos() throws NegocioException {
		List<Propiedad> result = null;
		;
		try {
			result = propiedadDao.encontrarTodos();
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public Propiedad nuevaPropiedad(Propiedad propiedad) throws NegocioException {
		Propiedad result = null;
		try {
			result = propiedadDao.crear(propiedad);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public void eliminar(Propiedad propiedad) throws NegocioException {
		try {
			propiedadDao.eliminar(propiedad);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public void guardar(Propiedad propiedad) throws NegocioException {
		try {
			propiedadDao.guardar(propiedad);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public Propiedad encontrar(String clave) throws NegocioException {
		Propiedad result = null;
		try {
			result = propiedadDao.encontrar(clave);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}
}
