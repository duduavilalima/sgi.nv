package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.transfer.MotivoDescontoDevolucaoTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;

public class NotaSaidaParcelaMotivoDescDevPesquisaTO extends BaseSearchTO {

	private Long codigoNotaSaidaParcela;
	private Long codigoMotivoDescontoDevolucao;
	private MotivoDescontoDevolucaoTO motivoDescontoDevolucao;
	private String tipo;
	private BigDecimal valor;

	public Long getCodigoNotaSaidaParcela() {
		return codigoNotaSaidaParcela;
	}

	public void setCodigoNotaSaidaParcela(Long codigoNotaSaidaParcela) {
		this.codigoNotaSaidaParcela = codigoNotaSaidaParcela;
	}

	public Long getCodigoMotivoDescontoDevolucao() {
		return codigoMotivoDescontoDevolucao;
	}

	public void setCodigoMotivoDescontoDevolucao(Long codigoMotivoDescontoDevolucao) {
		this.codigoMotivoDescontoDevolucao = codigoMotivoDescontoDevolucao;
	}

	public MotivoDescontoDevolucaoTO getMotivoDescontoDevolucao() {
		return motivoDescontoDevolucao;
	}

	public void setMotivoDescontoDevolucao(MotivoDescontoDevolucaoTO motivoDescontoDevolucao) {
		this.motivoDescontoDevolucao = motivoDescontoDevolucao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
