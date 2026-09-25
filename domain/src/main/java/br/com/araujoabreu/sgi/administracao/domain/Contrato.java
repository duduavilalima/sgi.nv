package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CONTRATO")
@SequenceGenerator(name = "SEQ_CONTRATO", sequenceName = "SEQ_CONTRATO",	allocationSize = 1)
public class Contrato extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -2642661126112802807L;
	private Unidade unidade;
	private Diretoria diretoria;
	private Pessoa cliente;
	private FormaContratacao formaContratacao;
	private SituacaoContrato situacao;
	private TipoContrato tipo;
	private String proposta;
	private EstagioComercial estagioComercial;
	private Date dataAssinatura;
	private Date dataInicioVigencia;
	private Date dataFimVigencia;
	private Integer prazoAtual;
	private BigDecimal valorMensal;
	private PeriodoReajuste periodoReajuste;
	private Prorrogacao prorrogacao;
	private String indiceReajuste;
	private String resumo;
	private BigDecimal percentualRetencao;
	private String contratoOriginal;
	private BigDecimal valorTotal;
	private PeriodicidadePagamento periodicidadePagamento;
	private Integer periodosVigencia;
	private Integer diaVencimento;
	private TipoVencimento diaVencimentoTipo;
	private Integer diaVencimento2;
	private TipoVencimento diaVencimento2Tipo;
	private Date dataPrimeiraParcela;
	private List<Gerencia> listaGerencias = new ArrayList<>();

	public Contrato() {
	}

	public Contrato(Pessoa cliente, TipoContrato tipo) {
		this.cliente = cliente;
		this.tipo = tipo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_UNIDADE", nullable = false)
	@NotNull
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_DIRETORIA", nullable = false)
	@NotNull
	public Diretoria getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(Diretoria diretoria) {
		this.diretoria = diretoria;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PESSOA", nullable = false)
	@NotNull
	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	@Column(name = "SITUACAO", nullable = false)
	@NotNull
	public SituacaoContrato getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoContrato situacao) {
		this.situacao = situacao;
	}

	@Column(name = "PRORROGACAO")
	@Enumerated(EnumType.ORDINAL)
	public Prorrogacao getProrrogacao() {
		return prorrogacao;
	}

	public void setProrrogacao(Prorrogacao prorrogacao) {
		this.prorrogacao = prorrogacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_FORMA_CONTRATACAO")
	public FormaContratacao getFormaContratacao() {
		return formaContratacao;
	}

	public void setFormaContratacao(FormaContratacao formaContratacao) {
		this.formaContratacao = formaContratacao;
	}

	@Column(name = "TIPO", nullable = false)
	@NotNull
	public TipoContrato getTipo() {
		return tipo;
	}

	public void setTipo(TipoContrato tipo) {
		this.tipo = tipo;
	}

	@Column(name = "PROPOSTA")
	public String getProposta() {
		return proposta;
	}

	public void setProposta(String proposta) {
		this.proposta = proposta;
	}

	@Column(name = "ESTAGIO_COMERCIAL")
	public EstagioComercial getEstagioComercial() {
		return estagioComercial;
	}

	public void setEstagioComercial(EstagioComercial estagioComercial) {
		this.estagioComercial = estagioComercial;
	}

	@Column(name = "DATA_ASSINATURA_ATUAL")
	public Date getDataAssinatura() {
		return dataAssinatura;
	}

	public void setDataAssinatura(Date dataAssinatura) {
		this.dataAssinatura = dataAssinatura;
	}

	@Column(name = "INICIO_VIGENCIA_ATUAL")
	public Date getDataInicioVigencia() {
		return dataInicioVigencia;
	}

	public void setDataInicioVigencia(Date dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	@Column(name = "FIM_VIGENCIA_ATUAL")
	public Date getDataFimVigencia() {
		return dataFimVigencia;
	}

	public void setDataFimVigencia(Date dataFimVigencia) {
		this.dataFimVigencia = dataFimVigencia;
	}

	@Column(name = "PRAZO_ATUAL")
	public Integer getPrazoAtual() {
		return prazoAtual;
	}

	public void setPrazoAtual(Integer prazoAtual) {
		this.prazoAtual = prazoAtual;
	}

	@Column(name = "VALOR_MENSAL_ATUAL")
	public BigDecimal getValorMensal() {
		return valorMensal;
	}

	public void setValorMensal(BigDecimal valorMensal) {
		this.valorMensal = valorMensal;
	}

	@Column(name = "PERIODO_REAJUSTE_ATUAL")
	public PeriodoReajuste getPeriodoReajuste() {
		return periodoReajuste;
	}

	public void setPeriodoReajuste(PeriodoReajuste periodoReajuste) {
		this.periodoReajuste = periodoReajuste;
	}

	@Column(name = "INDICE_REAJUSTE_ATUAL")
	public String getIndiceReajuste() {
		return indiceReajuste;
	}

	public void setIndiceReajuste(String indiceReajuste) {
		this.indiceReajuste = indiceReajuste;
	}

	@Column(name = "RESUMO")
	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	@Column(name = "CONTRATO_ORIGINAL")
	public String getContratoOriginal() {
		return contratoOriginal;
	}

	public void setContratoOriginal(String contratoOriginal) {
		this.contratoOriginal = contratoOriginal;
	}

	@Column(name = "PERIODICIDADE_PAGAMENTO")
	public PeriodicidadePagamento getPeriodicidadePagamento() {
		return periodicidadePagamento;
	}

	public void setPeriodicidadePagamento(PeriodicidadePagamento periodicidadePagamento) {
		this.periodicidadePagamento = periodicidadePagamento;
	}

	@Column(name = "PERIODOS_VIGENCIA")
	public Integer getPeriodosVigencia() {
		return periodosVigencia;
	}

	public void setPeriodosVigencia(Integer periodosVigencia) {
		this.periodosVigencia = periodosVigencia;
	}

	@Column(name = "DIA_VENCIMENTO")
	public Integer getDiaVencimento() {
		return diaVencimento;
	}

	public void setDiaVencimento(Integer diaVencimento) {
		this.diaVencimento = diaVencimento;
	}

	@Column(name = "DIA_VENCIMENTO_TIPO")
	public TipoVencimento getDiaVencimentoTipo() {
		return diaVencimentoTipo;
	}

	public void setDiaVencimentoTipo(TipoVencimento diaVencimentoTipo) {
		this.diaVencimentoTipo = diaVencimentoTipo;
	}

	@Column(name = "DIA_VENCIMENTO2")
	public Integer getDiaVencimento2() {
		return diaVencimento2;
	}

	public void setDiaVencimento2(Integer diaVencimento2) {
		this.diaVencimento2 = diaVencimento2;
	}

	@Column(name = "DIA_VENCIMENTO2_TIPO")
	public TipoVencimento getDiaVencimento2Tipo() {
		return diaVencimento2Tipo;
	}

	public void setDiaVencimento2Tipo(TipoVencimento diaVencimento2Tipo) {
		this.diaVencimento2Tipo = diaVencimento2Tipo;
	}

	@Column(name = "DATA_PRIMEIRA_PARCELA")
	public Date getDataPrimeiraParcela() {
		return dataPrimeiraParcela;
	}

	public void setDataPrimeiraParcela(Date dataPrimeiraParcela) {
		this.dataPrimeiraParcela = dataPrimeiraParcela;
	}

	@Column(name = "PERCENTUAL_RETENCAO")
	public BigDecimal getPercentualRetencao() {
		return percentualRetencao;
	}

	public void setPercentualRetencao(BigDecimal percentualRetencao) {
		this.percentualRetencao = percentualRetencao;
	}

	@Column(name = "VALOR_TOTAL")
	public BigDecimal getValorTotal() {
		if (this.valorTotal == null || this.valorTotal.equals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)))
			return getValorTotalCalculado();

		return this.valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	@Transient
	public String getNumero() {
		if (this.getCodigo() != null)
			return "CT/" + String.format("%06d", this.getCodigo());

		return "";
	}

	@Transient
	public String getNumeroVigenciaPeriodicidade() {
		StringBuilder label = new StringBuilder();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");

		label.append(this.getNumero());
		label.append(" - ");
		label.append(dateFormat.format(this.getDataInicioVigencia()));
		label.append(" - ");
		label.append(dateFormat.format(this.getDataFimVigencia()));
		if (this.getPeriodicidadePagamento() != null) {
			label.append(" - ");
			label.append(this.getPeriodicidadePagamento().getNome());
		}

		return label.toString();
	}

	@Transient
	public BigDecimal getValorTotalCalculado() {
		if (valorMensal != null && prazoAtual != null) {
			return valorMensal.multiply(new BigDecimal(prazoAtual));
		} else {
			return new BigDecimal(0L);
		}
	}

	// Está sendo usado na tela de pesquisa contrato
	@ManyToMany(targetEntity = br.com.araujoabreu.sgi.administracao.domain.Gerencia.class, fetch = FetchType.LAZY)
	@JoinTable(
		name = "CONTRATO_GERENCIA",
		joinColumns = @JoinColumn(name = "CODIGO_CONTRATO"),
		inverseJoinColumns = @JoinColumn(name = "CODIGO_GERENCIA", insertable = false, updatable = false)
	)
	public List<Gerencia> getListaGerencias() {
		return listaGerencias;
	}

	public void setListaGerencias(List<Gerencia> listaGerencias) {
		this.listaGerencias = listaGerencias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((getPrazoAtual() == null) ? 0 : getPrazoAtual().hashCode());
		result = prime * result + ((getResumo() == null) ? 0 : getResumo().hashCode());
		result = prime * result + ((getSituacao() == null) ? 0 : getSituacao().hashCode());
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

		if (!(Contrato.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}

		if (!(obj instanceof Contrato)) {
			return false;
		}

		Contrato other = (Contrato) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}

		if (getPrazoAtual() == null) {
			if (other.getPrazoAtual() != null) {
				return false;
			}
		} else if (!getPrazoAtual().equals(other.getPrazoAtual())) {
			return false;
		}

		if (getResumo() == null) {
			if (other.getResumo() != null) {
				return false;
			}
		} else if (!getResumo().equals(other.getResumo())) {
			return false;
		}

		if (getSituacao() != other.getSituacao()) {
			return false;
		}

		return true;
	}

	public enum SituacaoContrato {
		EM_ELABORACAO("Em elaboração", 0),
		EM_ANALISE("Em análise", 1),
		ATIVO("Ativo", 2),
		ENCERRADO("Encerrado", 3),
		CANCELADO("Cancelado", 4),
		PARALISADO("Paralisado", 5),
		EM_REVISAO("Em revisão", 6),
		ATIVO_PARALISADO("Ativo/Paralisado", 7);

		private final String nome;
		private final int ordem;

		SituacaoContrato(String nome, int ordem) {
			this.nome = nome;
			this.ordem = ordem;
		}

		public String getNome() {
			return nome;
		}

		public int getOrdem() {
			return ordem;
		}

		@Override
		public String toString() {
			switch (ordinal()) {
				case 0:
					return "Em elaboração";
				case 1:
					return "Em análise";
				case 2:
					return "Ativo";
				case 3:
					return "Encerrado";
				case 4:
					return "Cancelado";
				case 5:
					return "Paralisado";
				case 6:
					return "Em revisão";
				case 7:
					return "Ativo/Paralisado";
				default:
					return "";
			}
		}
	}

	public enum TipoContrato {
		CLIENTE("Cliente"),
		FORNECEDOR("Fornecedor");

		private final String nome;

		TipoContrato(String nome) {

			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			switch (ordinal()) {
				case 0:
					return "Cliente";
				case 1:
					return "Fornecedor";
				default:
					return "";
			}
		}
	}

	public enum EstagioComercial {
		PROPOSTA_ACEITA("Proposta aceita"),
		ASSINADO("Contrato assinado"),
		CARTA_INTENCAO("Carta de intenção"),
		;

		private final String nome;

		EstagioComercial(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return nome;
		}
	}

	public enum PeriodoReajuste {
		ANUAL("Anual"),
		SEMESTRAL("Semestral"),
		TRIMESTRAL("Trimestral"),
		;

		private final String nome;

		PeriodoReajuste(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return nome;
		}
	}

	public enum Prorrogacao {
		NA("Não se aplica"),
		IGUAIS_PERIODOS("Por iguais períodos");

		private final String nome;

		private Prorrogacao(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return nome;
		}
	}

	public enum PeriodicidadePagamento {
		CONTRA_APRESENTACAO("Contra-apresentação", 0, true),
		QUINZENAL("Quinzenal", 1, true),
		MENSAL("Mensal", 2, true),
		BIMESTRAL("Bimestral", 3, true),
		TRIMESTRAL("Trimestral", 4, true),
		QUADRIMESTRAL("Quadrimestral", 5, true),
		SEMESTRAL("Semestral", 6, true),
		ANUAL("Anual", 7, true),
		SEPARACAO("-------------------------", null, false),//indica se a opção ficará habilitada no componente selectOneMenu
		SELECIONE("Selecione...", null, true);

		private final String nome;
		private final Integer numero;
		private final boolean habilitado;

		private PeriodicidadePagamento(String nome, Integer numero, boolean habilitado) {
			this.nome = nome;
			this.numero = numero;
			this.habilitado = habilitado;
		}

		public String getNome() {
			return nome;
		}

		public boolean isHabilitado() {
			return habilitado;
		}

		public Integer getNumero() {
			return numero;
		}

	}

	public enum TipoVencimento {
		DIA_CORRIDO("Dia"),
		DIA_UTIL("Dia útil");

		private final String nome;

		private TipoVencimento(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}
	}

}
