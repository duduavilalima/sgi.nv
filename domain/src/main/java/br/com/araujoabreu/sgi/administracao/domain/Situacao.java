package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;


@Entity
public class Situacao extends AuditEntity implements DomainObject, Serializable {

	private static final long serialVersionUID = 3217544379198519543L;
	private String chave;
	private String descricao;
	private String nome;
	private Integer ordem;
	private Boolean permissaoCancelar;
	private Boolean permissaoEditar;
	private Boolean permissaoReativar;
	private Boolean permissaoSuspender;
	private String subsituacao;
	private String tabela;

	public Situacao() {
	}


	public String getChave() {
		return this.chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	public Integer getOrdem() {
		return this.ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}


	@Column(name = "permissao_cancelar")
	public Boolean getPermissaoCancelar() {
		return this.permissaoCancelar;
	}

	public void setPermissaoCancelar(Boolean permissaoCancelar) {
		this.permissaoCancelar = permissaoCancelar;
	}


	@Column(name = "permissao_editar")
	public Boolean getPermissaoEditar() {
		return this.permissaoEditar;
	}

	public void setPermissaoEditar(Boolean permissaoEditar) {
		this.permissaoEditar = permissaoEditar;
	}


	@Column(name = "permissao_reativar")
	public Boolean getPermissaoReativar() {
		return this.permissaoReativar;
	}

	public void setPermissaoReativar(Boolean permissaoReativar) {
		this.permissaoReativar = permissaoReativar;
	}


	@Column(name = "permissao_suspender")
	public Boolean getPermissaoSuspender() {
		return this.permissaoSuspender;
	}

	public void setPermissaoSuspender(Boolean permissaoSuspender) {
		this.permissaoSuspender = permissaoSuspender;
	}


	public String getSubsituacao() {
		return this.subsituacao;
	}

	public void setSubsituacao(String subsituacao) {
		this.subsituacao = subsituacao;
	}


	public String getTabela() {
		return this.tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Situacao)) return false;
		if (!super.equals(o)) return false;
		Situacao situacao = (Situacao) o;
		return Objects.equals(getChave(), situacao.getChave()) &&
			Objects.equals(getDescricao(), situacao.getDescricao()) &&
			Objects.equals(getNome(), situacao.getNome()) &&
			Objects.equals(getOrdem(), situacao.getOrdem()) &&
			Objects.equals(getPermissaoCancelar(), situacao.getPermissaoCancelar()) &&
			Objects.equals(getPermissaoEditar(), situacao.getPermissaoEditar()) &&
			Objects.equals(getPermissaoReativar(), situacao.getPermissaoReativar()) &&
			Objects.equals(getPermissaoSuspender(), situacao.getPermissaoSuspender()) &&
			Objects.equals(getSubsituacao(), situacao.getSubsituacao()) &&
			Objects.equals(getTabela(), situacao.getTabela());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getChave(), getDescricao(), getNome(), getOrdem(), getPermissaoCancelar(), getPermissaoEditar(), getPermissaoReativar(), getPermissaoSuspender(), getSubsituacao(), getTabela());
	}
}
