package br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.transfer;

import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class MotivoDescontoDevolucaoContaContabilTO extends BaseTO {

	private MotivoDescontoDevolucaoTO motivoDescontoDevolucao;
	private ContaContabilTO contaContabil;
	private String tipo;

	public MotivoDescontoDevolucaoTO getMotivoDescontoDevolucao() {
		return motivoDescontoDevolucao;
	}

	public void setMotivoDescontoDevolucao(MotivoDescontoDevolucaoTO motivoDescontoDevolucao) {
		this.motivoDescontoDevolucao = motivoDescontoDevolucao;
	}

	public ContaContabilTO getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabilTO contaContabil) {
		this.contaContabil = contaContabil;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
