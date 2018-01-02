package uy.com.agm.gaston.negocio.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.Gasto;
import uy.com.agm.gaston.modelo.Ingreso;
import uy.com.agm.gaston.modelo.Moneda;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.modelo.Propiedad;
import uy.com.agm.gaston.modelo.Usuario;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IAlertaGastoExcesivoTimerServices;
import uy.com.agm.gaston.negocio.interfaces.IGastoServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IIngresoServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IMailServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.INucleoFamiliarServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IPropiedadServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IUsuarioServicesLocal;
import uy.com.agm.gaston.soporte.util.DateHelper;
import uy.com.agm.gaston.soporte.util.MailSimple;
import uy.com.agm.gaston.soporte.util.MonedaHelper;

@Singleton
@Startup
public class AlertaGastoExcesivoTimerService implements IAlertaGastoExcesivoTimerServices {
	@EJB
	private INucleoFamiliarServicesLocal nfs;
	@EJB
	private IGastoServicesLocal gs;
	@EJB
	private IIngresoServicesLocal is;
	@EJB
	private IMailServicesLocal ms;
	@EJB
	private IUsuarioServicesLocal us;
	@EJB
	private IPropiedadServicesLocal ps;
	@Inject
	private Logger logger;
	@Resource
	TimerService timerService;

	private static final String KEY_MINUTOS_ALERTA_GASTO_EXCESIVO = "minutosAlertaGastoExcesivo";

	@Override
	public String getClaveMinutosAlertaGastoExcesivo() {
		return KEY_MINUTOS_ALERTA_GASTO_EXCESIVO;
	}

	@PostConstruct
	private void init() {
		agendarTimer();
	}

	private void agendarTimer() {
		Propiedad propiedad;
		Long milisegundos;

		try {
			propiedad = ps.encontrar(KEY_MINUTOS_ALERTA_GASTO_EXCESIVO);
			if (propiedad != null) {
				milisegundos = Long.parseLong(propiedad.getValor()) * 60 * 1000;
				crearTimer(milisegundos, propiedad);
			}
		} catch (NegocioException ne) {
			logger.error("Error en método init", ne);
		}
	}

	@Override
	public void resetTimer() {
		Collection<Timer> timers;

		timers = timerService.getTimers();
		for (Timer timer : timers) {
			timer.cancel();
		}
		agendarTimer();
	}

	private void reAgendarTimer(Timer timer) {
		Propiedad propiedad;
		Long milisegundos;

		propiedad = (Propiedad) timer.getInfo();
		milisegundos = Long.parseLong(propiedad.getValor()) * 60 * 1000;
		crearTimer(milisegundos, propiedad);
	}

	private void crearTimer(Long milisegundos, Propiedad propiedad) {
		// Timer no persistente
		timerService.createSingleActionTimer(milisegundos, new TimerConfig(propiedad, false));
	}

	@Timeout
	private void enviarAlertaGastoExcesivo(Timer timer) {
		alertaGastoExcesivo();
		reAgendarTimer(timer);
	}

	private void alertaGastoExcesivo() {
		Date fechaIni, fechaFin;
		Integer idNucleoFamiliar;
		Moneda nucleoFamiliarMoneda;
		List<NucleoFamiliar> nucleosFamiliares;
		List<Gasto> gastos;
		List<Ingreso> ingresos;
		BigDecimal totalGastos, totalIngresos, saldo;

		try {
			fechaFin = DateHelper.getHoy();
			nucleosFamiliares = nfs.obtenerNucleosFamiliares();

			for (NucleoFamiliar nucleoFamiliar : nucleosFamiliares) {
				idNucleoFamiliar = nucleoFamiliar.getId();
				nucleoFamiliarMoneda = nucleoFamiliar.getMoneda();
				fechaIni = nucleoFamiliar.getFechaUltimoCierre();
				if (fechaIni == null) {
					fechaIni = DateHelper.getYesterday(nucleoFamiliar.getFechaCreacion());
				}
				gastos = gs.encontrarTodos(idNucleoFamiliar, fechaIni, fechaFin);
				ingresos = is.encontrarTodos(idNucleoFamiliar, fechaIni, fechaFin);

				totalGastos = BigDecimal.ZERO;
				for (Gasto gasto : gastos) {
					totalGastos = totalGastos.add(MonedaHelper.convertirMontoAMonedaDestino(gasto.getMonto(),
							gasto.getMoneda(), nucleoFamiliarMoneda));
				}

				totalIngresos = BigDecimal.ZERO;
				for (Ingreso ingreso : ingresos) {
					totalIngresos = totalIngresos.add(MonedaHelper.convertirMontoAMonedaDestino(ingreso.getMonto(),
							ingreso.getMoneda(), nucleoFamiliarMoneda));
				}
				saldo = totalIngresos.subtract(totalGastos);
				if (saldo.compareTo(BigDecimal.ZERO) < 0) {
					enviarAlerta(nucleoFamiliar, fechaIni, fechaFin, totalGastos, totalIngresos, saldo);
				}
			}
		} catch (NegocioException ne) {
			logger.error("Error en método alertaGastoExcesivo", ne);
		}
	}

	private void enviarAlerta(NucleoFamiliar nucleoFamiliar, Date fechaIni, Date fechaFin, BigDecimal totalGastos,
			BigDecimal totalIngresos, BigDecimal saldo) throws NegocioException {
		String asunto = "Alerta de gasto excesivo";
		String mensaje;

		List<Usuario> integrantes = nfs.obtenerIntegrantes(nucleoFamiliar.getId());
		for (Usuario integrante : integrantes) {
			mensaje = getMensajeMailAlertaGastoExcesivo(integrante, nucleoFamiliar, fechaIni, fechaFin, totalGastos,
					totalIngresos, saldo);
			MailSimple mail = new MailSimple();
			mail.setPara(integrante.getEmail());
			mail.setAsunto(asunto);
			mail.setMensaje(mensaje);
			ms.enviarMail(mail);
		}
	}

	private String getMensajeMailAlertaGastoExcesivo(Usuario integrante, NucleoFamiliar nucleoFamiliar, Date fechaIni,
			Date fechaFin, BigDecimal totalGastos, BigDecimal totalIngresos, BigDecimal saldo) {
		String simboloMoneda = nucleoFamiliar.getMoneda().getSimbolo();
		String body = "Estimado " + us.getNombreCompleto(integrante.getNombre(), integrante.getApellido()) + ",";
		body += "<br/>";
		body += "<br/>GasTON Web ha detectado que al día de hoy el total de gastos del núcleo familiar ";
		body += "<font color='blue'>" + nucleoFamiliar.getNombre();
		body += "</font> al cual Ud. pertenece, supera el total de ingresos.";
		body += "<br/>";
		body += "<br/>Período: <font color='blue'>" + DateHelper.formatDate(fechaIni);
		body += "</font> al <font color='blue'>" + DateHelper.formatDate(fechaFin) + "</font>";
		body += "<br/>Moneda: <font color='blue'>" + nucleoFamiliar.getMoneda().getDescripcion() + "</font>";
		body += "<br/>Total de gastos: <font color='blue'>" + simboloMoneda + MonedaHelper.formatBigDecimal(totalGastos)
				+ "</font>";
		body += "<br/>Total de ingresos: <font color='blue'>" + simboloMoneda
				+ MonedaHelper.formatBigDecimal(totalIngresos) + "</font>";
		body += "<br/><font color='red'>Saldo: " + simboloMoneda + MonedaHelper.formatBigDecimal(saldo) + "</font>";
		body += "<br/>";
		body += "<br/>Muchas gracias por registrarse.";
		body += "<br/>";
		return body;
	}
}
