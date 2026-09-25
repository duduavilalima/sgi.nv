package br.com.araujoabreu.sgi.service.fecho.transfer;

import br.com.araujoabreu.sgi.service.categoriaMaoDeObra.transfer.CategoriaMaoDeObraTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class FechoMaoDeObraTO extends BaseTO {

	private BigDecimal quantidade;
	private BigDecimal valorUnitario;
	private String tipoContratacao;
	private CategoriaMaoDeObraTO categoriaMaoDeObra;
	private List<FechoMaoDeObraAdicionalTO> listaMaoDeObraAdicional;

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

	public String getTipoContratacao() {
		return tipoContratacao;
	}

	public void setTipoContratacao(String tipoContratacao) {
		this.tipoContratacao = tipoContratacao;
	}

	public CategoriaMaoDeObraTO getCategoriaMaoDeObra() {
		return categoriaMaoDeObra;
	}

	public void setCategoriaMaoDeObra(CategoriaMaoDeObraTO categoriaMaoDeObra) {
		this.categoriaMaoDeObra = categoriaMaoDeObra;
	}

	public List<FechoMaoDeObraAdicionalTO> getListaMaoDeObraAdicional() {
		return listaMaoDeObraAdicional;
	}

	public void setListaMaoDeObraAdicional(List<FechoMaoDeObraAdicionalTO> listaMaoDeObraAdicional) {
		this.listaMaoDeObraAdicional = listaMaoDeObraAdicional;
	}

	public BigDecimal getValorTotal() {
		BigDecimal total = new BigDecimal("0");
		if (valorUnitario != null && quantidade != null) {
			total = quantidade.multiply(valorUnitario.add(getTotalAdicionais()));
		}
		return total;
	}

	public BigDecimal getTotalAdicionais() {
		BigDecimal total = new BigDecimal("0");
		if (listaMaoDeObraAdicional != null && !listaMaoDeObraAdicional.isEmpty()) {
			for (FechoMaoDeObraAdicionalTO adicional : listaMaoDeObraAdicional) {
				if (adicional != null && adicional.getValorUnitario() != null) {
					total = total.add(adicional.getValorUnitario());
				}
			}
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}
}
