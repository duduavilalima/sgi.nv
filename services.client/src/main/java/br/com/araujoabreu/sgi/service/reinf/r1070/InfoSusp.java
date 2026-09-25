package br.com.araujoabreu.sgi.service.reinf.r1070;

import java.util.Date;

public class InfoSusp {

	private int codSusp; /*Código do Indicativo da Suspensão, atribuído pelo contribuinte. Este campo
						deve ser utilizado se, num mesmo processo, houver mais de uma matéria
						tributária objeto de contestação e as decisões forem diferentes para cada uma.
						Validação: Preenchimento obrigatório se houver mais de uma informação de
						Indicativo de Suspensão para um mesmo processo.
						'*/
	private String indSusp; /* Indicativo de suspensão da exigibilidade
						Validação: Se {IdeProcesso.tpProc} = [1], deve ser preenchido com [03, 90, 92]. Se
						{IdeProcesso.tpProc} = [2], deve ser preenchido com [01, 02, 04, 05, 08, 09, 10, 11, 12,
						13, 90, 92].
						Valores Válidos: 01, 02, 03, 04, 05, 08, 09, 10, 11, 12, 13, 90, 92.
						*/
	private Date dtDecisao; //Data da decisão, sentença ou despacho administrativo.
	private String indDeposito; /* Indicativo de Depósito do Montante Integral:
								S - Sim;
								N - Não.
								Validação: Se {indSusp} = [90], preencher obrigatoriamente com [N]. Se
								{indSusp} = [02, 03] preencher obrigatoriamente com [S].
								*/

	public int getCodSusp() {
		return codSusp;
	}

	public void setCodSusp(int codSusp) {
		this.codSusp = codSusp;
	}

	public String getIndSusp() {
		return indSusp;
	}

	public void setIndSusp(String indSusp) {
		this.indSusp = indSusp;
	}

	public Date getDtDecisao() {
		return dtDecisao;
	}

	public void setDtDecisao(Date dtDecisao) {
		this.dtDecisao = dtDecisao;
	}

	public String getIndDeposito() {
		return indDeposito;
	}

	public void setIndDeposito(String indDeposito) {
		this.indDeposito = indDeposito;
	}

	public enum IndSusp {
		LIMINAR_MANDADO_SEGURANCA("01", "Liminar em Mandado de Segurança"),
		DEPOSITO_JUDICIAL("02", "Depósito Judicial do Montante Integral"),
		DEPOSITO_ADMINISTRATIVO("03", "Depósito Administrativo do Montante Integral"),
		TUTELA("04", "Antecipação de Tutela"),
		LIMINAR_MEDIDA_CAUTELAR("05", "Liminar em Medida Cautelar"),
		SENTENCA_MANDADO_SEGURANCA("08", "Sentença em Mandado de Segurança Favorável ao Contribuinte"),
		SENTENCA_ACAO_ORDINARIA("09", "Sentença em Ação Ordinária Favorável ao Contribuinte e Confirmada pelo TRF"),
		ACORDAO_TRF("10", "Acórdão do TRF Favorável ao Contribuinte"),
		ACORDAO_STJ("11", "Acórdão do STJ em Recurso Especial Favorável ao Contribuinte"),
		ACORDAO_STF("12", "Acórdão do STF em Recurso Extraordinário Favorável ao Contribuinte"),
		SENTENCA_PRIMEIRA_INSTANCIA("13", "Sentença 1a instância não transitada em julgado com efeito suspensivo"),
		DECISAO_DEFINITIVA_CONTRIBUINTE("90", "Decisão Definitiva a favor do contribuinte"),
		SEM_SUPENSAO_EXIGIBILIDADE("92", "Sem suspensão da exigibilidade");


		final String valor;
		final String descricao;

		IndSusp(String valor, String descricao) {
			this.valor = valor;
			this.descricao = descricao;
		}

		public String getValor() {
			return valor;
		}

		public String getDescricao() {
			return descricao;
		}
	}
}
