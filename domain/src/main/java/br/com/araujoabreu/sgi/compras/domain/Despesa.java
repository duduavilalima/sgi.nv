package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Representação de uma despesa realizada. Pode ser detalhada a partir de um documento oficial, como <code>NotaFiscal</code> ou uma
 * <code>NotaPagamento</code>
 *
 * @author mlacerda
 */
@Entity
@Table(name = "DESPESA")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "SEQ_DESPESA", sequenceName = "SEQ_DESPESA", allocationSize = 1)
public class Despesa extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 1729351947764270473L;
	private BigDecimal valor = new BigDecimal(0);
	private BigDecimal valorHomologado = new BigDecimal(0);
	private String descricao;
	private Date data;
	private int version;

	@Column(name = "VALOR", nullable = false, precision = 9, scale = 2)
	@NotNull
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Column(name = "VALOR_HOMOLOGADO", nullable = false, precision = 9, scale = 2)
	@NotNull
	public BigDecimal getValorHomologado() {
		return valorHomologado;
	}

	public void setValorHomologado(BigDecimal valorHomologado) {
		this.valorHomologado = valorHomologado;
	}

	@Column(name = "DESCRICAO")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Data que ocorreu a despesa.
	 *
	 * @return
	 */
	@Column(name = "DATA", nullable = false)
	@NotNull
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Transient
	public boolean isNotaFiscal() {
		return (this instanceof NotaFiscal);
	}

	@Transient
	public String getNumeroDocumento() {
		return isNotaFiscal() ? ((NotaFiscal) this).getNumero() : "";
	}

	/**
	 * Atributo utilizado para controle de concorrencia
	 *
	 * @return
	 */
	@Version
	public int getVersion() {
		return version;
	}

	/**
	 * Determina a versão a entidade FundoFixo
	 *
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
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
		if (!(Despesa.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Despesa other = (Despesa) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (descricao == null) {
			if (other.getDescricao() != null) {
				return false;
			}
		} else if (!descricao.equals(other.getDescricao())) {
			return false;
		}
		if (valor == null) {
			if (other.getValor() != null) {
				return false;
			}
		} else if (!valor.equals(other.getValor())) {
			return false;
		}
		if (valorHomologado == null) {
			if (other.getValorHomologado() != null) {
				return false;
			}
		} else if (!valorHomologado.equals(other.getValorHomologado())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Despesa [getCodigo()=" + getCodigo() + ", getValorBruto()=" + getValor() + ", getValorHomologado()=" + getValorHomologado() + ", getDescricao()=" + getDescricao() + "]";
	}

}
