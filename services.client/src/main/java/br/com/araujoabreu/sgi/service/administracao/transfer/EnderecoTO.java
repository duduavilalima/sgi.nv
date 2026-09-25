package br.com.araujoabreu.sgi.service.administracao.transfer;

import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;

public class EnderecoTO extends BaseTO {

	private String endereco;
	private String numero;
	private String complemento;
	private String uf;
	private String bairro;
	private String cep;
	private TipoLogradouroTO tipoLogradouro;
	private CidadeTO cidade;
	private Boolean principal;
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;
	private String tipoEndereco;

	public EnderecoTO() {
	}

	public EnderecoTO(Long codigo) {
		setCodigo(codigo);
	}

	public EnderecoTO(Long codigo, String endereco, String cep, String uf) {
		setCodigo(codigo);
		setEndereco(endereco);
		setCep(cep);
		setUf(uf);
	}

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

	public TipoLogradouroTO getTipoLogradouro() {
		return tipoLogradouro;
	}

	public void setTipoLogradouro(TipoLogradouroTO tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
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

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getAtualizador() {
		return atualizador;
	}

	public void setAtualizador(String atualizador) {
		this.atualizador = atualizador;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public String getTipoEndereco() {
		return tipoEndereco;
	}

	public void setTipoEndereco(String tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}

	public String getEnderecoCompleto() {
		StringBuilder end = new StringBuilder();

		if (tipoLogradouro != null) {
			if (tipoLogradouro != null && !tipoLogradouro.getNome().trim().equals("")) {
				end.append(tipoLogradouro.getNome().trim()).append(" ");
			}
		}

		if (endereco != null) {
			if (!endereco.trim().equals("")) {
				end.append(endereco.trim());
			}
		}

		if (numero != null) {
			if (!numero.trim().equals("")) {
				end.append(", ").append(numero.trim());
			}
		}

		if (complemento != null) {
			if (!complemento.trim().equals("")) {
				end.append(" - ").append(complemento.trim());
			}
		}

		return end.toString();
	}
}
