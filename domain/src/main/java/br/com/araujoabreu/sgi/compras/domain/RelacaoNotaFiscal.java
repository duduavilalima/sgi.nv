package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "RELACAO_NF")
@SequenceGenerator(name="SEQ_RELACAO_NF", sequenceName="SEQ_RELACAO_NF", allocationSize=1)
public class RelacaoNotaFiscal extends AuditEntity {

	private static final long serialVersionUID = -7342103490884934012L;
	private Colaborador emissor;
	private Colaborador recebedor;
	private Colaborador cancelador;
	private Colaborador emitente;
	private Colaborador engenheiro;
	private Colaborador gerenteSupAdm;
	private StatusRelacaoNF status;
	private Date dataEmissao;
	private Date dataRecebimento;
	private Date dataCancelamento;
	private Date dataEnvio;
	private String observacao;
	private CentroFinanceiro centroFinanceiro;
	private TipoFornecimentoRN tipoFornecimentoRN;
	private Set<ItemRelacaoNotaFiscal> listaItensNF = new LinkedHashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ENVIO")
	public Colaborador getEmissor() {
		return emissor;
	}

	public void setEmissor(Colaborador emissor) {
		this.emissor = emissor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RECEB_FINANCEIRO")
	public Colaborador getRecebedor() {
		return recebedor;
	}

	public void setRecebedor(Colaborador recebedor) {
		this.recebedor = recebedor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_GER_SUPERVISOR_ADM")
	public Colaborador getGerenteSupAdm() {
		return gerenteSupAdm;
	}

	public void setGerenteSupAdm(Colaborador gerenteSupAdm) {
		this.gerenteSupAdm = gerenteSupAdm;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_EMISSOR")
	public Colaborador getEmitente() {
		return emitente;
	}

	public void setEmitente(Colaborador emitente) {
		this.emitente = emitente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ENG_RESPONSAVEL")
	public Colaborador getEngenheiro() {
		return engenheiro;
	}

	public void setEngenheiro(Colaborador engenheiro) {
		this.engenheiro = engenheiro;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CANCELAMENTO")
	public Colaborador getCancelador() {
		return cancelador;
	}

	public void setCancelador(Colaborador cancelador) {
		this.cancelador = cancelador;
	}

	@Column(name = "SITUACAO", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	public StatusRelacaoNF getStatus() {
		return status;
	}

	public void setStatus(StatusRelacaoNF status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_EMISSOR")
	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_RECEB_FINANCEIRO")
	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_ENVIO")
	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CANCELAMENTO")
	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	@Column(name = "OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "relacaoNotaFiscal")
	@OrderBy(value = "codigo")
	public Set<ItemRelacaoNotaFiscal> getListaItensNF() {
		return listaItensNF;
	}

	public void setListaItensNF(Set<ItemRelacaoNotaFiscal> listaItensNF) {
		this.listaItensNF = listaItensNF;
	}

	@Transient
	public boolean isEnviada() {
		return status.equals(StatusRelacaoNF.ENVIADA);
	}

	@Transient
	public boolean isAberta() {
		return status.equals(StatusRelacaoNF.ABERTA);
	}

	@Transient
	public boolean isCancelada() {
		return status.equals(StatusRelacaoNF.CANCELADA);
	}

	@Transient
	public BigDecimal getTotalNFs() {
		BigDecimal total = new BigDecimal("0");
		if (listaItensNF != null && !listaItensNF.isEmpty()) {
			for (ItemRelacaoNotaFiscal item : listaItensNF) {
				total = total.add(item.getNotaFiscal().getValor());
			}
		}
		return total;
	}

	@Transient
	public String getNumero() {
		if (getCodigo() != null) {
			return "RN/" + getCodigo();
		}
		return "";
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@Column(name = "TIPO_FORNECIMENTO")
	@Enumerated(EnumType.ORDINAL)
	public TipoFornecimentoRN getTipoFornecimentoRN() {
		return tipoFornecimentoRN;
	}

	public void setTipoFornecimentoRN(TipoFornecimentoRN tipoFornecimentoRN) {
		this.tipoFornecimentoRN = tipoFornecimentoRN;
	}

	@Transient
	public boolean isRateio() {
		if (listaItensNF != null && !listaItensNF.isEmpty()) {
			for (ItemRelacaoNotaFiscal itemRelacao : listaItensNF) {
				if (itemRelacao != null && itemRelacao.getNotaFiscal().isRateio()) {
					return true;
				}
			}
		}
		return false;
	}

	@Transient
	public boolean isCentroFinanceiroUnico() {
		if (listaItensNF != null && !listaItensNF.isEmpty()) {
			for (ItemRelacaoNotaFiscal itemRelacao : listaItensNF) {
				if (itemRelacao != null && !itemRelacao.getNotaFiscal().isCentroFinanceiroUnico()) {
					return false;
				}
			}
		}
		return true;
	}

	@Transient
	public boolean hasNFs() {
		if (listaItensNF != null && !listaItensNF.isEmpty())
			return true;
		return false;
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
		if (obj == null) {
			return false;
		}
		if (!(RelacaoNotaFiscal.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		RelacaoNotaFiscal other = (RelacaoNotaFiscal) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}


	public enum StatusRelacaoNF {
		ABERTA("Aberta"),
		ENVIADA("Enviada (aguardando recebimento)"),
		RECEBIDA_TOTAL("Recebida total"),
		RECEBIDA_PARCIAL("Recebida parcial"),
		CANCELADA("Cancelada"),
		RECEBIDA_TOTAL_PARCIAL("Recebida (Total/Parcial)");

		private final String nomeStatus;

		StatusRelacaoNF(String nomeStatus) {
			this.nomeStatus = nomeStatus;
		}

		public String getNomeStatus() {
			return nomeStatus;
		}

	}


	public enum TipoFornecimentoRN {
		MATERIAL("Aberta", "Material"), //TODO: Confirmar a descrição "Aberta" e "Enviada..,"
		SERVICO_CONJUGADA("Enviada (aguardando recebimento)", "Serviço/Conjugada"),
		DIVERSOS("Diversos", "Diversos");

		private final String nome;
		private final String descricao;

		TipoFornecimentoRN(String nome, String descricao) {
			this.nome = nome;
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		public String getNome() {
			return nome;
		}

	}

}
