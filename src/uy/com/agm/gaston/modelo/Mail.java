package uy.com.agm.gaston.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "MAIL")
@NamedQueries({
		@NamedQuery(name = "Mail.findByReceptor", query = "SELECT m FROM Mail m WHERE m.para = :para ORDER BY m.timestamp DESC") })
public class Mail {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_MAIL")
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_HORA_ENVIO")
	private Date timestamp;

	@Column(name = "ORIGEN", length = 50)
	private String de;

	@Column(name = "DESTINO")
	private String para;

	@Column(name = "COPIA")
	private String cc;

	@Column(name = "COPIA_OCULTA")
	private String cco;

	@Column(name = "ASUNTO")
	private String asunto;

	@Column(name = "MENSAJE")
	@Lob
	private String mensaje;

	@Column(name = "ESTADO")
	private String estado;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDe() {
		return de;
	}

	public void setDe(String de) {
		this.de = de;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getCco() {
		return cco;
	}

	public void setCco(String cco) {
		this.cco = cco;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
