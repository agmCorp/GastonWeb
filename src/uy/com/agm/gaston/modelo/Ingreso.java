package uy.com.agm.gaston.modelo;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "ID_INGRESO")) })
@NamedQueries({
		@NamedQuery(name = "Ingreso.findByNucleoFamiliar", query = "SELECT i FROM Ingreso i WHERE i.nucleoFamiliar.id = :idNucleoFamiliar ORDER BY i.fecha DESC"),
		@NamedQuery(name = "Ingreso.findByFecha", query = "SELECT i FROM Ingreso i WHERE i.nucleoFamiliar.id = :idNucleoFamiliar AND :fechaIni < i.fecha AND i.fecha <= :fechaFin ORDER BY i.fecha DESC") })
public class Ingreso extends Partida {
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CLA_INGRESO")
	private ClasificacionIngreso clasificacion;

	public ClasificacionIngreso getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(ClasificacionIngreso clasificacion) {
		this.clasificacion = clasificacion;
	}
}