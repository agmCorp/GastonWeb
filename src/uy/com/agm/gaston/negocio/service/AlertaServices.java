package uy.com.agm.gaston.negocio.service;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import uy.com.agm.gaston.modelo.Alerta;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.negocio.interfaces.IAlertaServicesLocal;
import uy.com.agm.gaston.negocio.interfaces.IAlertaServicesRemote;
import uy.com.agm.gaston.negocio.interfaces.IMailServicesLocal;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.persistencia.interfaces.IAlertaDao;
import uy.com.agm.gaston.soporte.util.AlertaVencimiento;
import uy.com.agm.gaston.soporte.util.MailSimple;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AlertaServices implements IAlertaServicesRemote, IAlertaServicesLocal {
	@EJB
	private IAlertaDao alertaDao;
	@Resource
	TimerService timerService;
	@EJB
	private IMailServicesLocal ms;
	@Inject
	private Logger logger;

	private static final String ERROR_MSG = "Error en negocio " + AlertaServices.class.getName();

	@Override
	public List<Alerta> encontrarTodos(String emailUsuario) throws NegocioException {
		List<Alerta> result = null;

		try {
			result = alertaDao.encontrarTodos(emailUsuario);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
		return result;
	}

	@Override
	public void eliminar(Alerta alerta) throws NegocioException {
		String id;
		Collection<Timer> timers;
		AlertaVencimiento alertaVencimiento;

		try {
			id = alerta.getId();
			timers = timerService.getTimers();
			for (Timer timer : timers) {
				alertaVencimiento = (AlertaVencimiento) timer.getInfo();
				if (alertaVencimiento.getId().equals(id)) {
					timer.cancel();
					break;
				}
			}
			alertaDao.eliminar(alerta);
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}

	@Timeout
	private void alertaVencimiento(Timer timer) {
		try {
			AlertaVencimiento alertaVencimiento = (AlertaVencimiento) timer.getInfo();
			ms.enviarMail(alertaVencimiento.getMail());
		} catch (NegocioException ne) {
			logger.error("Error en método alertaVencimiento", ne);
		}
	}

	@Override
	public void nuevaAlertaVencimiento(Alerta alerta, Long diffMillis) throws NegocioException {
		try {
			// Se almacena la alerta
			alertaDao.crear(alerta);

			// Se genera alerta de vencimiento persistente
			AlertaVencimiento alertaVencimiento = new AlertaVencimiento();
			alertaVencimiento.setId(alerta.getId());
			alertaVencimiento.setDuracion(diffMillis);
			MailSimple mail = new MailSimple();
			mail.setPara(alerta.getUsuario().getEmail());
			mail.setAsunto(alerta.getDescripcion());
			mail.setMensaje(alerta.getMensaje());
			alertaVencimiento.setMail(mail);
			timerService.createSingleActionTimer(diffMillis, new TimerConfig(alertaVencimiento, true));
		} catch (DaoException de) {
			throw new NegocioException(ERROR_MSG, de);
		}
	}
}
