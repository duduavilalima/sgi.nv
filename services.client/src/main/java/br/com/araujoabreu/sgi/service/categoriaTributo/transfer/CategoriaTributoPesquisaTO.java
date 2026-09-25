package br.com.araujoabreu.sgi.service.categoriaTributo.transfer;

import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.tributo.transfer.TributoTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CategoriaTributoPesquisaTO extends BaseSearchTO {

	private TributoTO tributoTO;
	private String descricao;
	private CidadeTO cidade;
	private Boolean principal;
	private List<AliquotaTO> aliquotas = new ArrayList<>();
	private BigDecimal minimoGuia;
	private Boolean habilitaFaturamento;

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

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public List<AliquotaTO> getAliquotas() {
		return aliquotas;
	}

	public void setAliquotas(List<AliquotaTO> aliquotas) {
		this.aliquotas = aliquotas;
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
}
