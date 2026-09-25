package br.com.araujoabreu.sgi.service.reinf.r2010;

import br.com.araujoabreu.sgi.service.reinf.r1070.IdeProcesso;

public class InfoProcRetPr {
	/*Informações de processos relacionados a não retenção de contribuição	previdenciária.
	Validação: A soma dos valores informados no campo {valorPrinc} deste
	grupo, com exceção dos valores informados para {indSusp} = [92], deve ser	igual a {vlrTotalNRetPrinc}. {
	*/
	private int tpProcRetPrinc = IdeProcesso.TipoProcesso.ADMINISTRATIVO.getValor();

	private String nrProcRetPrinc; //Informar o número do processo administrativo/judicial.
	//Validação: O processo deve existir na tabela de processos (R-1070).

	private Long codSuspPrinc;  //Código do Indicativo da Suspensão, atribuído pelo contribuinte. Este campo
	//deve ser utilizado se, num mesmo processo, houver mais de uma matéria
	//tributária objeto de contestação e as decisões forem diferentes para cada uma.
	//Validação: Preenchimento obrigatório se houver mais de uma informação de
	//Indicativo de Suspensão para um mesmo processo.
	//Se informado, deve constar na tabela de processos (R-1070 - campo
	//{codSusp}), vinculado ao número do processo informado em {nrProcRetPrinc}.

	private Double valorPrinc;  //Valor da retenção de contribuição previdenciária principal que deixou de ser
	// efetuada em função de processo administrativo ou judicial.


	public int getTpProcRetPrinc() {
		return tpProcRetPrinc;
	}

	public void setTpProcRetPrinc(int tpProcRetPrinc) {
		this.tpProcRetPrinc = tpProcRetPrinc;
	}

	public String getNrProcRetPrinc() {
		return nrProcRetPrinc;
	}

	public void setNrProcRetPrinc(String nrProcRetPrinc) {
		this.nrProcRetPrinc = nrProcRetPrinc;
	}

	public Long getCodSuspPrinc() {
		return codSuspPrinc;
	}

	public void setCodSuspPrinc(Long codSuspPrinc) {
		this.codSuspPrinc = codSuspPrinc;
	}

	public Double getValorPrinc() {
		return valorPrinc;
	}

	public void setValorPrinc(Double valorPrinc) {
		this.valorPrinc = valorPrinc;
	}
}
