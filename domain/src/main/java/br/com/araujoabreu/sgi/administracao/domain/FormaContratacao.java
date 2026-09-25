package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FORMA_CONTRATACAO")
@SequenceGenerator(name = "SEQ_FORMA_CONTRATACAO", sequenceName = "SEQ_FORMA_CONTRATACAO", allocationSize = 1)
public class FormaContratacao extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -3653758476764197076L;
	private String nome;
	private String descricao;
	private TipoContratacao tipo;

	@Column(name = "NOME", nullable = false)
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "DESCRICAO", nullable = false)
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "TIPO", nullable = false)
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	public TipoContratacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoContratacao tipo) {
		this.tipo = tipo;
	}

	public enum TipoContratacao {
		CLIENTE("Cliente"),
		FORNECEDOR("Fornecedor"),
		CLIENTE_FORNECEDOR("Cliente/Fornecedor");

		private final String nome;

		TipoContratacao(String nome) {

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
