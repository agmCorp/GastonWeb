package uy.com.agm.gaston.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import uy.com.agm.gaston.soporte.util.Tab;

@ManagedBean
@ViewScoped
public class AdminTabbedView implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Tab> tabs;
	private static final Integer PASSWORD_ACTUALIZAR_INDEX = 7;

	public List<Tab> getTabs() {
		return tabs;
	}

	public Tab getPasswordActualizarTab() {
		return tabs.get(PASSWORD_ACTUALIZAR_INDEX);
	}

	public void setTabs(List<Tab> tabs) {
		this.tabs = tabs;
	}

	@PostConstruct
	private void init() {
		tabs = new ArrayList<>();
		tabs.add(construirTab("Monedas", "id0", "/admin/MonedaListar.xhtml"));
		tabs.add(construirTab("Clasificaciones de ingreso", "id1", "/admin/ClasificacionIngresoListar.xhtml"));
		tabs.add(construirTab("Clasificaciones de gasto", "id2", "/admin/ClasificacionGastoListar.xhtml"));
		tabs.add(construirTab("Preguntas de seguridad", "id3", "/admin/PreguntaSeguridadListar.xhtml"));
		tabs.add(construirTab("Propiedades del sistema", "id4", "/admin/PropiedadListar.xhtml"));
		tabs.add(construirTab("Mis correos", "id5", "/common/MailListar.xhtml"));
		tabs.add(construirTab("Mi perfil", "id6", "/common/PerfilActualizar.xhtml"));
		tabs.add(construirTab("Cambio de contraseña", "id7", "/common/PasswordActualizar.xhtml"));
		tabs.add(construirTab("Lista de usuarios", "id8", "/common/UsuarioListar.xhtml"));

	}

	private Tab construirTab(String titulo, String id, String pagina) {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		String contextRoot = ec.getRequestContextPath();

		Tab tab = new Tab();
		tab.setTitulo(titulo);
		tab.setId(id);
		tab.setPagina(contextRoot + pagina);
		return tab;
	}
}
