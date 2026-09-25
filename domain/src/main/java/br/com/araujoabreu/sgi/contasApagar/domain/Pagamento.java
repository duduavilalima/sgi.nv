package br.com.araujoabreu.sgi.contasApagar.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.Gerencia;
import br.com.araujoabreu.sgi.administracao.domain.NaturezaPagamento;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.Nota.TipoDocumentoNota;
import br.com.araujoabreu.sgi.compras.domain.NotaPagamento;
import br.com.araujoabreu.sgi.compras.domain.ParcelaNota;
import br.com.araujoabreu.sgi.contabilidade.domain.ItemLoteInterfaceCap;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaDesconto;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquota;
import br.com.araujoabreu.sgi.tesouraria.domain.ItemRelacao;
import br.com.araujoabreu.sgi.tesouraria.domain.ItemRelacaoPagamento;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.DadosBancarios;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * Representa uma Conta a Pagar, um Pagamento que deve ser efetuado.
 */
@Entity
@Table(name = "PAGAMENTO")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name="SEQ_PAGAMENTO", sequenceName="SEQ_PAGAMENTO", allocationSize=1)
public class Pagamento extends AuditEntity implements DomainObject, Serializable {

	private static final long serialVersionUID = 1476524116632776720L;
	private BigDecimal valor = BigDecimal.ZERO;
	private BigDecimal valorJuros = BigDecimal.ZERO;
	private BigDecimal valorMulta = BigDecimal.ZERO;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorDevolucao = BigDecimal.ZERO;
	private Date data;
	private Pessoa credor;
	private NaturezaPagamento naturezaPagamento;
	private String numero;
	private Date dataVencimento;
	private Date dataPagamento;
	private Date dataMovimento;
	private Date dataAprovador;
	private Date dataAutorizador;
	private Date dataLiberacao;
	private Date dataAnalista;
	private Status status;
	private TipoPagamento tipo;
	private String descricao;
	private int checagem;
	private Colaborador emissor;
	private Colaborador supervisor;
	private Colaborador aprovador;
	private Colaborador analista;
	private Colaborador autorizador;
	private Colaborador liberador;
	private CentroFinanceiro centroFinanceiro;
	private List<ItemRelacao> itemRelacoes;
	private Boolean existeBoleto;
	private Boolean selecao;
	private Boolean atualizadoTesouraria = Boolean.FALSE;
	private boolean linhaDigitavelInformada = false;
	private List<NotaTributoAliquota> listaNotaTributoAliquota;
	private List<Pagamento> listaPagamento;
	private Pagamento tributoAcumulado;
	private List<ItemRelacaoPagamento> listaItemRelacao;
	private DadosBancarios dadosBancarios;
	private Colaborador operadorTesouraria;
	private Date dataTesouraria;
	private TipoDocumentoPagamento tipoDocumentoPagamento;
	private String linhaDigitavel;
	private String codigoBarras;
	private Banco bancoBoleto;
	private BigDecimal valorAdiantamento = BigDecimal.ZERO;
	private NotaPagamento npAdiantamento;
	private List<ItemLoteInterfaceCap> listaItemLoteInterfaceCap;
	private BigDecimal valorPagar;

	/**
	 * Construtor padrão.
	 */
	public Pagamento() {
		// Padrão.
	}

	public Pagamento(Long codigo) {
		setCodigo(codigo);
	}

	public Pagamento(Date dataPagamento) {
		super();
		this.dataPagamento = dataPagamento;
	}

	/**
	 * Construtor com instância de credor ({@link Pessoa}) e {@link DadosBancarios}.
	 *
	 * @param credor           O credor do pagamento.
	 * @param dadosBancarios   Os {@link DadosBancarios} do pagamento.
	 * @param centroFinanceiro O {@link CentroFinanceiro} do pagamento.
	 */
	public Pagamento(final Pessoa credor, final DadosBancarios dadosBancarios, final CentroFinanceiro centroFinanceiro) {
		super();
		this.credor = credor;
		this.dadosBancarios = dadosBancarios;
		this.centroFinanceiro = centroFinanceiro;
	}

	@Transient
	public boolean isAberto() {
		return Status.ABERTO.equals(getStatus());
	}

	@Transient
	public boolean isReAberto() {
		return Status.REABERTO.equals(getStatus());
	}

	@Transient
	public boolean isAjusteFiscal() {
		return Status.AJUSTE_FISCAL.equals(getStatus());
	}

	@Transient
	public boolean isCancelamento() {
		return Status.CANCELAMENTO.equals(getStatus());
	}

	@Transient
	public boolean isNotaFiscal() {
		return TipoPagamento.NOTA_FISCAL.equals(getTipo());
	}

	@Transient
	public boolean isTributo() {
		return TipoPagamento.TRIBUTO.equals(getTipo());
	}

	@Transient
	public boolean isNotaPagamento() {
		return TipoPagamento.NOTA_PAGAMENTO.equals(getTipo());
	}

	/**
	 * Verificar se {@link Pagamento} é um tributo acumulado.
	 *
	 * @return <code>true</code> caso seja um tributo acumulado.
	 * <code>false</code> caso contrário.
	 */
	@Transient
	public boolean checkTributoAcumulado() {
		return TipoPagamento.TRIBUTO_ACUMULADO.equals(getTipo());
	}

	@Transient
	public Gerencia getGerencia() {
		if (centroFinanceiro instanceof Gerencia) {
			return ((Gerencia) centroFinanceiro);

		} else if (centroFinanceiro instanceof CentroFinanceiro && centroFinanceiro.getPai() != null) {
			return ((Gerencia) centroFinanceiro.getPai());

		} else if (centroFinanceiro != null && (Gerencia.class.isAssignableFrom(Hibernate.getClass(centroFinanceiro)))) {
			return (Gerencia) ((HibernateProxy) centroFinanceiro).getHibernateLazyInitializer().getImplementation();
		}
		return null;
	}

	@Transient
	public Unidade getUnidade() {
		return getGerencia() != null ? getGerencia().getUnidade() : null;
	}

	/*
	 * FIXME: Por algum motivo, o hibernate estava lendo esse método.
	 * Modificado o nome para não haver concorrência com o método "get".
	 */

	@Column(name = "VALOR_ADIANTAMENTO", precision = 19, scale = 2)
	public BigDecimal getValorAdiantamento() {
		return valorAdiantamento;
	}

	public void setValorAdiantamento(BigDecimal valorAdiantamento) {
		this.valorAdiantamento = valorAdiantamento;
	}

	/*
	 * Definido para EAGER, pois estava causando erro carregar a tela de análise do pagamento
	 * na opção Conta a pagar > Seleção de contas a pagar
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JoinColumn(name = "CODIGO_NP_ADIANTAMENTO")
	public NotaPagamento getNpAdiantamento() {
		return npAdiantamento;
	}

	public void setNpAdiantamento(NotaPagamento npAdiantamento) {
		this.npAdiantamento = npAdiantamento;
	}

	@Column(name = "VALOR", nullable = false, precision = 19, scale = 2)
	@NotNull
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Column(name = "DATA", nullable = false)
	@NotNull
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	// Para conseguirmos a associacao polimorfica, precisamos usar Fetch Type = EAGER...
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CREDOR")
	public Pessoa getCredor() {
		return credor;
	}

	public void setCredor(Pessoa credor) {
		this.credor = credor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_TIPO_PAGAMENTO", nullable = false)
	@NotNull
	public NaturezaPagamento getNaturezaPagamento() {
		return naturezaPagamento;
	}

	public void setNaturezaPagamento(NaturezaPagamento naturezaPagamento) {
		this.naturezaPagamento = naturezaPagamento;
	}

	@Column(name = "NUMERO", nullable = false, length = 25)
	@NotNull
	@Length(max = 25)
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		if (numero != null) {
			if (TipoPagamento.NOTA_FISCAL.equals(this.getTipo())) {
				if (!numero.contains("NF")) {
					numero = "NF/" + numero;
				}
			} else if (TipoPagamento.NOTA_PAGAMENTO.equals(this.getTipo())) {
				if (!numero.contains("NP")) {
					numero = "NP/" + numero;
				}
			} else if (TipoPagamento.TRIBUTO.equals(this.getTipo())) {
				if (!numero.contains("TR")) {
					numero = "TR/" + numero;
				}
			} else if (TipoPagamento.TRIBUTO_ACUMULADO.equals(this.getTipo())) {
				if (!numero.contains("TA")) {
					numero = "TA/" + numero;
				}
			}
		}
		this.numero = numero;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_VENCIMENTO", nullable = false, length = 7)
	@NotNull
	public Date getDataVencimento() {
		return this.dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_PAGAMENTO", nullable = false, length = 7)
	@NotNull
	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_MOVIMENTO", length = 7)
	public Date getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "STATUS")
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TIPO", nullable = false)
	@NotNull
	public TipoPagamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoPagamento tipo) {
		this.tipo = tipo;
	}

	@Column(name = "OBSERVACAO")
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "CHECAGEM", nullable = false)
	@NotNull
	public int getChecagem() {
		return checagem;
	}

	public void setChecagem(int checagem) {
		this.checagem = checagem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_EMISSOR")
	public Colaborador getEmissor() {
		return emissor;
	}

	public void setEmissor(Colaborador emissor) {
		this.emissor = emissor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_SUPERVISOR")
	public Colaborador getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Colaborador supervisor) {
		this.supervisor = supervisor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ANALISTA")
	public Colaborador getAnalista() {
		return analista;
	}

	public void setAnalista(Colaborador analista) {
		this.analista = analista;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_APROVADOR")
	public Colaborador getAprovador() {
		return aprovador;
	}

	public void setAprovador(Colaborador aprovador) {
		this.aprovador = aprovador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_AUTORIZADOR")
	public Colaborador getAutorizador() {
		return autorizador;
	}

	public void setAutorizador(Colaborador autorizador) {
		this.autorizador = autorizador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinTable(name = "ITEM_RELACAO_PAGAMENTO", joinColumns = {@JoinColumn(name = "CODIGO_PAGAMENTO")}, inverseJoinColumns = {@JoinColumn(name = "CODIGO_ITEM_RELACAO")})
	@OrderBy(value = "codigo")
	public List<ItemRelacao> getItemRelacoes() {
		return itemRelacoes;
	}

	public void setItemRelacoes(List<ItemRelacao> itemRelacoes) {
		this.itemRelacoes = itemRelacoes;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_aprovador")
	public Date getDataAprovador() {
		return dataAprovador;
	}

	public void setDataAprovador(Date dataAprovador) {
		this.dataAprovador = dataAprovador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_autorizador")
	public Date getDataAutorizador() {
		return dataAutorizador;
	}

	public void setDataAutorizador(Date dataAutorizador) {
		this.dataAutorizador = dataAutorizador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_liberacao")
	public Date getDataLiberacao() {
		return dataLiberacao;
	}

	public void setDataLiberacao(Date dataLiberacao) {
		this.dataLiberacao = dataLiberacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_liberacao")
	public Colaborador getLiberador() {
		return liberador;
	}

	public void setLiberador(Colaborador liberador) {
		this.liberador = liberador;
	}

	@Column(name = "existeboleto")
	public Boolean getExisteBoleto() {
		return existeBoleto;
	}

	public void setExisteBoleto(Boolean existeBoleto) {
		this.existeBoleto = existeBoleto;
	}

	@Column(name = "atualizado_tesouraria")
	public Boolean getAtualizadoTesouraria() {
		return atualizadoTesouraria;
	}

	public void setAtualizadoTesouraria(Boolean atualizadoTesouraria) {
		this.atualizadoTesouraria = atualizadoTesouraria;
	}

	@Transient
	public boolean isLinhaDigitavelInformada() {
		return linhaDigitavelInformada;
	}

	public void setLinhaDigitavelInformada(boolean linhaDigitavelInformada) {
		this.linhaDigitavelInformada = linhaDigitavelInformada;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_analista")
	public Date getDataAnalista() {
		return dataAnalista;
	}

	public void setDataAnalista(Date dataAnalista) {
		this.dataAnalista = dataAnalista;
	}

	@Column(name = "VALOR_JUROS", precision = 9, scale = 2)
	public BigDecimal getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}

	@Column(name = "VALOR_MULTA", precision = 9, scale = 2)
	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	@Column(name = "VALOR_DESCONTO", precision = 9, scale = 2)
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	@Column(name = "VALOR_DEVOLUCAO", precision = 9, scale = 2)
	public BigDecimal getValorDevolucao() {
		return valorDevolucao;
	}

	public void setValorDevolucao(BigDecimal valorDevolucao) {
		this.valorDevolucao = valorDevolucao;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pagamento", fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy(value = "codigo")
	public List<NotaTributoAliquota> getListaNotaTributoAliquota() {
		return this.listaNotaTributoAliquota;
	}

	public void setListaNotaTributoAliquota(List<NotaTributoAliquota> listaNotaTributoAliquota) {
		this.listaNotaTributoAliquota = listaNotaTributoAliquota;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tributoAcumulado")
	@OrderBy(value = "numero")
	public List<Pagamento> getListaPagamento() {
		return this.listaPagamento;
	}

	public void setListaPagamento(List<Pagamento> listaPagamento) {
		this.listaPagamento = listaPagamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_TRIBUTO_ACUMULADO")
	public Pagamento getTributoAcumulado() {
		return tributoAcumulado;
	}

	public void setTributoAcumulado(Pagamento tributoAcumulado) {
		this.tributoAcumulado = tributoAcumulado;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pagamento")
	@OrderBy(value = "codigo")
	public List<ItemRelacaoPagamento> getListaItemRelacao() {
		return listaItemRelacao;
	}

	public void setListaItemRelacao(List<ItemRelacaoPagamento> listaItemRelacao) {
		this.listaItemRelacao = listaItemRelacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_DADOS_BANCARIOS")
	public DadosBancarios getDadosBancarios() {
		return dadosBancarios;
	}

	public void setDadosBancarios(DadosBancarios dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_OPERADOR_TESOURARIA")
	public Colaborador getOperadorTesouraria() {
		return operadorTesouraria;
	}

	public void setOperadorTesouraria(Colaborador operadorTesouraria) {
		this.operadorTesouraria = operadorTesouraria;
	}

	@Column(name = "DATA_TESOURARIA")
	public Date getDataTesouraria() {
		return dataTesouraria;
	}

	public void setDataTesouraria(Date dataTesouraria) {
		this.dataTesouraria = dataTesouraria;
	}

	@Column(name = "TIPO_DOC_PAGAMENTO")
	public TipoDocumentoPagamento getTipoDocumentoPagamento() {
		return tipoDocumentoPagamento;
	}

	public void setTipoDocumentoPagamento(TipoDocumentoPagamento tipoDocumentoPagamento) {
		this.tipoDocumentoPagamento = tipoDocumentoPagamento;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Column(name = "LINHA_DIGITAVEL")
	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}

	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}

	@Column(name = "CODIGO_BARRAS", length = 44)
	@Length(max = 44)
	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	@Override
	public String toString() {
		return "Pagamento{" +
			"valor=" + valor +
			", numero='" + numero + '\'' +
			", dataPagamento=" + dataPagamento +
			", status=" + status +
			"} " + super.toString();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_BANCO_BOLETO")
	public Banco getBancoBoleto() {
		return bancoBoleto;
	}

	public void setBancoBoleto(Banco bancoBoleto) {
		this.bancoBoleto = bancoBoleto;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pagamento", cascade = CascadeType.ALL)
	@OrderBy(value = "codigo")
	public List<ItemLoteInterfaceCap> getListaItemLoteInterfaceCap() {
		return listaItemLoteInterfaceCap;
	}

	public void setListaItemLoteInterfaceCap(List<ItemLoteInterfaceCap> listaItemLoteInterfaceCap) {
		this.listaItemLoteInterfaceCap = listaItemLoteInterfaceCap;
	}

	/**
	 * Obter o valor do pagamento incluindo multas e juros.
	 *
	 * @return O valor do pagamento incluindo multas e juros.
	 */
	@Transient
	public BigDecimal getValorPagar() {
		this.valorPagar = new BigDecimal(0);

		if (this.valorJuros == null)
			this.valorJuros = BigDecimal.ZERO;

		if (this.valorMulta == null)
			this.valorMulta = BigDecimal.ZERO;

		if (this.valorDesconto == null)
			this.valorDesconto = BigDecimal.ZERO;

		this.valorPagar = getValorLiquido().add(this.valorJuros).add(this.valorMulta).subtract(this.valorDesconto);

		this.valorPagar = valorPagar.setScale(2, RoundingMode.HALF_EVEN);

		return this.valorPagar;
	}

	public void setValorPagar(BigDecimal valorPagar) {
		this.valorPagar = valorPagar;
	}

	@Transient
	public NotaTributoAliquota getNotaTributoAliquota() {
		if (listaNotaTributoAliquota != null && !listaNotaTributoAliquota.isEmpty()) {
			return listaNotaTributoAliquota.get(0);
		}
		return null;
	}

	@Transient
	public BigDecimal getValorJurosMulta() {
		BigDecimal valor = BigDecimal.ZERO;

		if (this.valorJuros != null) {
			valor = valor.add(valorJuros);
		}

		if (this.valorMulta != null) {
			valor = valor.add(valorMulta);
		}

		return valor;
	}

	@Transient
	public BigDecimal getTotalOutrosDescontos() {
		if (this.getTipo() != null) {
			if (this.getTipo().equals(TipoPagamento.NOTA_FISCAL) || this.getTipo().equals(TipoPagamento.NOTA_PAGAMENTO)) {
				if (this instanceof ParcelaNota) {
					ParcelaNota parcela = ((ParcelaNota) this);
					if (parcela != null && parcela.getOrdem() != null && parcela.getOrdem() == 0) {
						BigDecimal total = new BigDecimal(0);
						if (this instanceof ParcelaNota) {
							Nota nota = ((ParcelaNota) this).getNota();
							if (nota.getListaNotaDesconto() != null && !nota.getListaNotaDesconto().isEmpty()) {
								for (NotaDesconto notaDesconto : nota.getListaNotaDesconto()) {
									total = total.add(notaDesconto.getValor());
								}
							}
						}
						return total;
					}
				}
			}
		}
		return BigDecimal.ZERO;
	}

	public void setTotalOutrosDescontos(BigDecimal totalOutrosDescontos) {
	}

	@Transient
	public BigDecimal getValorLiquido() {
		if (this.getTipo() != null) {
			if (this.getTipo().equals(TipoPagamento.TRIBUTO)) {
				return this.getValor();
			} else if (this.getTipo().equals(TipoPagamento.TRIBUTO_ACUMULADO)) {
//		        return calcularTotalAcumulado();
				return BigDecimal.ZERO;
			}

			if (this.valorDevolucao == null)
				this.valorDevolucao = BigDecimal.ZERO;

			BigDecimal valorLiquido = this.getValor();
			valorLiquido = valorLiquido.subtract(getTotalOutrosDescontos());
//		    valorLiquido = valorLiquido.subtract(calcularTotalAcumulado());
			valorLiquido = valorLiquido.subtract(this.valorDevolucao);

			if (valorAdiantamento != null)
				valorLiquido = valorLiquido.subtract(valorAdiantamento);

			return valorLiquido;
		}

		return new BigDecimal(0);

	}

	/**
	 * Status do {@link Pagamento}.
	 */
	public enum Status {
		ABERTO("Aberta"), // 0
		AJUSTE_FISCAL("Ajuste Fiscal"), // 1
		ANALISE("Em Análise"), // 2
		REABERTO("Reaberta"), // 3
		AUTORIZACAO("Em Autorização"), // 4
		CANCELAMENTO("Em Cancelamento"), // 5
		CANCELADO("Cancelada"), // 6
		AUTORIZADO("Autorizada"), // 7
		PAGAMENTO("Em Pagamento"), // 8
		RELACIONADO("Relacionada"), // 9
		FINALIZADO("Finalizada"), // 10
		APROVACAO("Em Aprovação"), // 11
		AGUARDANDO_AGRUPAMENTO("Aguardando Agrupamento"), // 12
		AGRUPADO("Agrupado"), // 13
		EM_HOMOLOGACAO_FUNDO_FIXO("Aguardando homologação (Fundo Fixo)"), // 14
		AGUARDANDO_LIBERACAO_RN("Aguardando liberação da RN"), // 15
		NAO_FINALIZADO("Não Finalizado"); // 16

		private final String descricao;

		private Status(String descricao) {
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

	/**
	 * Tipo de {@link Pagamento}.
	 */
	public enum TipoPagamento {
		NOTA_FISCAL("NF"),
		NOTA_PAGAMENTO("NP"),
		TRIBUTO("TR"),
		TRIBUTO_ACUMULADO("TA");

		private final String descricao;

		private TipoPagamento(String descricao) {
			this.descricao = descricao;
		}

		public static TipoPagamento getTipoPagamentoPorOrdinal(int ordinal) {
			for (TipoPagamento tipo : TipoPagamento.values()) {
				if (tipo.ordinal() == ordinal)
					return tipo;
			}

			return null;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return getDescricao();
		}

	}

	/**
	 * Tipo de documento do {@link Pagamento}.
	 */
	public enum TipoDocumentoPagamento {
		BOLETO("Boleto", "Boleto", TipoDocumentoNota.BOLETO_ANEXO),
		DEPOSITO("Depósito em conta/Transferência", "Depósito/Tranf.", TipoDocumentoNota.DEPOSITO),
		DDA("DDA", "DDA", TipoDocumentoNota.DEPOSITO),
		GUIA("Guia", "Guia", TipoDocumentoNota.DUPLICATA),
		DOC("DOC", "DOC", TipoDocumentoNota.DEPOSITO),
		TED("TED", "TED", TipoDocumentoNota.DEPOSITO),
		CONCESSIONARIA("Concessionária", "Concessionária", TipoDocumentoNota.DUPLICATA),
		OUTROS("Outros", "Outros", TipoDocumentoNota.DUPLICATA);

		private final String descricao;
		private final String abreviacao;
		private final TipoDocumentoNota tipoDePara;

		private TipoDocumentoPagamento(String descricao, String abreviacao, TipoDocumentoNota tipoDePara) {
			this.descricao = descricao;
			this.abreviacao = abreviacao;
			this.tipoDePara = tipoDePara;
		}

		public String getDescricao() {
			return descricao;
		}

		public String getAbreviacao() {
			return abreviacao;
		}

		public TipoDocumentoNota getTipoDePara() {
			return tipoDePara;
		}

		@Override
		public String toString() {
			return getDescricao();
		}
	}

}
