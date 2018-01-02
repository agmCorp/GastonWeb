package uy.com.agm.gaston.modelo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "NUCLEO_FAMILIAR")
@NamedQueries({
		@NamedQuery(name = "NucleoFamiliar.integranteByEmail", query = "SELECT nf FROM NucleoFamiliar nf, Usuario u WHERE u MEMBER OF nf.integrantes AND u.email = :email ORDER BY nf.fechaCreacion DESC"),
		@NamedQuery(name = "NucleoFamiliar.noAdministradosByEmail", query = "SELECT nf FROM NucleoFamiliar nf WHERE nf.administrador.email <> :emailAdministrador ORDER BY nf.fechaCreacion DESC"),
		@NamedQuery(name = "NucleoFamiliar.administradosByEmail", query = "SELECT nf FROM NucleoFamiliar nf WHERE nf.administrador.email = :emailAdministrador ORDER BY nf.fechaCreacion DESC") })
public class NucleoFamiliar {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_NUCLEO_FAMILIAR")
	@NotNull
	private Integer id;

	@Column(name = "NOMBRE", length = 100)
	@NotNull
	@Size(max = 100)
	private String nombre;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_MONEDA")
	private Moneda moneda;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_DE_CREACION")
	private Date fechaCreacion;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_DE_ULTIMO_CIERRE")
	private Date fechaUltimoCierre;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ADMINISTRADOR")
	private Usuario administrador;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "INTEGRANTE_NUCLEO_FAMILIAR", joinColumns = {
			@JoinColumn(name = "ID_NUCLEO_FAMILIAR") }, inverseJoinColumns = @JoinColumn(name = "ID_USUARIO"))
	private List<Usuario> integrantes;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Usuario getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Usuario administrador) {
		this.administrador = administrador;
	}

	public List<Usuario> getIntegrantes() {
		return integrantes;
	}

	public void setIntegrantes(List<Usuario> integrantes) {
		this.integrantes = integrantes;
	}

	public Date getFechaUltimoCierre() {
		return fechaUltimoCierre;
	}

	public void setFechaUltimoCierre(Date fechaUltimoCierre) {
		this.fechaUltimoCierre = fechaUltimoCierre;
	}

	public Moneda getMoneda() {
		return moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}
}
