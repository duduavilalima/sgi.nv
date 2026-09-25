package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class ServicoTipoTO extends BaseTO {
	private Long codigoPai;
	private ServicoTipoTO servicoTipoPai;
	private String sigla;
	private String nome;
	private String situacao;
	private String tipoFaturamento;

	public Long getCodigoPai() {
		return codigoPai;
	}

	public void setCodigoPai(Long codigoPai) {
		this.codigoPai = codigoPai;
	}

	public ServicoTipoTO getServicoTipoPai() {
		return servicoTipoPai;
	}

	public void setServicoTipoPai(ServicoTipoTO servicoTipoPai) {
		this.servicoTipoPai = servicoTipoPai;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getTipoFaturamento() {
		return tipoFaturamento;
	}

	public void setTipoFaturamento(String tipoFaturamento) {
		this.tipoFaturamento = tipoFaturamento;
	}
}
