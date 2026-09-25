package br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class MotivoDescontoDevolucaoTO extends BaseTO {

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
