package uy.com.agm.gaston.modelo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "MONEDA")
public class Moneda {
	@Id
	@Column(name = "ID_MONEDA")
	@NotNull
	private Integer id;

	@Column(name = "SIMBOLO", length = 5)
	@NotNull
	@Size(max = 5)
	private String simbolo;

	@Column(name = "DESCRIPCION", length = 100)
	@NotNull
	@Size(max = 100)
	private String descripcion;

	// Cuantas unidades de esta moneda me dan por dólar
	@Column(name = "ARBITRAJE")
	@NotNull
	private BigDecimal arbitraje;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getArbitraje() {
		return arbitraje;
	}

	public void setArbitraje(BigDecimal arbitraje) {
		this.arbitraje = arbitraje;
	}
}
