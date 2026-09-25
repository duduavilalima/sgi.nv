package br.com.araujoabreu.sgi.service.motivoRetorno.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class MotivoDocumentoTO extends BaseTO {
	private MotivoRetornoDocumentoTO motivoRetornoDocumento;
	private MotivoRetornoTO motivoRetorno;

	public MotivoRetornoDocumentoTO getMotivoRetornoDocumento() {
		return motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumentoTO motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}

	public MotivoRetornoTO getMotivoRetorno() {
		return motivoRetorno;
	}

	public void setMotivoRetorno(MotivoRetornoTO motivoRetorno) {
		this.motivoRetorno = motivoRetorno;
	}
}
