package uy.com.agm.gaston.negocio.interfaces;

import java.util.List;

import javax.ejb.Remote;

import uy.com.agm.gaston.modelo.Cierre;
import uy.com.agm.gaston.modelo.NucleoFamiliar;
import uy.com.agm.gaston.negocio.excepciones.NegocioException;

@Remote
public interface ICierreServicesRemote {
	public Cierre nuevoCierre(NucleoFamiliar nucleoFamiliar) throws NegocioException;

	public List<Cierre> obtenerCierres(NucleoFamiliar nucleoFamiliar) throws NegocioException;
}
