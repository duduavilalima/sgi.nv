package br.com.araujoabreu.sgi.service.comercial.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class RamoNegocioTO extends BaseTO {

	private Long codigo_pai;
	private String sigla;
	private String descricao;
	private Boolean selecionavel;

	public RamoNegocioTO() {
	}

	public RamoNegocioTO(Long codigo) {
		setCodigo(codigo);
	}

	public RamoNegocioTO(String sigla, String descricao) {
		this.descricao = descricao;
		this.sigla = sigla;
	}

	public Long getCodigo_pai() {
		return codigo_pai;
	}

	public void setCodigo_pai(Long codigo_pai) {
		this.codigo_pai = codigo_pai;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getSelecionavel() {
		return selecionavel;
	}

	public void setSelecionavel(Boolean selecionavel) {
		this.selecionavel = selecionavel;
	}
}
