package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TIPO_COBRANCA")
public class TipoCobranca extends BaseEntity {

	private static final long serialVersionUID = -841125974357163291L;
	private String chave;
	private String nome;
	private Integer ordem;
	private String descricao;
	private String sigla;

	@Column(name = "NOME", nullable = false, length = 30)
	@NotNull
	@Length(max = 30)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "CHAVE", nullable = false)
	@NotNull
	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	@Column(name = "ORDEM", nullable = false)
	@NotNull
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Column(name = "DESCRICAO")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	@Column(name = "SIGLA", nullable = false, length = 5)
	@NotNull
	@Length(max = 5)
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
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
		if (!(TipoCobranca.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		TipoCobranca other = (TipoCobranca) obj;
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
		return "TipoCobranca [codigo=" + getCodigo() + ", chave=" + chave
			+ ", nome=" + nome + ", ordem=" + ordem + "]";
	}

}
