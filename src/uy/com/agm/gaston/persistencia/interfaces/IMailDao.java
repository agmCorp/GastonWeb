package uy.com.agm.gaston.persistencia.interfaces;

import java.util.List;

import uy.com.agm.gaston.modelo.Mail;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;

public interface IMailDao extends IGenericDao<Mail, Integer> {
	List<Mail> obtenerMails(String emailReceptor) throws DaoException;
}
