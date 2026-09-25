package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "CONCILIACAO_LANCAMENTO")
@SequenceGenerator(name = "SEQ_CONCILIACAO_LANCAMENTO", sequenceName = "SEQ_CONCILIACAO_LANCAMENTO", allocationSize = 1)
public class ConciliacaoLancamento extends BaseEntity implements DomainObject {

	private static final long serialVersionUID = -3975127153555391160L;
	private ConciliacaoBancaria conciliacaoBancaria;
	private Integer registro;
	private String natureza;
	private Date lancamento;
	private StatusConciliacaoLancamento situacao;
	private Double valor;
	private DebitoCreditoEnum debitoCredito;
	private String historico;

	public ConciliacaoLancamento() {

	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONCILIACAO_BANCARIA")
	public ConciliacaoBancaria getConciliacaoBancaria() {
		return conciliacaoBancaria;
	}

	public void setConciliacaoBancaria(ConciliacaoBancaria conciliacaoBancaria) {
		this.conciliacaoBancaria = conciliacaoBancaria;
	}

	public Integer getRegistro() {
		return registro;
	}

	public void setRegistro(Integer registro) {
		this.registro = registro;
	}

	public String getNatureza() {
		return natureza;
	}

	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}

	public Date getLancamento() {
		return lancamento;
	}

	public void setLancamento(Date lancamento) {
		this.lancamento = lancamento;
	}

	public StatusConciliacaoLancamento getSituacao() {
		return situacao;
	}

	public void setSituacao(StatusConciliacaoLancamento situacao) {
		this.situacao = situacao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "debito_credito")
	public DebitoCreditoEnum getDebitoCredito() {
		return debitoCredito;
	}

	public void setDebitoCredito(DebitoCreditoEnum debitoCredito) {
		this.debitoCredito = debitoCredito;
	}

	@Column(name = "historico")
	@NotNull
	@Length(max = 60)
	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public enum StatusConciliacaoLancamento {
		PENDENTE("Pendente"),
		NAO_PENDENTE("Não pendente"),
		CANCELADO("Cancelado");

		private final String descricao;

		private StatusConciliacaoLancamento(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return this.descricao;
		}
	}

	public enum DebitoCreditoEnum {
		D("débito"),
		C("crédito");

		private final String descricao;

		private DebitoCreditoEnum(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return this.descricao;
		}
	}

}
