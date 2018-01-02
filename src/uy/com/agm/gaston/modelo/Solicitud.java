package uy.com.agm.gaston.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import uy.com.agm.gaston.soporte.enumerados.Estado;
import uy.com.agm.gaston.soporte.enumerados.TipoDeSolicitud;

@Entity
@Table(name = "SOLICITUD")
@NamedQueries({
		@NamedQuery(name = "Solicitud.find", query = "SELECT s FROM Solicitud s WHERE s.estado = :estado AND s.tipo = :tipo AND s.solicitante.email = :email AND s.nucleoFamiliar.id = :idNucleoFamiliar ORDER BY s.timestamp DESC"),
		@NamedQuery(name = "Solicitud.findByNucleoFamiliar", query = "SELECT s FROM Solicitud s WHERE s.nucleoFamiliar.id = :idNucleoFamiliar ORDER BY s.timestamp DESC") })
public class Solicitud {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_SOLICITUDES")
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_SOLICITANTE")
	private Usuario solicitante;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_NUCLEO_FAMILIAR")
	private NucleoFamiliar nucleoFamiliar;

	@Column(name = "TIPO", length = 50)
	@Enumerated(EnumType.STRING)
	private TipoDeSolicitud tipo;

	@Column(name = "ESTADO", length = 50)
	@Enumerated(EnumType.STRING)
	private Estado estado;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_HORA")
	private Date timestamp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Usuario solicitante) {
		this.solicitante = solicitante;
	}

	public NucleoFamiliar getNucleoFamiliar() {
		return nucleoFamiliar;
	}

	public void setNucleoFamiliar(NucleoFamiliar nucleoFamiliar) {
		this.nucleoFamiliar = nucleoFamiliar;
	}

	public TipoDeSolicitud getTipo() {
		return tipo;
	}

	public void setTipo(TipoDeSolicitud tipo) {
		this.tipo = tipo;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
