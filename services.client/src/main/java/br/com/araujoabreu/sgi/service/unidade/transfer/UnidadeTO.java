package br.com.araujoabreu.sgi.service.unidade.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UnidadeTO extends PessoaJuridicaTO {

	private String codnome;
	private String tipoUnidade;
	private Long codigoCidade;
	private UnidadeSimpleTO matriz;
	private Boolean zerarNumeracaoNfAno;
	private Boolean emitirNfFaturamento;
	private Integer proximaNf;
	private Integer proximaNfMaterial;
	private Short anoProximaNf;
	private Integer proximaNd;

	public UnidadeTO() {
	}

	public UnidadeTO(Long codigo) {
		this.setCodigo(codigo);
	}

	public UnidadeTO(Long codigo, String codnome) {
		this.setCodigo(codigo);
		this.codnome = codnome;
	}

	public String getCodnome() {
		return codnome;
	}

	public void setCodnome(String codnome) {
		this.codnome = codnome;
	}

	public String getTipoUnidade() {
		return tipoUnidade;
	}

	public void setTipoUnidade(String tipoUnidade) {
		this.tipoUnidade = tipoUnidade;
	}

	public Long getCodigoCidade() {
		return codigoCidade;
	}

	public void setCodigoCidade(Long codigoCidade) {
		this.codigoCidade = codigoCidade;
	}

	public UnidadeSimpleTO getMatriz() {
		return matriz;
	}

	public void setMatriz(UnidadeSimpleTO matriz) {
		this.matriz = matriz;
	}

	public Boolean getZerarNumeracaoNfAno() {
		return zerarNumeracaoNfAno;
	}

	public void setZerarNumeracaoNfAno(Boolean zerarNumeracaoNfAno) {
		this.zerarNumeracaoNfAno = zerarNumeracaoNfAno;
	}

	public Boolean getEmitirNfFaturamento() {
		return emitirNfFaturamento;
	}

	public void setEmitirNfFaturamento(Boolean emitirNfFaturamento) {
		this.emitirNfFaturamento = emitirNfFaturamento;
	}

	public Integer getProximaNf() {
		return proximaNf;
	}

	public void setProximaNf(Integer proximaNf) {
		this.proximaNf = proximaNf;
	}

	public Short getAnoProximaNf() {
		return anoProximaNf;
	}

	public void setAnoProximaNf(Short anoProximaNf) {
		this.anoProximaNf = anoProximaNf;
	}

	public Integer getProximaNd() {
		return proximaNd;
	}

	public void setProximaNd(Integer proximaNd) {
		this.proximaNd = proximaNd;
	}

	public Integer getProximaNfMaterial() {
		return proximaNfMaterial;
	}

	public void setProximaNfMaterial(Integer proximaNfMaterial) {
		this.proximaNfMaterial = proximaNfMaterial;
	}
}
