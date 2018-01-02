package uy.com.agm.gaston.negocio.service;

import java.util.ArrayList;
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
import uy.com.agm.gaston.modelo.Solicitud;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.INucleoFamiliarServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.INucleoFamiliarServicesRemote;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.ICierreDao;
import uy.com.agm.gaston.persistencia.interfaces.IGastoDao;
import uy.com.agm.gaston.persistencia.interfaces.IIngresoDao;
import uy.com.agm.gaston.persistencia.interfaces.INucleoFamiliarDao;
import uy.com.agm.gaston.persistencia.interfaces.ISolicitudDao;
import uy.com.agm.gaston.persistencia.interfaces.IUsuarioDao;
import uy.com.agm.gaston.soporte.util.DateHelper;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class NucleoFamiliarServices implements INucleoFamiliarServicesRemote, INucleoFamiliarServicesLocal {
	@EJB
	private INucleoFamiliarDao nucleoFamiliarDao;
	@EJB
	private IUsuarioDao usuarioDao;
	@EJB
	private ICierreDao cierreDao;
	@EJB
	private IGastoDao gastoDao;
	@EJB
	private IIngresoDao ingresoDao;
	@EJB
	private ISolicitudDao solicitudDao;

	private static final String ERROR_MSG = "Error en negocio " + NucleoFamiliarServices.class.getName();

	@Override
	public NucleoFamiliar nuevoNucleoFamiliar(String nombreNucleoFamiliar, Moneda moneda, String emailAdministrador)
			throws NegocioException {
		NucleoFamiliar result = null;
		try {
			NucleoFamiliar nucleoFamiliar = new NucleoFamiliar();
			nucleoFamiliar.setNombre(nombreNucleoFamiliar);
			nucleoFamiliar.setMoneda(moneda);
			nucleoFamiliar.setFechaCreacion(DateHelper.getHoy());
			Usuario administrador = usuarioDao.encontrar(emailAdministrador);
			nucleoFamiliar.setAdministrador(administrador);
			List<Usuario> integrantes = new ArrayList<>();
			integrantes.add(administrador);
			nucleoFamiliar.setIntegrantes(integrantes);
			result = nucleoFamiliarDao.crear(nucleoFamiliar);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public void eliminar(NucleoFamiliar nucleoFamiliar) throws NegocioException {
		try {
			Integer idNucleoFamiliar = nucleoFamiliar.getId();

			List<Gasto> gastos = gastoDao.encontrarTodos(idNucleoFamiliar);
			for (Gasto gasto : gastos) {
				gastoDao.eliminar(gasto);
			}

			List<Ingreso> ingresos = ingresoDao.encontrarTodos(idNucleoFamiliar);
			for (Ingreso ingreso : ingresos) {
				ingresoDao.eliminar(ingreso);
			}

			List<Cierre> cierres = cierreDao.encontrarTodos(idNucleoFamiliar);
			for (Cierre cierre : cierres) {
				cierreDao.eliminar(cierre);
			}

			List<Solicitud> solicitudes = solicitudDao.encontrarTodos(idNucleoFamiliar);
			for (Solicitud solicitud : solicitudes) {
				solicitudDao.eliminar(solicitud);
			}

			nucleoFamiliarDao.eliminar(nucleoFamiliar);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public List<NucleoFamiliar> obtenerNucleosFamiliares() throws NegocioException {
		List<NucleoFamiliar> result = null;
		try {
			result = nucleoFamiliarDao.encontrarTodos();
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public List<NucleoFamiliar> obtenerNucleosFamiliaresDeIntegrante(String emailIntegrante) throws NegocioException {
		List<NucleoFamiliar> result = null;
		try {
			result = nucleoFamiliarDao.obtenerNucleosFamiliaresDeIntegrante(emailIntegrante);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public List<Usuario> obtenerIntegrantes(Integer idNucleoFamiliar) throws NegocioException {
		List<Usuario> result = null;
		try {
			result = nucleoFamiliarDao.obtenerIntegrantes(idNucleoFamiliar);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public void agregarIntegrante(Integer idNucleoFamiliar, Usuario usuario) throws NegocioException {
		try {
			nucleoFamiliarDao.agregarIntegrante(idNucleoFamiliar, usuario);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public void eliminarIntegrante(Integer idNucleoFamiliar, Usuario integrante) throws NegocioException {
		try {
			nucleoFamiliarDao.eliminarIntegrante(idNucleoFamiliar, integrante);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public NucleoFamiliar encontrar(Integer idNucleoFamiliar) throws NegocioException {
		NucleoFamiliar result = null;
		try {
			result = nucleoFamiliarDao.encontrar(idNucleoFamiliar);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public List<NucleoFamiliar> obtenerNucleosFamiliaresNoAdministrados(String emailAdministrador)
			throws NegocioException {
		List<NucleoFamiliar> result = null;
		try {
			result = nucleoFamiliarDao.obtenerNucleosFamiliaresNoAdministrados(emailAdministrador);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public List<NucleoFamiliar> obtenerNucleosFamiliaresAdministrados(String emailAdministrador)
			throws NegocioException {
		List<NucleoFamiliar> result = null;
		try {
			result = nucleoFamiliarDao.obtenerNucleosFamiliaresAdministrados(emailAdministrador);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

}