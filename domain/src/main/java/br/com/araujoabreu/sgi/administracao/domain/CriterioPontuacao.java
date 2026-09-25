package br.com.araujoabreu.sgi.administracao.domain;

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
import java.util.Objects;

@Entity
@Table(name = "CRITERIO_PONTUACAO")
@SequenceGenerator(name = "SEQ_CRITERIO_PONTUACAO", sequenceName = "SEQ_CRITERIO_PONTUACAO", allocationSize = 1)
public class CriterioPontuacao extends BaseEntity {

	private static final long serialVersionUID = 6915358008639323840L;
	private Criterio criterio;
	private Pontuacao pontuacao;
	private Boolean conformidade;
	private Double peso;
	private Integer ordem;
	private String descricao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CRITERIO")
	@NotNull
	public Criterio getCriterio() {
		return criterio;
	}

	public void setCriterio(Criterio criterio) {
		this.criterio = criterio;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PONTUACAO")
	@NotNull
	public Pontuacao getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(Pontuacao pontuacao) {
		this.pontuacao = pontuacao;
	}

	@Column(name = "CONFORMIDADE", nullable = false)
	@NotNull
	public Boolean getConformidade() {
		return conformidade;
	}

	public void setConformidade(Boolean conformidade) {
		this.conformidade = conformidade;
	}

	@Column(name = "PESO", nullable = false)
	@NotNull
	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof HibernateProxy)
			o = ((HibernateProxy) o).getHibernateLazyInitializer().getImplementation();
		if (!(o instanceof CriterioPontuacao)) return false;
		CriterioPontuacao that = (CriterioPontuacao) o;
		return Objects.equals(getCodigo(), that.getCodigo()) &&
			Objects.equals(getCriterio(), that.getCriterio()) &&
			Objects.equals(getConformidade(), that.getConformidade()) &&
			Objects.equals(getPeso(), that.getPeso()) &&
			Objects.equals(getOrdem(), that.getOrdem()) &&
			Objects.equals(getDescricao(), that.getDescricao());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigo(), getCriterio(), getConformidade(), getPeso(), getOrdem(), getDescricao());
	}

}
