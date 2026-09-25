package br.com.araujoabreu.sgi.service.reinf.r2020;

import br.com.araujoabreu.sgi.service.reinf.r2010.InfoProcRetAd;
import br.com.araujoabreu.sgi.service.reinf.r2010.InfoProcRetPr;
import br.com.araujoabreu.sgi.service.reinf.r2010.Nfs;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

public class IdeTomador {

	private int tpInscTomador;
	private String nrInscTomador; /*Indicar o número de inscrição do tomador, conforme indicado no campo {tpInscTomador}.
								Validação: A inscrição informada deve ser compatível com o
								{tpInscTomador}. Se {indObra} = [0], o número informado deve ser um
								CNPJ válido e não pertencer ao contribuinte declarante na base cadastral da
								RFB. Se {indObra} for igual a 1 (Empreitada Total), o {nrInscTomador}
								informado deve ser um CNO de propriedade do estabelecimento prestador
								{ideEstabPrest} na base da RFB. Se {indObra} for igual a 2 (Empreitada
								Parcial), o {nrInscTomador} informado deve ser um CNO e não pertencer ao
								contribuinte declarante na base cadastral da RFB.
								*/
	private int indObra;
	private String vlrTotalBruto;
	private String vlrTotalBaseRet;
	private String vlrTotalRetPrinc;
	private String vlrTotalRetAdic;
	private String vlrTotalNRetPrinc;
	private String vlrTotalNRetAdic;

	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Nfs> nfs;
	private InfoProcRetPr procRetPr;
	private InfoProcRetAd infoProcRetAd;

	public int getTpInscTomador() {
		return tpInscTomador;
	}

	public void setTpInscTomador(int tpInscTomador) {
		this.tpInscTomador = tpInscTomador;
	}

	public String getNrInscTomador() {
		return nrInscTomador;
	}

	public void setNrInscTomador(String nrInscTomador) {
		this.nrInscTomador = nrInscTomador;
	}

	public int getIndObra() {
		return indObra;
	}

	public void setIndObra(int indObra) {
		this.indObra = indObra;
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

	public List<Nfs> getNfs() {
		return nfs;
	}

	public void setNfs(List<Nfs> nfs) {
		this.nfs = nfs;
	}

	public InfoProcRetPr getProcRetPr() {
		return procRetPr;
	}

	public void setProcRetPr(InfoProcRetPr procRetPr) {
		this.procRetPr = procRetPr;
	}

	public InfoProcRetAd getInfoProcRetAd() {
		return infoProcRetAd;
	}

	public void setInfoProcRetAd(InfoProcRetAd infoProcRetAd) {
		this.infoProcRetAd = infoProcRetAd;
	}
}

