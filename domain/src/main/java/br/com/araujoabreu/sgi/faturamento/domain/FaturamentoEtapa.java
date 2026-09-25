package br.com.araujoabreu.sgi.faturamento.domain;

import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoDocumentoEtapa;
import br.com.araujoabreu.sgi.compras.domain.NotaDebito;
import br.com.araujoabreu.sgi.compras.domain.NotaSaida;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "FATURAMENTO_ETAPA")
@SequenceGenerator(name="SEQ_FATURAMENTO_ETAPA", sequenceName="SEQ_FATURAMENTO_ETAPA", allocationSize=1)
public class FaturamentoEtapa extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -2815356688648236032L;
	private Date dataCancelamento;
	private Date dataFaturamento;
	private String discriminacaoCentral;
	private String discriminacaoFinal;
	private String discriminacaoInicial;
	private Date inicio;
	private Status situacao;
	private Date termino;
	private BigDecimal valor = BigDecimal.ZERO;
	private BigDecimal valorPermuta = BigDecimal.ZERO;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorMulta = BigDecimal.ZERO;
	private BigDecimal valorJuros = BigDecimal.ZERO;
	private Faturamento faturamento;
	private NotaSaida notaSaida;
	private NotaDebito notaDebito;
	private Pessoa responsavelFaturamento;
	private Pessoa cancelador;
	private List<MotivoRetornoDocumentoEtapa> motivosRetornoDocumentoEtapa;
	private int numero;
	private BigDecimal valorTributos = BigDecimal.ZERO;
	private BigDecimal valorLiquido = BigDecimal.ZERO;
	private BigDecimal percentualTributos = BigDecimal.ZERO;
	private Boolean exibeRetencaoContratual = Boolean.TRUE;
	private Boolean exibeRetencaoAcessoria = Boolean.TRUE;
	private Boolean exibePermuta = Boolean.TRUE;
	private Date dataUltAlteracaoValor;
	private BigDecimal ultAlteracaoValor;
	private Long codigoUltAlteracaoValor;
	private String descricao;

	public FaturamentoEtapa() {
	}

	@Column(name = "data_ult_alteracao_valor")
	public Date getDataUltAlteracaoValor() {
		return dataUltAlteracaoValor;
	}

	public void setDataUltAlteracaoValor(Date dataUltAlteracaoValor) {
		this.dataUltAlteracaoValor = dataUltAlteracaoValor;
	}

	@Column(name = "ult_alteracao_valor")
	public BigDecimal getUltAlteracaoValor() {
		return ultAlteracaoValor;
	}

	public void setUltAlteracaoValor(BigDecimal ultAlteracaoValor) {
		this.ultAlteracaoValor = ultAlteracaoValor;
	}

	@Column(name = "codigo_ult_alteracao_valor")
	public Long getCodigoUltAlteracaoValor() {
		return codigoUltAlteracaoValor;
	}

	public void setCodigoUltAlteracaoValor(Long codigoUltAlteracaoValor) {
		this.codigoUltAlteracaoValor = codigoUltAlteracaoValor;
	}

	@Column(name = "DATA_CANCELAMENTO")
	public Date getDataCancelamento() {
		return this.dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	@Column(name = "DATA_FATURAMENTO")
	public Date getDataFaturamento() {
		return this.dataFaturamento;
	}

	public void setDataFaturamento(Date dataFaturamento) {
		this.dataFaturamento = dataFaturamento;
	}

	@Column(name = "DISCRIMINACAO_CENTRAL", length = 2000)
	public String getDiscriminacaoCentral() {
		return this.discriminacaoCentral;
	}

	public void setDiscriminacaoCentral(String discriminacaoCentral) {
		this.discriminacaoCentral = discriminacaoCentral;
	}

	@Column(name = "DISCRIMINACAO_FINAL", nullable = false, length = 2000)
	public String getDiscriminacaoFinal() {
		return this.discriminacaoFinal;
	}

	public void setDiscriminacaoFinal(String discriminacaoFinal) {
		this.discriminacaoFinal = discriminacaoFinal;
	}

	@Column(name = "DISCRIMINACAO_INICIAL", nullable = false, length = 2000)
	public String getDiscriminacaoInicial() {
		return this.discriminacaoInicial;
	}

	public void setDiscriminacaoInicial(String discriminacaoInicial) {
		this.discriminacaoInicial = discriminacaoInicial;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "INICIO", nullable = false)
	public Date getInicio() {
		return this.inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	@Column(name = "SITUACAO", nullable = false)
	public Status getSituacao() {
		return this.situacao;
	}

	public void setSituacao(Status situacao) {
		this.situacao = situacao;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "TERMINO", nullable = false)
	public Date getTermino() {
		return this.termino;
	}

	public void setTermino(Date termino) {
		this.termino = termino;
	}

	@Column(name = "VALOR", nullable = false, precision = 15, scale = 2)
	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Column(name = "VALOR_PERMUTA", nullable = false, precision = 9, scale = 2)
	public BigDecimal getValorPermuta() {
		return this.valorPermuta;
	}

	public void setValorPermuta(BigDecimal valorPermuta) {
		this.valorPermuta = valorPermuta;
	}

	@Column(name = "VALOR_DESCONTO", nullable = false, precision = 9, scale = 2)
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	@Column(name = "VALOR_MULTA", nullable = false, precision = 9, scale = 2)
	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	@Column(name = "VALOR_JUROS", nullable = false, precision = 9, scale = 2)
	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	//bi-directional many-to-one association to Faturamento
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_FATURAMENTO", nullable = false)
	public Faturamento getFaturamento() {
		return this.faturamento;
	}

	public void setFaturamento(Faturamento faturamento) {
		this.faturamento = faturamento;
	}

	//bi-directional many-to-one association to NotaSaida
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA_SAIDA")
	public NotaSaida getNotaSaida() {
		return this.notaSaida;
	}

	public void setNotaSaida(NotaSaida notaSaida) {
		this.notaSaida = notaSaida;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA_DEBITO")
	public NotaDebito getNotaDebito() {
		return notaDebito;
	}

	public void setNotaDebito(NotaDebito notaDebito) {
		this.notaDebito = notaDebito;
	}

	//bi-directional many-to-one association to Pessoa
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESP_FATURAMENTO")
	public Pessoa getResponsavelFaturamento() {
		return this.responsavelFaturamento;
	}

	public void setResponsavelFaturamento(Pessoa responsavelFaturamento) {
		this.responsavelFaturamento = responsavelFaturamento;
	}

	//bi-directional many-to-one association to Pessoa
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CANCELAMENTO")
	public Pessoa getCancelador() {
		return this.cancelador;
	}

	public void setCancelador(Pessoa cancelador) {
		this.cancelador = cancelador;
	}

	//bi-directional many-to-one association to MotivoRetornoDocumentoEtapa
	@OneToMany(mappedBy = "faturamentoEtapa")
	public List<MotivoRetornoDocumentoEtapa> getMotivosRetornoDocumentoEtapa() {
		return this.motivosRetornoDocumentoEtapa;
	}

	public void setMotivosRetornoDocumentoEtapa(List<MotivoRetornoDocumentoEtapa> motivoRetornoDocumentoEtapas) {
		this.motivosRetornoDocumentoEtapa = motivoRetornoDocumentoEtapas;
	}

	public MotivoRetornoDocumentoEtapa addMotivoRetornoDocumentoEtapa(MotivoRetornoDocumentoEtapa motivoRetornoDocumentoEtapa) {
		getMotivosRetornoDocumentoEtapa().add(motivoRetornoDocumentoEtapa);
		motivoRetornoDocumentoEtapa.setFaturamentoEtapa(this);

		return motivoRetornoDocumentoEtapa;
	}

	public MotivoRetornoDocumentoEtapa removeMotivoRetornoDocumentoEtapa(MotivoRetornoDocumentoEtapa motivoRetornoDocumentoEtapa) {
		getMotivosRetornoDocumentoEtapa().remove(motivoRetornoDocumentoEtapa);
		motivoRetornoDocumentoEtapa.setFaturamentoEtapa(null);

		return motivoRetornoDocumentoEtapa;
	}

	@Column(name = "DESCRICAO", nullable = false)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "exibe_retencao_contratual")
	public Boolean getExibeRetencaoContratual() {
		return exibeRetencaoContratual;
	}

	public void setExibeRetencaoContratual(Boolean exibeRetencaoContratual) {
		this.exibeRetencaoContratual = exibeRetencaoContratual;
	}

	@Column(name = "exibe_retencao_acessoria")
	public Boolean getExibeRetencaoAcessoria() {
		return exibeRetencaoAcessoria;
	}

	public void setExibeRetencaoAcessoria(Boolean exibeRetencaoAcessoria) {
		this.exibeRetencaoAcessoria = exibeRetencaoAcessoria;
	}

	@Column(name = "exibe_permuta")
	public Boolean getExibePermuta() {
		return exibePermuta;
	}

	public void setExibePermuta(Boolean exibePermuta) {
		this.exibePermuta = exibePermuta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		if (!(obj instanceof FaturamentoEtapa)) {
			return false;
		}
		FaturamentoEtapa other = (FaturamentoEtapa) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;
		return true;
	}

	@Transient
	public String formataNumero(Integer posicao) {
		if (posicao != null) {
			String num = "";
			if (posicao.toString().length() < 3) {
				for (int x = 0; x < (3 - posicao.toString().length()); x++) {
					num += 0;
				}
			}
			return num + posicao;
		}
		return "";
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numeroEtapa) {
		this.numero = numeroEtapa;//formataNumero(numeroEtapa);
	}

	@Deprecated
	@Transient
	public BigDecimal getValorTributos() {
		if (getPercentualTributos() != null && !Objects.equals(getPercentualTributos(), BigDecimal.ZERO))
			this.valorTributos = (getValor().multiply(getPercentualTributos())).divide(new BigDecimal("100.00"), 2, RoundingMode.HALF_UP);

		return valorTributos;
	}

	public void setValorTributos(BigDecimal valorTributos) {
		this.valorTributos = valorTributos;
	}

	@Transient
	public BigDecimal getValorLiquido() {
		if (getValor() != null && !Objects.equals(getValor(), BigDecimal.ZERO) && getValorTributos() != null && !Objects.equals(getValorTributos(), BigDecimal.ZERO))
			this.valorLiquido = getValor().subtract(getValorTributos());

		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	@Transient
	public BigDecimal getPercentualTributos() {
		return percentualTributos;
	}

	public void setPercentualTributos(BigDecimal percentualTributos) {
		this.percentualTributos = percentualTributos;
	}

	public enum Status {
		A_FATURAR("A faturar", "A faturar"),
		FATURADA("Faturada", "Faturada"),
		RECEBIDA("Recebida", "Recebida"),
		CANCELADA("Cancelada", "Cancelada");

		private final String descricao;
		private final String nome;

		private Status(String descricao, String nome) {
			this.descricao = descricao;
			this.nome = nome;
		}

		public String getDescricao() {
			return descricao;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return getNome();
		}

	}

}
