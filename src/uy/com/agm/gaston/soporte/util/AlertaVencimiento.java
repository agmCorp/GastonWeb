package uy.com.agm.gaston.soporte.util;

import java.io.Serializable;

public class AlertaVencimiento implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private Long duracion;
	private MailSimple mail;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getDuracion() {
		return duracion;
	}

	public void setDuracion(Long duracion) {
		this.duracion = duracion;
	}

	public MailSimple getMail() {
		return mail;
	}

	public void setMail(MailSimple mail) {
		this.mail = mail;
	}

}
