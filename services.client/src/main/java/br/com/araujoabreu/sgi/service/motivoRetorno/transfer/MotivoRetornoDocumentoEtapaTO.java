package br.com.araujoabreu.sgi.service.motivoRetorno.transfer;

import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class MotivoRetornoDocumentoEtapaTO extends BaseTO {
	private FaturamentoEtapaTO faturamentoEtapa;
	private MotivoRetornoDocumentoTO motivoRetornoDocumento;

	public FaturamentoEtapaTO getFaturamentoEtapa() {
		return faturamentoEtapa;
	}

	public void setFaturamentoEtapa(FaturamentoEtapaTO faturamentoEtapa) {
		this.faturamentoEtapa = faturamentoEtapa;
	}

	public MotivoRetornoDocumentoTO getMotivoRetornoDocumento() {
		return motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumentoTO motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}
}
