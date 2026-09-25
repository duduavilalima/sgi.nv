package br.com.araujoabreu.sgi.service.fecho.transfer;

import br.com.araujoabreu.sgi.service.categoriaDespesaGeral.transfer.CategoriaDespesaGeralTO;
import br.com.araujoabreu.sgi.service.unidademedida.transfer.UnidadeMedidaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FechoDespesaGeralTO extends BaseTO {

	private String tipoDespesaGeral;
	private BigDecimal quantidade;
	private BigDecimal valorUnitario;
	private CategoriaDespesaGeralTO categoriaDespesaGeral;
	private String descricao;
	private UnidadeMedidaTO unidadeMedida;

	public String getTipoDespesaGeral() {
		return tipoDespesaGeral;
	}

	public void setTipoDespesaGeral(String tipoDespesaGeral) {
		this.tipoDespesaGeral = tipoDespesaGeral;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public CategoriaDespesaGeralTO getCategoriaDespesaGeral() {
		return categoriaDespesaGeral;
	}

	public void setCategoriaDespesaGeral(CategoriaDespesaGeralTO categoriaDespesaGeral) {
		this.categoriaDespesaGeral = categoriaDespesaGeral;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public UnidadeMedidaTO getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedidaTO unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public BigDecimal getValorTotalItem() {
		if (valorUnitario != null && quantidade != null) {
			return (valorUnitario.multiply(quantidade)).setScale(2, RoundingMode.HALF_EVEN);
		}
		return new BigDecimal("0");
	}
}
