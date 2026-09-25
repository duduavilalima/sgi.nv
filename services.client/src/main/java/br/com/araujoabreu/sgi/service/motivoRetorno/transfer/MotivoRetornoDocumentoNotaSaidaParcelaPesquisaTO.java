package br.com.araujoabreu.sgi.service.motivoRetorno.transfer;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class MotivoRetornoDocumentoNotaSaidaParcelaPesquisaTO extends BaseSearchTO {

	private NotaSaidaParcelaTO notaSaidaParcela;
	private MotivoRetornoDocumentoTO motivoRetornoDocumento;

	public NotaSaidaParcelaTO getNotaSaidaParcela() {
		return notaSaidaParcela;
	}

	public void setNotaSaidaParcela(NotaSaidaParcelaTO notaSaidaParcela) {
		this.notaSaidaParcela = notaSaidaParcela;
	}

	public MotivoRetornoDocumentoTO getMotivoRetornoDocumento() {
		return motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumentoTO motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}
}
