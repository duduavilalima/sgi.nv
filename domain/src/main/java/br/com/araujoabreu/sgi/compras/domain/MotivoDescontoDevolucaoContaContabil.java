package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "MOTIVO_DESCONTO_DEVOLUCAO_CONTA_CONTABIL")
@SequenceGenerator(name="SEQ_MOTIVO_DESCONTO_DEVOLUCAO_CONTA_CONTABIL", sequenceName="SEQ_MOTIVO_DESCONTO_DEVOLUCAO_CONTA_CONTABIL", allocationSize=1)
public class MotivoDescontoDevolucaoContaContabil extends BaseEntity {

	private static final long serialVersionUID = 2175519272303952933L;
	private MotivoDescontoDevolucao motivoDescontoDevolucao;
	private ContaContabil contaContabil;
	private Tipo tipo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_MOTIVO_DESCONTO_DEVOLUCAO", nullable = false)
	public MotivoDescontoDevolucao getMotivoDescontoDevolucao() {
		return motivoDescontoDevolucao;
	}

	public void setMotivoDescontoDevolucao(MotivoDescontoDevolucao motivoDescontoDevolucao) {
		this.motivoDescontoDevolucao = motivoDescontoDevolucao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL", nullable = false)
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@Column(nullable = false)
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		MotivoDescontoDevolucaoContaContabil that = (MotivoDescontoDevolucaoContaContabil) o;
		return Objects.equals(getMotivoDescontoDevolucao(), that.getMotivoDescontoDevolucao()) &&
			Objects.equals(getContaContabil(), that.getContaContabil()) &&
			getTipo() == that.getTipo();
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getMotivoDescontoDevolucao(), getContaContabil(), getTipo());
	}

	public enum Tipo {
		DESCONTO("Desconto"),
		DEVOLUCAO("Devolução"),
		DESCONTO_DEVOLUCAO("Desconto e devolução");

		Tipo(String descricao) {
		}

		@Override
		public String toString() {
			return super.toString();
		}
	}
}
