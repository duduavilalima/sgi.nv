package br.com.araujoabreu.sgi.service.motivoRetorno.transfer;


import br.com.araujoabreu.sgi.service.tesouraria.transfer.ChequeContaCorrenteTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class MotivoRetornoDocumentoChequeContaCorrenteTO extends BaseTO {
	private ChequeContaCorrenteTO chequeContaCorrenteTO;
	private MotivoRetornoDocumentoTO motivoRetornoDocumento;

	public ChequeContaCorrenteTO getChequeContaCorrenteTO() {
		return chequeContaCorrenteTO;
	}

	public void setChequeContaCorrenteTO(ChequeContaCorrenteTO chequeContaCorrenteTO) {
		this.chequeContaCorrenteTO = chequeContaCorrenteTO;
	}

	public MotivoRetornoDocumentoTO getMotivoRetornoDocumento() {
		return motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumentoTO motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}
}
