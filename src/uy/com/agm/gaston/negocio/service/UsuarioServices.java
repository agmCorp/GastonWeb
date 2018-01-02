package uy.com.agm.gaston.negocio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.RandomStringUtils;

import uy.com.agm.gaston.modelo.GrupoJaas;
import uy.com.agm.gaston.modelo.RegistroUsuarioPendiente;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesRemote;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IGrupoJaasDao;
import uy.com.agm.gaston.persistencia.interfaces.IRegistroUsuarioPendienteDao;
import uy.com.agm.gaston.persistencia.interfaces.IUsuarioDao;
import uy.com.agm.gaston.soporte.util.DateHelper;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UsuarioServices implements IUsuarioServicesRemote, IUsuarioServicesLocal {
	@EJB
	private IUsuarioDao usuarioDao;
	@EJB
	private IGrupoJaasDao grupoJaasDao;
	@EJB
	private IRegistroUsuarioPendienteDao registroUsuarioPendienteDao;

	private static final String ERROR_MSG = "Error en negocio " + UsuarioServices.class.getName();
	private static final Integer RANDOM_PASSWORD_LENGTH = 10;

	@Override
	public String getNombreCompleto(String email) throws NegocioException {
		String result = null;
		try {
			Usuario usuario = usuarioDao.encontrar(email);
			result = getNombreCompleto(usuario.getNombre(), usuario.getApellido());
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public String getNombreCompleto(String nombre, String apellido) {
		return nombre + " " + apellido;
	}

	@Override
	public void guardar(Usuario usuario) throws NegocioException {
		try {
			usuarioDao.guardar(usuario);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public Usuario encontrar(String email) throws NegocioException {
		Usuario result = null;
		try {
			result = usuarioDao.encontrar(email);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public List<GrupoJaas> obtenerGruposJaas(String email) throws NegocioException {
		List<GrupoJaas> result = null;
		try {
			result = usuarioDao.obtenerGruposJaas(email);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public void registrarUsuario(Usuario usuario, List<String> roles, String idActivacion) throws NegocioException {
		try {
			GrupoJaas grupoJaas;
			ArrayList<GrupoJaas> gruposJaas = new ArrayList<>();

			usuario = usuarioDao.crear(usuario);
			for (String rol : roles) {
				grupoJaas = grupoJaasDao.encontrar(rol);
				gruposJaas.add(grupoJaas);
				grupoJaasDao.agregarIntegrante(grupoJaas.getId(), usuario);
			}
			usuario.setGruposJaas(gruposJaas);
			usuario = usuarioDao.guardar(usuario);

			RegistroUsuarioPendiente registroUsuarioPendiente = new RegistroUsuarioPendiente();
			registroUsuarioPendiente.setTimestamp(DateHelper.getHoy());
			registroUsuarioPendiente.setIdActivacion(idActivacion);
			registroUsuarioPendiente.setUsuario(usuario);
			registroUsuarioPendienteDao.crear(registroUsuarioPendiente);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Override
	public String getRandomPassword() {
		StringBuilder pwd;
		Random random;
		int i;

		// La contraseña debe contener al menos un dígito, al menos una letra
		// mayúscula, al menos una letra minúscula, al menos un símbolo especial
		// (@#$%^&+=) y debe ser al menos de 8 caracteres de largo.

		// Se genera una contraseña alfanumérica genérica.
		pwd = new StringBuilder(RandomStringUtils.randomAlphanumeric(RANDOM_PASSWORD_LENGTH));

		// Se sustituyen aleatoriamente las condiciones mínimas antes
		// descriptas.
		random = new Random();
		i = random.nextInt(RANDOM_PASSWORD_LENGTH);
		pwd.setCharAt(i, RandomStringUtils.random(1, "0123456789").charAt(0));
		i = random.nextInt(RANDOM_PASSWORD_LENGTH);
		pwd.setCharAt(i, RandomStringUtils.random(1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ").charAt(0));
		i = random.nextInt(RANDOM_PASSWORD_LENGTH);
		pwd.setCharAt(i, RandomStringUtils.random(1, "abcdefghijklmnopqrstuvwxyz").charAt(0));
		i = random.nextInt(RANDOM_PASSWORD_LENGTH);
		pwd.setCharAt(i, RandomStringUtils.random(1, "@#$%^&+=").charAt(0));

		return pwd.toString();
	}

	@Override
	public Usuario activarUsuario(String idActivacion) throws NegocioException {
		Usuario result = null;
		try {
			RegistroUsuarioPendiente registroUsuarioPendiente = registroUsuarioPendienteDao.encontrar(idActivacion);
			if (registroUsuarioPendiente != null) {
				Usuario usuario = registroUsuarioPendiente.getUsuario();
				usuario.setActivo(true);
				registroUsuarioPendienteDao.eliminar(registroUsuarioPendiente);
				result = usuarioDao.guardar(usuario);
			}
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public List<Usuario> obtenerUsuarios() throws NegocioException {
		List<Usuario> result = null;
		try {
			result = usuarioDao.encontrarTodos();
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}
}
