package uy.com.agm.gaston.bean;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.PreguntaSeguridad;
import uy.com.agm.gaston.modelo.Propiedad;
import uy.com.agm.gaston.modelo.RegistroUsuarioPendiente;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IMailServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IPreguntaSeguridadServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IPropiedadServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IRegistroUsuarioPendienteServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesLocal;
import uy.com.agm.gaston.soporte.util.AppHelper;
import uy.com.agm.gaston.soporte.util.MailSimple;

@ManagedBean
@RequestScoped
public class UsuarioRegistrarBean implements Serializable {
	@EJB
	private IUsuarioServicesLocal us;
	@EJB
	private IMailServicesLocal ms;
	@EJB
	private IRegistroUsuarioPendienteServicesLocal rs;
	@EJB
	private IPreguntaSeguridadServicesLocal ps;
	@EJB
	private IPropiedadServicesLocal prs;
	@Inject
	private Logger logger;

	private static final long serialVersionUID = 1L;
	private static final String ROL_USUARIO = "USUARIO";
	private static final String APP_URL_DEFAULT = "http://www.gaston.com";
	private static final String KEY_MOSTRAR_CAPTCHA = "mostrarCaptcha";

	private List<PreguntaSeguridad> preguntas;
	private List<String> rolesSeleccionados;

	private Usuario nuevoUsuario;
	private String confPwd;
	private String idActivacionURL;
	private Boolean mostrarCaptcha;

	@PostConstruct
	private void init() {
		Propiedad propiedad;

		try {
			preguntas = ps.encontrarTodos();
			rolesSeleccionados = new ArrayList<>();
			rolesSeleccionados.add(ROL_USUARIO);
			propiedad = prs.encontrar(KEY_MOSTRAR_CAPTCHA);
			mostrarCaptcha = (propiedad != null) ? propiedad.getValor().toLowerCase().equals("true") : false;
			initNuevoUsuario();
		} catch (NegocioException ne) {
			logger.error("Error en método init", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	private void initNuevoUsuario() {
		nuevoUsuario = new Usuario();
		nuevoUsuario.setPreguntaSeguridad(new PreguntaSeguridad());
		nuevoUsuario.setActivo(false);
		nuevoUsuario.setDebeCambiarPassword(false);
	}

	private String getMensajeMailActivacion(String nombreCompletoUsuario, String emailUsuario, String idActivacion) {
		String absoluteApplicationUrl = APP_URL_DEFAULT;
		try {
			absoluteApplicationUrl = AppHelper.getAbsoluteApplicationUrl();
		} catch (URISyntaxException ue) {
			logger.error("Error en método getMensajeMailActivacion", ue);
		}
		String urlActivacion = absoluteApplicationUrl + "/public/UsuarioActivar.xhtml?id=" + idActivacion;
		String urlRecuperarPwd = absoluteApplicationUrl + "/public/UsuarioRecuperarPwd.xhtml";

		String body = "Bienvenido(a) " + nombreCompletoUsuario + " a GasTON Web.";
		body += "<br/>Su cuenta es la siguiente: ";
		body += "<font color='blue'>" + emailUsuario + "</font>";
		body += "<br/>";
		body += "<br/>Por favor, visite el siguiente vínculo para activar su cuenta: ";
		body += "<a href='" + urlActivacion + "'><font color='blue'>" + urlActivacion + "</font></a>";
		body += "<br/>";
		body += "<br/>Su contaseña ha sido almacenada internamente en nuestro sistema y no puede ser recuperada.";
		body += "<br/>En caso de olvidar su contraseña puede obtener una nueva ingresando al siguiente vínculo: ";
		body += "<a href='" + urlRecuperarPwd + "'><font color='blue'>" + urlRecuperarPwd + "</font></a>";
		body += "<br/>";
		body += "<br/>Muchas gracias por registrarse.";
		body += "<br/>";
		return body;
	}

	private void enviarMailActivacion(String nombreCompletoUsuario, String emailUsuario, String idActivacion) {
		try {
			MailSimple mail = new MailSimple();
			mail.setPara(emailUsuario);
			mail.setAsunto("Activación de usuario");
			mail.setMensaje(getMensajeMailActivacion(nombreCompletoUsuario, emailUsuario, idActivacion));
			ms.enviarMail(mail);
		} catch (NegocioException ne) {
			logger.error("Error en método enviarMailActivacion", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public Boolean mostrarCaptcha() {
		return mostrarCaptcha;
	}

	public void registrarUsuario() {
		try {
			if (!nuevoUsuario.getPassword().equals(confPwd)) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Las contraseñas no coinciden."));
			} else {
				String email = nuevoUsuario.getEmail();
				Usuario usuarioYaExistente = us.encontrar(email);
				if (usuarioYaExistente != null) {
					if (usuarioYaExistente.getActivo()) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Usuario existente", "Ya existe el usuario '" + email + "' registrado en el sistema."));
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Usuario inactivo", "Ya existe el usuario '" + email
										+ "' registrado en el sistema y aún no ha sido activado. Para recuperar el mail de activación seleccione la opción correspondiente en la pantalla de login."));
					}
				} else {
					String idActivacion = AppHelper.getUniqueId();
					enviarMailActivacion(us.getNombreCompleto(nuevoUsuario.getNombre(), nuevoUsuario.getApellido()),
							email, idActivacion);
					us.registrarUsuario(nuevoUsuario, rolesSeleccionados, idActivacion);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Nuevo Usuario",
							"Se ha enviado un mail a su casilla de correo '" + email + "' para activar su usuario."));
					initNuevoUsuario();
				}
			}
		} catch (NegocioException ne) {
			logger.error("Error en método registrarUsuario", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public void recuperarRegistroActivacion() {
		try {
			String email = nuevoUsuario.getEmail();
			Usuario usuarioRecuperarRegAct = us.encontrar(email);

			if (usuarioRecuperarRegAct != null) {
				if (!usuarioRecuperarRegAct.getActivo()) {
					RegistroUsuarioPendiente registroUsuarioPendiente = rs.encontrar(usuarioRecuperarRegAct.getId());
					enviarMailActivacion(
							us.getNombreCompleto(usuarioRecuperarRegAct.getNombre(),
									usuarioRecuperarRegAct.getApellido()),
							email, registroUsuarioPendiente.getIdActivacion());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Registro de activación",
							"Se ha enviado un mail a su casilla de correo '" + email + "' para activar su usuario."));
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registro de activación",
									"El usuario ya se encuentra activo, solicite una nueva contraseña."));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registro de activación",
								"No se encotró información de activación para el usuario '" + email
										+ "'. Por favor regístrese nuevamente en el sistema."));
			}
			initNuevoUsuario();
		} catch (NegocioException ne) {
			logger.error("Error en método recuperarRegistroActivacion", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public void activarUsuario() {
		try {
			nuevoUsuario = us.activarUsuario(idActivacionURL);
			if (nuevoUsuario == null) {
				FacesContext context = FacesContext.getCurrentInstance();
				ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) context
						.getApplication().getNavigationHandler();
				navigationHandler.handleNavigation(context, "", "LOGOUT");
			}
		} catch (NegocioException ne) {
			logger.error("Error en método activarUsuario", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public String getNombreCompleto() {
		return us.getNombreCompleto(nuevoUsuario.getNombre(), nuevoUsuario.getApellido());
	}

	public String getConfPwd() {
		return confPwd;
	}

	public void setConfPwd(String confPwd) {
		this.confPwd = confPwd;
	}

	public List<String> getRolesSeleccionados() {
		return rolesSeleccionados;
	}

	public void setRolesSeleccionados(List<String> rolesSeleccionados) {
		this.rolesSeleccionados = rolesSeleccionados;
	}

	public Usuario getNuevoUsuario() {
		return nuevoUsuario;
	}

	public void setNuevoUsuario(Usuario nuevoUsuario) {
		this.nuevoUsuario = nuevoUsuario;
	}

	public String getIdActivacionURL() {
		return idActivacionURL;
	}

	public void setIdActivacionURL(String idActivacionURL) {
		this.idActivacionURL = idActivacionURL;
	}

	public List<PreguntaSeguridad> getPreguntas() {
		return preguntas;
	}

	public void setPreguntas(List<PreguntaSeguridad> preguntas) {
		this.preguntas = preguntas;
	}
}
