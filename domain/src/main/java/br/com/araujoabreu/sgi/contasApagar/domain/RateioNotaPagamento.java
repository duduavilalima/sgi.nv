package br.com.araujoabreu.sgi.contasApagar.domain;

import br.com.araujoabreu.sgi.compras.domain.NotaPagamento;
import br.com.araujoabreu.sgi.compras.domain.Rateio;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "RATEIO_NOTA_PAGAMENTO")
@SequenceGenerator(name="SEQ_RATEIO_NOTA_PAGAMENTO", sequenceName="SEQ_RATEIO_NOTA_PAGAMENTO", allocationSize = 1)
public class RateioNotaPagamento extends Rateio {

	private static final long serialVersionUID = -4175910512945818966L;

	private NotaPagamento notaPagamento;
	private Long codigoCentroFinanceiro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA_PAGAMENTO", nullable = false)
	public NotaPagamento getNotaPagamento() {
		return notaPagamento;
	}

	public void setNotaPagamento(NotaPagamento notaPagamento) {
		this.notaPagamento = notaPagamento;
	}

	@Column(name = "CODIGO_CENTRO_FINANCEIRO")
	@NotNull
	public Long getCodigoCentroFinanceiro() {
		return codigoCentroFinanceiro;
	}

	public void setCodigoCentroFinanceiro(Long codigoCentroFinanceiro) {
		this.codigoCentroFinanceiro = codigoCentroFinanceiro;
	}
}
