package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name = "NATUREZA_FORNECIMENTO")
@SequenceGenerator(name="SEQ_NATUREZA_FORNECIMENTO", sequenceName="SEQ_NATUREZA_FORNECIMENTO", allocationSize=1)
public class NaturezaFornecimento extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -3936094735978369792L;
	private String nome;
	private TipoFornecimento tipo;

	public NaturezaFornecimento() {
	}

	public NaturezaFornecimento(Long codigo, String nome) {
		setCodigo(codigo);
		this.nome = nome;
	}

	@Column(name = "NOME")
	@Length(max = 100)
	@NotNull
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "TIPO")
	@NotNull
	public TipoFornecimento getTipo() {
		return tipo;
	}

	public void setTipo(TipoFornecimento tipo) {
		this.tipo = tipo;
	}

	@Transient
	public TipoFornecimento[] getTiposFornecimentos() {
		return TipoFornecimento.values();
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
		if (!(NaturezaFornecimento.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		NaturezaFornecimento other = (NaturezaFornecimento) obj;
		if (getCodigo() == null) {
			return other.getCodigo() == null;
		} else return getCodigo().equals(other.getCodigo());
	}

	@Override
	public String toString() {
		return "NaturezaFornecimento [codigo=" + getCodigo() + ", nome=" + nome + ", tipo=" + tipo + "]";
	}

	public enum TipoFornecimento {

		MATERIAIS(92, "Só material"),
		SERVICOS(106, "Só serviço"),
		CONJUGADO(92, "Serviço/Material");

		int codigoHistoricoPadrao;
		String descricao;

		TipoFornecimento(int codigoHistoricoPadrao, String descricao) {
			this.codigoHistoricoPadrao = codigoHistoricoPadrao;
			this.descricao = descricao;
		}

		public int getCodigoHistoricoPadrao() {
			return codigoHistoricoPadrao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return getDescricao();
		}
	}

}
