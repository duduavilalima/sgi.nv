package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.compras.interf.Material;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CATEGORIA_ITEM_COMPRA")
@SequenceGenerator(name="SEQ_CATEGORIA_ITEM_COMPRA", sequenceName="SEQ_CATEGORIA_ITEM_COMPRA", allocationSize=1)
public class CategoriaItemCompra extends AuditEntity implements Material {

	private static final long serialVersionUID = 5745492159989720087L;
	private String sigla;
	private String nome;
	private CategoriaItemCompra pai;

	private List<Material> children;
	private Boolean loaded = Boolean.FALSE;

	@Column(name = "SIGLA")
	@Length(max = 5)
	@NotNull
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Column(name = "NOME")
	@Length(max = 100)
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PAI")
	public CategoriaItemCompra getPai() {
		return pai;
	}

	public void setPai(CategoriaItemCompra pai) {
		this.pai = pai;
	}

	@Transient
	public TipoMaterial getTipo() {
		return TipoMaterial.AGRUPADOR;
	}

	@Override
	public String toString() {
		return this.sigla;
	}

	@Transient
	public List<Material> getChildren() {
		if (children == null) {
			List<Material> ret = new ArrayList<>();
			ret.add(new CategoriaItemCompra());
			return ret;
		}
		return this.children;
	}

	public void setChildren(List<Material> list) {
		this.loaded = true;
		this.children = list;
	}

	@Transient
	public Boolean isLoaded() {
		return this.loaded;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CategoriaItemCompra)) return false;
		if (!super.equals(o)) return false;
		CategoriaItemCompra that = (CategoriaItemCompra) o;
		return Objects.equals(getSigla(), that.getSigla()) &&
			Objects.equals(getNome(), that.getNome()) &&
			Objects.equals(loaded, that.loaded);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getSigla(), getNome(), loaded);
	}
}
