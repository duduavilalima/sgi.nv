package br.com.araujoabreu.sgi.service.fluxoCaixa.transfer;

import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeSimpleTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.util.Date;

public class PrevisaoFluxoCaixaPesquisaTO extends BaseSearchTO {

	private UnidadeSimpleTO unidade;
	private DiretoriaSimpleTO diretoria;
	private String despesaFC;
	private Date data;
	private Double valor;

	private DespesaFluxoCaixaTO despesaFluxoCaixa;

	public UnidadeSimpleTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeSimpleTO unidade) {
		this.unidade = unidade;
	}

	public DiretoriaSimpleTO getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(DiretoriaSimpleTO diretoria) {
		this.diretoria = diretoria;
	}

	public String getDespesaFC() {
		return despesaFC;
	}

	public void setDespesaFC(String despesaFC) {
		this.despesaFC = despesaFC;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public DespesaFluxoCaixaTO getDespesaFluxoCaixa() {
		return despesaFluxoCaixa;
	}

	public void setDespesaFluxoCaixa(DespesaFluxoCaixaTO despesaFluxoCaixa) {
		this.despesaFluxoCaixa = despesaFluxoCaixa;
	}
}
