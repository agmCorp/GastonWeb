package uy.com.agm.gaston.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ALERTA")
@NamedQueries({
		@NamedQuery(name = "Alerta.findByEmail", query = "SELECT a FROM Alerta a WHERE a.usuario.email = :email ORDER BY a.timestamp DESC") })
public class Alerta {
	@Id
	@Column(name = "ID_ALERTA")
	@NotNull
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_HORA_ALERTA")
	@NotNull
	private Date timestamp;

	@Column(name = "DESCRIPCION", length = 100)
	@Size(max = 100)
	@NotNull
	private String descripcion;

	@Column(name = "MENSAJE")
	@Lob
	private String mensaje;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
