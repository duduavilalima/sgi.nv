package br.com.araujoabreu.sgi.service.tipoCobranca.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.util.List;

public class TipoCobrancaFaturamentoPesquisaTO extends BaseSearchTO {

	private String descricao;
	private List<TipoCobrancaVencimentoTO> tipoCobrancaVencimentos;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<TipoCobrancaVencimentoTO> getTipoCobrancaVencimentos() {
		return tipoCobrancaVencimentos;
	}

	public void setTipoCobrancaVencimentos(List<TipoCobrancaVencimentoTO> tipoCobrancaVencimentos) {
		this.tipoCobrancaVencimentos = tipoCobrancaVencimentos;
	}
}
