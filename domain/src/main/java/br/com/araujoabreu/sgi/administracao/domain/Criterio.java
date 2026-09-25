package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;
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
import java.util.Objects;

/**
 * Representa um critério configurado que será utilizado para avaliação do fornecedor.
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "CRITERIO")
@SequenceGenerator(name="SEQ_CRITERIO", sequenceName="SEQ_CRITERIO", allocationSize=1)
public class Criterio extends BaseEntity {

	private static final long serialVersionUID = 696379814579069151L;
	private String nome;
	private Double peso;
	private TipoCriterio tipo;
	private String descricao;
	private Integer ordem;
	private List<ItemAvaliacao> itens = new ArrayList<>();
	private List<CriterioPontuacao> listaCriterioPontuacao = new ArrayList<>();

	/**
	 * Nome do critério.
	 *
	 * @return
	 */
	@Column(name = "NOME", nullable = false)
	@NotNull
	public String getNome() {
		return nome;
	}

	/**
	 * Determina o nome do critério
	 *
	 * @param nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Peso atribuido ao critério. Utilizado para calcular a pontuação geral.
	 *
	 * @return
	 */
	@Column(name = "PESO", nullable = false)
	@NotNull
	public Double getPeso() {
		return peso;
	}

	/**
	 * Determina o peso do critério.
	 *
	 * @param peso
	 */
	public void setPeso(Double peso) {
		this.peso = peso;
	}

	@Column(name = "TIPO", nullable = false)
	@NotNull
	public TipoCriterio getTipo() {
		return tipo;
	}

	public void setTipo(TipoCriterio tipo) {
		this.tipo = tipo;
	}

	@Column(name = "DESCRICAO", nullable = false)
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "ORDEM", nullable = false)
	@NotNull
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "criterio")
	public List<ItemAvaliacao> getItens() {
		return itens;
	}

	public void setItens(List<ItemAvaliacao> itens) {
		this.itens = itens;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "criterio")
	public List<CriterioPontuacao> getListaCriterioPontuacao() {
		return listaCriterioPontuacao;
	}

	public void setListaCriterioPontuacao(List<CriterioPontuacao> listaCriterioPontuacao) {
		this.listaCriterioPontuacao = listaCriterioPontuacao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Criterio)) return false;
		if (!super.equals(o)) return false;
		Criterio criterio = (Criterio) o;
		return Objects.equals(getNome(), criterio.getNome()) &&
			Objects.equals(getPeso(), criterio.getPeso()) &&
			getTipo() == criterio.getTipo() &&
			Objects.equals(getDescricao(), criterio.getDescricao()) &&
			Objects.equals(getOrdem(), criterio.getOrdem());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome(), getPeso(), getTipo(), getDescricao(), getOrdem());
	}

	public enum TipoCriterio {
		NOTA_FISCAL("Nota Fiscal");

		private final String nome;

		TipoCriterio(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

	}

}
