package uy.com.agm.gaston.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;

import uy.com.agm.gaston.modelo.Mail;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IMailServicesLocal;
import uy.com.agm.gaston.soporte.enumerados.Modo;
import uy.com.agm.gaston.soporte.util.AppHelper;

@ManagedBean
@SessionScoped
public class MailBean implements Serializable {
	@EJB
	private IMailServicesLocal ms;
	@Inject
	private Logger logger;

	private static final long serialVersionUID = 1L;

	private Mail mailSeleccionado;
	private Modo modo;

	// Filtros de la grilla
	private List<Mail> filtroMails;

	public void resetDataTable() throws IOException {
		// Se eliminan los filtros (bug de PF)
		filtroMails = null;
	}

	public List<Mail> obtenerMails(String emailReceptor) {
		List<Mail> result = null;
		try {
			result = ms.obtenerMails(emailReceptor);
		} catch (NegocioException ne) {
			logger.error("Error en método getMails", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public String parseMail(String mensaje) {
		return Jsoup.parse(mensaje).text();
	}

	public String visualizarMail(Mail mail) {
		mailSeleccionado = mail;
		this.modo = Modo.VISUALIZACION;
		return "VISUALIZAR";
	}

	public String preVisualizarMail(Mail mail) {
		mailSeleccionado = mail;
		this.modo = Modo.ELIMINACION;
		return "VISUALIZAR";
	}

	public String eliminarMail(Mail mail) {
		try {
			ms.eliminar(mail);
			mailSeleccionado = null;
		} catch (NegocioException ne) {
			logger.error("Error en método eliminarMail", ne);
			AppHelper.errorNegocioMensaje();
		}
		return "ELIMINAR";
	}

	public Mail getMail() {
		return mailSeleccionado;
	}

	public List<Mail> getFiltroMails() {
		return filtroMails;
	}

	public void setFiltroMails(List<Mail> filtroMails) {
		this.filtroMails = filtroMails;
	}

	public Boolean esModoCreacion() {
		return this.modo == Modo.CREACION;
	}

	public Boolean esModoEdicion() {
		return this.modo == Modo.EDICION;
	}

	public Boolean esModoEliminacion() {
		return this.modo == Modo.ELIMINACION;
	}

	public Boolean esModoVisualizacion() {
		return this.modo == Modo.VISUALIZACION;
	}
}
