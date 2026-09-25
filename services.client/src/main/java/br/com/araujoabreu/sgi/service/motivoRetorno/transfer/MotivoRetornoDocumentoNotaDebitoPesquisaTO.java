package br.com.araujoabreu.sgi.service.motivoRetorno.transfer;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaDebitoTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class MotivoRetornoDocumentoNotaDebitoPesquisaTO extends BaseSearchTO {

	private NotaDebitoTO notaDebito;
	private MotivoRetornoDocumentoTO motivoRetornoDocumento;

	public NotaDebitoTO getNotaDebito() {
		return notaDebito;
	}

	public void setNotaDebito(NotaDebitoTO notaDebito) {
		this.notaDebito = notaDebito;
	}

	public MotivoRetornoDocumentoTO getMotivoRetornoDocumento() {
		return motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumentoTO motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}
}
