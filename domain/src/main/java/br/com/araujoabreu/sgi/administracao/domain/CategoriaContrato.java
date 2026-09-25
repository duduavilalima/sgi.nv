package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "CATEGORIA_CONTRATO")
@SequenceGenerator(name = "SEQ_CATEGORIA_CONTRATO", sequenceName = "SEQ_CATEGORIA_CONTRATO", allocationSize = 1)
public class CategoriaContrato extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 8207799052816213904L;

	private String descricao;
	private TipoCategoriaContrato tipo;

	@Column(name = "DESCRICAO", nullable = false)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "TIPO", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public TipoCategoriaContrato getTipo() {
		return tipo;
	}

	public void setTipo(TipoCategoriaContrato tipo) {
		this.tipo = tipo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CategoriaContrato)) return false;
		if (!super.equals(o)) return false;
		CategoriaContrato that = (CategoriaContrato) o;
		return Objects.equals(getDescricao(), that.getDescricao()) &&
			getTipo() == that.getTipo();
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getDescricao(), getTipo());
	}

	@Override
	public String toString() {
		return "CategoriaContrato{" +
			"descricao='" + descricao + '\'' +
			", tipo=" + tipo +
			'}';
	}

	public enum TipoCategoriaContrato {
		CLIENTE("Cliente"),
		FORNECEDOR("Fornecedor"),
		CLIENTE_FORNECEDOR("Cliente/Fornecedor");

		private final String nome;

		TipoCategoriaContrato(String nome) {

			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return nome;
		}
	}
}
