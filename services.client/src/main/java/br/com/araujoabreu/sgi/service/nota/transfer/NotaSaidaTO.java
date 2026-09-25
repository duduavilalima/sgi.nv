package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoAliquotaTO;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NotaSaidaTO extends NotaFiscalTO {

	private PessoaTO emitente;
	private PessoaTO tranportadora;
	private CidadeTO municipioTransportadora;
	private Long codigo_cfopTransporte;
	private CidadeTO municipioTransporte;
	private Boolean isentoICMS = Boolean.FALSE;
	private String inscricaoSuframa;
	private String tipoDocumentoTransportadora;
	private String documentoTransportadora;
	private String nomeTransportadora;
	private String inscricaoEstadualTransportadora;
	private Boolean isentoICMSTransportadora = Boolean.FALSE;
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
	private List<NotaSaidaParcelaTO> notaSaidaParcelas;
	private ServicoTipoTO servicoTipo;
	private PessoaTO responsavelNumeracao;
	private Date dataNumeracao;
	private PessoaTO responsavelCancelarNumeracao;
	private Date dataCancelamentoNumeracao;
	private PessoaTO responsavelCadastroPrefeitura;
	private Date dataCadastroPrefeitura;
	private String protocoloEntrega;
	private FaturamentoEtapaTO faturamentoEtapaTO;
	private List<FaturamentoAliquotaTO> faturamentoAliquotas;

	private Boolean issRetidoCliente;
	private Boolean issRetidoClienteRecebimento;

	private Boolean linhaVermelha = false;
	private Boolean textoVermelha = false;

	private NotaSaidaTO notaAssociadaCancelamento;
	private PessoaTO respNotaAssociadaCancelamento;
	private Date dataRespNotaAssociadaCancelamento;
	private String tipoFornecimento;

	public NotaSaidaTO() {
	}

	public NotaSaidaTO(Long codigo) {
		setCodigo(codigo);
	}

	public NotaSaidaTO(Long codigo, String numero) {
		setCodigo(codigo);
		setNumero(numero);
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

	public List<NotaSaidaParcelaTO> getNotaSaidaParcelas() {
		return notaSaidaParcelas;
	}

	public void setNotaSaidaParcelas(List<NotaSaidaParcelaTO> notaSaidaParcelas) {
		this.notaSaidaParcelas = notaSaidaParcelas;
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

	public List<FaturamentoAliquotaTO> getFaturamentoAliquotas() {
		return faturamentoAliquotas;
	}

	public void setFaturamentoAliquotas(List<FaturamentoAliquotaTO> faturamentoAliquotas) {
		this.faturamentoAliquotas = faturamentoAliquotas;
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

	public Boolean getLinhaVermelha() {
		return linhaVermelha;
	}

	public void setLinhaVermelha(Boolean linhaVermelha) {
		this.linhaVermelha = linhaVermelha;
	}

	public Boolean getTextoVermelha() {
		return textoVermelha;
	}

	public void setTextoVermelha(Boolean textoVermelha) {
		this.textoVermelha = textoVermelha;
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

	public String getTipoFornecimento() {
		return tipoFornecimento;
	}

	public void setTipoFornecimento(String tipoFornecimento) {
		this.tipoFornecimento = tipoFornecimento;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NotaSaidaTO)) return false;
		if (!super.equals(o)) return false;
		NotaSaidaTO that = (NotaSaidaTO) o;
		return Objects.equals(getEmitente(), that.getEmitente()) &&
			Objects.equals(getTranportadora(), that.getTranportadora()) &&
			Objects.equals(getMunicipioTransportadora(), that.getMunicipioTransportadora()) &&
			Objects.equals(getCodigo_cfopTransporte(), that.getCodigo_cfopTransporte()) &&
			Objects.equals(getMunicipioTransporte(), that.getMunicipioTransporte()) &&
			Objects.equals(getIsentoICMS(), that.getIsentoICMS()) &&
			Objects.equals(getInscricaoSuframa(), that.getInscricaoSuframa()) &&
			Objects.equals(getTipoDocumentoTransportadora(), that.getTipoDocumentoTransportadora()) &&
			Objects.equals(getDocumentoTransportadora(), that.getDocumentoTransportadora()) &&
			Objects.equals(getNomeTransportadora(), that.getNomeTransportadora()) &&
			Objects.equals(getInscricaoEstadualTransportadora(), that.getInscricaoEstadualTransportadora()) &&
			Objects.equals(getIsentoICMSTransportadora(), that.getIsentoICMSTransportadora()) &&
			Objects.equals(getEnderecoTransportadora(), that.getEnderecoTransportadora()) &&
			Objects.equals(getBaseCalculoICMSTransporte(), that.getBaseCalculoICMSTransporte()) &&
			Objects.equals(getAliquotaICMSTransporte(), that.getAliquotaICMSTransporte()) &&
			Objects.equals(getInfoComplementarContribuinte(), that.getInfoComplementarContribuinte()) &&
			Objects.equals(getResponsavelUltimoArquivo(), that.getResponsavelUltimoArquivo()) &&
			Objects.equals(getDataUltimoArquivo(), that.getDataUltimoArquivo()) &&
			Objects.equals(getResponsavelEnvioRfb(), that.getResponsavelEnvioRfb()) &&
			Objects.equals(getDataEnvioRfb(), that.getDataEnvioRfb()) &&
			Objects.equals(getResponsavelCancelamento(), that.getResponsavelCancelamento()) &&
			Objects.equals(getDataCancelamento(), that.getDataCancelamento()) &&
			Objects.equals(getNotaSaidaParcelas(), that.getNotaSaidaParcelas()) &&
			Objects.equals(getServicoTipo(), that.getServicoTipo()) &&
			Objects.equals(getResponsavelNumeracao(), that.getResponsavelNumeracao()) &&
			Objects.equals(getDataNumeracao(), that.getDataNumeracao()) &&
			Objects.equals(getResponsavelCancelarNumeracao(), that.getResponsavelCancelarNumeracao()) &&
			Objects.equals(getDataCancelamentoNumeracao(), that.getDataCancelamentoNumeracao()) &&
			Objects.equals(getResponsavelCadastroPrefeitura(), that.getResponsavelCadastroPrefeitura()) &&
			Objects.equals(getDataCadastroPrefeitura(), that.getDataCadastroPrefeitura()) &&
			Objects.equals(getProtocoloEntrega(), that.getProtocoloEntrega()) &&
			Objects.equals(getFaturamentoEtapaTO(), that.getFaturamentoEtapaTO()) &&
			Objects.equals(getFaturamentoAliquotas(), that.getFaturamentoAliquotas()) &&
			Objects.equals(getIssRetidoCliente(), that.getIssRetidoCliente());
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), getEmitente(), getTranportadora(), getMunicipioTransportadora(), getCodigo_cfopTransporte(), getMunicipioTransporte(), getIsentoICMS(), getInscricaoSuframa(), getTipoDocumentoTransportadora(), getDocumentoTransportadora(), getNomeTransportadora(), getInscricaoEstadualTransportadora(), getIsentoICMSTransportadora(), getEnderecoTransportadora(), getBaseCalculoICMSTransporte(), getAliquotaICMSTransporte(), getInfoComplementarContribuinte(), getResponsavelUltimoArquivo(), getDataUltimoArquivo(), getResponsavelEnvioRfb(), getDataEnvioRfb(), getResponsavelCancelamento(), getDataCancelamento(), getNotaSaidaParcelas(), getServicoTipo(), getResponsavelNumeracao(), getDataNumeracao(), getResponsavelCancelarNumeracao(), getDataCancelamentoNumeracao(), getResponsavelCadastroPrefeitura(), getDataCadastroPrefeitura(), getProtocoloEntrega(), getFaturamentoEtapaTO(), getFaturamentoAliquotas(), getIssRetidoCliente());
	}

	@Override
	public String toString() {
		return "NotaSaidaTO{" +
			"emitente=" + emitente +
			", tranportadora=" + tranportadora +
			", municipioTransportadora=" + municipioTransportadora +
			", codigo_cfopTransporte=" + codigo_cfopTransporte +
			", municipioTransporte=" + municipioTransporte +
			", isentoICMS=" + isentoICMS +
			", inscricaoSuframa='" + inscricaoSuframa + '\'' +
			", tipoDocumentoTransportadora='" + tipoDocumentoTransportadora + '\'' +
			", documentoTransportadora='" + documentoTransportadora + '\'' +
			", nomeTransportadora='" + nomeTransportadora + '\'' +
			", inscricaoEstadualTransportadora='" + inscricaoEstadualTransportadora + '\'' +
			", isentoICMSTransportadora=" + isentoICMSTransportadora +
			", enderecoTransportadora='" + enderecoTransportadora + '\'' +
			", baseCalculoICMSTransporte=" + baseCalculoICMSTransporte +
			", aliquotaICMSTransporte=" + aliquotaICMSTransporte +
			", infoComplementarContribuinte='" + infoComplementarContribuinte + '\'' +
			", responsavelUltimoArquivo=" + responsavelUltimoArquivo +
			", dataUltimoArquivo=" + dataUltimoArquivo +
			", responsavelEnvioRfb=" + responsavelEnvioRfb +
			", dataEnvioRfb=" + dataEnvioRfb +
			", responsavelCancelamento=" + responsavelCancelamento +
			", dataCancelamento=" + dataCancelamento +
			", notaSaidaParcelas=" + notaSaidaParcelas +
			", servicoTipo=" + servicoTipo +
			", responsavelNumeracao=" + responsavelNumeracao +
			", dataNumeracao=" + dataNumeracao +
			", responsavelCancelarNumeracao=" + responsavelCancelarNumeracao +
			", dataCancelamentoNumeracao=" + dataCancelamentoNumeracao +
			", responsavelCadastroPrefeitura=" + responsavelCadastroPrefeitura +
			", dataCadastroPrefeitura=" + dataCadastroPrefeitura +
			", protocoloEntrega='" + protocoloEntrega + '\'' +
			", faturamentoEtapaTO=" + faturamentoEtapaTO +
			", faturamentoAliquotas=" + faturamentoAliquotas +
			", issRetidoCliente=" + issRetidoCliente +
			'}';
	}
}
