package br.com.araujoabreu.sgi.fundoFixo.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Representa uma movimentação bancária realizada no sistema. Uma movimentação pode ser
 * de crétido ou de débito
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "TRANSACAO")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name="SEQ_TRANSACAO", sequenceName="SEQ_TRANSACAO", allocationSize=1)
public class Transacao extends AuditEntity {

	private static final long serialVersionUID = 664651665936179119L;
	private BigDecimal valor = new BigDecimal(0);
	private Date data;
	private BigDecimal saldo;
	// TODO [marcus] revisar o relacionamento com fundo fixo - algum controle mais genérico de saldo.
	private FundoFixo fundoFixo;
	private CentroFinanceiro centroFinanceiro;

	@Column(name = "VALOR", precision = 9, scale = 2)
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Column(name = "SALDO")
	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	@Column(name = "DATA", nullable = false)
	@NotNull
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_FUNDO_FIXO")
	public FundoFixo getFundoFixo() {
		return fundoFixo;
	}

	public void setFundoFixo(FundoFixo fundoFixo) {
		this.fundoFixo = fundoFixo;
	}

	@Transient
	public boolean isDebito() {
		return (this instanceof Demonstrativo);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

}
