package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.Avaliacao;
import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.NaturezaFornecimento.TipoFornecimento;
import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.araujoabreu.sgi.compras.enums.TipoFrete;
import br.com.araujoabreu.sgi.compras.enums.TipoSeguro;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.TipoPagamento;
import br.com.araujoabreu.sgi.fundoFixo.domain.ItemDemonstrativo;
import br.com.araujoabreu.sgi.inrf.domain.NotaFiscalErroProcesso;
import br.com.araujoabreu.sgi.inrf.domain.Processo;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaJuridica;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Representa uma Nota Fiscal emitida ao término do processo de compra.
 * <br>
 * Para notas originadas a partir do processo cotação, um Pedido deverá ser associado ao nota
 * para as informaçoes da cotação.
 * <br>
 * Uma Nota Fiscal é composta por uma lista de Itens e um número de parcelas que geram pagamentos no contas a pagar.
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "NOTA_FISCAL")
public class NotaFiscal extends Nota implements Detail {

	private static final long serialVersionUID = 8647935109853833429L;
	private Date dataEmissao;
	private String serie;
	private String subserie;
	private Date limiteEmissao;
	private Date saida;
	private Date conclusao;
	private String chaveEletronica;
	private BigDecimal desconto;
	private BigDecimal valorFrete;
	private BigDecimal valorSeguro;
	private BigDecimal valorIpi;
	private BigDecimal valorOutrasDespesas;
	private TipoFrete tipoFrete;
	private TipoSeguro tipoSeguro;
	private NotaPagamento notaPagamento;
	private DocumentoFiscal documentoFiscal;
	private NaturezaNotaFiscal natureza = NaturezaNotaFiscal.VENDA_AVISTA;
	private List<ItemNotaFiscal> items = new ArrayList<>();
	private List<NotaFiscal> notasFiscais = new LinkedList<>();
	private List<Recebimento> recebimentos = new LinkedList<>();
	private NotaFiscal notaFiscalTransporte;
	private NaturezaAquisicao naturezaAquisicao;
	private TipoFornecimento tipoFornecimento;
	private TipoNotaFiscal tipoNotaFiscal;
	private TipoCobranca tipoCobranca;
	private List<ItemRelacaoNotaFiscal> listaItemRelacaoNotaFiscal = new ArrayList<>();
	private List<NotaPagamento> notasPagamento = new ArrayList<>();
	private List<NotaFiscalErroProcesso> listaNotaFiscalErroProcesso = new ArrayList<>();
	private ItemDemonstrativo itemDemonstrativo;
	private List<Avaliacao> listaAvaliacao;
	private Cfop cfop;
	private NotaFiscal notaFiscalOrigem;
	private String horaSaida;
	private Processo processo;
	private Pessoa clienteReembolso;

	public NotaFiscal() {
		setCredor(new PessoaJuridica());
		setTipo(TipoPagamento.NOTA_FISCAL);
	}
	public NotaFiscal(Pessoa credor) {
		setCredor(credor);
	}

	public void addItem(ItemNotaFiscal item) {
		item.setNotaFiscal(this);
		items.add(item);
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_EMISSAO", nullable = false, length = 7)
	@NotNull
	public Date getDataEmissao() {
		return this.dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		super.setData(dataEmissao);
		this.dataEmissao = dataEmissao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CFOP")
	public Cfop getCfop() {
		return cfop;
	}

	public void setCfop(Cfop cfop) {
		this.cfop = cfop;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA_FISCAL_ORIGEM")
	public NotaFiscal getNotaFiscalOrigem() {
		return notaFiscalOrigem;
	}

	public void setNotaFiscalOrigem(NotaFiscal notaFiscalOrigem) {
		this.notaFiscalOrigem = notaFiscalOrigem;
	}

	@Column(name = "HORA_SAIDA")
	public String getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(String horaSaida) {
		this.horaSaida = horaSaida;
	}

	@Column(name = "SERIE", length = 5)
	@Length(max = 5)
	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	@Column(name = "SUBSERIE", length = 5)
	@Length(max = 5)
	public String getSubserie() {
		return subserie;
	}

	public void setSubserie(String subserie) {
		this.subserie = subserie;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LIMITE_EMISSAO", length = 7)
	public Date getLimiteEmissao() {
		return this.limiteEmissao;
	}

	public void setLimiteEmissao(Date limiteEmissao) {
		this.limiteEmissao = limiteEmissao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAIDA")
	public Date getSaida() {
		return saida;
	}

	public void setSaida(Date dataSaida) {
		this.saida = dataSaida;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CONCLUSAO")
	public Date getConclusao() {
		return conclusao;
	}

	public void setConclusao(Date conclusao) {
		this.conclusao = conclusao;
	}

	@Column(name = "CHAVE_ELETRONICA", length = 44)
	@Length(max = 44)
	public String getChaveEletronica() {
		return chaveEletronica;
	}

	public void setChaveEletronica(String chaveEletronica) {
		this.chaveEletronica = chaveEletronica;
	}

	@Column(name = "DESCONTO", precision = 9, scale = 2)
	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	@Column(name = "VALOR_FRETE", precision = 9, scale = 2)
	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	@Column(name = "VALOR_SEGURO", precision = 9, scale = 2)
	public BigDecimal getValorSeguro() {
		return valorSeguro;
	}

	public void setValorSeguro(BigDecimal valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	@Column(name = "VALOR_IPI", precision = 9, scale = 2)
	public BigDecimal getValorIpi() {
		return valorIpi;
	}

	public void setValorIpi(BigDecimal valorIpi) {
		this.valorIpi = valorIpi;
	}

	@Column(name = "VALOR_OUTRAS_DESPESAS", precision = 9, scale = 2)
	public BigDecimal getValorOutrasDespesas() {
		return valorOutrasDespesas;
	}

	public void setValorOutrasDespesas(BigDecimal valorOutrasDespesas) {
		this.valorOutrasDespesas = valorOutrasDespesas;
	}

	@Column(name = "TIPO_FORNECIMENTO")
	public TipoFornecimento getTipoFornecimento() {
		return tipoFornecimento;
	}

	public void setTipoFornecimento(TipoFornecimento tipo) {
		this.tipoFornecimento = tipo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_TIPO_COBRANCA")
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_DOCUMENTO_FISCAL", nullable = false)
	@NotNull
	public DocumentoFiscal getDocumentoFiscal() {
		return documentoFiscal;
	}

	public void setDocumentoFiscal(DocumentoFiscal documentoFiscal) {
		this.documentoFiscal = documentoFiscal;
	}

	@Column(name = "TIPO_NOTA_FISCAL")
	public TipoNotaFiscal getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}

	public void setTipoNotaFiscal(TipoNotaFiscal tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
	}

	@Column(name = "TIPO_FRETE")
	@Enumerated(EnumType.ORDINAL)
	public TipoFrete getTipoFrete() {
		return tipoFrete;
	}

	public void setTipoFrete(TipoFrete tipoFrete) {
		this.tipoFrete = tipoFrete;
	}

	@Column(name = "TIPO_SEGURO")
	@Enumerated(EnumType.ORDINAL)
	public TipoSeguro getTipoSeguro() {
		return tipoSeguro;
	}

	public void setTipoSeguro(TipoSeguro tipoSeguro) {
		this.tipoSeguro = tipoSeguro;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "CODIGO_NOTA_PAGAMENTO")
	public NotaPagamento getNotaPagamento() {
		return notaPagamento;
	}

	public void setNotaPagamento(NotaPagamento notaPagamento) {
		this.notaPagamento = notaPagamento;
	}

	@Column(name = "NATUREZA", nullable = false)
	@NotNull
	public NaturezaNotaFiscal getNatureza() {
		return natureza;
	}

	public void setNatureza(NaturezaNotaFiscal natureza) {
		this.natureza = natureza;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "notaFiscal")
	@OrderBy(value = "codigo")
	public List<ItemNotaFiscal> getItems() {
		return this.items;
	}

	public void setItems(List<ItemNotaFiscal> items) {
		this.items = items;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "notaFiscal")
	public List<ItemRelacaoNotaFiscal> getListaItemRelacaoNotaFiscal() {
		return listaItemRelacaoNotaFiscal;
	}

	public void setListaItemRelacaoNotaFiscal(List<ItemRelacaoNotaFiscal> listaItemRelacaoNotaFiscal) {
		this.listaItemRelacaoNotaFiscal = listaItemRelacaoNotaFiscal;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "notaFiscal")
	@OrderBy(value = "codigo")
	public List<NotaPagamento> getNotasPagamento() {
		return notasPagamento;
	}

	public void setNotasPagamento(List<NotaPagamento> notasPagamento) {
		this.notasPagamento = notasPagamento;
	}

	@Transient
	public PessoaJuridica getFornecedor() {
		if (getCredor() instanceof PessoaJuridica) {
			return (PessoaJuridica) getCredor();
		}
		return null;
	}

	@Transient
	public void setFornecedor(PessoaJuridica fornecedor) {
		setCredor(fornecedor);
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "notaFiscal")
	@OrderBy(value = "codigo DESC")
	public List<Avaliacao> getListaAvaliacao() {
		return listaAvaliacao;
	}

	public void setListaAvaliacao(List<Avaliacao> listaAvaliacao) {
		this.listaAvaliacao = listaAvaliacao;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA_FISCAL_TRANSPORTE")
	public List<NotaFiscal> getNotasFiscais() {
		return notasFiscais;
	}

	public void setNotasFiscais(List<NotaFiscal> notasFiscais) {
		this.notasFiscais = notasFiscais;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "notaFiscal")
	public List<Recebimento> getRecebimentos() {
		return recebimentos;
	}

	public void setRecebimentos(List<Recebimento> itens) {
		this.recebimentos = itens;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA_FISCAL_TRANSPORTE")
	public NotaFiscal getNotaFiscalTransporte() {
		return notaFiscalTransporte;
	}

	public void setNotaFiscalTransporte(NotaFiscal notaFiscalTransporte) {
		this.notaFiscalTransporte = notaFiscalTransporte;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NATUREZA_AQUISICAO")
	public NaturezaAquisicao getNaturezaAquisicao() {
		return naturezaAquisicao;
	}

	public void setNaturezaAquisicao(NaturezaAquisicao naturezaAquisicao) {
		this.naturezaAquisicao = naturezaAquisicao;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "notaFiscal")
	public ItemDemonstrativo getItemDemonstrativo() {
		return itemDemonstrativo;
	}

	public void setItemDemonstrativo(ItemDemonstrativo itemDemonstrativo) {
		this.itemDemonstrativo = itemDemonstrativo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PROCESSO")
	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CLIENTE")
	public Pessoa getClienteReembolso() {
		return clienteReembolso;
	}

	public void setClienteReembolso(Pessoa clienteReembolso) {
		this.clienteReembolso = clienteReembolso;
	}

	@Transient
	public boolean isReembolso() {
		// TODO : ver comentário abaixo
		/* não há mais nota de reembolso, logo todas as ações que se referem a este método não devem ser executadas.
		 * Por enquanto método simplemente retorna false, mas o código que o utilizar deve ser revisto.
		 */
//		return (TipoNotaFiscal.REEMBOLSO.equals(tipoNotaFiscal));
		return false;
	}

	@Transient
	public boolean isFundoFixo() {
		return TipoNotaFiscal.FUNDO_FIXO.equals(tipoNotaFiscal)
			|| TipoNotaFiscal.REEMBOLSO.equals(tipoNotaFiscal)
			|| TipoNotaFiscal.ADIANTAMENTO.equals(tipoNotaFiscal)
			|| TipoNotaFiscal.OC_PC_ADIANTAMENTO.equals(tipoNotaFiscal)
			|| TipoNotaFiscal.OC_PC_FUNDO_FIXO.equals(tipoNotaFiscal)
			|| TipoNotaFiscal.OC_PC_REEMBOLSO.equals(tipoNotaFiscal);
	}

	@Transient
	public boolean isNormal() {
		return TipoNotaFiscal.NORMAL.equals(tipoNotaFiscal)
			|| TipoNotaFiscal.OC_PC_ADIANTAMENTO.equals(tipoNotaFiscal)
			|| TipoNotaFiscal.OC_PC_FUNDO_FIXO.equals(tipoNotaFiscal)
			|| TipoNotaFiscal.OC_PC_REEMBOLSO.equals(tipoNotaFiscal);
	}

	@Transient
	public boolean isAvulsa() {
		return TipoNotaFiscal.AVULSA.equals(tipoNotaFiscal);
	}

	@Transient
	public boolean isTransporte() {
		return TipoNotaFiscal.TRANSPORTE.equals(tipoNotaFiscal);
	}

	/**
	 * Retorna verdadeiro para OC de natureza de fornecimento de serviço.
	 *
	 * @return
	 */
	@Transient
	public boolean isServico() {
		return TipoFornecimento.SERVICOS.equals(getTipoFornecimento());
	}

	@Transient
	public boolean isMaterial() {
		return TipoFornecimento.MATERIAIS.equals(getTipoFornecimento());
	}

	@Transient
	public boolean isConjugado() {
		return TipoFornecimento.CONJUGADO.equals(getTipoFornecimento());
	}

	/**
	 * A nota gera pagamentos do tipo NOTA_FISCAL
	 *
	 * @return
	 */
	@Override
	@Transient
	public TipoPagamento getTipo() {
		return TipoPagamento.NOTA_FISCAL;
	}

	@Override
	@Transient
	public Boolean getPrincipal() {
		return Boolean.FALSE;
	}

	@Override
	@Transient
	public void setPrincipal(Boolean principal) {
	}

	@Transient
	public String getLabelTributosAplicaveis() {
		if (tipoFornecimento != null) {
			return tipoFornecimento.equals(TipoFornecimento.MATERIAIS) ? "Não aplicável" : "Tributos aplicáveis";
		}
		return "Não aplicável";
	}

	@Transient
	public BigDecimal getTotalSemImpostos() {
		BigDecimal totalSemImpostos = new BigDecimal("0");
		if (getValor() != null) {
			totalSemImpostos = getValor();
			if (valorFrete != null)
				totalSemImpostos = totalSemImpostos.subtract(valorFrete);
			if (valorIpi != null)
				totalSemImpostos = totalSemImpostos.subtract(valorIpi);
			if (valorOutrasDespesas != null)
				totalSemImpostos = totalSemImpostos.subtract(valorOutrasDespesas);
			if (valorSeguro != null)
				totalSemImpostos = totalSemImpostos.subtract(valorSeguro);
		}
		return totalSemImpostos;
	}

	@Transient
	public BigDecimal getTotalNota() {
		BigDecimal totalNota = new BigDecimal("0");
		if (getValor() != null) {
			totalNota = totalNota.add(getTotalItens());
			if (desconto != null)
				totalNota = totalNota.subtract(desconto);
			if (valorFrete != null)
				totalNota = totalNota.add(valorFrete);
			if (valorSeguro != null)
				totalNota = totalNota.add(valorSeguro);
			if (valorIpi != null)
				totalNota = totalNota.add(valorIpi);
			if (valorOutrasDespesas != null)
				totalNota = totalNota.add(valorOutrasDespesas);
		}
		return totalNota;
	}

	@Transient
	public String getCentrosFinanceirosAbreviados() {
		StringBuilder sbCentroFinanceiro = new StringBuilder();

		if (items != null && !items.isEmpty()) {
			List<CentroFinanceiro> listaCF = new ArrayList<>();
			for (ItemNotaFiscal itemNF : this.getItems()) {
				for (RateioNotaFiscal rateio : itemNF.getRateios()) {
					if (!listaCF.contains(rateio.getCentroFinanceiro()))
						listaCF.add(rateio.getCentroFinanceiro());
				}
			}

			Comparator<CentroFinanceiro> comparator = (arg0, arg1) -> {
				if (arg0 != null && arg0.getCodigo() != null
					&& arg1 != null && arg1.getCodigo() != null) {
					return arg0.getIdentificacaoFinal().compareTo(arg1.getIdentificacaoFinal());
				}
				return -1;
			};
			listaCF.sort(comparator);

			for (CentroFinanceiro cf : listaCF) {
				if (cf != null && cf.getCodigo() != null) {
					sbCentroFinanceiro.append(cf.getIdentificacaoFinal()).append(" / ");
				}
			}
		}
		return sbCentroFinanceiro.toString();
	}

	@Transient
	public boolean isCentroFinanceiroUnico() {
		CentroFinanceiro centroFinanceiro = new CentroFinanceiro();
		if (items != null && !items.isEmpty()) {
			for (ItemNotaFiscal item : items) {
				if (item != null) {
					for (RateioNotaFiscal rateio : item.getRateios()) {
						if (centroFinanceiro != null && !rateio.getCentroFinanceiro().getCodigo().equals(centroFinanceiro.getCodigo())) {
							return false;
						}
						centroFinanceiro = rateio.getCentroFinanceiro();
					}
				}
			}
		}
		return true;
	}

	@Transient
	public String getCentroFinanceiroRateioAgrupado() {
		if (getRateiosAgrupados() != null && !getRateiosAgrupados().isEmpty()) {
			if (getRateiosAgrupados().size() == 1) {
				return getRateiosAgrupados().get(0).getCentroFinanceiro().getIdentificadorFinal();
			} else
				return "Vários<sup>" + getRateiosAgrupados().size() + "</sup>";
		}
		return "";
	}

	@Transient
	public boolean isRateio() {
		if (this.getRateiosAgrupados() != null && !this.getRateiosAgrupados().isEmpty()) {
			return true;
		}
		return false;
	}

	@Transient
	public List<RateioNotaFiscal> getRateiosAgrupados() {
		List<RateioNotaFiscal> rateiosAgrupados = new ArrayList<>();
		if (items != null && !items.isEmpty()) {
			List<RateioNotaFiscal> rateios = new ArrayList<>();
			for (ItemNotaFiscal item : items) {
				rateios.addAll(item.getRateios());
			}
			List<Rateio> rateiosAux = super.getRateiosAgrupado(new ArrayList<>(rateios));
			for (Rateio rateioAux : rateiosAux) {
				rateiosAgrupados.add((RateioNotaFiscal) rateioAux);
			}
		}
		return rateiosAgrupados;
	}

	@Transient
	public int getQuantidadeItens() {
		if (this.getTipoNotaFiscal().equals(TipoNotaFiscal.NORMAL)) {
			if (this.getRecebimentos() != null) {
				return this.getRecebimentos().size();
			}
		} else {
			if (this.getItems() != null) {
				return this.getItems().size();
			}
		}
		return 0;
	}

	@Transient
	private BigDecimal getTotalItensOC() {
		BigDecimal total = new BigDecimal("0");
		for (Recebimento recebimento : recebimentos) {
			for (ItemRecebimento item : recebimento.getItens()) {
				total = total.add(item.getValor()).setScale(2, RoundingMode.HALF_EVEN);
			}
		}
		return total;
	}

	@Transient
	private BigDecimal getTotalItensAvulsa() {
		BigDecimal total = new BigDecimal("0");
		if (items != null && !items.isEmpty()) {
			for (ItemNotaFiscal item : items) {
				if (item != null && item.getCodigo() != null) {
					total = total.add(item.getValorTotal()).setScale(2, RoundingMode.HALF_UP);
				}
			}
		}
		return total;
	}

	@Transient
	public BigDecimal getTotalServicos() {
		BigDecimal total = new BigDecimal("0");
		if (tipoNotaFiscal.equals(TipoNotaFiscal.NORMAL)) {
			total = getTotalServicosOC();
		} else {
			total = getTotalServicosAvulsa();
		}
		return total;
	}

	@Transient
	private BigDecimal getTotalServicosOC() {
		BigDecimal total = new BigDecimal("0");
		for (Recebimento recebimento : recebimentos) {
			for (ItemRecebimento item : recebimento.getItens()) {
				if (item != null && item.getItemPedido().getItemProposta().getItemCompra().getTipoItem().getServico()) {
					BigDecimal totalItem = item.getValor();
					total = total.add(totalItem).setScale(2, RoundingMode.HALF_UP);
				}
			}
		}
		return total;
	}

	@Transient
	private BigDecimal getTotalServicosAvulsa() {
		BigDecimal total = new BigDecimal("0");
		if (items != null && !items.isEmpty()) {
			for (ItemNotaFiscal item : items) {
				if (item != null && item.getItemCompra().getNaturezaFornecimento().getTipo().equals(TipoFornecimento.SERVICOS)) {
					BigDecimal totalItem = item.getValor();
					total = total.add(totalItem).setScale(2, RoundingMode.HALF_UP);
				}
			}
		}
		return total;
	}

	@Transient
	public BigDecimal getTotalItens() {
		BigDecimal total = new BigDecimal("0");
		if (tipoNotaFiscal != null) {
			if (tipoNotaFiscal.equals(TipoNotaFiscal.NORMAL)) {
				total = getTotalItensOC();
			} else {
				total = getTotalItensAvulsa();
			}
		}
		return total;
	}

	@Transient
	public BigDecimal getTotalItensMaterial() {
		BigDecimal total = new BigDecimal("0");
		if (TipoFornecimento.MATERIAIS.equals(this.getTipoFornecimento()) || TipoFornecimento.CONJUGADO.equals(this.getTipoFornecimento())) {
			if (tipoNotaFiscal.equals(TipoNotaFiscal.NORMAL)) {
				total = getTotalItensOC();
			} else {
				total = getTotalItensAvulsa();
			}
		}
		return total;
	}

	@Transient
	public BigDecimal getValorRecebimentos() {
		BigDecimal valor = BigDecimal.ZERO;
		for (Recebimento recebimento : recebimentos) {
			valor = valor.add(recebimento.getTotal());
		}
		return valor.setScale(4, RoundingMode.HALF_EVEN);
	}

	@Transient
	public List<ItemNotaFiscal> getItensMateriais() {
		List<ItemNotaFiscal> itens = new ArrayList<>();
		for (ItemNotaFiscal itemNotaFiscal : items) {
			if (itemNotaFiscal.getItemCompra().getNaturezaFornecimento().getTipo().equals(TipoFornecimento.MATERIAIS)) {
				itens.add(itemNotaFiscal);
			}
		}
		return itens;
	}

	@Transient
	public List<ItemNotaFiscal> getItensServicos() {
		List<ItemNotaFiscal> itens = new ArrayList<>();
		for (ItemNotaFiscal itemNotaFiscal : items) {
			if (itemNotaFiscal.getItemCompra().getNaturezaFornecimento().getTipo().equals(TipoFornecimento.SERVICOS)) {
				itens.add(itemNotaFiscal);
			}
		}
		return itens;
	}

	@Transient
	public BigDecimal getTotalItensServicos() {
		return this.getTotalItens(TipoFornecimento.SERVICOS);
	}

	@Transient
	public BigDecimal getTotalItensMateriais() {
		return this.getTotalItens(TipoFornecimento.MATERIAIS);
	}

	@Transient
	public BigDecimal getTotalItens(TipoFornecimento tipoFornecimento) {
		BigDecimal total = new BigDecimal("0");
		List<ItemNotaFiscal> itens = new ArrayList<>();
		if (tipoFornecimento.equals(TipoFornecimento.MATERIAIS)) itens = this.getItensMateriais();
		if (tipoFornecimento.equals(TipoFornecimento.SERVICOS)) itens = this.getItensServicos();
		for (ItemNotaFiscal itemNotaFiscal : itens) {
			total = total.add(itemNotaFiscal.getValor());
		}
		return total;
	}

	@Transient
	private BigDecimal getValor(ItemRequisicaoCompra itemRequisicaoCompra, List<ItemRecebimento> itens) {
		for (ItemRecebimento itemRecebimento : itens) {
			if (itemRequisicaoCompra.getItemCompra().equals(itemRecebimento.getItemPedido().getItemProposta().getItemCompra())) {
				return itemRecebimento.getQuantidade().multiply(itemRecebimento.getPreco());
			}
		}
		return BigDecimal.ZERO;
	}

	@Transient
	public Map<CentroFinanceiro, BigDecimal> getValorPorCentroFinanceiro() {
		Map<CentroFinanceiro, BigDecimal> mapaRateio = new LinkedHashMap<>();
		for (Recebimento item : recebimentos) {
			Pedido pedido = item.getPedido();
			for (RequisicaoCompraRefinada requisicaoRefinada : pedido.getOrdemCompra().getRequisicoesCompraRefinadas()) {
				CentroFinanceiro cfRequisicaoCompra = requisicaoRefinada.getRequisicaoCompra().getCentroFinanceiro();
				BigDecimal valorCentroFinanceiro = mapaRateio.getOrDefault(cfRequisicaoCompra, BigDecimal.ZERO);
				for (ItemRequisicaoCompra itemRequisicaoCompra : requisicaoRefinada.getItens()) {
					valorCentroFinanceiro = valorCentroFinanceiro.add(getValor(itemRequisicaoCompra, item.getItens()));
				}
				mapaRateio.put(cfRequisicaoCompra, valorCentroFinanceiro);
			}
		}
		return mapaRateio;
	}

	@Transient
	public List<CentroFinanceiro> getValorPorCentroFinanceiroKeys() {
		List<CentroFinanceiro> keys = new ArrayList<>(getValorPorCentroFinanceiro().keySet());
		return keys;
	}

	@Transient
	public boolean isRateioOC() {
		if (this.getValorPorCentroFinanceiro() != null && !this.getValorPorCentroFinanceiro().isEmpty()) {
			return true;
		}
		return false;
	}

	@Transient
	public String getCentroFinanceiroRateioAgrupadoByIndice(int indice) {
		CentroFinanceiro centroFinanceiro = new CentroFinanceiro();
		if (tipoNotaFiscal.equals(TipoNotaFiscal.AVULSA)) {
			if (getRateiosAgrupados() != null && !getRateiosAgrupados().isEmpty()) {
				if (getRateiosAgrupados().size() == 1) {
					centroFinanceiro = getRateiosAgrupados().get(0).getCentroFinanceiro();
				}
			}
		}

		if (tipoNotaFiscal.equals(TipoNotaFiscal.NORMAL)) {
			if (getValorPorCentroFinanceiro() != null && getValorPorCentroFinanceiro().size() == 1) {
				centroFinanceiro = getValorPorCentroFinanceiro().keySet().iterator().next();
			}
		}

		if (centroFinanceiro != null && centroFinanceiro.getCodigo() != null)
			return centroFinanceiro.getIdentificadorFinal();
		else
			return "Vários<sup>" + indice + "</sup>";
	}

	@Transient
	public RelacaoNotaFiscal getRelacaoNotaFiscal() {
		if (listaItemRelacaoNotaFiscal != null && !listaItemRelacaoNotaFiscal.isEmpty()) {
			Comparator<ItemRelacaoNotaFiscal> comparator = (arg0, arg1) -> arg1.getRelacaoNotaFiscal().getCodigo().compareTo(arg0.getRelacaoNotaFiscal().getCodigo());

			listaItemRelacaoNotaFiscal.sort(comparator);
			return listaItemRelacaoNotaFiscal.get(0).getRelacaoNotaFiscal();
		}
		return new RelacaoNotaFiscal();
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "notaFiscal")
	public List<NotaFiscalErroProcesso> getListaNotaFiscalErroProcesso() {
		return listaNotaFiscalErroProcesso;
	}

	public void setListaNotaFiscalErroProcesso(List<NotaFiscalErroProcesso> listaNotaFiscalErroProcesso) {
		this.listaNotaFiscalErroProcesso = listaNotaFiscalErroProcesso;
	}

	public boolean notaFiscalComErro() {
		if (listaNotaFiscalErroProcesso != null && !listaNotaFiscalErroProcesso.isEmpty()) {
			return true;
		}
		return false;
	}

	@Transient
	public ParcelaNota getAdiantamento() {
		if (getParcelas() != null && !getParcelas().isEmpty()) {
			for (ParcelaNota parcelaNota : getParcelas()) {
				if (parcelaNota.getNpAdiantamento() != null && parcelaNota.getNpAdiantamento().getCodigo() != null)
					return parcelaNota;
			}
		}
		return null;
	}

	//** Dados de Auditoria **//
//	@Override
	@Transient
	public Long getEntityCode() {
		if (getCodigo() == null) {
			return null;
		}
		return getCodigo();
	}

	//	@Override
	@Transient
	public String getAuditMessage() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String cr = "\n";
		StringBuilder stbMessage = new StringBuilder();

		stbMessage.append("Numero : ").append(this.getNumero()).append(cr);
		stbMessage.append("Tipo da nota : ").append(this.getTipoNotaFiscal()).append(cr);
		stbMessage.append("Fornecedor : ").append(this.getFornecedor().getCodigo()).append(" - ").append(this.getFornecedor().getNome()).append(cr);
		stbMessage.append("Chave eletrônica : ").append(this.chaveEletronica).append(cr);
		stbMessage.append("Valor dos itens : ").append(this.getTotalItens()).append(cr);
		stbMessage.append("Valor total : ").append(super.getValor()).append(cr);
		stbMessage.append("Emissão : ").append(sdf.format(this.getDataEmissao())).append(cr);
		stbMessage.append("Vencimento : ").append(sdf.format(this.getDataVencimento())).append(cr);

		if (this.getItems() != null && !this.getItems().isEmpty()) {
			stbMessage.append(cr).append("ITENS : ").append(cr);
			for (ItemNotaFiscal inf : this.getItems())
				stbMessage.append(" - ").append(inf.mensagemAuditoriaNF()).append(cr);
		}

		if (this.getRecebimentos() != null && !this.getRecebimentos().isEmpty()) {
			stbMessage.append(cr).append("OCs : ").append(cr);
			for (Recebimento rec : this.getRecebimentos())
				stbMessage.append(" - ").append(rec.getPedido().getOrdemCompra().getNumero()).append(cr);
		}

		stbMessage.append(cr);
		stbMessage.append("Data criacao : ").append(super.getDataCriacao() == null ? "" : sdf.format(super.getDataCriacao())).append(" - Colaborador : ").append(super.getCriador()).append(cr);
		stbMessage.append("Data atualizacao : ").append(super.getDataAtualizacao() == null ? "" : sdf.format(super.getDataAtualizacao())).append(" - Colaborador : ").append(this.getAtualizador()).append(cr);

		return stbMessage.toString();
	}

	//	@Override
	@Transient
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}

	public enum TipoNotaFiscal {
		/**
		 * Fornecimento.
		 */
		AVULSA("Fornecimento", "Forn", "Fornecimento", 0),
		/**
		 * Ordem de Compra.
		 */
		NORMAL("Ordem de Compra", "OC", "Ordem de compra", 1),
		FUNDO_FIXO("NF/PC/Fundo Fixo", "NF/PC/FF", "Prestação de contas - Fornecimento - Fundo Fixo", 2),
		REEMBOLSO("NF/PC/Reembolso", "NF/PC/RE", "Prestação de contas - Fornecimento - Reembolso", 3),
		TRANSPORTE("Transporte", "Trans", "Transporte", 8),
		ADIANTAMENTO("NF/PC/Adiantamento", "NF/PC/AD", "Prestação de contas - Fornecimento - Adiantamento", 4),
		DEVOLUCAO_PARCIAL("Devolução parcial", "DP", "Devolução parcial", 9),// Usado somente para Nota de Saída.
		DEVOLUCAO_TOTAL("Devolução total", "DT", "Devolução total", 10),// Usado somente para Nota de Saída.
		OC_PC_FUNDO_FIXO("OC/PC/Fundo Fixo", "OC/PC/FF", "Prestação de contas - Ordem de Compra - Fundo Fixo", 5),
		OC_PC_REEMBOLSO("OC/PC/Reembolso", "OC/PC/RE", "Prestação de contas - Ordem de Compra - Reembolso", 6),
		OC_PC_ADIANTAMENTO("OC/PC/Adiantamento", "OC/PC/AD", "Prestação de contas - Ordem de Compra - Adiantamento", 7),
		FATURAMENTO("NF/Faturamento", "NF/Fat", "Faturamento", 11),
		FATURAMENTO_EXCLUSAO_LOGICA("NF/Faturamento - Exclusão lógica", "NF/Fat/EL", "Faturamento - Exclusão lógica", 12);

		private final String nome;
		private final String abreviado;
		private final String hint;
		private final Integer ordem;

		private TipoNotaFiscal(String nome, String abreviado, String hint, Integer ordem) {
			this.nome = nome;
			this.abreviado = abreviado;
			this.hint = hint;
			this.ordem = ordem;
		}

		public String getNome() {
			return nome;
		}

		public String getAbreviado() {
			return abreviado;
		}

		public String getHint() {
			return hint;
		}

		public Integer getOrdem() {
			return ordem;
		}
	}

	public enum NaturezaNotaFiscal {
		VENDA_AVISTA("Venda à vista"),
		VENDA_APRAZO("Venda a prazo");

		private final String descricao;

		private NaturezaNotaFiscal(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return getDescricao();
		}
	}

	public enum Divergencia {
		INDIFERENTE("Indiferente"),
		COM_DIVERGENCIA("Só exibir NFs COM divergência entre aquisição e o tipo do item"),
		SEM_DIVERGENCIA("Exibir NFs SEM divergência entre aquisição e o tipo do item");

		private final String descricao;

		private Divergencia(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}

	public enum SituacaoNota {
		ABERTA_REABERTA("Aberta/Reaberta", null, true),
		ABERTA("Aberta", Nota.StatusNota.ABERTA, true),
		REABERTA("Reaberta", Nota.StatusNota.REABERTA, true),
		EM_APROVACAO_NOVA_NF("Em Aprovação (nova nota)", Nota.StatusNota.EM_APROVACAO_NOVA_NF, true),
		EM_APROVACAO_RETORNO_FINANCEIRO("Em Aprovação (retorno financeiro)", Nota.StatusNota.EM_APROVACAO_RETORNO_FINANCEIRO, true),
		EM_APROVACAO_RETORNO_AJUSTES("Em Aprovação (após ajustes)", Nota.StatusNota.EM_APROVACAO_RETORNO_AJUSTES, true),
		EM_AJUSTE_FISCAL("Em ajuste fiscal", StatusNota.EM_AJUSTE_FISCAL, true),
		APROVADA(" Aprovada ", Nota.StatusNota.APROVADA, true),
		CANCELADA(" Cancelada ", Nota.StatusNota.CANCELADA, true),
		EM_APROVACAO_FUNDO_FIXO("Em Aprovação (Prestação de contas)", Nota.StatusNota.EM_APROVACAO_FUNDO_FIXO, true),
		EM_RELACAO_NOTAS("Em relação de notas", Nota.StatusNota.EM_RELACAO_NOTAS, true),
		RECEBIDA_FINANCEIRO("Recebida pelo financeiro", Nota.StatusNota.RECEBIDA_FINANCEIRO, true),
		SEPARACAO("----------------------------------------", null, false);

		private final String nomeStatus;
		private final Nota.StatusNota statusNota;
		private final boolean habilitado; //indica se a opção ficará habilitad no componente selectOneMenu

		private SituacaoNota(String nomeStatus, Nota.StatusNota statusNota, boolean habilitado) {
			this.nomeStatus = nomeStatus;
			this.statusNota = statusNota;
			this.habilitado = habilitado;
		}

		public Nota.StatusNota getStatusNota() {
			return statusNota;
		}

		public boolean isHabilitado() {
			return habilitado;
		}

		public String getNomeStatus() {
			return nomeStatus;
		}

	}

}
