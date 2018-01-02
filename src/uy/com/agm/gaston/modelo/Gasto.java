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
@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "ID_GASTO")) })
@NamedQueries({
		@NamedQuery(name = "Gasto.findByNucleoFamiliar", query = "SELECT g FROM Gasto g WHERE g.nucleoFamiliar.id = :idNucleoFamiliar ORDER BY g.fecha DESC"),
		@NamedQuery(name = "Gasto.findByFecha", query = "SELECT g FROM Gasto g WHERE g.nucleoFamiliar.id = :idNucleoFamiliar AND :fechaIni < g.fecha AND g.fecha <= :fechaFin ORDER BY g.fecha DESC") })
public class Gasto extends Partida {
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CLA_GASTO")
	private ClasificacionGasto clasificacion;

	public ClasificacionGasto getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(ClasificacionGasto clasificacion) {
		this.clasificacion = clasificacion;
	}
}
