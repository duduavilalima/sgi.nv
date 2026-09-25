package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.ServicoTipo;
import br.com.araujoabreu.sgi.faturamento.domain.FaturamentoEtapa;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import javax.swing.text.MaskFormatter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "NOTA_SAIDA")
public class NotaSaida extends NotaFiscal {

	private static final long serialVersionUID = 9091414010443410096L;
	private Pessoa emitente;
	private Pessoa tranportadora;
	private Cidade municipioTransportadora;
	private Cfop cfopTransporte;
	private Cidade municipioTransporte;
	private Boolean isentoICMS = Boolean.FALSE;
	private String inscricaoSuframa;
	private TipoDocumentoTransportadora tipoDocumentoTransportadora;
	private String documentoTransportadora;
	private String nomeTransportadora;
	private String inscricaoEstadualTransportadora;
	private Boolean isentoICMSTransportadora = Boolean.FALSE;
	private String enderecoTransportadora;
	private BigDecimal baseCalculoICMSTransporte;
	private BigDecimal aliquotaICMSTransporte;
	private String infoComplementarContribuinte;
	private List<ItemNotaSaida> listaItensNotaSaida = new ArrayList<>();
	private Pessoa responsavelUltimoArquivo;
	private Date dataUltimoArquivo;
	private Pessoa responsavelEnvioRfb;
	private Date dataEnvioRfb;
	private ServicoTipo servicoTipo;
	private String protocoloEntrega;
	private List<FaturamentoEtapa> faturamentoEtapas;
	private List<NotaSaidaParcela> notaSaidaParcelas;
	private Pessoa responsavelNumeracao;
	private Date dataNumeracao;
	private Pessoa responsavelCancelarNumeracao;
	private Date dataCancelamentoNumeracao;
	private Pessoa responsavelCadastroPrefeitura;
	private Date dataCadastroPrefeitura;

	private Boolean issRetidoCliente;
	private Boolean issRetidoClienteRecebimento;

	private NotaSaida notaAssociadaCancelamento;
	private Pessoa respNotaAssociadaCancelamento;
	private Date dataRespNotaAssociadaCancelamento;

	public NotaSaida() {
		super();
	}

	public NotaSaida(Pessoa credor) {
		super(credor);
	}

	public NotaSaida(Long codigo) {
		super.setCodigo(codigo);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_EMITENTE", nullable = false)
	@NotNull
	public Pessoa getEmitente() {
		return emitente;
	}

	public void setEmitente(Pessoa emitente) {
		this.emitente = emitente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_TRANSPORTADORA")
	public Pessoa getTranportadora() {
		return tranportadora;
	}

	public void setTranportadora(Pessoa tranportadora) {
		this.tranportadora = tranportadora;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_MUNICIPIO_TRANSPORTADORA")
	public Cidade getMunicipioTransportadora() {
		return municipioTransportadora;
	}

	public void setMunicipioTransportadora(Cidade municipioTransportadora) {
		this.municipioTransportadora = municipioTransportadora;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CFOP_TRANSPORTE")
	public Cfop getCfopTransporte() {
		return cfopTransporte;
	}

	public void setCfopTransporte(Cfop cfopTransporte) {
		this.cfopTransporte = cfopTransporte;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_MUNICIPIO_TRANSPORTE")
	public Cidade getMunicipioTransporte() {
		return municipioTransporte;
	}

	public void setMunicipioTransporte(Cidade municipioTransporte) {
		this.municipioTransporte = municipioTransporte;
	}

	@Column(name = "ISENTO_ICMS", nullable = false)
	@NotNull
	public Boolean getIsentoICMS() {
		return isentoICMS;
	}

	public void setIsentoICMS(Boolean isentoICMS) {
		this.isentoICMS = isentoICMS;
	}

	@Column(name = "INSCR_SUFRAMA", length = 9)
	@Length(max = 9)
	public String getInscricaoSuframa() {
		return inscricaoSuframa;
	}

	public void setInscricaoSuframa(String inscricaoSuframa) {
		this.inscricaoSuframa = inscricaoSuframa;
	}

	@Column(name = "TIPO_DOCUMENTO_TRANSPORTADORA")
	public TipoDocumentoTransportadora getTipoDocumentoTransportadora() {
		return tipoDocumentoTransportadora;
	}

	public void setTipoDocumentoTransportadora(
		TipoDocumentoTransportadora tipoDocumentoTransportadora) {
		this.tipoDocumentoTransportadora = tipoDocumentoTransportadora;
	}

	@Column(name = "DOCUMENTO_TRANSPORTADORA")
	public String getDocumentoTransportadora() {
		return documentoTransportadora;
	}

	public void setDocumentoTransportadora(String documentoTransportadora) {
		this.documentoTransportadora = documentoTransportadora;
	}

	@Column(name = "NOME_TRANSPORTADORA")
	public String getNomeTransportadora() {
		return nomeTransportadora;
	}

	public void setNomeTransportadora(String nomeTransportadora) {
		this.nomeTransportadora = nomeTransportadora;
	}

	@Column(name = "INSCRICAO_ESTADUAL_TRANSPORTADORA")
	public String getInscricaoEstadualTransportadora() {
		return inscricaoEstadualTransportadora;
	}

	public void setInscricaoEstadualTransportadora(
		String inscricaoEstadualTransportadora) {
		this.inscricaoEstadualTransportadora = inscricaoEstadualTransportadora;
	}

	@Column(name = "ISENTO_ICMS_TRANSPORTADORA")
	public Boolean getIsentoICMSTransportadora() {
		return isentoICMSTransportadora;
	}

	public void setIsentoICMSTransportadora(Boolean isentoICMSTransportadora) {
		this.isentoICMSTransportadora = isentoICMSTransportadora;
	}

	@Column(name = "ENDERECO_TRANSPORTADORA")
	public String getEnderecoTransportadora() {
		return enderecoTransportadora;
	}

	public void setEnderecoTransportadora(String enderecoTransportadora) {
		this.enderecoTransportadora = enderecoTransportadora;
	}

	@Column(name = "ICMS_BASE_CALCULO_TRANSPORTE")
	public BigDecimal getBaseCalculoICMSTransporte() {
		return baseCalculoICMSTransporte;
	}

	public void setBaseCalculoICMSTransporte(BigDecimal baseCalculoICMSTransporte) {
		this.baseCalculoICMSTransporte = baseCalculoICMSTransporte;
	}

	@Column(name = "ICMS_ALIQUOTA_TRANSPORTE")
	public BigDecimal getAliquotaICMSTransporte() {
		return aliquotaICMSTransporte;
	}

	public void setAliquotaICMSTransporte(BigDecimal aliquotaICMSTransporte) {
		this.aliquotaICMSTransporte = aliquotaICMSTransporte;
	}

	@Column(name = "INFO_COMPLEMENTARES_CONTRIBUINTE")
	public String getInfoComplementarContribuinte() {
		return infoComplementarContribuinte;
	}

	public void setInfoComplementarContribuinte(String infoComplementarContribuinte) {
		this.infoComplementarContribuinte = infoComplementarContribuinte;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "notaSaida")
	@OrderBy(value = "codigo")
	public List<ItemNotaSaida> getListaItensNotaSaida() {
		return listaItensNotaSaida;
	}

	public void setListaItensNotaSaida(List<ItemNotaSaida> listaItensNotaSaida) {
		this.listaItensNotaSaida = listaItensNotaSaida;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESP_ULTIMO_ARQUIVO")
	public Pessoa getResponsavelUltimoArquivo() {
		return responsavelUltimoArquivo;
	}

	public void setResponsavelUltimoArquivo(Pessoa responsavelUltimoArquivo) {
		this.responsavelUltimoArquivo = responsavelUltimoArquivo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_RESP_ULTIMO_ARQUIVO", length = 7)
	public Date getDataUltimoArquivo() {
		return dataUltimoArquivo;
	}

	public void setDataUltimoArquivo(Date dataUltimoArquivo) {
		this.dataUltimoArquivo = dataUltimoArquivo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESP_ENVIO_RFB")
	public Pessoa getResponsavelEnvioRfb() {
		return responsavelEnvioRfb;
	}

	public void setResponsavelEnvioRfb(Pessoa responsavelEnvioRfb) {
		this.responsavelEnvioRfb = responsavelEnvioRfb;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_RESP_ENVIO_RFB", length = 7)
	public Date getDataEnvioRfb() {
		return dataEnvioRfb;
	}

	public void setDataEnvioRfb(Date dataEnvioRfb) {
		this.dataEnvioRfb = dataEnvioRfb;
	}

	@OneToMany(mappedBy = "notaSaida")
	public List<FaturamentoEtapa> getFaturamentoEtapas() {
		return faturamentoEtapas;
	}

	public void setFaturamentoEtapas(List<FaturamentoEtapa> faturamentoEtapas) {
		this.faturamentoEtapas = faturamentoEtapas;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "notaSaida")
	public List<NotaSaidaParcela> getNotaSaidaParcelas() {
		return notaSaidaParcelas;
	}

	public void setNotaSaidaParcelas(List<NotaSaidaParcela> notaSaidaParcelas) {
		this.notaSaidaParcelas = notaSaidaParcelas;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_SERVICO_TIPO")
	public ServicoTipo getServicoTipo() {
		return servicoTipo;
	}

	public void setServicoTipo(ServicoTipo servicoTipo) {
		this.servicoTipo = servicoTipo;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_RESP_NUMERACAO")
	public Pessoa getResponsavelNumeracao() {
		return responsavelNumeracao;
	}

	public void setResponsavelNumeracao(Pessoa responsavelNumeracao) {
		this.responsavelNumeracao = responsavelNumeracao;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_RESP_CANC_NUMERACAO")
	public Pessoa getResponsavelCancelarNumeracao() {
		return responsavelCancelarNumeracao;
	}

	public void setResponsavelCancelarNumeracao(Pessoa responsavelCancelarNumeracao) {
		this.responsavelCancelarNumeracao = responsavelCancelarNumeracao;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_RESP_CAD_PREFEITURA")
	public Pessoa getResponsavelCadastroPrefeitura() {
		return responsavelCadastroPrefeitura;
	}

	public void setResponsavelCadastroPrefeitura(Pessoa responsavelCadastroPrefeitura) {
		this.responsavelCadastroPrefeitura = responsavelCadastroPrefeitura;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_RESP_NUMERACAO")
	public Date getDataNumeracao() {
		return dataNumeracao;
	}

	public void setDataNumeracao(Date dataNumeracao) {
		this.dataNumeracao = dataNumeracao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_RESP_CANC_NUMERACAO")
	public Date getDataCancelamentoNumeracao() {
		return dataCancelamentoNumeracao;
	}

	public void setDataCancelamentoNumeracao(Date dataCancelamentoNumeracao) {
		this.dataCancelamentoNumeracao = dataCancelamentoNumeracao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_RESP_CAD_PREFEITURA")
	public Date getDataCadastroPrefeitura() {
		return dataCadastroPrefeitura;
	}

	public void setDataCadastroPrefeitura(Date dataCadastroPrefeitura) {
		this.dataCadastroPrefeitura = dataCadastroPrefeitura;
	}

	@Column(name = "PROTOCOLO_ENTREGA")
	public String getProtocoloEntrega() {
		return protocoloEntrega;
	}

	public void setProtocoloEntrega(String protocoloEntrega) {
		this.protocoloEntrega = protocoloEntrega;
	}

	@Column(name = "iss_retido_cliente", nullable = false)
	public Boolean getIssRetidoCliente() {
		return issRetidoCliente;
	}

	@Column(name = "iss_retido_cliente_recebimento", nullable = false)
	public Boolean getIssRetidoClienteRecebimento() {
		return issRetidoClienteRecebimento;
	}

	public void setIssRetidoClienteRecebimento(Boolean issRetidoClienteRecebimento) {
		this.issRetidoClienteRecebimento = issRetidoClienteRecebimento;
	}

	public void setIssRetidoCliente(Boolean issRetidoCliente) {
		this.issRetidoCliente = issRetidoCliente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_nota_associada_cancelamento")
	public NotaSaida getNotaAssociadaCancelamento() {
		return notaAssociadaCancelamento;
	}

	public void setNotaAssociadaCancelamento(NotaSaida notaAssociadaCancelamento) {
		this.notaAssociadaCancelamento = notaAssociadaCancelamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_resp_nota_associada_cancelamento")
	public Pessoa getRespNotaAssociadaCancelamento() {
		return respNotaAssociadaCancelamento;
	}

	public void setRespNotaAssociadaCancelamento(Pessoa respNotaAssociadaCancelamento) {
		this.respNotaAssociadaCancelamento = respNotaAssociadaCancelamento;
	}

	@Column(name = "data_resp_nota_associada_cancelamento")
	public Date getDataRespNotaAssociadaCancelamento() {
		return dataRespNotaAssociadaCancelamento;
	}

	public void setDataRespNotaAssociadaCancelamento(Date dataRespNotaAssociadaCancelamento) {
		this.dataRespNotaAssociadaCancelamento = dataRespNotaAssociadaCancelamento;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NotaSaida)) return false;
		if (!super.equals(o)) return false;
		NotaSaida notaSaida = (NotaSaida) o;
		return Objects.equals(getEmitente(), notaSaida.getEmitente()) &&
			Objects.equals(getTranportadora(), notaSaida.getTranportadora()) &&
			Objects.equals(getMunicipioTransportadora(), notaSaida.getMunicipioTransportadora()) &&
			Objects.equals(getCfopTransporte(), notaSaida.getCfopTransporte()) &&
			Objects.equals(getMunicipioTransporte(), notaSaida.getMunicipioTransporte()) &&
			Objects.equals(getIsentoICMS(), notaSaida.getIsentoICMS()) &&
			Objects.equals(getInscricaoSuframa(), notaSaida.getInscricaoSuframa()) &&
			getTipoDocumentoTransportadora() == notaSaida.getTipoDocumentoTransportadora() &&
			Objects.equals(getDocumentoTransportadora(), notaSaida.getDocumentoTransportadora()) &&
			Objects.equals(getNomeTransportadora(), notaSaida.getNomeTransportadora()) &&
			Objects.equals(getInscricaoEstadualTransportadora(), notaSaida.getInscricaoEstadualTransportadora()) &&
			Objects.equals(getIsentoICMSTransportadora(), notaSaida.getIsentoICMSTransportadora()) &&
			Objects.equals(getEnderecoTransportadora(), notaSaida.getEnderecoTransportadora()) &&
			Objects.equals(getBaseCalculoICMSTransporte(), notaSaida.getBaseCalculoICMSTransporte()) &&
			Objects.equals(getAliquotaICMSTransporte(), notaSaida.getAliquotaICMSTransporte()) &&
			Objects.equals(getInfoComplementarContribuinte(), notaSaida.getInfoComplementarContribuinte()) &&
			Objects.equals(getListaItensNotaSaida(), notaSaida.getListaItensNotaSaida()) &&
			Objects.equals(getResponsavelUltimoArquivo(), notaSaida.getResponsavelUltimoArquivo()) &&
			Objects.equals(getDataUltimoArquivo(), notaSaida.getDataUltimoArquivo()) &&
			Objects.equals(getResponsavelEnvioRfb(), notaSaida.getResponsavelEnvioRfb()) &&
			Objects.equals(getDataEnvioRfb(), notaSaida.getDataEnvioRfb()) &&
			Objects.equals(getServicoTipo(), notaSaida.getServicoTipo()) &&
			Objects.equals(getProtocoloEntrega(), notaSaida.getProtocoloEntrega()) &&
			Objects.equals(getFaturamentoEtapas(), notaSaida.getFaturamentoEtapas()) &&
			Objects.equals(getNotaSaidaParcelas(), notaSaida.getNotaSaidaParcelas()) &&
			Objects.equals(getResponsavelNumeracao(), notaSaida.getResponsavelNumeracao()) &&
			Objects.equals(getDataNumeracao(), notaSaida.getDataNumeracao()) &&
			Objects.equals(getResponsavelCancelarNumeracao(), notaSaida.getResponsavelCancelarNumeracao()) &&
			Objects.equals(getDataCancelamentoNumeracao(), notaSaida.getDataCancelamentoNumeracao()) &&
			Objects.equals(getResponsavelCadastroPrefeitura(), notaSaida.getResponsavelCadastroPrefeitura()) &&
			Objects.equals(getDataCadastroPrefeitura(), notaSaida.getDataCadastroPrefeitura()) &&
			Objects.equals(getIssRetidoCliente(), notaSaida.getIssRetidoCliente());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getEmitente(), getTranportadora(), getMunicipioTransportadora(), getCfopTransporte(), getMunicipioTransporte(), getIsentoICMS(), getInscricaoSuframa(), getTipoDocumentoTransportadora(), getDocumentoTransportadora(), getNomeTransportadora(), getInscricaoEstadualTransportadora(), getIsentoICMSTransportadora(), getEnderecoTransportadora(), getBaseCalculoICMSTransporte(), getAliquotaICMSTransporte(), getInfoComplementarContribuinte(), getListaItensNotaSaida(), getResponsavelUltimoArquivo(), getDataUltimoArquivo(), getResponsavelEnvioRfb(), getDataEnvioRfb(), getServicoTipo(), getProtocoloEntrega(), getFaturamentoEtapas(), getNotaSaidaParcelas(), getResponsavelNumeracao(), getDataNumeracao(), getResponsavelCancelarNumeracao(), getDataCancelamentoNumeracao(), getResponsavelCadastroPrefeitura(), getDataCadastroPrefeitura(), getIssRetidoCliente());
	}

	@Override
	public String toString() {
		return "NotaSaida [emitente=" + emitente + ", tranportadora="
			+ tranportadora + ", municipioTransportadora="
			+ municipioTransportadora + ", cfopTransporte="
			+ cfopTransporte + ", municipioTransporte="
			+ municipioTransporte + ", isentoICMS=" + isentoICMS
			+ ", tipoDocumentoTransportadora="
			+ tipoDocumentoTransportadora + ", documentoTransportadora="
			+ documentoTransportadora + ", nomeTransportadora="
			+ nomeTransportadora + ", inscricaoEstadualTransportadora="
			+ inscricaoEstadualTransportadora
			+ ", isentoICMSTransportadora=" + isentoICMSTransportadora
			+ ", enderecoTransportadora=" + enderecoTransportadora
			+ ", baseCalculoICMSTransporte=" + baseCalculoICMSTransporte
			+ ", aliquotaICMSTransporte=" + aliquotaICMSTransporte
			+ ", infoComplementarContirubuinte="
			+ infoComplementarContribuinte + "]";
	}

	/**
	 * Obter o total de {@link ItemNotaSaida}.
	 *
	 * @return O total de {@link ItemNotaSaida}.
	 */
	@Transient
	public BigDecimal getValorTotal() {
		if (this.listaItensNotaSaida == null || this.listaItensNotaSaida.isEmpty()) {
			return BigDecimal.valueOf(0d);
		} else {
			BigDecimal total = BigDecimal.valueOf(0d);
			for (ItemNotaSaida itemNotaSaida : this.listaItensNotaSaida) {
				total = total.add(itemNotaSaida.getValorTotal());
			}
			return total.setScale(2, RoundingMode.HALF_UP);
		}
	}

	@Transient
	public String getChaveAcesso() {
		if (getChaveEletronica() == null || getChaveEletronica().trim().isEmpty()) {
			return null;
		}
		try {
			MaskFormatter formatter = new MaskFormatter("####.####.####.####.####.####.#.#########.##########");
			formatter.setValueContainsLiteralCharacters(false);
			return formatter.valueToString(getChaveEletronica());
		} catch (ParseException e) {
			return "#error!";
		}
	}

	/**
	 * Obter o total da base de cálculo do ICMS.
	 *
	 * @return O total da base de cálculo do ICMS.
	 */
	@Transient
	public BigDecimal getValorTotalBaseCalculoICMS() {
		if (this.listaItensNotaSaida.isEmpty()) {
			return null;
		} else {
			BigDecimal total = BigDecimal.valueOf(0d);
			for (ItemNotaSaida itemNotaSaida : this.listaItensNotaSaida) {
				if (itemNotaSaida.getBaseCalculoICMS() != null) {
					total = total.add(itemNotaSaida.getBaseCalculoICMS());
				}
			}
			return total.setScale(2, RoundingMode.HALF_UP);
		}
	}

	/**
	 * Obter o total do ICMS.
	 *
	 * @return O total do ICMS.
	 */
	@Transient
	public BigDecimal getValorTotalICMS() {
		if (this.listaItensNotaSaida.isEmpty()) {
			return null;
		} else {
			BigDecimal total = BigDecimal.valueOf(0d);
			for (ItemNotaSaida itemNotaSaida : this.listaItensNotaSaida) {
				if (itemNotaSaida.getValorDoIcms() != null) {
					total = total.add(new BigDecimal(itemNotaSaida.getValorDoIcms()));
				}
			}
			return total.setScale(2, RoundingMode.HALF_UP);
		}
	}

	/**
	 * Obter o total do IPI.
	 *
	 * @return O total do IPI.
	 */
	@Transient
	public BigDecimal getValorTotalIPI() {
		if (this.listaItensNotaSaida.isEmpty()) {
			return null;
		} else {
			BigDecimal total = BigDecimal.valueOf(0d);
			for (ItemNotaSaida itemNotaSaida : this.listaItensNotaSaida) {
				if (itemNotaSaida.getValorDoIpi() != null) {
					total = total.add(new BigDecimal(itemNotaSaida.getValorDoIpi()));
				}
			}
			return total.setScale(2, RoundingMode.HALF_UP);
		}
	}

	public enum TipoDocumentoTransportadora {
		CNPJ("CNPJ"),
		CPF("CPF");

		private final String descricao;

		private TipoDocumentoTransportadora(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return descricao;
		}
	}

}
