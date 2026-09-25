package br.com.araujoabreu.sgi.service.motivoRetorno.transfer;


import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequesEmitidosExibirTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class MotivoRetornoDocumentoChequeContaCorrentePesquisaTO extends BaseSearchTO {

	private ChequesEmitidosExibirTO chequesEmitidosExibirTO;
	private MotivoRetornoTO motivoRetornoTO;

	public ChequesEmitidosExibirTO getChequesEmitidosExibirTO() {
		return chequesEmitidosExibirTO;
	}

	public void setChequesEmitidosExibirTO(ChequesEmitidosExibirTO chequesEmitidosExibirTO) {
		this.chequesEmitidosExibirTO = chequesEmitidosExibirTO;
	}

	public MotivoRetornoTO getMotivoRetornoTO() {
		return motivoRetornoTO;
	}

	public void setMotivoRetornoTO(MotivoRetornoTO motivoRetornoTO) {
		this.motivoRetornoTO = motivoRetornoTO;
	}
}
