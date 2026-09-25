package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name = "NATUREZA_PAGAMENTO")
@SequenceGenerator(name="SEQ_NATUREZA_PAGAMENTO", sequenceName="SEQ_NATUREZA_PAGAMENTO", allocationSize=1)
public class NaturezaPagamento extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -4280804944043905781L;
	private String nome;
	private String contaContabil;
	private TipoDespesa tipoDespesa;

	public NaturezaPagamento() {
	}

	public NaturezaPagamento(Long codigo) {
		setCodigo(codigo);
	}

	public NaturezaPagamento(Long codigo, String nome) {
		setCodigo(codigo);
		this.nome = nome;
	}

	@Column(name = "NOME", nullable = false, length = 30)
	@Length(max = 30)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "CONTA_CONTABIL", nullable = false)
	public String getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(String contaContabil) {
		this.contaContabil = contaContabil;
	}

	@Column(name = "TIPO_DESPESA", nullable = false)
	public TipoDespesa getTipoDespesa() {
		return tipoDespesa;
	}

	public void setTipoDespesa(TipoDespesa tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}

	@Transient
	public TipoDespesa[] getTiposDespesas() {
		return TipoDespesa.values();
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
		if (!(NaturezaPagamento.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		NaturezaPagamento other = (NaturezaPagamento) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	public enum TipoDespesa {Interna, Externa}
}
