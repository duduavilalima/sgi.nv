package br.com.araujoabreu.sgi.service.contaContabil.transfer;

import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeSimpleTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class ContaContabilTO extends BaseTO {

	private String numero;

	private String descricao;

	private UnidadeSimpleTO empresa;

	public ContaContabilTO() {
	}

	public ContaContabilTO(Long codigo) {
		setCodigo(codigo);
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public UnidadeSimpleTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(UnidadeSimpleTO empresa) {
		this.empresa = empresa;
	}
}
