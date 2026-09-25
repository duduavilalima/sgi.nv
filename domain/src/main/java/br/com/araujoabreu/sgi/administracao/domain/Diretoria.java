package br.com.araujoabreu.sgi.administracao.domain;

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

@Entity
@Table(name = "DIRETORIA")
@SequenceGenerator(name = "SEQ_DIRETORIA", sequenceName = "SEQ_DIRETORIA", allocationSize = 1)
public class Diretoria extends BaseEntity {

	private static final long serialVersionUID = 4879025750263127731L;
	private String nome;
	private String numero;
	private Unidade unidade;
	private String descricao;
	private Boolean permiteContrato;
	private Colaborador diretor;

	public Diretoria() {
	}

	public Diretoria(Long codigo) {
		setCodigo(codigo);
	}

	@Column(name = "NOME", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "NUMERO", nullable = false)
	@NotNull
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "DESCRICAO")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "PERMITE_CONTRATO")
	public Boolean getPermiteContrato() {
		return permiteContrato;
	}

	public void setPermiteContrato(Boolean permiteContrato) {
		this.permiteContrato = permiteContrato;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_UNIDADE", nullable = false)
	@NotNull
	public Unidade getUnidade() {
		return this.unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_DIRETOR")
	public Colaborador getDiretor() {
		return diretor;
	}

	public void setDiretor(Colaborador diretor) {
		this.diretor = diretor;
	}

	public enum TipoDiretoria {
		MANUTENCAO("Manutenção"),
		INSTALACAO("Instalação");

		private final String nome;

		private TipoDiretoria(String nome) {
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
