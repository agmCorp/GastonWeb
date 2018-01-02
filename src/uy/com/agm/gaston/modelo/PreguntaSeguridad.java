package uy.com.agm.gaston.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "PREGUNTA_SEGURIDAD")
public class PreguntaSeguridad {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_PREGUNTA")
	@NotNull
	private Integer id;

	@Column(name = "PREGUNTA", length = 50)
	@Size(max = 50)
	@NotNull
	private String pregunta;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
}
