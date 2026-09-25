package br.com.araujoabreu.sgi.fundoFixo.vo;

import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "VW_ANALISE_SALDO_FUNDO_FIXO")
public class FundoFixoVO implements Serializable {

	/**
	 * @author bruno.barata
	 */
	private static final long serialVersionUID = -9072362030275133425L;

	@Id
	@NotNull
	@Column(name = "fundo")
	private Long codigo;

	@Column(name = "total_aprovacao")
	private BigDecimal totalAprovacao;

	@Column(name = "saldo_pendente")
	private BigDecimal saldoPendente;

	@Column(name = "total_homologacao")
	private BigDecimal totalHomologacao;

	@Column(name = "saldo")
	private BigDecimal saldo;

	@Column(name = "limite")
	private BigDecimal limite;

	@Column(name = "colaborador")
	private String colaborador;

	@Transient
	private boolean isDiferencaSaldo;

	@Transient
	private boolean isDiferencaSaldoPendente;

	@Transient
	private BigDecimal saldoPendenteCorreto;

	@Transient
	private BigDecimal saldoCorreto;

	public FundoFixoVO() {
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public BigDecimal getTotalAprovacao() {
		return totalAprovacao;
	}

	public void setTotalAprovacao(BigDecimal totalAprovacao) {
		this.totalAprovacao = totalAprovacao;
	}

	public BigDecimal getSaldoPendente() {
		return saldoPendente;
	}

	public void setSaldoPendente(BigDecimal saldoPendente) {
		this.saldoPendente = saldoPendente;
	}

	public BigDecimal getTotalHomologacao() {
		return totalHomologacao;
	}

	public void setTotalHomologacao(BigDecimal totalHomologacao) {
		this.totalHomologacao = totalHomologacao;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public BigDecimal getLimite() {
		return limite;
	}

	public void setLimite(BigDecimal limite) {
		this.limite = limite;
	}

	public String getColaborador() {
		return colaborador;
	}

	public void setColaborador(String colaborador) {
		this.colaborador = colaborador;
	}

	public BigDecimal getSaldoPendenteCorreto() {
		return saldoPendenteCorreto;
	}

	public void setSaldoPendenteCorreto(BigDecimal saldoPendenteCorreto) {
		this.saldoPendenteCorreto = totalAprovacao.add(totalHomologacao);
	}

	public BigDecimal getSaldoCorreto() {
		return saldoCorreto;
	}

	public void setSaldoCorreto(BigDecimal saldoCorreto) {
		this.saldoCorreto = limite.subtract(saldoPendenteCorreto);
	}

	public boolean isDiferencaSaldo() {
		return !(limite.subtract(totalAprovacao.add(totalHomologacao)).equals(saldo));
	}

	public boolean isDiferencaSaldoPendente() {
		return !(totalAprovacao.add(totalHomologacao).equals(saldoPendente));
	}

	@Override
	public String toString() {
		return "FundoFixoVO [codigo=" + codigo + ", totalAprovacao=" + totalAprovacao + ", saldoPendente=" + saldoPendente + ", totalHomologacao=" + totalHomologacao + ", saldo=" + saldo + ", limite=" + limite + ", colaborador=" + colaborador
			+ ", isDiferencaSaldo=" + isDiferencaSaldo + ", isDiferencaSaldoPendente=" + isDiferencaSaldoPendente + ", saldoPendenteCorreto=" + saldoPendenteCorreto + ", saldoCorreto=" + saldoCorreto + "]";
	}


}
