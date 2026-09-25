package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.MotivoRetornoOrdemCompra;
import br.com.araujoabreu.sgi.administracao.domain.NaturezaFornecimento;
import br.com.araujoabreu.sgi.administracao.domain.NaturezaFornecimento.TipoFornecimento;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.compras.enums.TipoFreteOC;
import br.com.araujoabreu.sgi.compras.enums.TipoSeguro;
import br.com.araujoabreu.sgi.controleFiscal.domain.Tributo;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Uma <code>OrdemCompra</code> é criada automaticamente pelo sistema para agrupar requisições de compra de uma
 * determinada natureza de fornecimento. De acordo com a natureza, o sistema associa os fornecedores com capacidade
 * de atendimento.
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "ORDEM_COMPRA")
@SequenceGenerator(name="SEQ_ORDEM_COMPRA", sequenceName="SEQ_ORDEM_COMPRA", allocationSize=1)
public class OrdemCompra extends AuditEntity {

	private static final long serialVersionUID = -2599300315820525462L;
	private String numero;
	private Status status = Status.ABERTA;
	private Date dataAbertura;
	private Date dataEnvioCotacao;
	private Date dataEnvioClassificacao;
	private Date dataEnvioAutorizacao;
	private Date dataEnvioRecebimento;
	private Date dataEntrega;
	private TipoFreteOC tipoFrete;
	private TipoSeguro tipoSeguro;
	private NaturezaFornecimento naturezaFornecimento;
	private MeioContato meioContatoEntrega;
	private MeioContato meioContatoCobranca;
	private MeioContato meioContatoFaturamento;
	private List<Tributo> tributos = new ArrayList<>(0);
	private List<Proposta> propostas = new ArrayList<>(0);
	private List<Pedido> pedidos = new ArrayList<>(0);
	private List<RequisicaoCompraRefinada> requisicoesCompraRefinadas = new ArrayList<>(0);
	private PessoaFisica solicitante; // TODO: Pq não é um Colaborador?
	private Colaborador autorizador;
	private Colaborador classificador;
	private List<FornecedorHabilitado> fornecedores = new ArrayList<>();
	private List<ItemOrdemCompra> itens = new ArrayList<>(0);
	private CentroFinanceiro centroFinanceiro;
	private Boolean individual = false;
	private Boolean urgente = false;
	private Date dataLimite;
	private List<MotivoRetornoOrdemCompra> motivosRetorno;
	private Unidade unidade;
	private String condicaoPagamento;

	public void addRequisicaoCompraRefinada(RequisicaoCompraRefinada requisicaoCompraRefinada) {
		requisicoesCompraRefinadas.add(requisicaoCompraRefinada);
		requisicaoCompraRefinada.setOrdemCompra(this);
	}

	public void addProposta(Proposta proposta) {
		proposta.setOrdemCompra(this);
		propostas.add(proposta);
	}

	public void addPedido(Pedido pedido) {
		pedido.setOrdemCompra(this);
		pedidos.add(pedido);
	}

	public void addFornecedor(Pessoa fornecedor) {
		FornecedorHabilitado fornecedorHabilitado = new FornecedorHabilitado();
		fornecedorHabilitado.setFornecedor(fornecedor);
		fornecedorHabilitado.setOrdemCompra(this);
		fornecedores.add(fornecedorHabilitado);
	}

	public void addFornecedores(List<Pessoa> fornecedors) {
		for (Pessoa fornecedor : fornecedors) {
			addFornecedor(fornecedor);
		}
	}

	@Column(name = "NUMERO", length = 14)
	@NotNull
	@Length(max = 14)
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "STATUS", nullable = false)
	@NotNull
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column(name = "DATA_ABERTURA", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	@Column(name = "DATA_ENVIO_COTACAO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataEnvioCotacao() {
		return dataEnvioCotacao;
	}

	public void setDataEnvioCotacao(Date dataEnvioCotacao) {
		this.dataEnvioCotacao = dataEnvioCotacao;
	}

	@Column(name = "DATA_ENVIO_CLASSIFICACAO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataEnvioClassificacao() {
		return dataEnvioClassificacao;
	}

	public void setDataEnvioClassificacao(Date dataEnvioClassificacao) {
		this.dataEnvioClassificacao = dataEnvioClassificacao;
	}

	@Column(name = "DATA_ENVIO_AUTORIZACAO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataEnvioAutorizacao() {
		return dataEnvioAutorizacao;
	}

	public void setDataEnvioAutorizacao(Date dataEnvioAutorizacao) {
		this.dataEnvioAutorizacao = dataEnvioAutorizacao;
	}

	@Column(name = "DATA_ENVIO_RECEBIMENTO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataEnvioRecebimento() {
		return dataEnvioRecebimento;
	}

	public void setDataEnvioRecebimento(Date dataEnvioRecebimento) {
		this.dataEnvioRecebimento = dataEnvioRecebimento;
	}

	@Column(name = "DATA_ENTREGA")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	@Column(name = "TIPO_FRETE")
	public TipoFreteOC getTipoFrete() {
		return tipoFrete;
	}

	public void setTipoFrete(TipoFreteOC tipoFrete) {
		this.tipoFrete = tipoFrete;
	}

	@Column(name = "TIPO_SEGURO")
	public TipoSeguro getTipoSeguro() {
		return tipoSeguro;
	}

	public void setTipoSeguro(TipoSeguro tipoSeguro) {
		this.tipoSeguro = tipoSeguro;
	}

	@ManyToMany(targetEntity = Tributo.class, fetch = FetchType.LAZY)
	@JoinTable(
		name = "ORDEM_COMPRA_TRIBUTO",
		joinColumns = @JoinColumn(name = "CODIGO_ORDEM_COMPRA"),
		inverseJoinColumns = @JoinColumn(name = "CODIGO_TRIBUTO")
	)
	public List<Tributo> getTributos() {
		return tributos;
	}

	public void setTributos(List<Tributo> tributos) {
		this.tributos = tributos;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NATUREZA_FORNECIMENTO")
	@NotNull
	public NaturezaFornecimento getNaturezaFornecimento() {
		return naturezaFornecimento;
	}

	public void setNaturezaFornecimento(NaturezaFornecimento naturezaFornecimento) {
		this.naturezaFornecimento = naturezaFornecimento;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_MEIO_CONTATO_ENTREGA")
	public MeioContato getMeioContatoEntrega() {
		return this.meioContatoEntrega;
	}

	public void setMeioContatoEntrega(MeioContato meioContatoEntrega) {
		this.meioContatoEntrega = meioContatoEntrega;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_MEIO_CONTATO_COBRANCA")
	public MeioContato getMeioContatoCobranca() {
		return meioContatoCobranca;
	}

	public void setMeioContatoCobranca(MeioContato meioContatoCobranca) {
		this.meioContatoCobranca = meioContatoCobranca;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_MEIO_CONTATO_FATURA")
	public MeioContato getMeioContatoFaturamento() {
		return meioContatoFaturamento;
	}

	public void setMeioContatoFaturamento(MeioContato meioContatoFaturamento) {
		this.meioContatoFaturamento = meioContatoFaturamento;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ordemCompra")
	public List<Proposta> getPropostas() {
		return propostas;
	}

	public void setPropostas(List<Proposta> propostas) {
		this.propostas = propostas;
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "ordemCompra")
	@OrderBy(value = "codigo ASC")
	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "ordemCompra")
	public List<RequisicaoCompraRefinada> getRequisicoesCompraRefinadas() {
		return requisicoesCompraRefinadas;
	}

	public void setRequisicoesCompraRefinadas(List<RequisicaoCompraRefinada> requisicoesCompraRefinadas) {
		this.requisicoesCompraRefinadas = requisicoesCompraRefinadas;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_SOLICITANTE")
	public PessoaFisica getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(PessoaFisica solicitante) {
		this.solicitante = solicitante;
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
	@JoinColumn(name = "CODIGO_CLASSIFICADOR")
	public Colaborador getClassificador() {
		return classificador;
	}

	public void setClassificador(Colaborador classificador) {
		this.classificador = classificador;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ordemCompra")
	public List<FornecedorHabilitado> getFornecedoresHabilitados() {
		return this.fornecedores;
	}

	public void setFornecedoresHabilitados(List<FornecedorHabilitado> fornecedores) {
		this.fornecedores = fornecedores;
	}

	@Transient
	public List<Pessoa> getFornecedores() {
		List<Pessoa> list = new ArrayList<>();
		for (FornecedorHabilitado item : fornecedores) {
			list.add(item.getFornecedor());
		}
		return list;
	}

	/**
	 * Retorna o Centro Finaceiro.
	 *
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	@NotNull
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@Column(name = "INDIVIDUAL", nullable = false, length = 1)
	@NotNull
	public Boolean getIndividual() {
		return individual;
	}

	public void setIndividual(Boolean individual) {
		this.individual = individual;
	}

	@Transient
	public boolean isAberto() {
		return Status.ABERTA.equals(getStatus());
	}

	@Transient
	public boolean isAnalise() {
		return Status.ANALISE.equals(getStatus());
	}

	@Transient
	public boolean isCotacao() {
		return Status.COTACAO.equals(getStatus());
	}

	@Transient
	public boolean isClassificacao() {
		return Status.CLASSIFICACAO.equals(getStatus());
	}

	@Transient
	public boolean isAutorizao() {
		return Status.AUTORIZACAO.equals(getStatus());
	}

	@Transient
	public boolean isRecebimento() {
		return Status.RECEBIMENTO.equals(getStatus());
	}

	@Transient
	public boolean isCancelada() {
		return Status.CANCELADA.equals(getStatus());
	}

	@Transient
	public boolean isDivulgado() {
		return getStatus().ordinal() >= Status.COTACAO.ordinal();
	}

	@Transient
	public boolean isRecebimentoIniciado() {
		return Status.RECEBIMENTO_INICIADO.equals(getStatus());
	}

	@Transient
	public boolean isFinalizada() {
		return Status.FINALIZADA.equals(getStatus());
	}

	/**
	 * Retorna uma descrição completa sobre o endereço de entrega. O endereço de entrega pode ser vazio -> quando
	 * a ordem tem apenas uma RC.
	 *
	 * @return
	 */
	@Transient
	public String getDescricaoContatoEntrega() {
		if (meioContatoEntrega != null) {
			return meioContatoEntrega.getDescricaoContato();
		} else {
			RequisicaoCompraRefinada rc = this.requisicoesCompraRefinadas.get(0);
			return rc.getRequisicaoCompra().getMeioContatoEntrega().getDescricaoContato();
		}
	}

	@Transient
	public String getDescricaoContatoEntregaPedido() {
		if (meioContatoEntrega != null) {
			return meioContatoEntrega.getDescricaoContatoPlanilhaPedidos();
		} else {
			RequisicaoCompraRefinada rc = this.requisicoesCompraRefinadas.get(0);
			return rc.getRequisicaoCompra().getMeioContatoEntrega().getDescricaoContatoPlanilhaPedidos();
		}
	}

	/**
	 * Retorna uma descrição completa sobre o endereço de cobranca
	 *
	 * @return
	 */
	@Transient
	public String getDescricaoContatoCobranca() {
		return (meioContatoCobranca != null) ? meioContatoCobranca.getDescricaoContato() : "";
	}

	/**
	 * Retorna uma descrição completa sobre o endereço de faturamento
	 *
	 * @return
	 */
	@Transient
	public String getDescricaoContatoFaturamento() {
		return (meioContatoFaturamento != null) ? meioContatoFaturamento.getDescricaoContato() : "";
	}

	@Transient
	public List<ItemOrdemCompra> getItens() {
		return itens;
	}

	public void setItens(List<ItemOrdemCompra> itens) {
		this.itens = itens;
	}

	/**
	 * Retorna verdadeiro para OC de natureza de fornecimento de serviço.
	 *
	 * @return
	 */
	@Transient
	public boolean isServico() {
		return TipoFornecimento.SERVICOS.equals(getNaturezaFornecimento().getTipo());
	}

	@Transient
	public boolean isMaterial() {
		return TipoFornecimento.MATERIAIS.equals(getNaturezaFornecimento().getTipo());
	}

	@Transient
	public String getLabelTributosAplicaveis() {
		if (naturezaFornecimento != null && naturezaFornecimento.getTipo() != null) {
			return TipoFornecimento.MATERIAIS.equals(naturezaFornecimento.getTipo()) ? "Não aplicável" : "Tributos aplicáveis";
		}
		return "Não aplicável";
	}

	/**
	 * Determina a data limite para o recebimento dos itens da requisição de compra.
	 * Informação utlizada para priorizar e orientar o comprador
	 *
	 * @return
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_LIMITE", nullable = false)
	@NotNull
	public Date getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(Date dataLimite) {
		this.dataLimite = dataLimite;
	}

	/**
	 * Indica se a OC tem uma requisição de compra urgente.
	 *
	 * @return
	 */
	@Column(name = "URGENTE", nullable = false, length = 1)
	@NotNull
	public Boolean getUrgente() {
		return urgente;
	}

	public void setUrgente(Boolean urgente) {
		this.urgente = urgente;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ordemCompra")
	public List<MotivoRetornoOrdemCompra> getMotivosRetorno() {
		return motivosRetorno;
	}

	public void setMotivosRetorno(List<MotivoRetornoOrdemCompra> motivosRetorno) {
		this.motivosRetorno = motivosRetorno;
	}

	@JoinColumn(name = "codigo_unidade")
	@ManyToOne(fetch = FetchType.LAZY)
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@Column(name = "CONDICAO_PAGAMENTO")
	public String getCondicaoPagamento() {
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(String condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	private void addPedidos(StringBuffer auditoria) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (pedidos != null && !pedidos.isEmpty()) {
			auditoria.append(" \n ");
			for (Pedido pedido : pedidos) {
				if (pedido != null && pedido.getCodigo() != null) {
					auditoria.append("Fornecedor: ").append(pedido.getFornecedor().getCodigo());
					auditoria.append(" \n ");
					auditoria.append("Status: ").append(pedido.getStatus() != null ? pedido.getStatus().name() : "");
					auditoria.append(" \n ");
					auditoria.append("Total Recebimento: ").append(pedido.getTotal());
					auditoria.append(" \n ");
					auditoria.append("Total proposta: ").append(pedido.getTotalSolicitado());
					auditoria.append(" \n ");
					if (pedido.getRecebimentos() != null && !pedido.getRecebimentos().isEmpty()) {
						for (Recebimento recebimento : pedido.getRecebimentos()) {
							auditoria.append("Recebedor: ").append(recebimento.getRecebedor().getNome());
							auditoria.append(" \n ");
							auditoria.append("Status: ").append(recebimento.getStatus().name());
							auditoria.append(" \n ");
							auditoria.append("Data recebimento: ").append(sdf.format(recebimento.getDataRecebimento()));
							auditoria.append(" \n ");
							auditoria.append("Total: ").append(sdf.format(recebimento.getTotal()));
							auditoria.append(" \n ");
						}
					}
				}
			}
		}
	}

	private void addPropostas(StringBuffer auditoria) {
		if (propostas != null && !propostas.isEmpty()) {
			auditoria.append(" \n ");
			for (Proposta proposta : propostas) {
				if (proposta != null && proposta.getCodigo() != null) {
					auditoria.append("Proposta: ").append(proposta.getNumero());
					auditoria.append(" \n ");
					auditoria.append("Total: ").append(proposta.getTotal());
					auditoria.append(" \n ");
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void addFornecedores(StringBuffer auditoria) {
		if (fornecedores != null && !fornecedores.isEmpty()) {
			auditoria.append(" \n ");
			for (FornecedorHabilitado fornecedor : fornecedores) {
				if (fornecedor != null && fornecedor.getCodigo() != null) {
					auditoria.append("Fornecedor: ").append(fornecedor.getFornecedor().getCodigo());
					auditoria.append(" \n ");
				}
			}
		}
	}

	private void addRequisicoes(StringBuffer auditoria) {
		if (requisicoesCompraRefinadas != null && !requisicoesCompraRefinadas.isEmpty()) {
			auditoria.append(" \n ");
			for (RequisicaoCompraRefinada requisicao : requisicoesCompraRefinadas) {
				if (requisicao != null && requisicao.getCodigo() != null) {
					auditoria.append(requisicao.getRequisicaoCompra().getNumero());
					auditoria.append(" \n ");
				}
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
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

		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		if (!(obj instanceof OrdemCompra)) {
			return false;
		}

		OrdemCompra other = (OrdemCompra) obj;

		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "OrdemCompra [codigo=" + getCodigo() + ", numero=" + numero + ", status=" + status + "]";
	}

	public enum Status {
		ABERTA("Aberta"),
		ANALISE("Análise"),
		COTACAO("Cotação"),
		CLASSIFICACAO("Em negociação"),
		AUTORIZACAO("Autorização"),
		RECEBIMENTO("A receber"),
		RECEBIMENTO_INICIADO("Recebimento parcial"),
		FINALIZADA("Finalizada"),
		DISPONIVEL_NF("Recebida sem NF"),
		CANCELADA("Cancelada");

		private final String nome;

		Status(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

	}

}
