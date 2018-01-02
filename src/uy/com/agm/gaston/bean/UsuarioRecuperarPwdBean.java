package uy.com.agm.gaston.bean;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesLocal;
import uy.com.agm.gaston.soporte.util.AppHelper;

@ManagedBean
@RequestScoped
public class UsuarioRecuperarPwdBean implements Serializable {
	@EJB
	private IUsuarioServicesLocal us;
	// Inyección
	@ManagedProperty(value = "#{usuarioOlvidoPwdBean}")
	private UsuarioOlvidoPwdBean usuarioOlvidoPwd;
	@Inject
	private Logger logger;
	private static final long serialVersionUID = 1L;

	public String recuperarPwd() {
		String outcome = "";
		try {
			String email = usuarioOlvidoPwd.getUsuario().getEmail();
			usuarioOlvidoPwd.setUsuario(us.encontrar(email));

			if (usuarioOlvidoPwd.getUsuario() != null) {
				if (usuarioOlvidoPwd.getUsuario().getActivo()) {
					outcome = "PREGUNTA_SEGURIDAD";
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Recuperacion de contraseña", "El usuario '" + email
									+ "' no está activo. Si no ha recibido un correo electrónico para su activación puede solicitar nuevamente su envío desde la pantalla de login."));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Usuario inexistente", "No existe el usuario '" + email
								+ "' registrado en el sistema. Por favor verifique su dirección de correo electrónico."));
			}
		} catch (NegocioException ne) {
			logger.error("Error en método recuperarPwd", ne);
			AppHelper.errorNegocioMensaje();
		}
		return outcome;
	}

	public UsuarioOlvidoPwdBean getUsuarioOlvidoPwd() {
		return usuarioOlvidoPwd;
	}

	public void setUsuarioOlvidoPwd(UsuarioOlvidoPwdBean usuarioOlvidoPwd) {
		this.usuarioOlvidoPwd = usuarioOlvidoPwd;
	}
}
