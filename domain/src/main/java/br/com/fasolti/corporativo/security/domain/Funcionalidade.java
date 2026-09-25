package br.com.fasolti.corporativo.security.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representa um item de menu. Seria a folha da árvore, a funcionalidade associada a um dado módulo
 *
 * @author Rodrigo Barreiros
 * @since 0.2; 14/02/2009
 */
@Entity
@Table(name = "FUNCIONALIDADE")
@SequenceGenerator(name = "SEQ_FUNCIONALIDADE", sequenceName = "SEQ_FUNCIONALIDADE", allocationSize = 1)
public class Funcionalidade extends BaseEntity implements MenuableObject, DomainObject {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String url;
	private Integer ordem;
	private Modulo modulo;

	@Override
	@Column(name = "NOME", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	@Transient
	public String getLink() {
		return url;
	}

	@Column(name = "URL", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_MODULO", nullable = false)
	@NotNull
	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	@Override
	@Column(name = "ORDEM", nullable = false)
	@NotNull
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Override
	@Transient
	public MenuableObject getParent() {
		return modulo;
	}

	@Override
	@Transient
	public List<MenuableObject> getChildren() {
		return Collections.emptyList();
	}

	@Override
	@Transient
	public boolean isAvailable() {
		return true;
	}

	@Override
	@Transient
	public String getSecurityContext() {
		return url;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Funcionalidade)) return false;
		Funcionalidade that = (Funcionalidade) o;
		return Objects.equals(getCodigo(), that.getCodigo()) &&
			Objects.equals(getNome(), that.getNome()) &&
			Objects.equals(url, that.url) &&
			Objects.equals(getOrdem(), that.getOrdem()) &&
			Objects.equals(getModulo(), that.getModulo());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigo(), getNome(), url, getOrdem(), getModulo());
	}

	@Override
	public String toString() {
		return "Funcionalidade{" +
			"codigo=" + getCodigo() +
			", nome='" + nome + '\'' +
			", url='" + url + '\'' +
			", ordem=" + ordem +
			", modulo=" + modulo +
			'}';
	}
}
