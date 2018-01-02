package uy.com.agm.gaston.modelo;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CLASIFICACION_GASTO")
@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "ID_CLA_GASTO")) })
public class ClasificacionGasto extends Rubro {

}
