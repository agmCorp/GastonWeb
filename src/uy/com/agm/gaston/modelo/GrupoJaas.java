package uy.com.agm.gaston.modelo;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "GRUPO_JAAS")
@NamedQueries({
		@NamedQuery(name = "GrupoJaas.findByName", query = "SELECT g FROM GrupoJaas g WHERE g.nombre = :nombre") })
public class GrupoJaas {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_GRUPO_JAAS")
	private Integer id;

	@Column(name = "NOMBRE", length = 30)
	private String nombre;

	@Column(name = "DESCRIPCION", length = 100)
	@Size(max = 100)
	private String descripcion;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "INTEGRANTE_GRUPO_JAAS", joinColumns = {
			@JoinColumn(name = "ID_GRUPO_JAAS") }, inverseJoinColumns = @JoinColumn(name = "ID_USUARIO"))
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Usuario> getIntegrantes() {
		return integrantes;
	}

	public void setIntegrantes(List<Usuario> integrantes) {
		this.integrantes = integrantes;
	}
}
