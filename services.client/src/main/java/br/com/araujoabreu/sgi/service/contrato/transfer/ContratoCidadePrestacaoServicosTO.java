package br.com.araujoabreu.sgi.service.contrato.transfer;

import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class ContratoCidadePrestacaoServicosTO extends BaseTO {
	private CidadeTO cidade;
	private ContratoTO contrato;
	private Long codigoContrato;

	public CidadeTO getCidade() {
		return cidade;
	}

	public void setCidade(CidadeTO cidade) {
		this.cidade = cidade;
	}

	public ContratoTO getContrato() {
		return contrato;
	}

	public void setContrato(ContratoTO contrato) {
		this.contrato = contrato;
	}

	public Long getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(Long codigoContrato) {
		this.codigoContrato = codigoContrato;
	}
}
