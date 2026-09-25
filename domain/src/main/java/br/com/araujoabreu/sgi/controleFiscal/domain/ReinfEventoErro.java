package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "REINF_EVENTO_ERRO")
@SequenceGenerator(name="SEQ_REINF_EVENTO_ERRO", sequenceName="SEQ_REINF_EVENTO_ERRO", allocationSize=1)
public class ReinfEventoErro extends BaseEntity {

	private static final long serialVersionUID = 4217691376080650585L;
	private String idErro;
	private String descricao;
	private ReinfEvento reinfEvento;

	@Column(name = "id_erro")
	@NotNull
	public String getIdErro() {
		return idErro;
	}

	public void setIdErro(String idErro) {
		this.idErro = idErro;
	}

	@Column(name = "descricao")
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_reinf_evento")
	@NotNull
	public ReinfEvento getReinfEvento() {
		return reinfEvento;
	}

	public void setReinfEvento(ReinfEvento reinfEvento) {
		this.reinfEvento = reinfEvento;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ReinfEventoErro{");
		sb.append("idErro='").append(idErro).append('\'');
		sb.append(", descricao='").append(descricao).append('\'');
		sb.append(", reinfEvento=").append(reinfEvento);
		sb.append('}');
		return sb.toString();
	}
}
