package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * CentroFinanceiro representa uma abstração de um Centro de Lucro (Contrato) ou
 * um Centro de Custo (Gerencia)
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "CENTRO_FINANCEIRO")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SequenceGenerator(name = "SEQ_CENTRO_FINANCEIRO", sequenceName = "SEQ_CENTRO_FINANCEIRO",	allocationSize = 1)
public class CentroFinanceiro extends AuditEntity implements DomainObject, Comparable<CentroFinanceiro> {

	private static final long serialVersionUID = 1720832918250979542L;
	//private Long codigo;
	private String identificador;
	private String identificadorFinal;
	private String nome;
	private String observacao;
	private String formaPagamento;
	private String criterioMedicao;
	private String nomeEngenheiroResposavel;
	private Boolean selecionado;
	private Boolean cumulativo;
	private Integer totalHH;
	private Date dataPrevisaoReajuste;
	private SituacaoCentroFinanceiro situacao = SituacaoCentroFinanceiro.ATIVO;
	private Garantias garantias;
	private EscopoCompra escopoCompra;
	private TipoCentroFinanceiro tipo;
	private Unidade unidade;
	private Contrato contrato;
	private CentroFinanceiro pai;
	private Colaborador contatoAA;
	private Colaborador engenheiroResponsavel;
	private Fluxo fluxo;
	private List<CentroFinanceiroEndereco> listaEnderecos;
	private CentroFinanceiro gerenciaAnterior;
	private Integer proximoNumOC;
	private String cei;
	private Date dataPrevisaoPrimeiraParcela;
	private Date dataEncerramento;
	private Boolean permiteClienteLivre;

	public CentroFinanceiro() {
	}

	public CentroFinanceiro(Long codigo) {
		setCodigo(codigo);
	}

	public CentroFinanceiro(Contrato contrato, Colaborador contatoAA) {
		super();
		this.contrato = contrato;
		this.contatoAA = contatoAA;
	}

	@Column(name = "IDENTIFICADOR", nullable = false, length = 11)
	@NotNull
	@Length(max = 11)
	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	/**
	 * Campo derivado que contém o último código do identificador.
	 *
	 * @return String
	 */
	@Formula("substring( substring(identificador, position('.' in identificador)+1), position('.' in "
		+ "	substring(identificador, position('.' in identificador)+1) )+1 )")
	public String getIdentificadorFinal() {
		return identificadorFinal;
	}

	public void setIdentificadorFinal(String identificadorFinal) {
		this.identificadorFinal = identificadorFinal;
	}

	@Column(name = "NOME", nullable = false, length = 100)
	@Length(max = 100)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "TIPO", nullable = false)
	@NotNull
	public TipoCentroFinanceiro getTipo() {
		return tipo;
	}

	public void setTipo(TipoCentroFinanceiro tipo) {
		this.tipo = tipo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CODIGO_PAI")
	public CentroFinanceiro getPai() {
		return pai;
	}

	public void setPai(CentroFinanceiro pai) {
		this.pai = pai;
	}

	@Column(name = "GARANTIAS")
	public Garantias getGarantias() {
		return garantias;
	}

	public void setGarantias(Garantias garantias) {
		this.garantias = garantias;
	}

	@Column(name = "ESCOPO_COMPRA")
	public EscopoCompra getEscopoCompra() {
		return escopoCompra;
	}

	public void setEscopoCompra(EscopoCompra escopoCompra) {
		this.escopoCompra = escopoCompra;
	}

	@Column(name = "TOTAL_HH")
	public Integer getTotalHH() {
		return totalHH;
	}

	public void setTotalHH(Integer totalHH) {
		this.totalHH = totalHH;
	}

	@Column(name = "CUMULATIVO", nullable = false)
	@NotNull
	public Boolean getCumulativo() {
		return cumulativo;
	}

	public void setCumulativo(Boolean cumulativo) {
		this.cumulativo = cumulativo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CODIGO_UNIDADE")
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@Column(name = "SITUACAO", nullable = false)
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	public SituacaoCentroFinanceiro getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoCentroFinanceiro situacao) {
		this.situacao = situacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_FLUXO")
	public Fluxo getFluxo() {
		return fluxo;
	}

	public void setFluxo(Fluxo fluxo) {
		this.fluxo = fluxo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTRATO")
	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_ENGENHEIRO_RESPONSAVEL")
	public Colaborador getEngenheiroResponsavel() {
		return engenheiroResponsavel;
	}

	public void setEngenheiroResponsavel(Colaborador engenheiroResponsavel) {
		this.engenheiroResponsavel = engenheiroResponsavel;
	}

	@Column(name = "ENGENHEIRO_RESPONSAVEL")
	public String getNomeEngenheiroResposavel() {
		return nomeEngenheiroResposavel;
	}

	public void setNomeEngenheiroResposavel(String nomeEngenheiroResposavel) {
		this.nomeEngenheiroResposavel = nomeEngenheiroResposavel;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_CONTATO_AAE")
	public Colaborador getContatoAA() {
		return contatoAA;
	}

	public void setContatoAA(Colaborador contatoAA) {
		this.contatoAA = contatoAA;
	}

	@Column(name = "PROXIMA_OC", nullable = false)
	public Integer getProximoNumOC() {
		return proximoNumOC;
	}

	public void setProximoNumOC(Integer proximoNumOC) {
		this.proximoNumOC = proximoNumOC;
	}

	@OneToMany(mappedBy = "centroFinanceiro")
	public List<CentroFinanceiroEndereco> getListaEnderecos() {
		return listaEnderecos;
	}

	public void setListaEnderecos(List<CentroFinanceiroEndereco> listaEnderecos) {
		this.listaEnderecos = listaEnderecos;
	}

	@Column(name = "CRITERIO_MEDICAO", length = 100)
	public String getCriterioMedicao() {
		return criterioMedicao;
	}

	public void setCriterioMedicao(String criterioMedicao) {
		this.criterioMedicao = criterioMedicao;
	}

	@Column(name = "FORMA_PAGAMENTO", length = 100)
	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	@Column(name = "OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_PREVISAO_REAJUSTE")
	public Date getDataPrevisaoReajuste() {
		return dataPrevisaoReajuste;
	}

	public void setDataPrevisaoReajuste(Date dataPrevisaoReajuste) {
		this.dataPrevisaoReajuste = dataPrevisaoReajuste;
	}

	@Column(name = "CEI", length = 15)
	public String getCei() {
		return cei;
	}

	public void setCei(String cei) {
		this.cei = cei;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_PREVISAO_PRIMEIRA_PARCELA")
	public Date getDataPrevisaoPrimeiraParcela() {
		return dataPrevisaoPrimeiraParcela;
	}

	public void setDataPrevisaoPrimeiraParcela(Date dataPrevisaoPrimeiraParcela) {
		this.dataPrevisaoPrimeiraParcela = dataPrevisaoPrimeiraParcela;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_ENCERRAMENTO")
	public Date getDataEncerramento() {
		return dataEncerramento;
	}

	public void setDataEncerramento(Date dataEncerramento) {
		this.dataEncerramento = dataEncerramento;
	}

	@Column(name = "PERMITE_CLIENTE_LIVRE")
	public Boolean getPermiteClienteLivre() {
		return permiteClienteLivre;
	}

	public void setPermiteClienteLivre(Boolean permiteClienteLivre) {
		this.permiteClienteLivre = permiteClienteLivre;
	}

	@Transient
	public String getIdentificacao() {
		return getIdentificador() + " - " + StringUtils.substring(getNome(), 0, 10) + (getNome().length() > 10 ? "..." : "");
	}

	@Transient
	public String getIdentificacaoFinal() {
		if (getIdentificadorFinal() != null && !getIdentificadorFinal().isEmpty() && getNome() != null && !getNome().isEmpty()) {
			return getIdentificadorFinal() + " - " + getNome();
		}
		return "";
	}


	@Transient
	public String getIdentificacaoFinalAbreviado() {
		return getIdentificadorFinal() + " - " + StringUtils.substring(getNome(), 0, 17) + (getNome().length() > 17 ? "..." : "");
	}

	@Transient
	public boolean isCusto() {
		return TipoCentroFinanceiro.CENTRO_CUSTO.equals(getTipo());
	}

	@Transient
	public String getNomeAbreviado() {
		return StringUtils.substring(getNome(), 0, 31) + (getNome().length() > 31 ? "..." : "");
	}

	@Transient
	public boolean isAtivo() {
		if (SituacaoCentroFinanceiro.ATIVO.equals(getSituacao())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Transient
	public CentroFinanceiro getGerencia() {
		if (this != null && this.getPai() == null) {
			return this;
		} else if (this != null && this.getPai() != null) {
			return this.getPai();
		}
		return null;
	}

	@Transient
	public String getNumero() {
		if (this.identificador == null || StringUtils.isEmpty(this.identificador)) {
			return null;
		}

		return StringUtils.right(this.identificador, 4);
	}

	@Transient
	public String getNomeEngenheiroResponsavel() {
		if (engenheiroResponsavel != null && engenheiroResponsavel.getCodigo() != null) {
			return engenheiroResponsavel.getNome();
		}
		if (nomeEngenheiroResposavel != null && !nomeEngenheiroResposavel.isEmpty()) {
			return nomeEngenheiroResposavel;
		}
		return "";
	}

	@Override
	public int compareTo(CentroFinanceiro obj) {
		return this.getIdentificadorFinal().compareToIgnoreCase(obj.getIdentificadorFinal());
	}

	@Transient
	public Integer getPrazo() {
		if (contrato != null && contrato.getCodigo() != null) {
			return contrato.getPrazoAtual();
		}
		return null;
	}



	@Transient
	public String getFormaContratacao() {
		if (contrato != null && contrato.getCodigo() != null) {
			if (contrato.getFormaContratacao() != null) {
				return contrato.getFormaContratacao().getNome();
			}
		}
		return "";
	}

	@Transient
	public boolean isEmAnalise() {
		if (SituacaoCentroFinanceiro.EM_ANALISE_GERENCIA.equals(this.situacao) ||
			SituacaoCentroFinanceiro.EM_ANALISE_DIRETORIA.equals(this.situacao) ||
			SituacaoCentroFinanceiro.EM_ANALISE_RH.equals(this.situacao) ||
			SituacaoCentroFinanceiro.EM_ANALISE_FINANCEIRO.equals(this.situacao) ||
			SituacaoCentroFinanceiro.EM_ANALISE_CONTABILIDADE.equals(this.situacao) ||
			SituacaoCentroFinanceiro.EM_ANALISE_COMERCIAL.equals(this.situacao) ||
			SituacaoCentroFinanceiro.EM_ANALISE_PRESIDENCIA.equals(this.situacao)
		) {
			return true;
		}
		return false;
	}

	@Transient
	public String getObservacaohtml() {
		if (this.observacao != null && !this.observacao.isEmpty()) {
			return this.observacao.replace("\n", "<br />");
		}
		return observacao;
	}

	@Transient
	public CentroFinanceiro getGerenciaAnterior() {
		return gerenciaAnterior;
	}

	public void setGerenciaAnterior(CentroFinanceiro gerenciaAnterior) {
		this.gerenciaAnterior = gerenciaAnterior;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CentroFinanceiro)) return false;
		if (!super.equals(o)) return false;
		CentroFinanceiro that = (CentroFinanceiro) o;
		return Objects.equals(getIdentificador(), that.getIdentificador()) &&
			Objects.equals(getIdentificadorFinal(), that.getIdentificadorFinal()) &&
			Objects.equals(getNome(), that.getNome()) &&
			Objects.equals(getObservacao(), that.getObservacao()) &&
			Objects.equals(getFormaPagamento(), that.getFormaPagamento()) &&
			Objects.equals(getCriterioMedicao(), that.getCriterioMedicao()) &&
			Objects.equals(getNomeEngenheiroResposavel(), that.getNomeEngenheiroResposavel()) &&
			Objects.equals(selecionado, that.selecionado) &&
			Objects.equals(getCumulativo(), that.getCumulativo()) &&
			Objects.equals(getTotalHH(), that.getTotalHH()) &&
			Objects.equals(getDataPrevisaoReajuste(), that.getDataPrevisaoReajuste()) &&
			getSituacao() == that.getSituacao() &&
			getGarantias() == that.getGarantias() &&
			getEscopoCompra() == that.getEscopoCompra() &&
			getTipo() == that.getTipo() &&
			Objects.equals(getUnidade(), that.getUnidade()) &&
			Objects.equals(getContrato(), that.getContrato()) &&
			Objects.equals(getPai(), that.getPai()) &&
			Objects.equals(getContatoAA(), that.getContatoAA()) &&
			Objects.equals(getEngenheiroResponsavel(), that.getEngenheiroResponsavel()) &&
			Objects.equals(getFluxo(), that.getFluxo()) &&
			Objects.equals(getListaEnderecos(), that.getListaEnderecos()) &&
			Objects.equals(getGerenciaAnterior(), that.getGerenciaAnterior()) &&
			Objects.equals(getProximoNumOC(), that.getProximoNumOC()) &&
			Objects.equals(cei, that.cei);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getIdentificador(), getIdentificadorFinal(), getNome(), getObservacao(), getFormaPagamento(), getCriterioMedicao(), getNomeEngenheiroResposavel(), selecionado, getCumulativo(), getTotalHH(), getDataPrevisaoReajuste(), getSituacao(), getGarantias(), getEscopoCompra(), getTipo(), getUnidade(), getContrato(), getPai(), getContatoAA(), getEngenheiroResponsavel(), getFluxo(), getListaEnderecos(), getGerenciaAnterior(), getProximoNumOC(), cei);
	}

	@Override
	public String toString() {
		return "CentroFinanceiro{" +
			"identificador='" + identificador + '\'' +
			", identificadorFinal='" + identificadorFinal + '\'' +
			", nome='" + nome + '\'' +
			", observacao='" + observacao + '\'' +
			", formaPagamento='" + formaPagamento + '\'' +
			", criterioMedicao='" + criterioMedicao + '\'' +
			", nomeEngenheiroResposavel='" + nomeEngenheiroResposavel + '\'' +
			", selecionado=" + selecionado +
			", cumulativo=" + cumulativo +
			", totalHH=" + totalHH +
			", dataPrevisaoReajuste=" + dataPrevisaoReajuste +
			", situacao=" + situacao +
			", garantias=" + garantias +
			", escopoCompra=" + escopoCompra +
			", tipo=" + tipo +
			", unidade=" + unidade +
			", contrato=" + contrato +
			", pai=" + pai +
			", contatoAA=" + contatoAA +
			", engenheiroResponsavel=" + engenheiroResponsavel +
			", fluxo=" + fluxo +
			", listaEnderecos=" + listaEnderecos +
			", gerenciaAnterior=" + gerenciaAnterior +
			", proximoNumOC=" + proximoNumOC +
			", cei='" + cei + '\'' +
			'}';
	}

	public enum TipoCentroFinanceiro {
		CENTRO_CUSTO,
		CENTRO_LUCRO
	}

	/**
	 * Constant utilizada para definir o escopo de uma compra. Se local, o
	 * processo de compra procede dentro da gerencia do usuário
	 */
	public enum EscopoCompra {
		LOCAL,
		MATRIZ
	}

	public enum Garantias {
		NAO_EXIGIDAS("Não exigidas"),
		FIANCA_BANCARIA("Fiança bancária"),
		CAUCAO("Caução"),
		SEGURO_GARANTIA("Seguro-garantia");

		private final String nome;

		private Garantias(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return this.nome;
		}
	}

	public enum SituacaoCentroFinanceiro {
		INATIVO("Inativo", 0),
		ATIVO("Ativo", 1),
		EM_ELABORACAO("Em elaboração", 2),
		EM_REVISAO("Em revisão", 3),
		EM_ANALISE_GERENCIA("Em análise - Gerência", 4),
		EM_ANALISE_DIRETORIA("Em análise - Diretoria", 5),
		EM_ANALISE_RH("Em análise - RH", 6),
		EM_ANALISE_FINANCEIRO("Em análise - Financeiro", 7),
		EM_ANALISE_CONTABILIDADE("Em análise - Contabilidade", 8),
		EM_ANALISE_COMERCIAL("Em análise - Comercial", 9),
		EM_ANALISE_PRESIDENCIA("Em análise - Presidência", 10),
		ENCERRADO("Encerrado", 11),
		CANCELADO("Cancelado", 12),
		PARALISADO("Paralisado", 13),
		REATIVADO("Reativado", 14),
		CRIACAO("Criado", 15),
		EM_ANALISE_GESTAO("Em análise - Gestão", 16),
		ENCERRADO_COM_FATURAMENTO("Encerrado, mas com faturamento", 17);

		private final String nomeSituacao;
		private final int ordemSituacao;

		SituacaoCentroFinanceiro(String nomeSituacao, int ordemSituacao) {
			this.nomeSituacao = nomeSituacao;
			this.ordemSituacao = ordemSituacao;
		}

		public String getNomeSituacao() {
			return nomeSituacao;
		}

		public int getOrdemSituacao() {
			return ordemSituacao;
		}

		@Override
		public String toString() {
			return this.nomeSituacao;
		}
	}
}
