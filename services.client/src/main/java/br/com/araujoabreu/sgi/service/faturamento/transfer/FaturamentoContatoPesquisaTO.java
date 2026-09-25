package br.com.araujoabreu.sgi.service.faturamento.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class FaturamentoContatoPesquisaTO extends BaseSearchTO {
	private Long codigoFaturamento;
	private Long codigoContato;
	private Integer ordem;

	public Long getCodigoFaturamento() {
		return codigoFaturamento;
	}

	public void setCodigoFaturamento(Long codigoFaturamento) {
		this.codigoFaturamento = codigoFaturamento;
	}

	public Long getCodigoContato() {
		return codigoContato;
	}

	public void setCodigoContato(Long codigoContato) {
		this.codigoContato = codigoContato;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}
