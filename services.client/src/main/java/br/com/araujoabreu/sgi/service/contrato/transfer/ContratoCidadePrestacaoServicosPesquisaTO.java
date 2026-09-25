package br.com.araujoabreu.sgi.service.contrato.transfer;

import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

public class ContratoCidadePrestacaoServicosPesquisaTO extends BaseSearchTO {
	private Long codigoContrato;
	private Long codigoCidade;
	private CidadeTO cidade;
	private ContratoTO contrato;

	public Long getCodigoContrato() {
		return codigoContrato;
	}

	public void setCodigoContrato(Long codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public Long getCodigoCidade() {
		return codigoCidade;
	}

	public void setCodigoCidade(Long codigoCidade) {
		this.codigoCidade = codigoCidade;
	}

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
}
