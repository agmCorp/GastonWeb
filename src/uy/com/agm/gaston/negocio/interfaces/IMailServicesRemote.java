package uy.com.agm.gaston.negocio.interfaces;

import java.util.List;

import javax.ejb.Remote;

import uy.com.agm.gaston.modelo.Mail;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;
import uy.com.agm.gaston.soporte.util.MailSimple;

@Remote
public interface IMailServicesRemote {
	public void enviarMail(MailSimple mailSimple) throws NegocioException;

	public List<Mail> obtenerMails(String emailReceptor) throws NegocioException;

	public void eliminar(Mail mail) throws NegocioException;
}
