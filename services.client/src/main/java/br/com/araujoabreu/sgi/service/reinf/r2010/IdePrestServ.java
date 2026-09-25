package br.com.araujoabreu.sgi.service.reinf.r2010;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

public class IdePrestServ {
	private String cnpjPrestador; /* Preencher com o CNPJ do Prestador de Serviços
								Validação: Deve ser um CNPJ válido. Não pode pertencer ao declarante. Se
								{indObra} for igual a 1 (Empreitada Total) o cnpj do prestador terá que ser o
								proprietário do CNO do campo {nrInscEstab}
								*/
	private String vlrTotalBruto;     //Valor bruto da(s) nota(s) fiscal(is)
	private String vlrTotalBaseRet;    //Soma da base de cálculo da retenção da contribuição	previdenciária das notas fiscais emitidas para o contratante
	// Validação: Deve corresponder a soma do campo {vlrBruto} dos registros vinculados.
	private String vlrTotalRetPrinc;  //Soma do valor da retenção das notas fiscais de serviço emitidas para o contratante
	// Validação: Deve corresponder a soma do campo {vlrBaseRet} dos registros vinculados
	private String vlrTotalRetAdic;  //Soma do valor do adicional de retenção das notas fiscais
	//Validação: Deve corresponder a soma do campo {vlrRetencao}, subtraído da soma do campo {vlrRetSub} dos registros vinculados
	private String vlrTotalNRetPrinc; //Valor da retenção principal que deixou de ser efetuada pelo contratante ou que	foi depositada em juízo em decorrência da decisão judicial
	//Validação: Deve corresponder a soma do campo {vlrAdicional} dos registros vinculados.
	private String vlrTotalNRetAdic; //Valor da retenção adicional que deixou de ser efetuada pelo contratante ou que foi depositada em juízo em decorrência da decisão judicial
	//Validação: Deve corresponder a soma do campo {vlrNRetPrinc} dos registros vinculados.
	private int indCPRB = TipoContribuicao.RECEITA_BRUTA.getValor();  //0 - Não é contribuinte da Contribuição Previdenciária sobre a Receita Bruta (CPRB) - Retenção 11%;
	//Validação: Deve corresponder a soma do campo {vlrNRetAdic} dos registros	vinculados.
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Nfs> nfs; // Detalhamento das notas fiscais de serviços prestados pela empresa identificada no registro superior
	//1 - Contribuinte da Contribuição Previdenciária sobre a Receita Bruta (CPRB) - Retenção 3,5%.
	private InfoProcRetPr infoProcRetPr; //Informações de processos relacionados a não retenção de contribuição	previdenciária.
	private InfoProcRetAd infoProcRetAd;
	//Validação: A soma dos valores informados no campo {valorPrinc} deste
	//grupo, com exceção dos valores informados para {indSusp} = [92], deve ser	igual a {vlrTotalNRetPrinc}.

	public String getCnpjPrestador() {
		return cnpjPrestador;
	}

	public void setCnpjPrestador(String cnpjPrestador) {
		this.cnpjPrestador = cnpjPrestador;
	}

	public String getVlrTotalBruto() {
		return vlrTotalBruto;
	}

	public void setVlrTotalBruto(String vlrTotalBruto) {
		this.vlrTotalBruto = vlrTotalBruto;
	}

	public String getVlrTotalBaseRet() {
		return vlrTotalBaseRet;
	}

	public void setVlrTotalBaseRet(String vlrTotalBaseRet) {
		this.vlrTotalBaseRet = vlrTotalBaseRet;
	}

	public String getVlrTotalRetPrinc() {
		return vlrTotalRetPrinc;
	}

	public void setVlrTotalRetPrinc(String vlrTotalRetPrinc) {
		this.vlrTotalRetPrinc = vlrTotalRetPrinc;
	}

	public String getVlrTotalRetAdic() {
		return vlrTotalRetAdic;
	}

	public void setVlrTotalRetAdic(String vlrTotalRetAdic) {
		this.vlrTotalRetAdic = vlrTotalRetAdic;
	}

	public String getVlrTotalNRetPrinc() {
		return vlrTotalNRetPrinc;
	}

	public void setVlrTotalNRetPrinc(String vlrTotalNRetPrinc) {
		this.vlrTotalNRetPrinc = vlrTotalNRetPrinc;
	}

	public String getVlrTotalNRetAdic() {
		return vlrTotalNRetAdic;
	}

	public void setVlrTotalNRetAdic(String vlrTotalNRetAdic) {
		this.vlrTotalNRetAdic = vlrTotalNRetAdic;
	}

	public int getIndCPRB() {
		return indCPRB;
	}

	public void setIndCPRB(int indCPRB) {
		this.indCPRB = indCPRB;
	}

	public List<Nfs> getNfs() {
		return nfs;
	}

	public void setNfs(List<Nfs> nfs) {
		this.nfs = nfs;
	}

	public InfoProcRetPr getInfoProcRetPr() {
		return infoProcRetPr;
	}

	public void setInfoProcRetPr(InfoProcRetPr infoProcRetPr) {
		this.infoProcRetPr = infoProcRetPr;
	}

	public InfoProcRetAd getInfoProcRetAd() {
		return infoProcRetAd;
	}

	public void setInfoProcRetAd(InfoProcRetAd infoProcRetAd) {
		this.infoProcRetAd = infoProcRetAd;
	}

	@JsonIgnore
	public String getDescricaoIndCPRB() {
		for (TipoContribuicao tc : TipoContribuicao.values()) {
			if (tc.ordinal() == this.indCPRB)
				return tc.descricao;
		}

		return null;
	}

	public enum TipoContribuicao {
		NAO_RECEITA_BRUTA(0, "Não é contribuinte da Contribuição Previdenciária sobre a Receita Bruta(CPRB) - Retenção 11%;"),
		RECEITA_BRUTA(1, "Contribuinte da Contribuição Previdenciária sobre a Receita Bruta (CPRB) - Retenção 3,5%");

		final int valor;
		final String descricao;

		TipoContribuicao(int valor, String descricao) {
			this.valor = valor;
			this.descricao = descricao;
		}

		public int getValor() {
			return valor;
		}
	}
}
