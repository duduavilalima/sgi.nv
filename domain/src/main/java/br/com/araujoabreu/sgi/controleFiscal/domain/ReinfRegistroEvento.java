package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "REINF_REGISTRO_EVENTO")
@SequenceGenerator(name="SEQ_REINF_REGISTRO_EVENTO", sequenceName="SEQ_REINF_REGISTRO_EVENTO", allocationSize=1)
public class ReinfRegistroEvento extends BaseEntity {

	private static final long serialVersionUID = -6377545243806752515L;
	private String nome;
	private String descricao;
	private String namespace;
	private Boolean periodico;
	private Boolean obrigatorio;
//	private List<ReinfEvento> eventos;

	@Column(name = "nome")
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	@Column(name = "descricao")
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "namespace")
	@NotNull
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@Column(name = "periodico")
	@NotNull
	public Boolean getPeriodico() {
		return periodico;
	}

	public void setPeriodico(Boolean periodico) {
		this.periodico = periodico;
	}

	@Column(name = "obrigatorio")
	@NotNull
	public Boolean getObrigatorio() {
		return obrigatorio;
	}

	public void setObrigatorio(Boolean obrigatorio) {
		this.obrigatorio = obrigatorio;
	}

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "reinfRegistroEvento")
//	@OrderBy(value = "codigo ASC")
//	public List<ReinfEvento> getEventos() {
//		return eventos;
//	}
//
//	public void setEventos(List<ReinfEvento> eventos) {
//		this.eventos = eventos;
//	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("nome", nome)
			.append("descricao", descricao)
			.append("namespace", namespace)
			.append("periodico", periodico)
			.append("obrigatorio", obrigatorio)
//			.append("eventos", eventos)
			.toString();
	}
}
