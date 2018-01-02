package uy.com.agm.gaston.modelo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Partida {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_PARTIDA")
	private Integer id;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA")
	private Date fecha;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_MONEDA")
	private Moneda moneda;

	@Column(name = "MONTO")
	@NotNull
	private BigDecimal monto;

	@Column(name = "DESCRIPCION", length = 100)
	@NotNull
	@Size(max = 100)
	private String descripcion;

	@Column(name = "OBSERVACIONES")
	@Lob
	private String observaciones;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_NUCLEO_FAMILIAR")
	private NucleoFamiliar nucleoFamiliar;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_USUARIO_REG")
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CIERRE")
	private Cierre cierre;

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

	public Moneda getMoneda() {
		return moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public NucleoFamiliar getNucleoFamiliar() {
		return nucleoFamiliar;
	}

	public void setNucleoFamiliar(NucleoFamiliar nucleoFamiliar) {
		this.nucleoFamiliar = nucleoFamiliar;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Cierre getCierre() {
		return cierre;
	}

	public void setCierre(Cierre cierre) {
		this.cierre = cierre;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
