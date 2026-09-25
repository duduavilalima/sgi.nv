package br.com.araujoabreu.sgi.service.comercial.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaJuridicaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class ObraClienteTO extends BaseTO {

	private PessoaJuridicaTO pessoa;
	private ObraTO obra;
	private Boolean principal;
	private int percFaturamento;

	public ObraClienteTO() {
	}

	public ObraClienteTO(Long codigo) {
		setCodigo(codigo);
	}

	public PessoaJuridicaTO getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaJuridicaTO pessoa) {
		this.pessoa = pessoa;
	}

	public ObraTO getObra() {
		return obra;
	}

	public void setObra(ObraTO obra) {
		this.obra = obra;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public Boolean isPrincipal() {
		return this.principal;
	}

	public int getPercFaturamento() {
		return percFaturamento;
	}

	public void setPercFaturamento(int percFaturamento) {
		this.percFaturamento = percFaturamento;
	}
}
