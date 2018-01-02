package uy.com.agm.gaston.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "PROPIEDAD")
@NamedQueries({
		@NamedQuery(name = "Propiedad.findByClave", query = "SELECT p FROM Propiedad p WHERE p.clave = :clave") })
public class Propiedad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_PROPIEDAD")
	private Integer id;

	@Column(name = "CLAVE", length = 100)
	@NotNull
	@Size(max = 100)
	private String clave;

	@Column(name = "VALOR", length = 100)
	@NotNull
	@Size(max = 100)
	private String valor;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
