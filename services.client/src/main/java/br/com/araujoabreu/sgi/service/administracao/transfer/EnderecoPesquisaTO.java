package br.com.araujoabreu.sgi.service.administracao.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class EnderecoPesquisaTO extends BaseSearchTO {
	private Long codigoTipoLogradouro;
	private Long codigoCidade;
	private Long codigoPessoa;
	private String endereco;
	private String numero;
	private String complemento;
	private String uf;
	private String bairro;
	private String cep;
	private String logradouroTipo;
	private String nomeCidade;

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouroTipo() {
		return logradouroTipo;
	}

	public void setLogradouroTipo(String logradouroTipo) {
		this.logradouroTipo = logradouroTipo;
	}

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	public Long getCodigoTipoLogradouro() {
		return codigoTipoLogradouro;
	}

	public void setCodigoTipoLogradouro(Long codigoTipoLogradouro) {
		this.codigoTipoLogradouro = codigoTipoLogradouro;
	}

	public Long getCodigoCidade() {
		return codigoCidade;
	}

	public void setCodigoCidade(Long codigoCidade) {
		this.codigoCidade = codigoCidade;
	}

	public Long getCodigoPessoa() {
		return codigoPessoa;
	}

	public void setCodigoPessoa(Long codigoPessoa) {
		this.codigoPessoa = codigoPessoa;
	}
}
