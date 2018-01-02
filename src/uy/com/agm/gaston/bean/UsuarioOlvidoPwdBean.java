package uy.com.agm.gaston.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import uy.com.agm.gaston.modelo.PreguntaSeguridad;
import uy.com.agm.gaston.modelo.Usuario;

@ManagedBean
@RequestScoped
public class UsuarioOlvidoPwdBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	@PostConstruct
	private void init() {
		usuario = new Usuario();
		usuario.setPreguntaSeguridad(new PreguntaSeguridad());
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
