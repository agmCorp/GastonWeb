package uy.com.agm.gaston.negocio.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import uy.com.agm.gaston.modelo.RegistroUsuarioPendiente;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IRegistroUsuarioPendienteServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IRegistroUsuarioPendienteServicesRemote;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IRegistroUsuarioPendienteDao;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RegistroUsuarioPendienteServices
		implements IRegistroUsuarioPendienteServicesRemote, IRegistroUsuarioPendienteServicesLocal {
	@EJB
	private IRegistroUsuarioPendienteDao registroUsuarioPendienteDao;
	private static final String ERROR_MSG = "Error en negocio " + RegistroUsuarioPendienteServices.class.getName();

	@Override
	public RegistroUsuarioPendiente encontrar(Integer idUsuario) throws NegocioException {
		RegistroUsuarioPendiente result = null;
		try {
			result = registroUsuarioPendienteDao.encontrarRegistroDeUsuario(idUsuario);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public RegistroUsuarioPendiente encontrar(String idActivacion) throws NegocioException {
		RegistroUsuarioPendiente result = null;
		try {
			result = registroUsuarioPendienteDao.encontrar(idActivacion);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}
}
