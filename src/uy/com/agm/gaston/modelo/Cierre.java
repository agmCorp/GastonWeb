package uy.com.agm.gaston.modelo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CIERRE")
@NamedQueries({
		@NamedQuery(name = "Cierre.findByNucleoFamiliar", query = "SELECT c FROM Cierre c WHERE c.nucleoFamiliar.id = :idNucleoFamiliar ORDER BY c.fecha DESC") })
public class Cierre {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_CIERRE")
	private Integer id;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_DE_CIERRE")
	private Date fecha;

	@Column(name = "TOTAL_INGRESOS")
	private BigDecimal totalIngresos;

	@Column(name = "TOTAL_EGRESOS")
	private BigDecimal totalEgresos;

	@Column(name = "SALDO")
	private BigDecimal saldo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_NUCLEO_FAMILIAR")
	private NucleoFamiliar nucleoFamiliar;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getTotalIngresos() {
		return totalIngresos;
	}

	public void setTotalIngresos(BigDecimal totalIngresos) {
		this.totalIngresos = totalIngresos;
	}

	public BigDecimal getTotalEgresos() {
		return totalEgresos;
	}

	public void setTotalEgresos(BigDecimal totalEgresos) {
		this.totalEgresos = totalEgresos;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public NucleoFamiliar getNucleoFamiliar() {
		return nucleoFamiliar;
	}

	public void setNucleoFamiliar(NucleoFamiliar nucleoFamiliar) {
		this.nucleoFamiliar = nucleoFamiliar;
	}

}
