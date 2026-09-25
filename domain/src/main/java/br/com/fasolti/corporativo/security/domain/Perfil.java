package br.com.fasolti.corporativo.security.domain;

import br.com.araujoabreu.sgi.administracao.domain.MensagemPerfil;
import br.com.araujoabreu.sgi.administracao.domain.MotivoAplicacao;
import br.com.fasolti.corporativo.pessoa.domain.RelacionamentoContato;
import org.hibernate.Hibernate;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Table(name = "PERFIL")
public class Perfil extends Receptor {

	private static final long serialVersionUID = 4445611732880262798L;
	private String nome;
	private String descricao;
	private TipoAcesso tipoAcesso;
	/**
	 * Indica se o perfil obriga que seja selecionada a gerência (não será
	 * permitida a seleção de contratos).
	 */
	private Boolean obrigatorioGerencia;
	/**
	 * Indica se o perfil obriga que sejam informados os dados bancários.
	 */
	private Boolean obrigatorioBanco;

	private List<MotivoAplicacao> listaMotivoAplicacao;
	private List<RelacionamentoContato> listaRelacionamentoContatos;
	private List<MensagemPerfil> listaMensagemPerfil;

	public Perfil() {
		setTipo(TipoReceptor.PERFIL);
	}

	@Column(name = "NOME", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getNome() {
		return this.nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	@Column(name = "DESCRICAO")
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "TIPO_ACESSO", nullable = false)
	@NotNull
	public TipoAcesso getTipoAcesso() {
		return tipoAcesso;
	}

	public void setTipoAcesso(final TipoAcesso tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}

	@Column(name = "OBRIGA_GERENCIA")
	public Boolean getObrigatorioGerencia() {
		return obrigatorioGerencia;
	}

	public void setObrigatorioGerencia(final Boolean obrigatorioGerencia) {
		this.obrigatorioGerencia = obrigatorioGerencia;
	}

	@Column(name = "OBRIGA_BANCO")
	public Boolean getObrigatorioBanco() {
		return obrigatorioBanco;
	}

	public void setObrigatorioBanco(final Boolean obrigatorioBanco) {
		this.obrigatorioBanco = obrigatorioBanco;
	}

	@OneToMany(
		cascade = CascadeType.ALL,
		fetch = FetchType.LAZY,
		mappedBy = "perfil")
	@OrderBy(value = "codigo")
	public List<MotivoAplicacao> getListaMotivoAplicacao() {
		return listaMotivoAplicacao;
	}

	public void setListaMotivoAplicacao(
		final List<MotivoAplicacao> listaMotivoAplicacao) {
		this.listaMotivoAplicacao = listaMotivoAplicacao;
	}

	@OneToMany(
		cascade = CascadeType.ALL,
		fetch = FetchType.LAZY,
		mappedBy = "perfil")
	public List<RelacionamentoContato> getListaRelacionamentoContatos() {
		return listaRelacionamentoContatos;
	}

	public void setListaRelacionamentoContatos(
		final List<RelacionamentoContato> listaRelacionamentoContatos) {
		this.listaRelacionamentoContatos = listaRelacionamentoContatos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "perfil")
	public List<MensagemPerfil> getListaMensagemPerfil() {
		return listaMensagemPerfil;
	}

	public void setListaMensagemPerfil(List<MensagemPerfil> listaMensagemPerfil) {
		this.listaMensagemPerfil = listaMensagemPerfil;
	}

	@Transient
	@Override
	public String getChave() {
		return nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((obrigatorioBanco == null) ? 0 : obrigatorioBanco.hashCode());
		result = prime * result + ((obrigatorioGerencia == null) ? 0 : obrigatorioGerencia.hashCode());
		result = prime * result + ((tipoAcesso == null) ? 0 : tipoAcesso.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(Perfil.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Perfil other = (Perfil) obj;
		if (getDescricao() == null) {
			if (other.getDescricao() != null) {
				return false;
			}
		} else if (!getDescricao().equals(other.getDescricao())) {
			return false;
		}
		if (getNome() == null) {
			if (other.getNome() != null) {
				return false;
			}
		} else if (!getNome().equals(other.getNome())) {
			return false;
		}
		if (getObrigatorioBanco() == null) {
			if (other.getObrigatorioBanco() != null) {
				return false;
			}
		} else if (!getObrigatorioBanco().equals(other.getObrigatorioBanco())) {
			return false;
		}
		if (getObrigatorioGerencia() == null) {
			if (other.getObrigatorioGerencia() != null) {
				return false;
			}
		} else if (!getObrigatorioGerencia().equals(other.getObrigatorioGerencia())) {
			return false;
		}
		if (getTipoAcesso() != other.getTipoAcesso()) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Perfil [nome=" + nome + ", descricao=" + descricao
			+ ", tipoAcesso=" + tipoAcesso + "]";
	}

	public static enum Chave {
		colaborador
	}

}
