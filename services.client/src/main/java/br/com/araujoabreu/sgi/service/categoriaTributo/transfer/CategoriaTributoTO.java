package br.com.araujoabreu.sgi.service.categoriaTributo.transfer;

import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.tributo.transfer.TributoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;

public class CategoriaTributoTO extends BaseTO {

	private TributoTO tributoTO;
	private CidadeTO cidade;
	private String descricao;
	private BigDecimal minimoGuia;
	private Boolean habilitaFaturamento;
	private Integer codigoReceita;
	private String tipoAliquota;
	private String referenciaData;
	private Integer periodoApuracao;
	private String antecipaPosterga;
	private Integer primeiroDia;
	private String percentualAliquota;

	public TributoTO getTributoTO() {
		return tributoTO;
	}

	public void setTributoTO(TributoTO tributoTO) {
		this.tributoTO = tributoTO;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public CidadeTO getCidade() {
		return cidade;
	}

	public void setCidade(CidadeTO cidade) {
		this.cidade = cidade;
	}

	public BigDecimal getMinimoGuia() {
		return minimoGuia;
	}

	public void setMinimoGuia(BigDecimal minimoGuia) {
		this.minimoGuia = minimoGuia;
	}

	public Boolean getHabilitaFaturamento() {
		return habilitaFaturamento;
	}

	public void setHabilitaFaturamento(Boolean habilitaFaturamento) {
		this.habilitaFaturamento = habilitaFaturamento;
	}

	public Integer getCodigoReceita() {
		return codigoReceita;
	}

	public void setCodigoReceita(Integer codigoReceita) {
		this.codigoReceita = codigoReceita;
	}

	public String getTipoAliquota() {
		return tipoAliquota;
	}

	public void setTipoAliquota(String tipoAliquota) {
		this.tipoAliquota = tipoAliquota;
	}

	public String getReferenciaData() {
		return referenciaData;
	}

	public void setReferenciaData(String referenciaData) {
		this.referenciaData = referenciaData;
	}

	public Integer getPeriodoApuracao() {
		return periodoApuracao;
	}

	public void setPeriodoApuracao(Integer periodoApuracao) {
		this.periodoApuracao = periodoApuracao;
	}

	public String getAntecipaPosterga() {
		return antecipaPosterga;
	}

	public void setAntecipaPosterga(String antecipaPosterga) {
		this.antecipaPosterga = antecipaPosterga;
	}

	public Integer getPrimeiroDia() {
		return primeiroDia;
	}

	public void setPrimeiroDia(Integer primeiroDia) {
		this.primeiroDia = primeiroDia;
	}

	public String getPercentualAliquota() {
		return percentualAliquota;
	}

	public void setPercentualAliquota(String percentualAliquota) {
		this.percentualAliquota = percentualAliquota;
	}
}
