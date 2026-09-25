package br.com.araujoabreu.sgi.service.contato.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class ContatoPesquisaTO extends BaseSearchTO {
	private String nome;
	private String email;
	private String nomeRelacionamentoContato;
	private Long codigoContrato;
	private Long codigoCliente;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNomeRelacionamentoContato() {
		return nomeRelacionamentoContato;
	}

	public void setNomeRelacionamentoContato(String nomeRelacionamentoContato) {
		this.nomeRelacionamentoContato = nomeRelacionamentoContato;
	}

	public Long getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(Long codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public Long getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(Long codigoPessoa) {
		this.codigoCliente = codigoPessoa;
	}
}
