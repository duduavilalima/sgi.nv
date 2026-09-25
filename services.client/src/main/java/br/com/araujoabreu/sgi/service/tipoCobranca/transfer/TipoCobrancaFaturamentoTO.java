package br.com.araujoabreu.sgi.service.tipoCobranca.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.List;

public class TipoCobrancaFaturamentoTO extends BaseTO {
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
