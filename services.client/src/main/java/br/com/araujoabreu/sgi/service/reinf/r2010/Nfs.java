package br.com.araujoabreu.sgi.service.reinf.r2010;

import java.util.Date;

public class Nfs { //Detalhamento das notas fiscais de serviços prestados pela empresa identificada	no registro superior

	private String serie; //Informar o número de série da nota fiscal/fatura ou do Recibo Provisório de
	//Serviço - RPS. Preencher com 0 (zero) caso não exista número de série.

	private String numDocto;  //Número da Nota Fiscal/Fatura ou do Recibo Provisório de Serviço - RPS.

	private String dtEmissaoNF; //Data de Emissão da Nota Fiscal/Fatura ou do Recibo Provisório de Serviço - RPS.

	private String vlrBruto; //Preencher com o valor bruto da nota fiscal ou do Recibo Provisório de Serviço - RPS.

	private String obs; //Observações (Tamanho máximo do campo é de  caracteres)

	private InfoTpServ infoTpServ; //Informações sobre os tipos de Serviços constantes da Nota Fiscal


	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getNumDocto() {
		return numDocto;
	}

	public void setNumDocto(String numDocto) {
		this.numDocto = numDocto;
	}

	public String getDtEmissaoNF() {
		return dtEmissaoNF;
	}

	public void setDtEmissaoNF(String dtEmissaoNF) {
		this.dtEmissaoNF = dtEmissaoNF;
	}

	public String getVlrBruto() {
		return vlrBruto;
	}

	public void setVlrBruto(String vlrBruto) {
		this.vlrBruto = vlrBruto;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public InfoTpServ getInfoTpServ() {
		return infoTpServ;
	}

	public void setInfoTpServ(InfoTpServ infoTpServ) {
		this.infoTpServ = infoTpServ;
	}
}
