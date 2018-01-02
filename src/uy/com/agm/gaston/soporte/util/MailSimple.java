package uy.com.agm.gaston.soporte.util;

import java.io.Serializable;

public class MailSimple implements Serializable {
	private static final long serialVersionUID = 1L;
	private String para;
	private String asunto;
	private String mensaje;

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}
