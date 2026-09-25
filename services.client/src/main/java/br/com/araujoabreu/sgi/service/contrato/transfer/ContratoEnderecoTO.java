package br.com.araujoabreu.sgi.service.contrato.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.EnderecoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class ContratoEnderecoTO extends BaseTO {

	private ContratoTO contrato;
	private EnderecoTO endereco;
	private String tipo;
	private Boolean principal;

	public ContratoTO getContrato() {
		return contrato;
	}

	public void setContrato(ContratoTO contrato) {
		this.contrato = contrato;
	}

	public EnderecoTO getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoTO endereco) {
		this.endereco = endereco;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}
}
