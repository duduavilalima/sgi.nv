package br.com.araujoabreu.sgi.faturamento.domain;

import br.com.fasolti.corporativo.pessoa.domain.Contato;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FATURAMENTO_CONTATO")
@SequenceGenerator(name="SEQ_FATURAMENTO_CONTATO", sequenceName="SEQ_FATURAMENTO_CONTATO", allocationSize=1)
public class FaturamentoContato extends BaseEntity {

	private static final long serialVersionUID = 682624920081117765L;
	private Faturamento faturamento;
	private Contato contato;
	private Integer ordem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_FATURAMENTO", nullable = false)
	public Faturamento getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(Faturamento faturamento) {
		this.faturamento = faturamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTATO", nullable = false)
	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	@Column(name = "ORDEM", nullable = false)
	@NotNull
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}
