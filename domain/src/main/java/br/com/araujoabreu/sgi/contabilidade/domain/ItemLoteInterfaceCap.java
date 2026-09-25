package br.com.araujoabreu.sgi.contabilidade.domain;

import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

/*
 * Esta Entidade representa a tabela que armazena as contas contábeis que não
 * possuem a coluna "codigo_fluxo_caixa" preenchido.
 */

@Entity
@Table(name = "ITEM_LOTE_INTERFACE_CAP")
@SequenceGenerator(name = "SEQ_ITEM_LOTE_INTERFACE_CAP", sequenceName = "SEQ_ITEM_LOTE_INTERFACE_CAP", allocationSize = 1)
public class ItemLoteInterfaceCap extends BaseEntity {

	private static final long serialVersionUID = 7326479930057281662L;
	private LoteInterface loteInterface;
	private Pagamento pagamento;
	private ContaContabil contaContabil;
	private BigDecimal valor;
	private String numeroContaContabil;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_LOTE_INTERFACE")
	@NotNull
	public LoteInterface getLoteInterface() {
		return loteInterface;
	}

	public void setLoteInterface(LoteInterface loteInterface) {
		this.loteInterface = loteInterface;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PAGAMENTO")
	@NotNull
	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL")
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@Column(name = "VALOR")
	@NotNull
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Column(name = "NUMERO_CONTA_CONTABIL")
	public String getNumeroContaContabil() {
		return numeroContaContabil;
	}

	public void setNumeroContaContabil(String numeroContaContabil) {
		this.numeroContaContabil = numeroContaContabil;
	}

}
