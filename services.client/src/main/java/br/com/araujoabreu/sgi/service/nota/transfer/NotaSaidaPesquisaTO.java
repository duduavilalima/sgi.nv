package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.math.BigDecimal;
import java.util.Date;

public class NotaSaidaPesquisaTO extends BaseSearchTO {

	private String nomeCliente;
	private PessoaTO emitente;
	private PessoaTO tranportadora;
	private CidadeTO municipioTransportadora;
	private Long codigo_cfopTransporte;
	private CidadeTO municipioTransporte;
	private Boolean isentoICMS;
	private String inscricaoSuframa;
	private String tipoDocumentoTransportadora;
	private String documentoTransportadora;
	private String nomeTransportadora;
	private String inscricaoEstadualTransportadora;
	private Boolean isentoICMSTransportadora;
	private String enderecoTransportadora;
	private BigDecimal baseCalculoICMSTransporte;
	private BigDecimal aliquotaICMSTransporte;
	private String infoComplementarContribuinte;
	private PessoaTO responsavelUltimoArquivo;
	private Date dataUltimoArquivo;
	private PessoaTO responsavelEnvioRfb;
	private Date dataEnvioRfb;
	private PessoaTO responsavelCancelamento;
	private Date dataCancelamento;
	private ServicoTipoTO servicoTipo;
	private PessoaTO responsavelNumeracao;
	private Date dataNumeracao;
	private PessoaTO responsavelCancelarNumeracao;
	private Date dataCancelamentoNumeracao;
	private PessoaTO responsavelCadastroPrefeitura;
	private Date dataCadastroPrefeitura;
	private String protocoloEntrega;
	private FaturamentoEtapaTO faturamentoEtapaTO;

	private Boolean issRetidoCliente;
	private Boolean issRetidoClienteRecebimento;

	private NotaSaidaTO notaAssociadaCancelamento;
	private PessoaTO respNotaAssociadaCancelamento;
	private Date dataRespNotaAssociadaCancelamento;

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public PessoaTO getEmitente() {
		return emitente;
	}

	public void setEmitente(PessoaTO emitente) {
		this.emitente = emitente;
	}

	public PessoaTO getTranportadora() {
		return tranportadora;
	}

	public void setTranportadora(PessoaTO tranportadora) {
		this.tranportadora = tranportadora;
	}

	public CidadeTO getMunicipioTransportadora() {
		return municipioTransportadora;
	}

	public void setMunicipioTransportadora(CidadeTO municipioTransportadora) {
		this.municipioTransportadora = municipioTransportadora;
	}

	public Long getCodigo_cfopTransporte() {
		return codigo_cfopTransporte;
	}

	public void setCodigo_cfopTransporte(Long codigo_cfopTransporte) {
		this.codigo_cfopTransporte = codigo_cfopTransporte;
	}

	public CidadeTO getMunicipioTransporte() {
		return municipioTransporte;
	}

	public void setMunicipioTransporte(CidadeTO municipioTransporte) {
		this.municipioTransporte = municipioTransporte;
	}

	public Boolean getIsentoICMS() {
		return isentoICMS;
	}

	public void setIsentoICMS(Boolean isentoICMS) {
		this.isentoICMS = isentoICMS;
	}

	public String getInscricaoSuframa() {
		return inscricaoSuframa;
	}

	public void setInscricaoSuframa(String inscricaoSuframa) {
		this.inscricaoSuframa = inscricaoSuframa;
	}

	public String getTipoDocumentoTransportadora() {
		return tipoDocumentoTransportadora;
	}

	public void setTipoDocumentoTransportadora(String tipoDocumentoTransportadora) {
		this.tipoDocumentoTransportadora = tipoDocumentoTransportadora;
	}

	public String getDocumentoTransportadora() {
		return documentoTransportadora;
	}

	public void setDocumentoTransportadora(String documentoTransportadora) {
		this.documentoTransportadora = documentoTransportadora;
	}

	public String getNomeTransportadora() {
		return nomeTransportadora;
	}

	public void setNomeTransportadora(String nomeTransportadora) {
		this.nomeTransportadora = nomeTransportadora;
	}

	public String getInscricaoEstadualTransportadora() {
		return inscricaoEstadualTransportadora;
	}

	public void setInscricaoEstadualTransportadora(String inscricaoEstadualTransportadora) {
		this.inscricaoEstadualTransportadora = inscricaoEstadualTransportadora;
	}

	public Boolean getIsentoICMSTransportadora() {
		return isentoICMSTransportadora;
	}

	public void setIsentoICMSTransportadora(Boolean isentoICMSTransportadora) {
		this.isentoICMSTransportadora = isentoICMSTransportadora;
	}

	public String getEnderecoTransportadora() {
		return enderecoTransportadora;
	}

	public void setEnderecoTransportadora(String enderecoTransportadora) {
		this.enderecoTransportadora = enderecoTransportadora;
	}

	public BigDecimal getBaseCalculoICMSTransporte() {
		return baseCalculoICMSTransporte;
	}

	public void setBaseCalculoICMSTransporte(BigDecimal baseCalculoICMSTransporte) {
		this.baseCalculoICMSTransporte = baseCalculoICMSTransporte;
	}

	public BigDecimal getAliquotaICMSTransporte() {
		return aliquotaICMSTransporte;
	}

	public void setAliquotaICMSTransporte(BigDecimal aliquotaICMSTransporte) {
		this.aliquotaICMSTransporte = aliquotaICMSTransporte;
	}

	public String getInfoComplementarContribuinte() {
		return infoComplementarContribuinte;
	}

	public void setInfoComplementarContribuinte(String infoComplementarContribuinte) {
		this.infoComplementarContribuinte = infoComplementarContribuinte;
	}

	public PessoaTO getResponsavelUltimoArquivo() {
		return responsavelUltimoArquivo;
	}

	public void setResponsavelUltimoArquivo(PessoaTO responsavelUltimoArquivo) {
		this.responsavelUltimoArquivo = responsavelUltimoArquivo;
	}

	public Date getDataUltimoArquivo() {
		return dataUltimoArquivo;
	}

	public void setDataUltimoArquivo(Date dataUltimoArquivo) {
		this.dataUltimoArquivo = dataUltimoArquivo;
	}

	public PessoaTO getResponsavelEnvioRfb() {
		return responsavelEnvioRfb;
	}

	public void setResponsavelEnvioRfb(PessoaTO responsavelEnvioRfb) {
		this.responsavelEnvioRfb = responsavelEnvioRfb;
	}

	public Date getDataEnvioRfb() {
		return dataEnvioRfb;
	}

	public void setDataEnvioRfb(Date dataEnvioRfb) {
		this.dataEnvioRfb = dataEnvioRfb;
	}

	public PessoaTO getResponsavelCancelamento() {
		return responsavelCancelamento;
	}

	public void setResponsavelCancelamento(PessoaTO responsavelCancelamento) {
		this.responsavelCancelamento = responsavelCancelamento;
	}

	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public ServicoTipoTO getServicoTipo() {
		return servicoTipo;
	}

	public void setServicoTipo(ServicoTipoTO servicoTipo) {
		this.servicoTipo = servicoTipo;
	}

	public PessoaTO getResponsavelNumeracao() {
		return responsavelNumeracao;
	}

	public void setResponsavelNumeracao(PessoaTO responsavelNumeracao) {
		this.responsavelNumeracao = responsavelNumeracao;
	}

	public Date getDataNumeracao() {
		return dataNumeracao;
	}

	public void setDataNumeracao(Date dataNumeracao) {
		this.dataNumeracao = dataNumeracao;
	}

	public PessoaTO getResponsavelCancelarNumeracao() {
		return responsavelCancelarNumeracao;
	}

	public void setResponsavelCancelarNumeracao(PessoaTO responsavelCancelarNumeracao) {
		this.responsavelCancelarNumeracao = responsavelCancelarNumeracao;
	}

	public Date getDataCancelamentoNumeracao() {
		return dataCancelamentoNumeracao;
	}

	public void setDataCancelamentoNumeracao(Date dataCancelamentoNumeracao) {
		this.dataCancelamentoNumeracao = dataCancelamentoNumeracao;
	}

	public PessoaTO getResponsavelCadastroPrefeitura() {
		return responsavelCadastroPrefeitura;
	}

	public void setResponsavelCadastroPrefeitura(PessoaTO responsavelCadastroPrefeitura) {
		this.responsavelCadastroPrefeitura = responsavelCadastroPrefeitura;
	}

	public Date getDataCadastroPrefeitura() {
		return dataCadastroPrefeitura;
	}

	public void setDataCadastroPrefeitura(Date dataCadastroPrefeitura) {
		this.dataCadastroPrefeitura = dataCadastroPrefeitura;
	}

	public String getProtocoloEntrega() {
		return protocoloEntrega;
	}

	public void setProtocoloEntrega(String protocoloEntrega) {
		this.protocoloEntrega = protocoloEntrega;
	}

	public FaturamentoEtapaTO getFaturamentoEtapaTO() {
		return faturamentoEtapaTO;
	}

	public void setFaturamentoEtapaTO(FaturamentoEtapaTO faturamentoEtapaTO) {
		this.faturamentoEtapaTO = faturamentoEtapaTO;
	}

	public Boolean getIssRetidoCliente() {
		return issRetidoCliente;
	}

	public void setIssRetidoCliente(Boolean issRetidoCliente) {
		this.issRetidoCliente = issRetidoCliente;
	}

	public Boolean getIssRetidoClienteRecebimento() {
		return issRetidoClienteRecebimento;
	}

	public void setIssRetidoClienteRecebimento(Boolean issRetidoClienteRecebimento) {
		this.issRetidoClienteRecebimento = issRetidoClienteRecebimento;
	}

	public NotaSaidaTO getNotaAssociadaCancelamento() {
		return notaAssociadaCancelamento;
	}

	public void setNotaAssociadaCancelamento(NotaSaidaTO notaAssociadaCancelamento) {
		this.notaAssociadaCancelamento = notaAssociadaCancelamento;
	}

	public PessoaTO getRespNotaAssociadaCancelamento() {
		return respNotaAssociadaCancelamento;
	}

	public void setRespNotaAssociadaCancelamento(PessoaTO respNotaAssociadaCancelamento) {
		this.respNotaAssociadaCancelamento = respNotaAssociadaCancelamento;
	}

	public Date getDataRespNotaAssociadaCancelamento() {
		return dataRespNotaAssociadaCancelamento;
	}

	public void setDataRespNotaAssociadaCancelamento(Date dataRespNotaAssociadaCancelamento) {
		this.dataRespNotaAssociadaCancelamento = dataRespNotaAssociadaCancelamento;
	}
}
