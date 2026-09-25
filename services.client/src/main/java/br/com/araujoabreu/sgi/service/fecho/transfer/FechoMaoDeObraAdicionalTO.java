package br.com.araujoabreu.sgi.service.fecho.transfer;

import br.com.araujoabreu.sgi.service.categoriamaodeobraadicional.transfer.CategoriaMaoDeObraAdicionalTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;

public class FechoMaoDeObraAdicionalTO extends BaseTO {

	private BigDecimal valorUnitario;
	private CategoriaMaoDeObraAdicionalTO categoriaMaoDeObraAdicional;

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public CategoriaMaoDeObraAdicionalTO getCategoriaMaoDeObraAdicional() {
		return categoriaMaoDeObraAdicional;
	}

	public void setCategoriaMaoDeObraAdicional(CategoriaMaoDeObraAdicionalTO categoriaMaoDeObraAdicional) {
		this.categoriaMaoDeObraAdicional = categoriaMaoDeObraAdicional;
	}
}
