package br.com.araujoabreu.sgi.comercial.domain;

import br.com.araujoabreu.sgi.administracao.domain.Contrato;
import br.com.araujoabreu.sgi.administracao.domain.Contrato.PeriodoReajuste;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ADITIVO")
@SequenceGenerator(name = "SEQ_ADITIVO", sequenceName = "SEQ_ADITIVO", allocationSize = 1)
public class Aditivo extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -6458046624086283119L;
	private Integer numero;
	private Contrato contrato;
	private Date dataAssinatura;
	private Date dataInicioVigencia;
	private Date dataFimVigencia;
	private Integer prazoAtual;
	private BigDecimal valorMensal;
	private PeriodoReajuste periodoReajuste;
	private String indiceReajuste;
	private String resumo;

	public Aditivo() {
		super();
	}

	public Aditivo(Long codigo) {
		setCodigo(codigo);
	}

	public Aditivo(Contrato contrato) {
		super();
		this.contrato = contrato;
	}

	@Column(name = "NUMERO", nullable = false)
	@NotNull
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTRATO", nullable = false)
	@NotNull
	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	@Column(name = "DATA_ASSINATURA")
	public Date getDataAssinatura() {
		return dataAssinatura;
	}

	public void setDataAssinatura(Date dataAssinatura) {
		this.dataAssinatura = dataAssinatura;
	}

	@Column(name = "INICIO_VIGENCIA")
	public Date getDataInicioVigencia() {
		return dataInicioVigencia;
	}

	public void setDataInicioVigencia(Date dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	@Column(name = "FIM_VIGENCIA")
	public Date getDataFimVigencia() {
		return dataFimVigencia;
	}

	public void setDataFimVigencia(Date dataFimVigencia) {
		this.dataFimVigencia = dataFimVigencia;
	}

	@Column(name = "PRAZO")
	public Integer getPrazoAtual() {
		return prazoAtual;
	}

	public void setPrazoAtual(Integer prazoAtual) {
		this.prazoAtual = prazoAtual;
	}

	@Column(name = "VALOR_MENSAL")
	public BigDecimal getValorMensal() {
		return valorMensal;
	}

	public void setValorMensal(BigDecimal valorMensal) {
		this.valorMensal = valorMensal;
	}

	@Column(name = "PERIODO_REAJUSTE")
	public PeriodoReajuste getPeriodoReajuste() {
		return periodoReajuste;
	}

	public void setPeriodoReajuste(PeriodoReajuste periodoReajuste) {
		this.periodoReajuste = periodoReajuste;
	}

	@Column(name = "INDICE_REAJUSTE")
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


	@Transient
	public void preRemove() {
		setContrato(null);
	}

	@Transient
	public String getResumoMinimo() {
		if (resumo != null && !resumo.isEmpty()) {
			if (resumo.trim().length() <= 20) {
				return numero + " - " + resumo;
			} else {
				return numero + " - " + resumo.substring(0, 20);
			}
		}
		return "";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Aditivo)) return false;
		if (!super.equals(o)) return false;
		Aditivo aditivo = (Aditivo) o;
		return Objects.equals(getNumero(), aditivo.getNumero()) &&
			Objects.equals(getDataAssinatura(), aditivo.getDataAssinatura()) &&
			Objects.equals(getDataInicioVigencia(), aditivo.getDataInicioVigencia()) &&
			Objects.equals(getDataFimVigencia(), aditivo.getDataFimVigencia()) &&
			Objects.equals(getPrazoAtual(), aditivo.getPrazoAtual()) &&
			Objects.equals(getValorMensal(), aditivo.getValorMensal()) &&
			Objects.equals(getIndiceReajuste(), aditivo.getIndiceReajuste()) &&
			Objects.equals(getResumo(), aditivo.getResumo());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNumero(), getDataAssinatura(), getDataInicioVigencia(), getDataFimVigencia(), getPrazoAtual(), getValorMensal(), getIndiceReajuste(), getResumo());
	}

	@Override
	public String toString() {
		return "Aditivo{" +
			"numero=" + numero +
			", dataAssinatura=" + dataAssinatura +
			", dataInicioVigencia=" + dataInicioVigencia +
			", dataFimVigencia=" + dataFimVigencia +
			", prazoAtual=" + prazoAtual +
			", valorMensal=" + valorMensal +
			", indiceReajuste='" + indiceReajuste + '\'' +
			", resumo='" + resumo + '\'' +
			'}';
	}
}
