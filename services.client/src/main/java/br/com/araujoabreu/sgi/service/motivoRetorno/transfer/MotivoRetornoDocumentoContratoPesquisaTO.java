package br.com.araujoabreu.sgi.service.motivoRetorno.transfer;

import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class MotivoRetornoDocumentoContratoPesquisaTO extends BaseSearchTO {

	private ContratoTO contrato;
	private MotivoRetornoDocumentoTO motivoRetornoDocumento;

	public ContratoTO getContrato() {
		return contrato;
	}

	public void setContrato(ContratoTO contrato) {
		this.contrato = contrato;
	}

	public MotivoRetornoDocumentoTO getMotivoRetornoDocumento() {
		return motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumentoTO motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}
}
