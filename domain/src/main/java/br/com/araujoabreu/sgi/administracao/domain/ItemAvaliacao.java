package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Item que determina a pontuação de um determinado critério de avaliaçao.
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "ITEM_AVALIACAO")
@SequenceGenerator(name="SEQ_ITEM_AVALIACAO", sequenceName="SEQ_ITEM_AVALIACAO", allocationSize=1)
public class ItemAvaliacao extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2403915331987517042L;
	private Avaliacao avaliacao;
	private Criterio criterio;
	private Pontuacao pontuacao;
	private String justificativa;


	public ItemAvaliacao() {
		super();
	}

	public ItemAvaliacao(Long codigo) {
		setCodigo(codigo);
	}

	public ItemAvaliacao(Avaliacao avaliacao, Criterio criterio) {
		super();
		this.avaliacao = avaliacao;
		this.criterio = criterio;
	}

	public ItemAvaliacao(Long codigo, Avaliacao avaliacao, Criterio criterio, Pontuacao pontuacao, String justificativa) {
		super();
		setCodigo(codigo);
		this.avaliacao = avaliacao;
		this.criterio = criterio;
		this.pontuacao = pontuacao;
		this.justificativa = justificativa;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_AVALIACAO", nullable = false)
	@NotNull
	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CRITERIO", nullable = false)
	@NotNull
	@OrderBy(value = "ordem")
	public Criterio getCriterio() {
		return criterio;
	}

	public void setCriterio(Criterio criterio) {
		this.criterio = criterio;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PONTUACAO")
	public Pontuacao getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(Pontuacao pontuacao) {
		this.pontuacao = pontuacao;
	}

	@Column(name = "JUSTIFICATIVA")
	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
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
		if (!(ItemAvaliacao.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		ItemAvaliacao other = (ItemAvaliacao) obj;
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
		return "ItemAvaliacao [codigo=" + getCodigo() + "]";
	}

}
