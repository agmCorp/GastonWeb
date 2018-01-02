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

import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IMailServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesLocal;
import uy.com.agm.gaston.soporte.util.AppHelper;
import uy.com.agm.gaston.soporte.util.MailSimple;

@ManagedBean
@RequestScoped
public class UsuarioPreguntaSeguridadBean implements Serializable {
	@EJB
	private IUsuarioServicesLocal us;
	@EJB
	private IMailServicesLocal ms;

	@Inject
	private Logger logger;
	// Inyección
	@ManagedProperty(value = "#{usuarioOlvidoPwdBean}")
	private UsuarioOlvidoPwdBean usuarioOlvidoPwd;

	private String respuesta;
	private static final long serialVersionUID = 1L;

	public void recuperarPwd() {
		Usuario usuarioRecuperarPwd;
		String email, nuevaPwd;

		try {
			if (respuesta == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe indicar una respuesta."));
			} else {
				if (validarRespuesta(usuarioOlvidoPwd.getUsuario().getRespuestaPreguntaSeguridad(), respuesta)) {
					email = usuarioOlvidoPwd.getUsuario().getEmail();
					usuarioRecuperarPwd = us.encontrar(email);
					nuevaPwd = us.getRandomPassword();
					enviarMailRecuperacionPwd(email, nuevaPwd);
					usuarioRecuperarPwd.setPassword(nuevaPwd);
					usuarioRecuperarPwd.setDebeCambiarPassword(true);
					us.guardar(usuarioRecuperarPwd);
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Recuperacion de contraseña",
									"Se ha enviado un mail a su casilla de correo '" + email + "'."));
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La respuesta no es correcta."));
				}
				respuesta = "";
			}
		} catch (NegocioException ne) {
			logger.error("Error en método recuperarPwd", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	private Boolean validarRespuesta(String respuestaCorrecta, String respuesta) {
		return respuestaCorrecta.replaceAll("\\s", "").toUpperCase()
				.equals(respuesta.replaceAll("\\s", "").toUpperCase());
	}

	private void enviarMailRecuperacionPwd(String emailUsuario, String nuevaPwd) {
		try {
			MailSimple mail = new MailSimple();
			mail.setPara(emailUsuario);
			mail.setAsunto("Recuperación de contraseña");
			mail.setMensaje(getMensajeMailRecuperacionPwd(nuevaPwd));
			ms.enviarMail(mail);
		} catch (NegocioException ne) {
			logger.error("Error en método enviarMailRecuperacionPwd", ne);
			AppHelper.errorNegocioMensaje();
		}
	}

	private String getMensajeMailRecuperacionPwd(String nuevaPwd) {
		String body = "Se ha generado una nueva contraseña para su cuenta:";
		body += "<br/><font color='blue'>" + nuevaPwd + "</font>";
		body += "<br/>";
		body += "<br/>Ingrese a GasTON Web a la brevedad para establecer una contraseña de su preferencia.";
		body += " Si usted no solicitó esta notificación por favor ignore este mensaje.";
		body += "<br/>";
		body += "<br/>Muchas gracias por registrarse.";
		body += "<br/>";
		return body;
	}

	public String getNombreCompleto() {
		return us.getNombreCompleto(usuarioOlvidoPwd.getUsuario().getNombre(),
				usuarioOlvidoPwd.getUsuario().getApellido());
	}

	public UsuarioOlvidoPwdBean getUsuarioOlvidoPwd() {
		return usuarioOlvidoPwd;
	}

	public void setUsuarioOlvidoPwd(UsuarioOlvidoPwdBean usuarioOlvidoPwd) {
		this.usuarioOlvidoPwd = usuarioOlvidoPwd;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

}
