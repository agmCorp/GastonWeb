package uy.com.agm.gaston.persistencia.interfaces;

import java.util.List;

import uy.com.agm.gaston.modelo.Solicitud;
import uy.com.agm.gaston.persistencia.excepciones.DaoException;
import uy.com.agm.gaston.soporte.enumerados.Estado;
import uy.com.agm.gaston.soporte.enumerados.TipoDeSolicitud;

public interface ISolicitudDao extends IGenericDao<Solicitud, Integer> {
	public Solicitud obtenerSolicitud(Estado estado, TipoDeSolicitud tipoDeSolicitud, Integer idNucleoFamiliar,
			String emailSolicitante) throws DaoException;

	public List<Solicitud> encontrarTodos(Integer idNucleoFamiliar) throws DaoException;
}
