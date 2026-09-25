package br.com.araujoabreu.sgi.service.reinf.r2010;

public class InfoTpServ { //Informações sobre os tipos de Serviços constantes da Nota Fiscal
	private String tpServico; //Informar o tipo de serviço, conforme tabela 6.

	private String vlrBaseRet; //Valor da Base de cálculo da retenção da contribuição previdenciária.

	private String vlrRetencao; //Preencher com o valor da retenção apurada de acordo com o que determina a	legislação vigente relativa aos serviços contidos na nota fiscal/fatura.
	//Se {indCPRB}= [0] preencher com valor correspondente a 11% de
	//{vlrBaseRet} . Se {indCPRB}= [1] preencher com valor correspondente a 3,5% de {vlrBaseRet}.

	private String vlrRetSub; //Informar o valor da retenção destacada na nota fiscal relativo aos serviços
	//subcontratados, se houver, desde que todos os documentos envolvidos se
	//refiram à mesma competência e ao mesmo serviço, conforme disciplina a	legislação.

	private String vlrNRetPrinc; //Valor da retenção principal que deixou de ser efetuada pelo contratante ou que
	// foi depositada em juízo em decorrência de decisão judicial/administrativa
	// Validação: Não pode ser superior a ((11% de {vlrBaseRet} se {indCPRB} = [0]
	// ou a 3,5% de {vlrBaseRet} se {indCPRB} = [1]) - {vlrRetSub}).

	private String vlrServicos15; //Valor dos Serviços prestados por segurados em condições especiais, cuja
	//atividade permita concessão de aposentadoria especial após 15 anos de	contribuição

	private String vlrServicos20;//Valor dos Serviços prestados por segurados em condições especiais, cuja
	//atividade permita concessão de aposentadoria especial após 15 anos de	contribuição

	private String vlrServicos25;//Valor dos Serviços prestados por segurados em condições especiais, cuja
	//atividade permita concessão de aposentadoria especial após 25 anos de	contribuição

								/* O somatório dos campos {vlrServicos15}, {vlrServicos20} e {vlrServicos25}
								não pode ser superior ao {vlrBaseRet} indicado na nota fiscal . */

	private String vlrAdicional;//Adicional apurado de retenção da nota fiscal, caso os serviços tenham sido
	//prestados sob condições especiais que ensejem aposentadoria especial aos
	//trabalhadores após 15, 20, ou 25 anos de contribuição. Preencher com o valor
	//correspondente ao somatório de 4% sobre o {vlrServicos15} mais 3% sobre
	//{vlrServicos20} mais 2% sobre {vlrServicos25}

	private String vlrNRetAdic; //Valor da retenção adicional que deixou de ser efetuada pelo contratante ou que
	//foi depositada em juízo em decorrência de decisão judicial/administrativa
	//Validação: Não pode ser superior a (4% sobre {vlrServicos15} + 3% sobre
	//{vlrServicos20} + 2% sobre {vlrServicos25}).


	public String getTpServico() {
		return tpServico;
	}

	public void setTpServico(String tpServico) {
		this.tpServico = tpServico;
	}

	public String getVlrBaseRet() {
		return vlrBaseRet;
	}

	public void setVlrBaseRet(String vlrBaseRet) {
		this.vlrBaseRet = vlrBaseRet;
	}

	public String getVlrRetencao() {
		return vlrRetencao;
	}

	public void setVlrRetencao(String vlrRetencao) {
		this.vlrRetencao = vlrRetencao;
	}

	public String getVlrRetSub() {
		return vlrRetSub;
	}

	public void setVlrRetSub(String vlrRetSub) {
		this.vlrRetSub = vlrRetSub;
	}

	public String getVlrNRetPrinc() {
		return vlrNRetPrinc;
	}

	public void setVlrNRetPrinc(String vlrNRetPrinc) {
		this.vlrNRetPrinc = vlrNRetPrinc;
	}

	public String getVlrServicos15() {
		return vlrServicos15;
	}

	public void setVlrServicos15(String vlrServicos15) {
		this.vlrServicos15 = vlrServicos15;
	}

	public String getVlrServicos20() {
		return vlrServicos20;
	}

	public void setVlrServicos20(String vlrServicos20) {
		this.vlrServicos20 = vlrServicos20;
	}

	public String getVlrServicos25() {
		return vlrServicos25;
	}

	public void setVlrServicos25(String vlrServicos25) {
		this.vlrServicos25 = vlrServicos25;
	}

	public String getVlrAdicional() {
		return vlrAdicional;
	}

	public void setVlrAdicional(String vlrAdicional) {
		this.vlrAdicional = vlrAdicional;
	}

	public String getVlrNRetAdic() {
		return vlrNRetAdic;
	}

	public void setVlrNRetAdic(String vlrNRetAdic) {
		this.vlrNRetAdic = vlrNRetAdic;
	}
}
