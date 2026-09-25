package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Entidade relacionada a Nota que define as informaçoes para o Pagamento.
 * <br>
 * Uma parcela é uma instância de um pagamento que será tratado no contas a pagar.
 * <p>
 * Quando uma parcela é unica o seu numero será igual ao numero da nota, já para nota com parcelamento, o numero da parcela
 * terá como sufixo o número da parcela. numeroNota + "-N", onde N será o numero da parcela.
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "PARCELA_NOTA")
public class ParcelaNota extends Pagamento {

	private static final long serialVersionUID = -5363516600327258687L;

	private Nota nota;
	private Integer ordem;
	private boolean parcelaAtual;

	public ParcelaNota() {
	}

	@Override
	public void setCodigo(Long codigo) {
		super.setCodigo(codigo);
		if (nota != null) {
			// A ordem começa de 0 portanto devemos somar sempre um ao exibir
			setNumero(nota.isParcelaUnica() ? nota.getNumero() : nota.getNumero() + "-" + (getOrdem() + 1));
		}
	}

	/**
	 * @return the nota
	 */
	@ManyToOne
	@JoinColumn(name = "CODIGO_NOTA")
	public Nota getNota() {
		return nota;
	}

	/**
	 * @param nota the nota to set
	 */
	public void setNota(Nota nota) {
		this.nota = nota;
	}

	@Column(nullable = false)
	@NotNull
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	/**
	 * Retorna verdadeiro caso a parcela esteja nos status ABERTO ou REABERTO.
	 *
	 * @return
	 */
	@Transient
	public boolean isEditavel() {
		return (Status.ABERTO.equals(getStatus()) || Status.REABERTO.equals(getStatus()));
	}

	@Transient
	public BigDecimal getSaldoNPAdiantamento() {
		BigDecimal saldo = new BigDecimal("0");
		if (getNpAdiantamento() != null && getNpAdiantamento().getCodigo() != null) {
			saldo = this.getValor().subtract(getNpAdiantamento().getValor());
			saldo = saldo.setScale(2, RoundingMode.HALF_EVEN);
		}
		return saldo;
	}

	@Transient
	public Date getDataEmissaoNotaFiscal() {
		if (nota != null && nota.getTipo().equals(TipoPagamento.NOTA_FISCAL))
			if (nota instanceof NotaFiscal) {
				return ((NotaFiscal) nota).getDataEmissao();
			}
		return null;
	}


	@Transient
	public Date getDataEmissaoNotaPagamento() {
		if (nota != null && nota.getTipo().equals(TipoPagamento.NOTA_PAGAMENTO))
			if (nota instanceof NotaPagamento) {
				return nota.getDataCriacao();
			} else {
				return nota.getDataCriacao();
			}
		return null;
	}

	@Transient
	public boolean isParcelaAtual() {
		return parcelaAtual;
	}

	public void setParcelaAtual(boolean parcelaAtual) {
		this.parcelaAtual = parcelaAtual;
	}
}
