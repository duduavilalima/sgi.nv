package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Pontuacao configurada para a avaliação do fornecedor.
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "PONTUACAO")
@SequenceGenerator(name="SEQ_PONTUACAO", sequenceName="SEQ_PONTUACAO", allocationSize=1)
public class Pontuacao extends BaseEntity {

	private static final long serialVersionUID = -6703530831117289245L;
	private String nome;

	private List<ItemAvaliacao> itens = new ArrayList<>();
	private List<CriterioPontuacao> listaCriterioPontuacao = new ArrayList<>();

	public Pontuacao() {

	}

	public Pontuacao(Long codigo, String nome) {
		setCodigo(codigo);
		this.nome = nome;
	}

	/**
	 * Nome do nota.
	 *
	 * @return
	 */
	@Column(name = "NOME", nullable = false)
	@NotNull
	public String getNome() {
		return nome;
	}

	/**
	 * Determina o nome da nota.
	 *
	 * @param nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pontuacao")
	public List<ItemAvaliacao> getItens() {
		return itens;
	}

	public void setItens(List<ItemAvaliacao> itens) {
		this.itens = itens;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pontuacao")
	public List<CriterioPontuacao> getListaCriterioPontuacao() {
		return listaCriterioPontuacao;
	}

	public void setListaCriterioPontuacao(List<CriterioPontuacao> listaCriterioPontuacao) {
		this.listaCriterioPontuacao = listaCriterioPontuacao;
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
		if (!(Pontuacao.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Pontuacao other = (Pontuacao) obj;
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
		return "Pontuacao [codigo=" + getCodigo() + ", nome=" + nome + "]";
	}

}
