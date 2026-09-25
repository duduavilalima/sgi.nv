package br.com.araujoabreu.sgi.service.administracao.transfer;


public class PessoaJuridicaPesquisaTO extends PessoaPesquisaTO {
	private String inscricaoEstadual;
	private String ufInscricaoEstadual;
	private String inscricaoMunicipal;
	private String tipoPessoaJuridica;
	private String inscEstadualUf;
	private String contacontabil;

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getUfInscricaoEstadual() {
		return ufInscricaoEstadual;
	}

	public void setUfInscricaoEstadual(String ufInscricaoEstadual) {
		this.ufInscricaoEstadual = ufInscricaoEstadual;
	}

	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public String getTipoPessoaJuridica() {
		return tipoPessoaJuridica;
	}

	public void setTipoPessoaJuridica(String tipoPessoaJuridica) {
		this.tipoPessoaJuridica = tipoPessoaJuridica;
	}

	public String getInscEstadualUf() {
		StringBuilder sb = new StringBuilder();
		if (inscricaoEstadual != null) {
			if (ufInscricaoEstadual != null) {
				sb.append(inscricaoEstadual).append("/").append(ufInscricaoEstadual);
			}
		}

		inscEstadualUf = sb.toString();

		return inscEstadualUf;
	}

	public void setInscEstadualUf(String inscEstadualUf) {
		this.inscEstadualUf = inscEstadualUf;
	}

	public String getContacontabil() {
		return contacontabil;
	}

	public void setContacontabil(String contacontabil) {
		this.contacontabil = contacontabil;
	}
}
