package br.com.araujoabreu.sgi.service.reinf.r2010;

import br.com.araujoabreu.sgi.service.reinf.r1070.IdeProcesso;

public class InfoProcRetAd {
	/*
	Informações de processos relacionados a não retenção de contribuição previdenciária adicional.
	Validação: A soma dos valores informados no campo {valorAdic} deste grupo, com exceção dos valores informados para {indSusp} = [92], deve ser
	igual a {vlrTotalNRetAdic}.
	*/

	private int tpProcRetAdic = IdeProcesso.TipoProcesso.ADMINISTRATIVO.getValor();

	private String nrProcRetAdic; //Informar o número do processo administrativo/judicial.
	//Validação: O processo deve existir na tabela de processos (R-1070).

	private Long codSuspAdic; //Código do Indicativo da Suspensão, atribuído pelo contribuinte. Este campo
	//deve ser utilizado se, num mesmo processo, houver mais de uma matéria
	//tributária objeto de contestação e as decisões forem diferentes para cada uma.
	//Validação: Preenchimento obrigatório se houver mais de uma informação de
	//Indicativo de Suspensão para um mesmo processo.
	//Se informado, deve constar na tabela de processos (R-1070 - campo
	//{codSusp}), vinculado ao número do processo informado em {nrProcRetAdic}.

	private Double valorAdic; //Valor da retenção de contribuição previdenciária adicional que deixou de ser
	//efetuada em função de processo administrativo ou judicial.


	public int getTpProcRetAdic() {
		return tpProcRetAdic;
	}

	public void setTpProcRetAdic(int tpProcRetAdic) {
		this.tpProcRetAdic = tpProcRetAdic;
	}

	public String getNrProcRetAdic() {
		return nrProcRetAdic;
	}

	public void setNrProcRetAdic(String nrProcRetAdic) {
		this.nrProcRetAdic = nrProcRetAdic;
	}

	public Long getCodSuspAdic() {
		return codSuspAdic;
	}

	public void setCodSuspAdic(Long codSuspAdic) {
		this.codSuspAdic = codSuspAdic;
	}

	public Double getValorAdic() {
		return valorAdic;
	}

	public void setValorAdic(Double valorAdic) {
		this.valorAdic = valorAdic;
	}
}
