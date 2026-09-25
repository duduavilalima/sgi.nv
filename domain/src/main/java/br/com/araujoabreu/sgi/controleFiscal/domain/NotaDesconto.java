package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "NOTA_OUTRO_DESCONTO")
@SequenceGenerator(name = "SEQ_NOTA_OUTRO_DESCONTO", sequenceName = "SEQ_NOTA_OUTRO_DESCONTO", allocationSize = 1)
public class NotaDesconto extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2331443990629451415L;
	private Integer ordem;
	private Nota nota;
	private OutroDesconto outroDesconto;
	private ContaContabil contaContabil;
	private BigDecimal valor;

	private String numeroContaContabil;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA")
	public Nota getNota() {
		return nota;
	}

	public void setNota(Nota nota) {
		this.nota = nota;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_OUTRO_DESCONTO")
	public OutroDesconto getOutroDesconto() {
		return outroDesconto;
	}

	public void setOutroDesconto(OutroDesconto outroDesconto) {
		this.outroDesconto = outroDesconto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL")
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@Column(name = "VALOR")
	@NotNull
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Transient
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Transient
	public String getNumeroContaContabil() {
		return numeroContaContabil;
	}

	public void setNumeroContaContabil(String numeroContaContabil) {
		this.numeroContaContabil = numeroContaContabil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((ordem == null) ? 0 : ordem.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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

		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		if (!(obj instanceof NotaDesconto)) {
			return false;
		}

		NotaDesconto other = (NotaDesconto) obj;
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
		return "NotaDesconto [ordem=" + ordem + ", codigo=" + getCodigo()
			+ ", valor=" + valor + "]";
	}

}
