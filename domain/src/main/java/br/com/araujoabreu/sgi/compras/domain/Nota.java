package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.Contrato;
import br.com.araujoabreu.sgi.administracao.domain.Gerencia;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoNota;
import br.com.araujoabreu.sgi.administracao.domain.NaturezaPagamento;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.Status;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.TipoDocumentoPagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.Pagamento.TipoPagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaDesconto;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquotaEmissao;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Representa uma nota emitida para pagamento de um credor.
 * <p>
 * O numero nao é gerado automaticamente e deve ser informado pelo
 * - usuario.setNumero(getTipo().getDescricao()+"/"+getCodigo());
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "NOTA")
public class Nota extends Despesa {

	private static final long serialVersionUID = 4375896885378543564L;

	private String numero;
	private Pessoa credor;
	private NaturezaPagamento naturezaPagamento;
	private StatusNota status = StatusNota.ABERTA;
	private TipoPagamento tipo;
	private CentroFinanceiro centroFinanceiro;
	private List<NotaTributoAliquota> listaNotaTributoAliquota = null;
	private List<NotaTributoAliquotaEmissao> listaNotaTributoAliquotaEmissao = null;
	private List<ParcelaNota> parcelas = new ArrayList<>();
	private List<MotivoRetornoNota> motivosRetorno;
	private Colaborador emissor;
	private Date dataEmissor;
	private FormaPagamento formaPagamento = FormaPagamento.PARCELA_UNICA;
	private Unidade empresa;
	private boolean conferenciaContabilidade;
	private Colaborador conferenciaColaborador;
	private Date conferenciaDataAtualizacao;
	private Colaborador gerente;
	private Date dataContrFiscal;
	private Colaborador contrFiscal;
	private String duplicata;
	private TipoDocumentoNota tipoDocumentoNota;
	private Integer numBoletoAnexo;
	private List<NotaAnexo> anexos = new ArrayList<>();
	private List<NotaDesconto> listaNotaDesconto;
	//inidica quando a NF retornou para ajustes da área financeira(Ajuste Fiscal ou CAP) para o gerente
	private Date dataRetornoFinanceiro;
	private Boolean selecao;
	private Pessoa responsavelCancelamento;
	private Date dataCancelamento;
	private Pessoa responsavelReversaoCancelamento;
	private Date dataReversaoCancelamento;

	@Transient
	public Gerencia getGerencia() {
		if (centroFinanceiro instanceof Gerencia) {
			return ((Gerencia) centroFinanceiro);
		} else if (centroFinanceiro instanceof CentroFinanceiro && centroFinanceiro.getPai() != null) {
			return ((Gerencia) centroFinanceiro.getPai());
		} else {
			return (Gerencia) centroFinanceiro;
		}
	}

	// Para conseguirmos a associacao polimorfica, precisamos usar Fetch Type = EAGER...
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CREDOR", nullable = false)
	@NotNull
	public Pessoa getCredor() {
		if (credor instanceof HibernateProxy) {
			credor = (Pessoa) ((HibernateProxy) credor).getHibernateLazyInitializer().getImplementation();
		}

		return credor;
	}

	public void setCredor(Pessoa credor) {
		this.credor = credor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_TIPO_PAGAMENTO")
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
		if (numero == null) {
			numero = "";
		}

		if (TipoPagamento.NOTA_FISCAL.equals(this.getTipo()) && (numero != null && numero.length() > 0)) {
			if (!numero.contains("NF")) {
				numero = "NF/" + numero;
			}
		} else if (TipoPagamento.NOTA_PAGAMENTO.equals(this.getTipo())) {
			if (!numero.contains("NP")) {
				numero = "NP/" + numero;
			}
		}
		this.numero = numero;
	}

//	@Transient
//	public Unidade getUnidade() {
//		return getGerencia().getUnidade();
//	}

	@Column(name = "STATUS", nullable = false)
	@NotNull
	public StatusNota getStatus() {
		return status;
	}

	public void setStatus(StatusNota status) {
		this.status = status;
	}

	/**
	 * Define o tipo da nota. NF, NP...
	 *
	 * @return
	 */
	@Column(name = "TIPO", nullable = false)
	@NotNull
	public TipoPagamento getTipo() {
		return this.tipo;
	}

	public void setTipo(TipoPagamento tipo) {
		this.tipo = tipo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "nota")
	public List<NotaTributoAliquota> getListaNotaTributoAliquota() {
		return this.listaNotaTributoAliquota;
	}

	public void setListaNotaTributoAliquota(List<NotaTributoAliquota> listaNotaTributoAliquota) {
		if (this.listaNotaTributoAliquota == null) {
			this.listaNotaTributoAliquota = listaNotaTributoAliquota;
		} else {
			this.listaNotaTributoAliquota.retainAll(listaNotaTributoAliquota);
			this.listaNotaTributoAliquota.addAll(listaNotaTributoAliquota);
		}
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "nota")
	public List<NotaTributoAliquotaEmissao> getListaNotaTributoAliquotaEmissao() {
		return listaNotaTributoAliquotaEmissao;
	}

	public void setListaNotaTributoAliquotaEmissao(List<NotaTributoAliquotaEmissao> listaNotaTributoAliquotaEmissao) {
		this.listaNotaTributoAliquotaEmissao = listaNotaTributoAliquotaEmissao;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "nota")
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotNull
	@OrderColumn(name = "ordem")
	public List<ParcelaNota> getParcelas() {
		return parcelas;
	}

	public void setParcelas(List<ParcelaNota> parcelas) {
		this.parcelas = parcelas;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_EMISSOR")
	public Colaborador getEmissor() {
		return emissor;
	}

	public void setEmissor(Colaborador emissor) {
		this.emissor = emissor;
	}

	@Column(name = "FORMA_PAGAMENTO", nullable = false, length = 1)
	@NotNull
	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	@Transient
	public boolean isAberta() {
		return StatusNota.ABERTA.equals(getStatus());
	}

	/**
	 * Retorna a data de vencimento da primeira parcela.
	 *
	 * @return data de vencimento da primeira parcela. Null caso nota não tenha parcelas.
	 */
	@Transient
	public Date getDataVencimento() {
		return (getParcelas().isEmpty() ? null : getParcelas().get(0).getDataVencimento());
	}

	/**
	 * Retorna verdadeiro caso exista alguma parcela com status ReAberta.
	 *
	 * @return
	 */
	@Transient
	public boolean isReaberta() {
		return StatusNota.REABERTA.equals(getStatus());
	}

	/**
	 * Retorna verdadeira se existe parcela com status Em Cancelamento.
	 *
	 * @return
	 */
	@Transient
	public boolean hasCancelamento() {
		for (ParcelaNota parcela : getParcelas()) {
			if (Status.CANCELAMENTO.equals(parcela.getStatus())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * A Nota poderá ser alterada se a sua primeira estiver em um estágio que permite a edição.
	 *
	 * @return
	 */
	@Transient
	public boolean isEditavel() {
		return (getParcelas().isEmpty() || getParcelas().get(0).isEditavel());
	}

	/**
	 * Retorno o status da primeira parcela que não está finalizada. Retorna STATUS.FINALIZADO caso todas as parcelas estejam finalizadas
	 *
	 * @return
	 */
	@Transient
	public Status getStatusParcela() {
		for (ParcelaNota parcela : getParcelas()) {
			if (!Status.FINALIZADO.equals(parcela.getStatus())) {
				return parcela.getStatus();
			}
		}
		return Status.FINALIZADO;
	}

	/**
	 * Retorna verdadeiro se a nota foi paga com parcela única.
	 *
	 * @return
	 */
	@Transient
	public boolean isParcelaUnica() {
		return FormaPagamento.PARCELA_UNICA.equals(getFormaPagamento());
	}

	@Transient
	public FormaPagamento[] getFormasPagamento() {
		return FormaPagamento.values();
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_emissor")
	public Date getDataEmissor() {
		return dataEmissor;
	}

	public void setDataEmissor(Date dataEmissor) {
		this.dataEmissor = dataEmissor;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "nota")
	public List<MotivoRetornoNota> getMotivosRetorno() {
		return motivosRetorno;
	}

	public void setMotivosRetorno(List<MotivoRetornoNota> motivosRetorno) {
		this.motivosRetorno = motivosRetorno;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "nota")
	public List<NotaAnexo> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<NotaAnexo> anexos) {
		this.anexos = anexos;
	}

	@Column(name = "data_retorno_financeiro")
	public Date getDataRetornoFinanceiro() {
		return dataRetornoFinanceiro;
	}

	public void setDataRetornoFinanceiro(Date dataRetornoFinanceiro) {
		this.dataRetornoFinanceiro = dataRetornoFinanceiro;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_empresa")
	public Unidade getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Unidade empresa) {
		this.empresa = empresa;
	}

	@Column(name = "CONFERENCIA_CONTABILIDADE", length = 1)
	public boolean isConferenciaContabilidade() {
		return conferenciaContabilidade;
	}

	public void setConferenciaContabilidade(boolean conferenciaContabilidade) {
		this.conferenciaContabilidade = conferenciaContabilidade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONFERENCIA_CODIGO_COLABORADOR")
	public Colaborador getConferenciaColaborador() {
		return conferenciaColaborador;
	}

	public void setConferenciaColaborador(Colaborador conferenciaColaborador) {
		this.conferenciaColaborador = conferenciaColaborador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONFERENCIA_DATA_ATUALIZACAO")
	public Date getConferenciaDataAtualizacao() {
		return conferenciaDataAtualizacao;
	}

	public void setConferenciaDataAtualizacao(Date conferenciaDataAtualizacao) {
		this.conferenciaDataAtualizacao = conferenciaDataAtualizacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_GERENTE")
	public Colaborador getGerente() {
		return gerente;
	}

	public void setGerente(Colaborador gerente) {
		this.gerente = gerente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_contr_fiscal")
	public Colaborador getContrFiscal() {
		return contrFiscal;
	}

	public void setContrFiscal(Colaborador contrFiscal) {
		this.contrFiscal = contrFiscal;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_contr_fiscal")
	public Date getDataContrFiscal() {
		return dataContrFiscal;
	}

	public void setDataContrFiscal(Date dataContrFiscal) {
		this.dataContrFiscal = dataContrFiscal;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "nota")
	public List<NotaDesconto> getListaNotaDesconto() {
		return listaNotaDesconto;
	}

	public void setListaNotaDesconto(List<NotaDesconto> listaNotaDesconto) {
		this.listaNotaDesconto = listaNotaDesconto;
	}

	@Transient
	public BigDecimal getTotalDescontos() {
		BigDecimal total = new BigDecimal("0");
		if (listaNotaDesconto != null && !listaNotaDesconto.isEmpty()) {
			for (NotaDesconto desconto : listaNotaDesconto) {
				total = total.add(desconto.getValor());
			}
		}
		return total;
	}

	protected List<Rateio> getRateiosAgrupado(List<Rateio> rateios) {
		BigDecimal total = new BigDecimal(0);
		List<Rateio> rateiosAgrupados = new ArrayList<>();
		if (rateios != null && !rateios.isEmpty()) {
			List<Rateio> rateiosAux = new ArrayList<>(rateios);
			rateioLoop:
			for (Rateio rateio : rateios) {
				for (Rateio rateioAux : rateiosAux) {
					if (rateio.getCentroFinanceiro().getCodigo().equals(rateioAux.getCentroFinanceiro().getCodigo())) {
						total = total.add(rateioAux.getValor());
					}
				}
				for (Rateio rateioAgrupado : rateiosAgrupados) {
					if (rateioAgrupado.getCentroFinanceiro().getCodigo().equals(rateio.getCentroFinanceiro().getCodigo())) {
						total = new BigDecimal(0);
						continue rateioLoop;
					}
				}
				rateio.setValorAgrupado(total);
				rateiosAgrupados.add(rateio);
				total = new BigDecimal(0);
			}
		}
		return rateiosAgrupados;
	}

	@Transient
	public Boolean getSelecao() {
		return selecao;
	}

	public void setSelecao(Boolean selecao) {
		this.selecao = selecao;
	}

	@Transient
	public ParcelaNota getUltimaParcela() {
		if (parcelas != null && !parcelas.isEmpty()) {
			return parcelas.get(parcelas.size() - 1);
		}
		return new ParcelaNota();
	}

	@Transient
	public BigDecimal getPrimeiraParcela() {
		if (parcelas != null && !parcelas.isEmpty()) {
			return parcelas.get(0).getValor();
		}
		return new BigDecimal("0");
	}

	@Transient
	public ParcelaNota getPrimeiroPagamento() {
		if (parcelas != null && !parcelas.isEmpty()) {
			return parcelas.get(0);
		}
		return null;
	}

	@Transient
	public boolean isPrimeiraParcelaPaga() {
		if (parcelas != null && !parcelas.isEmpty()) {
			return parcelas.get(0).getStatus().equals(Status.FINALIZADO);
		}
		return false;
	}

	@Transient
	public Date getDataEmissaoNF() {
		if (this instanceof NotaFiscal) {
			NotaFiscal notaFiscal = (NotaFiscal) this;
			return notaFiscal.getDataEmissao();
		}
		return null;
	}

	@Column(name = "DUPLICATA", length = 20)
	@Length(max = 20)
	public String getDuplicata() {
		return this.duplicata;
	}

	public void setDuplicata(String duplicata) {
		this.duplicata = duplicata;
	}

	@Column(name = "tipo_doc_pagamento")
	public TipoDocumentoNota getTipoDocumentoNota() {
		return tipoDocumentoNota;
	}

	public void setTipoDocumentoNota(TipoDocumentoNota tipoDocumentoNota) {
		this.tipoDocumentoNota = tipoDocumentoNota;
	}

	@Column(name = "boleto_anexo")
	public Integer getNumBoletoAnexo() {
		return numBoletoAnexo;
	}

	public void setNumBoletoAnexo(Integer numBoletoAnexo) {
		this.numBoletoAnexo = numBoletoAnexo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CANCELAMENTO")
	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_CANCELAMENTO")
	public Pessoa getResponsavelCancelamento() {
		return responsavelCancelamento;
	}

	public void setResponsavelCancelamento(Pessoa responsavelCancelamento) {
		this.responsavelCancelamento = responsavelCancelamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_REVERSAO_CANCELAMENTO")
	public Pessoa getResponsavelReversaoCancelamento() {
		return responsavelReversaoCancelamento;
	}

	public void setResponsavelReversaoCancelamento(Pessoa responsavelReversaoCancelamento) {
		this.responsavelReversaoCancelamento = responsavelReversaoCancelamento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_REVERSAO_CANCELAMENTO")
	public Date getDataReversaoCancelamento() {
		return dataReversaoCancelamento;
	}

	public void setDataReversaoCancelamento(Date dataReversaoCancelamento) {
		this.dataReversaoCancelamento = dataReversaoCancelamento;
	}

	@Transient
	public Colaborador getAprovador() {
		if (this.parcelas != null && !this.parcelas.isEmpty()) {
			ParcelaNota parcela = this.parcelas.get(0);
			return parcela.getAprovador();
		}
		return new Colaborador();
	}

	@Transient
	public Colaborador getAutorizador() {
		if (this.parcelas != null && !this.parcelas.isEmpty()) {
			ParcelaNota parcela = this.parcelas.get(0);
			return parcela.getAutorizador();
		}
		return new Colaborador();
	}

	@Transient
	public String getDataAprovador() {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, new Locale("pt", "BR"));
		if (this.parcelas != null && !this.parcelas.isEmpty()) {
			ParcelaNota parcela = this.parcelas.get(0);
			if (parcela.getDataAprovador() != null) {
				return df.format(parcela.getDataAprovador());
			}
		}
		return "";
	}

	@Transient
	public String getListaTributosSelecionados() {
		StringBuilder sbLista = new StringBuilder();
		String lista = "";

		if (listaNotaTributoAliquota != null && !listaNotaTributoAliquota.isEmpty()) {
			sbLista.append("Tributos selecionados: ");
			for (NotaTributoAliquota notaTributoAliquota : listaNotaTributoAliquota) {
				if (notaTributoAliquota.getAliquota().getCategoriaTributo() == null) {
					continue;
				}
				sbLista.append(notaTributoAliquota.getAliquota().getCategoriaTributo().getTributo().getNome());
				sbLista.append(", ");
			}
			lista = sbLista.toString().trim();
			lista = lista.substring(0, lista.length() - 1);
		} else
			lista = "Sem tributos selecionados.";

		return lista.trim();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(Nota.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Nota other = (Nota) obj;
		if (numero == null) {
			if (other.getNumero() != null) {
				return false;
			}
		} else if (!numero.equals(other.getNumero())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Nota [" + (getNumero() != null ? "getNumero()=" + getNumero() + ", " : "") + (getStatus() != null ? "getStatus()=" + getStatus() : "") + "]";
	}

	@Transient
	public List<NotaTributoAliquota> getListaNotaTributoAliquotaSemISS() {
		List<NotaTributoAliquota> lista = new ArrayList<>();
		if (listaNotaTributoAliquota != null && !listaNotaTributoAliquota.isEmpty()) {
			for (NotaTributoAliquota nta : listaNotaTributoAliquota) {
				if (nta.getAliquota() != null && nta.getAliquota().getCategoriaTributo() != null
					&& nta.getAliquota().getCategoriaTributo().getTributo() != null
					&& !nta.getAliquota().getCategoriaTributo().getTributo().getNome().trim().equalsIgnoreCase("ISS")) {
					lista.add(nta);
				}
			}
		}
		return lista;
	}

	@Transient
	public NotaTributoAliquota getListaNotaTributoAliquotaISS() {
		if (listaNotaTributoAliquota != null && !listaNotaTributoAliquota.isEmpty()) {
			for (NotaTributoAliquota nta : listaNotaTributoAliquota) {
				if (nta.getAliquota() != null && nta.getAliquota().getCategoriaTributo() != null
					&& nta.getAliquota().getCategoriaTributo().getTributo() != null
					&& nta.getAliquota().getCategoriaTributo().getTributo().getNome().trim().equalsIgnoreCase("ISS")) {
					return nta;
				}
			}
		}
		return new NotaTributoAliquota();
	}

	@Transient
	public boolean hasTributoIss() {
		if (getListaNotaTributoAliquotaISS() != null && getListaNotaTributoAliquotaISS().getCodigo() != null)
			return true;
		return false;
	}

	@Transient
	public String getCidadeEstadoISS() {
		NotaTributoAliquota nta = getListaNotaTributoAliquotaISS();
		if (nta != null && nta.getAliquota().getCategoriaTributo() != null && nta.getAliquota().getCategoriaTributo().getCidade() != null) {
			return nta.getAliquota().getCategoriaTributo().getCidade().getCidadeEstado();
		}
		return "";
	}

	@Transient
	public String getCidadeEstadoCredor() {
		if (credor != null && credor.getEndereco() != null && credor.getEndereco().getCidadeUf() != null) {
			return credor.getEndereco().getCidadeUf();
		}
		return "";
	}

	@Transient
	public boolean isNotaCarioca() {
		NotaTributoAliquota nta = getListaNotaTributoAliquotaISS();
		if (nta != null && nta.getValidaPrefeitura() != null && nta.getValidaPrefeitura())
			return true;
		return false;
	}

	@Transient
	public NotaTributoAliquota getNotaTributoAliquota(String nome) {
		if (listaNotaTributoAliquota != null && !listaNotaTributoAliquota.isEmpty() && nome != null) {
			for (NotaTributoAliquota nta : listaNotaTributoAliquota) {
				if (nta.getAliquota().getCategoriaTributo().getTributo().getNome().trim().equals(nome.trim())) {
					return nta;
				}
			}
		}
		return null;
	}

	@Transient
	public BigDecimal getValorDesconto() {
		BigDecimal valorDesconto = BigDecimal.ZERO;

		if (!parcelas.isEmpty() && parcelas != null) {
			for (ParcelaNota pNota : parcelas) {
				valorDesconto = valorDesconto.add(pNota.getValorDesconto());
			}
		}

		if (valorDesconto != null) {
			valorDesconto = valorDesconto.setScale(2, RoundingMode.UP);
		}
		return valorDesconto;
	}

	@Transient
	public BigDecimal getTotalTributos() {
		BigDecimal total = new BigDecimal("0");
		if (listaNotaTributoAliquota != null && !listaNotaTributoAliquota.isEmpty()) {
			for (NotaTributoAliquota nta : listaNotaTributoAliquota) {
				if (nta != null && nta.getTotalTributos() != null) {
					total = total.add(nta.getTotalTributos());
				}
			}
		}
		return total;
	}

	@Transient
	public Contrato getContratoFornecedor() {
		if (credor != null && credor.getListaContratos() != null && !credor.getListaContratos().isEmpty()) {
			return credor.getListaContratos().get(0);
		}
		return null;
	}

	public enum StatusNota {
		ABERTA("Aberta", 0), // 0
		REABERTA("Reaberta", 1), // 1
		EM_APROVACAO_NOVA_NF("Em Aprovação (nova nota)", 2), // 2
		EM_APROVACAO_RETORNO_FINANCEIRO("Em Aprovação (retorno financeiro)", 3), // 3
		EM_APROVACAO_RETORNO_AJUSTES("Em Aprovação (após ajustes)", 4), // 4
		EM_AJUSTE_FISCAL("Em Ajuste Fiscal", 5), // 5
		APROVADA("Aprovada", 6), // 6
		CANCELADA("Cancelada", 7), // 7
		EM_APROVACAO_FUNDO_FIXO("Em Aprovação (Prestação de contas)", 8), // 8
		EM_RELACAO_NOTAS("Em Relação de Notas", 9), // 9
		RECEBIDA_FINANCEIRO("Recebida pelo Financeiro", 10), // 10
		FINALIZADO("Finalizado", 11), // 11
		ABERTA_FATURAMENTO("Aberta", 50), // 12
		A_PROCESSAR_FATURAMENTO("A processar", 51), // 13
		EM_PROCESSAMENTO_FATURAMENTO("Em processamento", 52), // 14
		EM_RECEBIMENTO_FATURAMENTO("Em recebimento", 53), // 15
		RECEBIDA_FATURAMENTO("Recebida", 54), // 16
		CANCELADA_FATURAMENTO("Cancelada Faturamento", 55), // 17
		DEVOLUCAO_TOTAL("Devolução total", 56); // 18

		private final String nomeStatus;
		private final int ordemStatus;

		StatusNota(String nomeStatus, int ordemStatus) {
			this.nomeStatus = nomeStatus;
			this.ordemStatus = ordemStatus;
		}

		public String getNomeStatus() {
			return nomeStatus;
		}

		public int getOrdemStatus() {
			return ordemStatus;
		}

		@Override
		public String toString() {
			return getNomeStatus();
		}

	}

	public enum TipoDocumentoNota {
		BOLETO_ANEXO("Boleto anexo", "Boleto", TipoDocumentoPagamento.BOLETO),
		BOLETO_CORREIO("Boleto correio", "Não se aplica", TipoDocumentoPagamento.BOLETO),
		DEPOSITO("Depósito em conta", "Depósito em conta/Transferência", TipoDocumentoPagamento.DEPOSITO),
		DUPLICATA("Duplicata", "Não se aplica", TipoDocumentoPagamento.OUTROS);

		private final String nomeStatus; //nome exibido na Nota Fiscal
		private final String nomeStatusNP; //nome exibido na Nota de Pagamento
		private final TipoDocumentoPagamento tipoDePara;

		TipoDocumentoNota(String nomeStatus, String nomeStatusNP, TipoDocumentoPagamento tipoDePara) {
			this.nomeStatus = nomeStatus;
			this.nomeStatusNP = nomeStatusNP;
			this.tipoDePara = tipoDePara;
		}

		public String getNomeStatus() {
			return nomeStatus;
		}

		public String getNomeStatusNP() {
			return nomeStatusNP;
		}

		public TipoDocumentoPagamento getTipoDePara() {
			return tipoDePara;
		}

	}

	public enum FormaPagamento {
		PARCELA_UNICA("Parcela única"), PARCELADA("Parcelada");

		private final String nome;

		FormaPagamento(String nome) {
			this.nome = nome;
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
