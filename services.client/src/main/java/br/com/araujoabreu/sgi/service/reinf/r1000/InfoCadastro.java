package br.com.araujoabreu.sgi.service.reinf.r1000;

public class InfoCadastro {
	private String classTrib; //Preencher com o código correspondente à classificação tributária do	contribuinte, conforme tabela 8.

	private int indEscrituracao; /*	Indicativo da obrigatoriedade do contribuinte em fazer a sua escrituração
									contábil na ECD Escrituração Contábil Digital:
									0 - Empresa Não obrigada à ECD;
									1 - Empresa obrigada à ECD.
								*/

	private int indDesoneracao; /* Indicativo de desoneração da folha pela CPRB
									0 - Não Aplicável;
									1 - Empresa enquadrada nos termos da Lei 12.546/2011 e alterações.
								*/

	private int indAcordoIsenMulta; /* Indicativo da existência de acordo internacional para isenção de multa:
										0 - Sem acordo;
										1 - Com acordo.
										Validação: Só pode ser igual a [1] se {classTrib} for igual a [60].
									*/

	private int indSitPJ; /* Indicativo da Situação da Pessoa Jurídica:
							0 - Situação Normal;
							1 - Extinção;
							2 - Fusão;
							3 - Cisão;
							4 - Incorporação.
						  */

	private Contato contato; //Informações de contato
	private SoftHouse softHouse; // Informações da(s) empresa(s) desenvolvedora(s) da(s) aplicação(ões) que	gera(m) os arquivos transmitidos para o ambiente nacional da EFD-ReinfR1000.
	private InfoEFR infoEFR; //Informações de órgãos públicos estaduais e municipais relativas a Ente Federativo Responsável - EFR

	public String getClassTrib() {
		return classTrib;
	}

	public void setClassTrib(String classTrib) {
		this.classTrib = classTrib;
	}

	public int getIndEscrituracao() {
		return indEscrituracao;
	}

	public void setIndEscrituracao(int indEscrituracao) {
		this.indEscrituracao = indEscrituracao;
	}

	public int getIndDesoneracao() {
		return indDesoneracao;
	}

	public void setIndDesoneracao(int indDesoneracao) {
		this.indDesoneracao = indDesoneracao;
	}

	public int getIndAcordoIsenMulta() {
		return indAcordoIsenMulta;
	}

	public void setIndAcordoIsenMulta(int indAcordoIsenMulta) {
		this.indAcordoIsenMulta = indAcordoIsenMulta;
	}

	public int getIndSitPJ() {
		return indSitPJ;
	}

	public void setIndSitPJ(int indSitPJ) {
		this.indSitPJ = indSitPJ;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public SoftHouse getSoftHouse() {
		return softHouse;
	}

	public void setSoftHouse(SoftHouse softHouse) {
		this.softHouse = softHouse;
	}

	public InfoEFR getInfoEFR() {
		return infoEFR;
	}

	public void setInfoEFR(InfoEFR infoEFR) {
		this.infoEFR = infoEFR;
	}
}
