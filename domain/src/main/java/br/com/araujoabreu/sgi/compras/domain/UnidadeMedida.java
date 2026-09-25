package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "UNIDADE_MEDIDA")
@SequenceGenerator(name="SEQ_UNIDADE_MEDIDA", sequenceName="SEQ_UNIDADE_MEDIDA", allocationSize=1)
public class UnidadeMedida extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 1125323705934077207L;
	private String nome;
	private String descricao;
	private String mascaraEdicao;
	private Integer casaDecimal;

	private List<ItemCompra> itens;

	public UnidadeMedida() {
	}

	public UnidadeMedida(Long codigo, String nome) {
		setCodigo(codigo);
		this.nome = nome;
	}

	@Column(name = "NOME", nullable = false, length = 20)
	@NotNull
	@Length(max = 20)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "DESCRICAO", length = 50)
	@Length(max = 50)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "MASCARA_EDICAO", nullable = false, length = 7)
	@NotNull
	@Length(max = 7)
	public String getMascaraEdicao() {
		return mascaraEdicao;
	}

	public void setMascaraEdicao(String mascaraEdicao) {
		this.mascaraEdicao = mascaraEdicao;
	}

	@Column(name = "CASA_DECIMAL", nullable = false)
	@NotNull
	public Integer getNumCasasDecimais() {
		return casaDecimal;
	}

	public void setNumCasasDecimais(Integer casaDecimal) {
		this.casaDecimal = casaDecimal;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "unidadeMedida")
	@OrderBy(value = "codigo")
	public List<ItemCompra> getItens() {
		return itens;
	}

	public void setItens(List<ItemCompra> itens) {
		this.itens = itens;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UnidadeMedida)) return false;
		if (!super.equals(o)) return false;
		UnidadeMedida that = (UnidadeMedida) o;
		return Objects.equals(getNome(), that.getNome()) &&
			Objects.equals(getDescricao(), that.getDescricao()) &&
			Objects.equals(getMascaraEdicao(), that.getMascaraEdicao()) &&
			Objects.equals(casaDecimal, that.casaDecimal);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome(), getDescricao(), getMascaraEdicao(), casaDecimal);
	}
}
