package br.com.araujoabreu.sgi.service.administracao.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;


public class PessoaPesquisaTO extends BaseSearchTO {
	private String documento;
	private String nome;
	private String nomeFantasia;
	private String tipoPessoa;
	private PessoaTO clientePrincipalTO;
	private String nomeFantasiaDocumento;
	private String situacao;
	private Long codigoEndereco;
	private String cidade;
	private String uf;


	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public PessoaTO getClientePrincipalTO() {
		return clientePrincipalTO;
	}

	public void setClientePrincipalTO(PessoaTO clientePrincipalTO) {
		this.clientePrincipalTO = clientePrincipalTO;
	}

	public String getNomeFantasiaDocumento() {
		return nomeFantasiaDocumento;
	}

	public void setNomeFantasiaDocumento(String nomeFantasiaDocumento) {
		this.nomeFantasiaDocumento = nomeFantasiaDocumento;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Long getCodigoEndereco() {
		return codigoEndereco;
	}

	public void setCodigoEndereco(Long codigoEndereco) {
		this.codigoEndereco = codigoEndereco;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCidadeUf() {
		String cidadeUf = this.cidade != null ? this.cidade : null;

		cidadeUf = cidadeUf == null ? "" : cidadeUf + (this.uf == null ? "" : "/" + this.uf);

		return cidadeUf;

	}
}
