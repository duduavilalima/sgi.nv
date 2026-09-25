package br.com.araujoabreu.sgi.fundoFixo.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.EntityStatus;
import br.com.araujoabreu.sgi.administracao.domain.NaturezaDespesa;
import br.com.araujoabreu.sgi.fundoFixo.enums.TipoConta;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Representa o saldo disponível na conta do funcionário para pequenas
 * despesas. O funcionário não pode gastar a mais do que está fixado
 * inicialmente, por isso a razão do nome "fundo fixo".
 * <p>
 * Para esse controle é necessário relacionar as despesas efetudas com o fundo fixo,
 * associando-as a qualquer uma natureza da despesa  .
 *
 * @author mlacerda
 * @see NaturezaDespesa
 */
@Entity
@Table(name = "FUNDO_FIXO")
@SequenceGenerator(name="SEQ_FUNDO_FIXO", sequenceName="SEQ_FUNDO_FIXO", allocationSize=1)
public class FundoFixo extends AuditEntity implements DomainObject, Serializable {

	private static final long serialVersionUID = 1152747513564316528L;
	private Colaborador colaborador;
	private CentroFinanceiro centroFinanceiro;
	private BigDecimal limite;
	private BigDecimal saldo;
	private EntityStatus status;
	private String descricao;

	private BigDecimal saldoPendente;

	private Colaborador alteradorLimite;
	private Date dataAlteracaoLimite;
	private BigDecimal limiteAnterior;

	private Colaborador inativador;
	private Date dataInativacao;

	private TipoConta tipo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_COLABORADOR")
	@NotNull
	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	@NotNull
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@Column(name = "LIMITE", nullable = false, precision = 19, scale = 2)
	public BigDecimal getLimite() {
		return limite;
	}

	public void setLimite(BigDecimal limite) {
		this.limite = limite;
	}

	@Column(name = "SALDO", nullable = false, precision = 19, scale = 2)
	@NotNull
	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	@Transient
	public void sensibilizar(Transacao transacao) {
		BigDecimal valor = transacao.getValor();
		if (transacao.isDebito())
			this.saldo = this.saldo.subtract(valor);
		else
			this.saldo = this.saldo.add(valor);

		// registra o saldo na transacao para historico e adiciona no fundofixo
		transacao.setSaldo(this.saldo);
		transacao.setData(new Date());
	}

	@Transient
	public void sensibilizarRejeicaoDemonstrativo(Transacao transacao) {
		BigDecimal valor = transacao.getValor();
		this.saldo = this.saldo.add(valor);

		// registra o saldo na transacao para historico e adiciona no fundofixo
		transacao.setSaldo(this.saldo);
		transacao.setData(new Date());
	}

	/**
	 * @return the status
	 */
	@Column(name = "STATUS", nullable = false, length = 1)
	@NotNull
	public EntityStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(EntityStatus status) {
		this.status = status;
	}

	@Column(name = "DESCRICAO")
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "SALDO_PENDENTE", nullable = false, precision = 19, scale = 2)
	@NotNull
	public BigDecimal getSaldoPendente() {
		return saldoPendente;
	}

	public void setSaldoPendente(BigDecimal saldoPendente) {
		this.saldoPendente = saldoPendente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_LIMITE_ANTERIOR")
	public Colaborador getAlteradorLimite() {
		return alteradorLimite;
	}

	public void setAlteradorLimite(Colaborador alteradorLimite) {
		this.alteradorLimite = alteradorLimite;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_LIMITE_ANTERIOR")
	public Date getDataAlteracaoLimite() {
		return dataAlteracaoLimite;
	}

	public void setDataAlteracaoLimite(Date dataAlteracaoLimite) {
		this.dataAlteracaoLimite = dataAlteracaoLimite;
	}

	@Column(name = "LIMITE_ANTERIOR")
	public BigDecimal getLimiteAnterior() {
		return limiteAnterior;
	}

	public void setLimiteAnterior(BigDecimal limiteAnterior) {
		this.limiteAnterior = limiteAnterior;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_INATIVADOR")
	public Colaborador getInativador() {
		return inativador;
	}

	public void setInativador(Colaborador inativador) {
		this.inativador = inativador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_INATIVACAO")
	public Date getDataInativacao() {
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	@Column(name = "TIPO")
	public TipoConta getTipo() {
		return tipo;
	}

	public void setTipo(TipoConta tipo) {
		this.tipo = tipo;
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
		if (!(FundoFixo.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		FundoFixo other = (FundoFixo) obj;
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
		return "FundoFixo [codigo=" + getCodigo() + ", limite=" + limite + ", saldo=" + saldo + ", saldoPendente=" + saldoPendente + ", limiteAnterior="
			+ limiteAnterior + "]";
	}


}
