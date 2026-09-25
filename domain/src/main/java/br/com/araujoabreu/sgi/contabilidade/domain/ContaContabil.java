package br.com.araujoabreu.sgi.contabilidade.domain;

import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CONTA_CONTABIL")
@SequenceGenerator(name = "SEQ_CONTA_CONTABIL", sequenceName = "SEQ_CONTA_CONTABIL", allocationSize = 1)
public class ContaContabil extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -329231777369706606L;
	private ContaContabil pai;
	private String numero;
	private String numeroFormatado;
	private String descricao;
	private Boolean notaPagamento;
	private TipoContaContabil tipo;
	private ClassificacaoContaContabil classificacao;
	private String contaFCont;
	private TipoNPContaContabil tipoNotaPagamento;
	private String codigoNotaPagamento;
	private String fluxoCaixa;
	private TipoEspecial tipoEspecial;
	private Unidade empresa;
	private List<ItemLoteInterfaceCap> listaItemLoteInterfaceCap = new ArrayList<>();

	public ContaContabil() {
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL_PAI")
	public ContaContabil getPai() {
		return pai;
	}

	public void setPai(ContaContabil pai) {
		this.pai = pai;
	}

	@Column(name = "NUMERO")
	@NotNull
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "DESCRICAO")
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "NOTA_PAGAMENTO")
	@NotNull
	public Boolean getNotaPagamento() {
		return notaPagamento;
	}

	public void setNotaPagamento(Boolean notaPagamento) {
		this.notaPagamento = notaPagamento;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TIPO_CONTA")
	public TipoContaContabil getTipo() {
		return tipo;
	}

	public void setTipo(TipoContaContabil tipo) {
		this.tipo = tipo;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "CLASSIFICACAO_CONTA")
	public ClassificacaoContaContabil getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(ClassificacaoContaContabil classificacao) {
		this.classificacao = classificacao;
	}

	@Column(name = "CONTA_FCONT")
	public String getContaFCont() {
		return contaFCont;
	}

	public void setContaFCont(String contaFCont) {
		this.contaFCont = contaFCont;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "NOTA_PAGAMENTO_TIPO")
	public TipoNPContaContabil getTipoNotaPagamento() {
		return tipoNotaPagamento;
	}

	public void setTipoNotaPagamento(TipoNPContaContabil tipoNotaPagamento) {
		this.tipoNotaPagamento = tipoNotaPagamento;
	}

	@Column(name = "nota_pagamento_codigo")
	public String getCodigoNotaPagamento() {
		return codigoNotaPagamento;
	}

	public void setCodigoNotaPagamento(String codigoNotaPagamento) {
		this.codigoNotaPagamento = codigoNotaPagamento;
	}

	/**
	 * Código da conta no Sistema de Fluxo de Caixa.
	 *
	 * @return Código da conta no Sistema de Fluxo de Caixa.
	 */
	@Column(name = "fluxo_caixa_codigo", length = 2)
	@Length(max = 2)
	public String getFluxoCaixa() {
		return fluxoCaixa;
	}

	public void setFluxoCaixa(String fluxoCaixa) {
		this.fluxoCaixa = fluxoCaixa;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TIPO_ESPECIAL")
	public TipoEspecial getTipoEspecial() {
		return tipoEspecial;
	}

	public void setTipoEspecial(TipoEspecial tipoEspecial) {
		this.tipoEspecial = tipoEspecial;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_UNIDADE")
	public Unidade getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Unidade empresa) {
		this.empresa = empresa;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contaContabil")
	@OrderBy(value = "codigo")
	public List<ItemLoteInterfaceCap> getListaItemLoteInterfaceCap() {
		return listaItemLoteInterfaceCap;
	}

	public void setListaItemLoteInterfaceCap(List<ItemLoteInterfaceCap> listaItemLoteInterfaceCap) {
		this.listaItemLoteInterfaceCap = listaItemLoteInterfaceCap;
	}

	@Transient
	public String getNumeroFormatado() {
		return numeroFormatado;
	}

	public void setNumeroFormatado(String numeroFormatado) {
		this.numeroFormatado = numeroFormatado;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ContaContabil)) return false;
		if (!super.equals(o)) return false;
		ContaContabil that = (ContaContabil) o;
		return Objects.equals(getNumero(), that.getNumero()) &&
			Objects.equals(getNumeroFormatado(), that.getNumeroFormatado()) &&
			Objects.equals(getDescricao(), that.getDescricao()) &&
			Objects.equals(getNotaPagamento(), that.getNotaPagamento()) &&
			getTipo() == that.getTipo() &&
			Objects.equals(getContaFCont(), that.getContaFCont()) &&
			Objects.equals(getCodigoNotaPagamento(), that.getCodigoNotaPagamento()) &&
			Objects.equals(getFluxoCaixa(), that.getFluxoCaixa());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNumero(), getNumeroFormatado(), getDescricao(), getNotaPagamento(), getTipo(), getContaFCont(), getCodigoNotaPagamento(), getFluxoCaixa());
	}

	public enum TipoContaContabil {

		ANALITICA("Analítica", "Analítica"),
		SINTETICA("Sintética", "Sintética"),
		SINTETICA_CENTROCUSTO("Sintética/Centro de custo", "Sintética/CC"),
		CENTROCUSTO("Centro de custo", "Centro de custo");

		private final String nome;
		private final String nomeReduzido;

		private TipoContaContabil(String nome, String nomeReduzido) {
			this.nome = nome;
			this.nomeReduzido = nomeReduzido;
		}

		public String getNome() {
			return nome;
		}

		public String getNomeReduzido() {
			return nomeReduzido;
		}

		@Override
		public String toString() {
			return nome;
		}
	}

	public enum ClassificacaoContaContabil {

		ItemCompra("Item de compra - Despesa"),
		Fornecedor("Fornecedor"),
		Colaborador("Colaborador"),
		RH("RH"),
		Tributo("Tributo"),
		Banco("Banco"),
		Cliente("Cliente"),
		Tesouraria("Tesouraria"),
		Outros("Outros"),
		Faturamento("Faturamento"),
		Faturamento_receber("A receber (faturamento)"),
		TRANSFERENCIA_ENTRE_BANCOS("Transferência entre bancos (lançamentos de Tesouraria)'"),
		Cliente_CSLL("Cliente CSLL");

		private final String nome;

		private ClassificacaoContaContabil(String nome) {
			this.nome = nome;
		}

		public static ClassificacaoContaContabil getClassificacaoByOrdinal(Integer ordinal) {
			if (ordinal != null) {
				for (ClassificacaoContaContabil classificacao : ClassificacaoContaContabil.values()) {
					if (classificacao.ordinal() == ordinal) {
						return classificacao;
					}
				}
			}
			return null;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return nome;
		}
	}

	public enum TipoNPContaContabil {

		CAP("Contas a pagar"),
		GERENCIA("Gerência"),
		GERENCIA_ESPECIFICA("Gerência específica");

		private final String descricao;

		private TipoNPContaContabil(String descricao) {
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

	public enum TipoEspecial {

		FUNDO_FIXO("Fundo fixo"),
		ADIANTAMENTO("Adiantamento"),
		REEMBOLSO("Reembolso"),
		EMPRESTIMO("Empréstimo"),
		FORNECEDOR("Fornecedor"),
		RESCISAO("NP rescisão/Salários a Pagar"),
		MULTAS("Multas"),
		JUROS("Juros"),
		DESCONTOS("Descontos concedidos."),
		CLIENTE_ONERADO("Cliente onerado"),
		CLIENTE_DESONERADO("Cliente desonerado"),
		FATURAMENTO_CONTRATUAL_ONERADO("Faturamento contratual (onerado)"),
		FATURAMENTO_EXTRA_ONERADO("Faturamento extra (onerado)"),
		FATURAMENTO_CONTRATUAL_DESONERADO("Faturamento contratual (desonerado)"),
		FATURAMENTO_EXTRA_DESONERADO("Faturamento extra (desonerado)"),
		FATURAMENTO_CANCELADOS_ONERADO("Faturamento cancelados (onerado)"),
		FATURAMENTO_CANCELADOS_DESONERADO("Faturamento cancelados (desonerado)"),
		FATURAMENTO_INSS_DESONERADO_DEBITO("Faturamento - INSS - Desoneração - Débito"),
		FATURAMENTO_INSS_DESONERADO_FP_DEBITO("Faturamento - INSS - Desoneração FP - Débito"),
		FATURAMENTO_INSS_COMPENSAR("Faturamento - INSS a compensar"),
		FATURAMENTO_INSS_RETIDO("Faturamento - INSS retido pelos clientes"),
		TRANSFERENCIA_ENTRE_BANCOS("Transferência entre bancos (lançamentos de Tesouraria)'"),
		FATURAMENTO_IRRF_DEBITO("Faturamento - IRRF - Débito"),
		FATURAMENTO_IRRF_CREDITO("Faturamento - IRRF - Crédito"),
		FATURAMENTO_PIS_DEBITO("Faturamento - PIS - Débito"),
		FATURAMENTO_PIS_CREDITO("Faturamento - PIS - Crédito"),
		FATURAMENTO_COFINS_DEBITO("Faturamento - COFINS - Débito"),
		FATURAMENTO_COFINS_CREDITO("Faturamento - COFINS - Crédito"),
		CLIENTE_CSLL("Cliente - CSLL"),
		ISS("ISS"),
		CSLL_DEBITO("CSLL - Débito"),
		FATURAMENTO_INSS_DESONERADO_FP_CREDITO("Faturamento - INSS - Desoneração FP - Crédito"),
		FATURAMENTO_SISTEMA_FISCAL_CSLL_PIS("Faturamento - Sistema Fiscal CSLL/PIS"),
		FATURAMENTO_SISTEMA_FISCAL_CSLL_COFINS("Faturamento - Sistema Fiscal CSLL/COFINS"),
		FATURAMENTO_INSS_ONERADO("Faturamento - INSS - Onerado"),
		FATURAMENTO_RETENCAO_CONTRATUAL("Faturamento - Retenção contratual"),
		JUROS_RECEBIDO("Juros recebidos"),
		NOTA_DEBITO("Nota de débito (ND)"),
		FATURAMENTO_CONTRATUAL_MATERIAL("Faturamento contratual (material)"),
		FATURAMENTO_EXTRA_MATERIAL("Faturamento extra (material)");
		private final String nome;

		private TipoEspecial(String nome) {
			this.nome = nome;
		}

		public static TipoEspecial getTipoEspecialByOrdinal(Integer ordinal) {
			if (ordinal != null) {
				for (TipoEspecial tipoEspecial : TipoEspecial.values()) {
					if (tipoEspecial.ordinal() == ordinal) {
						return tipoEspecial;
					}
				}
			}
			return null;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return nome;
		}
	}
}
