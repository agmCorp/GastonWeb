package uy.com.agm.gaston.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.GrupoJaas;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesLocal;
import uy.com.agm.gaston.soporte.util.AppHelper;

@ManagedBean
@ViewScoped
public class UsuarioBean implements Serializable {
	@EJB
	private IUsuarioServicesLocal us;
	@Inject
	private Logger logger;

	// Filtros de la grilla
	private List<Usuario> filtroUsuarios;

	private static final long serialVersionUID = 1L;

	public void resetDataTable() throws IOException {
		// Se eliminan los filtros (bug de PF)
		filtroUsuarios = null;
	}

	public List<Usuario> getUsuarios() {
		List<Usuario> result = null;
		try {
			result = us.obtenerUsuarios();
		} catch (NegocioException ne) {
			logger.error("Error en método getUsuarios", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}

	public List<Usuario> getFiltroUsuarios() {
		return filtroUsuarios;
	}

	public void setFiltroUsuarios(List<Usuario> filtroUsuarios) {
		this.filtroUsuarios = filtroUsuarios;
	}

	public String obtenerDescripcionRoles(Usuario usuario) {
		String result = null;
		try {
			List<GrupoJaas> gruposJaas = us.obtenerGruposJaas(usuario.getEmail());
			if (gruposJaas.size() == 1) {
				result = gruposJaas.get(0).getDescripcion();
			} else {
				result = gruposJaas.get(0).getDescripcion() + " y " + gruposJaas.get(1).getDescripcion();
			}
		} catch (NegocioException ne) {
			logger.error("Error en método obtenerDescripcionRoles", ne);
			AppHelper.errorNegocioMensaje();
		}
		return result;
	}
}
