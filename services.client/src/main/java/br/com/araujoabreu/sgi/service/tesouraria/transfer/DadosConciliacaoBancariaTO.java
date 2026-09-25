package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.List;

public class DadosConciliacaoBancariaTO extends BaseTO {
	//FIXME Tem ID?
	private ConciliacaoBancariaTO conciliacaoBancaria;
	private List<ConciliacaoLancamentoTO> listaConciliacaoLancamento;

	public ConciliacaoBancariaTO getConciliacaoBancaria() {
		return conciliacaoBancaria;
	}

	public void setConciliacaoBancaria(ConciliacaoBancariaTO conciliacaoBancaria) {
		this.conciliacaoBancaria = conciliacaoBancaria;
	}

	public List<ConciliacaoLancamentoTO> getListaConciliacaoLancamento() {
		return listaConciliacaoLancamento;
	}

	public void setListaConciliacaoLancamento(List<ConciliacaoLancamentoTO> listaConciliacaoLancamento) {
		this.listaConciliacaoLancamento = listaConciliacaoLancamento;
	}
}
