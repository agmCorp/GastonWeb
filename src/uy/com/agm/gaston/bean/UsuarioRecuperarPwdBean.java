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
	// Inyecci�n
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
							"Recuperacion de contrase�a", "El usuario '" + email
									+ "' no est� activo. Si no ha recibido un correo electr�nico para su activaci�n puede solicitar nuevamente su env�o desde la pantalla de login."));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Usuario inexistente", "No existe el usuario '" + email
								+ "' registrado en el sistema. Por favor verifique su direcci�n de correo electr�nico."));
			}
		} catch (NegocioException ne) {
			logger.error("Error en m�todo recuperarPwd", ne);
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
