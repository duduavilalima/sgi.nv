package br.com.araujoabreu.sgi.service.tesouraria.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class RetornoBancarioTO extends BaseTO {

	private String identificacaoErro;
	private String descricaoOriginal;
	private String descricao;
	private Boolean utilizado;
	private String statusItemRelacao;

	public RetornoBancarioTO() {
	}

	public String getIdentificacaoErro() {
		return identificacaoErro;
	}

	public void setIdentificacaoErro(String identificacaoErro) {
		this.identificacaoErro = identificacaoErro;
	}

	public String getDescricaoOriginal() {
		return descricaoOriginal;
	}

	public void setDescricaoOriginal(String descricaoOriginal) {
		this.descricaoOriginal = descricaoOriginal;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getUtilizado() {
		return utilizado;
	}

	public void setUtilizado(Boolean utilizado) {
		this.utilizado = utilizado;
	}

	public String getStatusItemRelacao() {
		return statusItemRelacao;
	}

	public void setStatusItemRelacao(String statusItemRelacao) {
		this.statusItemRelacao = statusItemRelacao;
	}
}
