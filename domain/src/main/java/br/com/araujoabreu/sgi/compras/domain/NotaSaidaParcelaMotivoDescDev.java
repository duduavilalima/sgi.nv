package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "NOTA_SAIDA_PARCELA_MOTIVO_DESC_DEV")
@SequenceGenerator(name="SEQ_NOTA_SAIDA_PARCELA_MOTIVO_DESC_DEV", sequenceName="SEQ_NOTA_SAIDA_PARCELA_MOTIVO_DESC_DEV", allocationSize=1)
public class NotaSaidaParcelaMotivoDescDev extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 345063071602969389L;
	private Long codigoNotaSaidaParcela;
	private Long codigoMotivoDescontoDevolucao;
	private MotivoDescontoDevolucao motivoDescontoDevolucao = new MotivoDescontoDevolucao();
	private Tipo tipo;
	private BigDecimal valor;

	public NotaSaidaParcelaMotivoDescDev() {
	}

	@Column(name = "codigo_nota_saida_parcela", nullable = false)
	public Long getCodigoNotaSaidaParcela() {
		return codigoNotaSaidaParcela;
	}

	public void setCodigoNotaSaidaParcela(Long codigoNotaSaidaParcela) {
		this.codigoNotaSaidaParcela = codigoNotaSaidaParcela;
	}

	@Column(name = "codigo_motivo_desconto_devolucao", nullable = false)
	public Long getCodigoMotivoDescontoDevolucao() {
		return codigoMotivoDescontoDevolucao;
	}

	public void setCodigoMotivoDescontoDevolucao(Long codigoMotivoDescontoDevolucao) {
		this.codigoMotivoDescontoDevolucao = codigoMotivoDescontoDevolucao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_motivo_desconto_devolucao", insertable = false, updatable = false)
	public MotivoDescontoDevolucao getMotivoDescontoDevolucao() {
		return motivoDescontoDevolucao;
	}

	public void setMotivoDescontoDevolucao(MotivoDescontoDevolucao motivoDescontoDevolucao) {
		this.motivoDescontoDevolucao = motivoDescontoDevolucao;
	}

	@Column(nullable = false)
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Column(name = "valor", nullable = false, precision = 2)
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "NotaSaidaParcelaMotivoDescDev{" +
			"codigoNotaSaidaParcela=" + codigoNotaSaidaParcela +
			", codigoMotivoDescontoDevolucao=" + codigoMotivoDescontoDevolucao +
			", valor=" + valor +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NotaSaidaParcelaMotivoDescDev)) return false;
		if (!super.equals(o)) return false;
		NotaSaidaParcelaMotivoDescDev that = (NotaSaidaParcelaMotivoDescDev) o;
		return Objects.equals(getCodigoNotaSaidaParcela(), that.getCodigoNotaSaidaParcela()) &&
			Objects.equals(getCodigoMotivoDescontoDevolucao(), that.getCodigoMotivoDescontoDevolucao()) &&
			Objects.equals(getValor(), that.getValor());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getCodigoNotaSaidaParcela(), getCodigoMotivoDescontoDevolucao(), getValor());
	}

	public enum Tipo {
		DESCONTO("Desconto"),
		DEVOLUCAO("Devolução");

		Tipo(String descricao) {
		}

		@Override
		public String toString() {
			return super.toString();
		}
	}
}
