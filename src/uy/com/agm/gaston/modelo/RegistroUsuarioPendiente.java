package uy.com.agm.gaston.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "REGISTRO_DE_ACTIVACION")
@NamedQueries({
		@NamedQuery(name = "RegistroUsuarioPendiente.findByIdActivacion", query = "SELECT r FROM RegistroUsuarioPendiente r WHERE r.idActivacion = :idActivacion ORDER BY r.timestamp DESC"),
		@NamedQuery(name = "RegistroUsuarioPendiente.findByIdUsuario", query = "SELECT r FROM RegistroUsuarioPendiente r WHERE r.usuario.id = :idUsuario ORDER BY r.timestamp DESC") })
public class RegistroUsuarioPendiente {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_REGISTRO_ACTIVACION")
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_HORA_REGISTRO")
	private Date timestamp;

	@Column(name = "ID_UNICO_DE_ACTIVACION")
	private String idActivacion;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIdActivacion() {
		return idActivacion;
	}

	public void setIdActivacion(String idActivacion) {
		this.idActivacion = idActivacion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
