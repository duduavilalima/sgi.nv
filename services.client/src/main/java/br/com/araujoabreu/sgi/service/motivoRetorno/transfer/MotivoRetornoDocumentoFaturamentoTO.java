package br.com.araujoabreu.sgi.service.motivoRetorno.transfer;

import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class MotivoRetornoDocumentoFaturamentoTO extends BaseTO {
	private FaturamentoTO faturamento;
	private MotivoRetornoDocumentoTO motivoRetornoDocumento;

	public FaturamentoTO getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(FaturamentoTO faturamento) {
		this.faturamento = faturamento;
	}

	public MotivoRetornoDocumentoTO getMotivoRetornoDocumento() {
		return motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumentoTO motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}
}
