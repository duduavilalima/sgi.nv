package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.araujoabreu.sgi.inrf.domain.ProcessoAnexo;
import br.com.fasolti.commons.domain.DomainObject;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name = "NATUREZA_DESPESA_CONTA_CONTABIL")
public class NaturezaDespesaContaContabil implements DomainObject {

	private static final long serialVersionUID = -1288929925013396692L;
	@EmbeddedId
	private PKNaturezaConta pkNaturezaConta;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NATUREZA_DESPESA", insertable = false, updatable = false)
	@NotNull
	private NaturezaDespesa naturezaDespesa;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL", insertable = false, updatable = false)
	@NotNull
	private ContaContabil contaContabil;

	public NaturezaDespesaContaContabil() {
		this.naturezaDespesa = new NaturezaDespesa();
		this.contaContabil = new ContaContabil();
	}

	public PKNaturezaConta getPkNaturezaConta() {
		return pkNaturezaConta;
	}

	public void setPkNaturezaConta(PKNaturezaConta pkNaturezaConta) {
		this.pkNaturezaConta = pkNaturezaConta;
	}

	public NaturezaDespesa getNaturezaDespesa() {
		return naturezaDespesa;
	}

	public void setNaturezaDespesa(NaturezaDespesa naturezaDespesa) {
		this.naturezaDespesa = naturezaDespesa;
	}

	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@Transient
	public void atualizarPK() {
		if (this.pkNaturezaConta == null)
			this.pkNaturezaConta = new PKNaturezaConta(this.naturezaDespesa.getCodigo(), this.contaContabil.getCodigo());
		else {
			this.pkNaturezaConta.setCodigoNaturezaDespesa(this.naturezaDespesa.getCodigo());
			this.pkNaturezaConta.setCodigoContaContabil(this.contaContabil.getCodigo());
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
			+ ((contaContabil == null) ? 0 : contaContabil.hashCode());
		result = prime * result
			+ ((naturezaDespesa == null) ? 0 : naturezaDespesa.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}

		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		if (!(obj instanceof ProcessoAnexo)) {
			return false;
		}

		NaturezaDespesaContaContabil other = (NaturezaDespesaContaContabil) obj;

		if (this.pkNaturezaConta == null)
			return false;
		else if (other.getPkNaturezaConta() == null)
			return false;
		else
			return this.pkNaturezaConta.equals(other.getPkNaturezaConta());

	}

	@Override
	public Long getId() {
		return null;
	}

	@Override
	public void setId(Long id) {

	}

	@Embeddable
	public static class PKNaturezaConta implements Serializable {

		private static final long serialVersionUID = -3994696634578459L;

		@Column(name = "CODIGO_NATUREZA_DESPESA", nullable = false)
		private Long codigoNaturezaDespesa;

		@Column(name = "CODIGO_CONTA_CONTABIL", nullable = false)
		private Long codigoContaContabil;

		public PKNaturezaConta() {
		}

		public PKNaturezaConta(Long codigoNaturezaDespesa, Long codigoContaContabil) {
			super();
			this.codigoNaturezaDespesa = codigoNaturezaDespesa;
			this.codigoContaContabil = codigoContaContabil;
		}

		public Long getCodigoNaturezaDespesa() {
			return codigoNaturezaDespesa;
		}

		public void setCodigoNaturezaDespesa(Long codigoNaturezaDespesa) {
			this.codigoNaturezaDespesa = codigoNaturezaDespesa;
		}

		public Long getCodigoContaContabil() {
			return codigoContaContabil;
		}

		public void setCodigoContaContabil(Long codigoContaContabil) {
			this.codigoContaContabil = codigoContaContabil;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
				* result
				+ ((codigoContaContabil == null) ? 0 : codigoContaContabil
				.hashCode());
			result = prime
				* result
				+ ((codigoNaturezaDespesa == null) ? 0
				: codigoNaturezaDespesa.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;

			if (obj == null)
				return false;

			if (getClass() != obj.getClass())
				return false;

			PKNaturezaConta other = (PKNaturezaConta) obj;
			if (codigoContaContabil == null) {
				if (other.codigoContaContabil != null)
					return false;
			} else if (!codigoContaContabil.equals(other.codigoContaContabil))
				return false;

			if (codigoNaturezaDespesa == null) {
				if (other.codigoNaturezaDespesa != null)
					return false;
			} else if (!codigoNaturezaDespesa.equals(other.codigoNaturezaDespesa))
				return false;

			return true;
		}

		@Override
		public String toString() {
			return "PKNaturezaConta [codigoNaturezaDespesa="
				+ codigoNaturezaDespesa + ", codigoContaContabil="
				+ codigoContaContabil + "]";
		}
	}


}
