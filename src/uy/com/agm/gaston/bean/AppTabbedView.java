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
public class AppTabbedView implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Tab> tabs;
	private static final Integer PASSWORD_ACTUALIZAR_INDEX = 8;

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
		tabs.add(construirTab("Núcleos familiares", "id0", "/app/NucleoFamiliarListar.xhtml"));
		tabs.add(construirTab("Ingreso y egreso de núcleos familiares", "id1", "/app/NucleoFamiliarIngEgListar.xhtml"));
		tabs.add(construirTab("Gastos e ingresos", "id2", "/app/NucleoFamiliarGastoIngresoListar.xhtml"));
		tabs.add(construirTab("Cierres", "id3", "/app/NucleoFamiliarCierreListar.xhtml"));
		tabs.add(construirTab("Solicitudes recibidas", "id4", "/app/SolicitudListar.xhtml"));
		tabs.add(construirTab("Mis correos", "id5", "/common/MailListar.xhtml"));
		tabs.add(construirTab("Mis alertas", "id6", "/app/AlertaListar.xhtml"));
		tabs.add(construirTab("Mi perfil", "id7", "/common/PerfilActualizar.xhtml"));
		tabs.add(construirTab("Cambio de contraseña", "id8", "/common/PasswordActualizar.xhtml"));
		tabs.add(construirTab("Lista de usuarios", "id9", "/common/UsuarioListar.xhtml"));
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
