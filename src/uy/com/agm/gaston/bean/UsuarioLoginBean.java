package uy.com.agm.gaston.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.PreguntaSeguridad;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IPreguntaSeguridadServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesLocal;
import uy.com.agm.gaston.soporte.util.AppHelper;

@ManagedBean
@SessionScoped
public class UsuarioLoginBean implements Serializable {
	@EJB
	private IUsuarioServicesLocal us;
	@EJB
	private IPreguntaSeguridadServicesLocal ps;
	@Inject
	private Logger logger;

	private static final long serialVersionUID = 1L;

	private List<PreguntaSeguridad> preguntas;
	private Usuario usuarioSeleccionado;
	private String nombreCompleto;
	private String confPwd;

	@PostConstruct
	private void init() {
		try {
			String email = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
			usuarioSeleccionado = us.encontrar(email);
			this.nombreCompleto = us.getNombreCompleto(usuarioSeleccionado.getNombre(),
					usuarioSeleccionado.getApellido());
			usuarioSeleccionado.setGruposJaas(us.obtenerGruposJaas(email));
			preguntas = ps.encontrarTodos();
		} catch (NegocioException ne) {
			logger.error("Error en método init", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	/*
	 * Existe un bug en WildFly previsto de solucionar. Al cambiar la contraseña
	 * del usuario, aún se puede utilizar la contraseña anterior. Solo cuando el
	 * usuario utiliza la nueva contraseña por primera vez, la anterior se
	 * elimina. Este método vacía el cache.
	 */
	public void clearCache(String username) {
		try {
			ObjectName jaasMgr = new ObjectName("jboss.as:subsystem=security,security-domain=gaston");
			Object[] params = { username };
			String[] signature = { "java.lang.String" };
			MBeanServer server = MBeanServerFactory.findMBeanServer(null).get(0);
			server.invoke(jaasMgr, "flushCache", params, signature);
		} catch (Exception ex) {
		}
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		clearCache(usuarioSeleccionado.getEmail());
		return "LOGOUT";
	}

	public void determinarSeccion() throws IOException {
		if (usuarioSeleccionado.getGruposJaas().size() == 1) {
			FacesContext context = FacesContext.getCurrentInstance();
			ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) context.getApplication()
					.getNavigationHandler();
			navigationHandler.handleNavigation(context, "", usuarioSeleccionado.getGruposJaas().get(0).getNombre());
		}
	}

	public void actualizarDatosBasicos() {
		try {
			us.guardar(usuarioSeleccionado);
			nombreCompleto = us.getNombreCompleto(usuarioSeleccionado.getNombre(), usuarioSeleccionado.getApellido());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Perfil actualizado", "La información ha sido actualizada."));
		} catch (NegocioException ne) {
			logger.error("Error en método actualizarDatosBasicos", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public void actualizarPassword() {
		try {
			if (!usuarioSeleccionado.getPassword().equals(confPwd)) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Las contraseñas no coinciden."));
			} else {
				usuarioSeleccionado.setDebeCambiarPassword(false);
				us.guardar(usuarioSeleccionado);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Perfil actualizado", "La contraseña ha sido actualizada."));
			}
		} catch (NegocioException ne) {
			logger.error("Error en método actualizarPassword", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public String getConfPwd() {
		return confPwd;
	}

	public void setConfPwd(String confPwd) {
		this.confPwd = confPwd;
	}

	public Usuario getUsuario() {
		return usuarioSeleccionado;
	}

	public List<PreguntaSeguridad> getPreguntas() {
		return preguntas;
	}

	public void setPreguntas(List<PreguntaSeguridad> preguntas) {
		this.preguntas = preguntas;
	}
}