package uy.com.agm.gaston.soporte.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class AppHelper {
	public static String getUniqueId() {
		return UUID.randomUUID().toString();
	}

	public static String getAbsoluteApplicationUrl() throws URISyntaxException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		URI uri = new URI(request.getRequestURL().toString());
		uri = new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), request.getContextPath().toString(), null,
				null);
		return uri.toString();
	}

	public static void errorNegocioMensaje() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, "No es posible completar la operación", ""));
	}
}
