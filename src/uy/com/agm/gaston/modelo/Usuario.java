package uy.com.agm.gaston.modelo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import uy.com.agm.gaston.modelo.constraint.Email;
import uy.com.agm.gaston.modelo.constraint.Password;
import uy.com.agm.gaston.modelo.constraint.Telefono;

@Entity
@Table(name = "USUARIO")
@NamedQueries({
		@NamedQuery(name = "Usuario.findByEmail", query = "SELECT u FROM Usuario u WHERE u.email = :email ORDER BY u.email ASC") })
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_USUARIO")
	private Integer id;

	@Column(name = "EMAIL", length = 50)
	@Email
	private String email;

	@Column(name = "PASSWORD", length = 30)
	@Password
	private String password;

	@Column(name = "NOMBRE", length = 30)
	@NotNull
	@Size(max = 30)
	private String nombre;

	@Column(name = "APELLIDO", length = 30)
	@NotNull
	@Size(max = 30)
	private String apellido;

	@Column(name = "TELEFONO", length = 30)
	@Telefono
	private String telefono;

	@Column(name = "ACTIVO")
	private Boolean activo;

	@Column(name = "DEBE_CAMBIAR_PASSWORD")
	private Boolean debeCambiarPassword;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PREGUNTA")
	private PreguntaSeguridad preguntaSeguridad;

	@Column(name = "RESPUESTA", length = 50)
	@NotNull
	@Size(max = 50)
	private String respuestaPreguntaSeguridad;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "integrantes")
	private List<NucleoFamiliar> nucleosFamiliares;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "integrantes")
	private List<GrupoJaas> gruposJaas;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public List<NucleoFamiliar> getNucleosFamiliares() {
		return nucleosFamiliares;
	}

	public void setNucleosFamiliares(List<NucleoFamiliar> nucleosFamiliares) {
		this.nucleosFamiliares = nucleosFamiliares;
	}

	public List<GrupoJaas> getGruposJaas() {
		return gruposJaas;
	}

	public void setGruposJaas(List<GrupoJaas> gruposJaas) {
		this.gruposJaas = gruposJaas;
	}

	public PreguntaSeguridad getPreguntaSeguridad() {
		return preguntaSeguridad;
	}

	public void setPreguntaSeguridad(PreguntaSeguridad preguntaSeguridad) {
		this.preguntaSeguridad = preguntaSeguridad;
	}

	public String getRespuestaPreguntaSeguridad() {
		return respuestaPreguntaSeguridad;
	}

	public void setRespuestaPreguntaSeguridad(String respuestaPreguntaSeguridad) {
		this.respuestaPreguntaSeguridad = respuestaPreguntaSeguridad;
	}

	public Boolean getDebeCambiarPassword() {
		return debeCambiarPassword;
	}

	public void setDebeCambiarPassword(Boolean debeCambiarPassword) {
		this.debeCambiarPassword = debeCambiarPassword;
	}

}
