package br.com.araujoabreu.sgi.service.motivoRetorno.transfer;

/**
 * Classe criada para transferir objetos para executar o retorno de assinatura de um centro financeiro.
 * "Tela Comercial > Centros Financeiros > Exibição > Aba ASSINATURAS".
 */
public class RetornoAssinaturaTO {

	private MotivoRetornoDocumentoTO motivoRetornoDocumento;
	private Long codEtapaFluxo;
	private Long codCentroFinanceiro;
	private String chave;

	public MotivoRetornoDocumentoTO getMotivoRetornoDocumento() {
		return motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumentoTO motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}

	public Long getCodEtapaFluxo() {
		return codEtapaFluxo;
	}

	public void setCodEtapaFluxo(Long codEtapaFluxo) {
		this.codEtapaFluxo = codEtapaFluxo;
	}

	public Long getCodCentroFinanceiro() {
		return codCentroFinanceiro;
	}

	public void setCodCentroFinanceiro(Long codCentroFinanceiro) {
		this.codCentroFinanceiro = codCentroFinanceiro;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}
}
