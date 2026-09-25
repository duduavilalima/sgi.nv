package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CONTRATO_PARCELA_PAGAMENTO")
@SequenceGenerator(name = "SEQ_CONTRATO_PARCELA_PAGAMENTO", sequenceName = "SEQ_CONTRATO_PARCELA_PAGAMENTO", allocationSize = 1)
public class ContratoParcelaPagamento extends BaseEntity {

	private static final long serialVersionUID = -5711024909156834941L;
	private ContratoParcela contratoParcela;
	private Pagamento pagamento;
	private String referencia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTRATO_PARCELA")
	public ContratoParcela getContratoParcela() {
		return contratoParcela;
	}

	public void setContratoParcela(ContratoParcela contratoParcela) {
		this.contratoParcela = contratoParcela;
	}

	@Column(name = "REFERENCIA")
	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PAGAMENTO")
	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

}
