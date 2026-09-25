package br.com.araujoabreu.sgi.contabilidade.domain;

import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "PESSOA_CONTA_CONTABIL")
@SequenceGenerator(name = "SEQ_PESSOA_CONTA_CONTABIL", sequenceName = "SEQ_PESSOA_CONTA_CONTABIL", allocationSize = 1)
public class PessoaContaContabil extends BaseEntity {

	private static final long serialVersionUID = -2184831984065621985L;
	private Pessoa pessoa;
	private ContaContabil contaContabil;
	private TipoRelacionamento tipoEspecial;
	private Unidade unidade;
	private String numeroContaContabil;
	private LoteInterface loteInterface;

	public PessoaContaContabil() {

	}

	public PessoaContaContabil(TipoRelacionamento tipo, ContaContabil contaContabil) {
		this.tipoEspecial = tipo;
		this.contaContabil = contaContabil;
		this.unidade = contaContabil.getEmpresa();
		this.numeroContaContabil = contaContabil.getNumero();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PESSOA", nullable = false)
	@NotNull
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL", nullable = false)
	@NotNull
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@Column(name = "TIPO", nullable = false)
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	public TipoRelacionamento getTipoEspecial() {
		return tipoEspecial;
	}

	public void setTipoEspecial(TipoRelacionamento tipoEspecial) {
		this.tipoEspecial = tipoEspecial;
	}

	@Transient
	public String getNumeroContaContabil() {
		if (this.numeroContaContabil == null && this.contaContabil != null && this.contaContabil.getNumero() != null) {
			this.numeroContaContabil = this.contaContabil.getNumero() + (this.contaContabil.getDescricao() == null ? "" : (" - " + this.contaContabil.getDescricao()));
		}
		return numeroContaContabil;
	}

	public void setNumeroContaContabil(String numeroContaContabil) {
		this.numeroContaContabil = numeroContaContabil;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_lote_interface")
	public LoteInterface getLoteInterface() {
		return loteInterface;
	}

	public void setLoteInterface(LoteInterface loteInterface) {
		this.loteInterface = loteInterface;
	}

	@Transient
	public Unidade getUnidade() {
		if (contaContabil != null) {
			unidade = contaContabil.getEmpresa();
		}
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public enum TipoRelacionamento {

		FORNECEDOR("Fornecedor", "Fornecedor"),
		FUNDO_FIXO("Colaborador - Fundo fixo", "Fundo fixo"),
		ADIANTAMENTO("Colaborador - Adiantamento", "Adiantamento"),
		REEMBOLSO("Colaborador - Reembolso", "Reembolso"),
		EMPRESTIMO("Colaborador - Empréstimo", "Empréstimo"),
		CLIENTE_REEMBOLSO("Cliente - Reembolso", "Cliente Reembolso"),
		FATURAMENTO_CONTRATUAL("Faturamento - Contratual", "Faturamento Contratual"),
		FATURAMENTO_EXTRA_CONTRATUAL("Faturamento - Extra contratual", "Faturamento - Extra contratual"),
		FATURAMENTO_RECEBER("Faturamento - A receber", "Faturamento - A receber"),
		CLIENTE("Cliente", "Cliente"),
		CLIENTE_CSLL("Cliente CSLL", "Cliente CSLL");


		private final String nome;
		private final String codnome;

		private TipoRelacionamento(String nome, String codnome) {
			this.nome = nome;
			this.codnome = codnome;
		}

		public String getNome() {
			return nome;
		}

		public String getCodnome() {
			return codnome;
		}

		@Override
		public String toString() {
			return codnome;
		}
	}

}
