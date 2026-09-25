package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcela;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CONCILIACAO_LANCAMENTO_ASSOCIACAO")
@SequenceGenerator(name = "SEQ_CONCILIACAO_LANCAMENTO_ASSOCIACAO", sequenceName = "SEQ_CONCILIACAO_LANCAMENTO_ASSOCIACAO", allocationSize = 1)
public class ConciliacaoLancamentoAssociacao extends BaseEntity implements DomainObject {

	private static final long serialVersionUID = 2920310480846171434L;

	private ConciliacaoLancamento conciliacaoLancamento;
	private ItemRelacao itemRelacao;
	private Lancamento lancamento;
	private NotaSaidaParcela notaSaidaParcela;
	private TipoConciliacaoLancamentoAssociacao tipo;

	public ConciliacaoLancamentoAssociacao() {

	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONCILIACAO_LANCAMENTO")
	public ConciliacaoLancamento getConciliacaoLancamento() {
		return conciliacaoLancamento;
	}

	public void setConciliacaoLancamento(ConciliacaoLancamento conciliacaoLancamento) {
		this.conciliacaoLancamento = conciliacaoLancamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ITEM_RELACAO")
	public ItemRelacao getItemRelacao() {
		return itemRelacao;
	}

	public void setItemRelacao(ItemRelacao itemRelacao) {
		this.itemRelacao = itemRelacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_LANCAMENTO_TESOURARIA")
	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA_SAIDA_PARCELA")
	public NotaSaidaParcela getNotaSaidaParcela() {
		return notaSaidaParcela;
	}

	public void setNotaSaidaParcela(NotaSaidaParcela notaSaidaParcela) {
		this.notaSaidaParcela = notaSaidaParcela;
	}

	public TipoConciliacaoLancamentoAssociacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoConciliacaoLancamentoAssociacao tipo) {
		this.tipo = tipo;
	}

	public enum TipoConciliacaoLancamentoAssociacao {
		ASSOCIACAO_COM_ITEM_RELACAO_BANCARIA("Associação com item de relação bancária"),
		ASSOCIACAO_COM_LANCAMENTO_TESOURARIA("Associação com lançamento de Tesouraria"),
		ASSOCIACAO_COM_PARCELA_NOTA_SAIDA("Associação com parcela de nota de saída");

		private final String descricao;

		TipoConciliacaoLancamentoAssociacao(String descricao) {
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
