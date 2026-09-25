package br.com.fasolti.corporativo.security.domain;

import br.com.araujoabreu.sgi.administracao.domain.MensagemUsuario;
import br.com.fasolti.corporativo.pessoa.domain.FornecedorUsuario;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import org.hibernate.Hibernate;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USUARIO")
public class Usuario extends Receptor implements Serializable, UserDetails {

	private static final long serialVersionUID = -2846030845244755485L;

	private String login;
	private String senha;
	private List<UsuarioPerfil> usuarioPerfils = new ArrayList<>(0);
	private TipoAcesso tipoAcesso;
	private List<FornecedorUsuario> fornecedores = new ArrayList<>();
	private Date dataUltimoAcesso;

	private List<MensagemUsuario> listaMensagens;

	private PessoaFisica pessoaFisica;

	public Usuario() {
		setTipo(TipoReceptor.USUARIO);
	}

	@Column(name = "LOGIN", nullable = false, length = 100, unique = true)
	@NotNull
	@Length(max = 100)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Column(name = "SENHA", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "usuario")
	public List<UsuarioPerfil> getUsuarioPerfils() {
		return usuarioPerfils;
	}

	public void setUsuarioPerfils(List<UsuarioPerfil> usuarioPerfils) {
		this.usuarioPerfils = usuarioPerfils;
	}

	@Transient
	public List<Perfil> getPerfilOrdenados() {
		List<Perfil> list = new ArrayList<>();
		if (usuarioPerfils != null && !usuarioPerfils.isEmpty()) {
			Comparator<UsuarioPerfil> comparatorPerfil = (arg0, arg1) -> arg0.getPerfil().getDescricao().compareToIgnoreCase(arg1.getPerfil().getDescricao());
			usuarioPerfils.sort(comparatorPerfil);

			for (UsuarioPerfil peUsuarioPerfil : usuarioPerfils) {
				list.add(peUsuarioPerfil.getPerfil());
			}

		}
		return list;
	}

	@Transient
	public List<Perfil> getPerfis() {
		List<Perfil> listaPerfis = new ArrayList<>();
		for (UsuarioPerfil usuarioPerfil : this.getUsuarioPerfils())
			listaPerfis.add(usuarioPerfil.getPerfil());

		return listaPerfis;
	}

	public void setPerfis(List<Perfil> perfis) {
		if (perfis != null && !perfis.isEmpty()) {
			Comparator<Perfil> comparatorPerfil = (arg0, arg1) -> arg0.getDescricao().compareToIgnoreCase(arg1.getDescricao());
			perfis.sort(comparatorPerfil);

			usuarioPerfils.clear();

			for (Perfil perfil : perfis) {
				usuarioPerfils.add(new UsuarioPerfil(this, perfil));
			}

		} else {
			usuarioPerfils.clear();
		}
	}

	@Column(name = "TIPO_ACESSO", nullable = false)
	@NotNull
	public TipoAcesso getTipoAcesso() {
		return tipoAcesso;
	}

	public void setTipoAcesso(TipoAcesso tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public List<FornecedorUsuario> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<FornecedorUsuario> fornecedores) {
		this.fornecedores = fornecedores;
	}

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "usuario")
	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	@Transient
	@Override
	public String getChave() {
		return login;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ULTIMO_ACESSO", length = 7)
	public Date getDataUltimoAcesso() {
		return dataUltimoAcesso;
	}

	public void setDataUltimoAcesso(Date dataUltimoAcesso) {
		this.dataUltimoAcesso = dataUltimoAcesso;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public List<MensagemUsuario> getListaMensagens() {
		return listaMensagens;
	}

	public void setListaMensagens(List<MensagemUsuario> listaMensagens) {
		this.listaMensagens = listaMensagens;
	}

	@Transient
	public boolean temRole(String role) {
		for (UsuarioPerfil perfil : usuarioPerfils) {
			if (perfil.getPerfil().getNome().equals(role)) {
				return true;
			}
		}

		return false;
	}

	@Transient
	public Integer getDiasInatividade() {
		Integer dias = null;
		if (dataUltimoAcesso != null) {
			long tempo1 = dataUltimoAcesso.getTime();
			long tempo2 = new Date().getTime();
			long difTempo = tempo2 - tempo1;
			dias = (int) ((difTempo + 60L * 60 * 1000) / (24L * 60 * 60 * 1000));
		}
		return dias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		result = prime * result + ((tipoAcesso == null) ? 0 : tipoAcesso.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(Usuario.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Usuario other = (Usuario) obj;
		if (getLogin() == null) {
			if (other.getLogin() != null) {
				return false;
			}
		} else if (!getLogin().equals(other.getLogin())) {
			return false;
		}
		if (getSenha() == null) {
			if (other.getSenha() != null) {
				return false;
			}
		} else if (!getSenha().equals(other.getSenha())) {
			return false;
		}
		if (getTipoAcesso() != other.getTipoAcesso()) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Usuario [login=" + login + ", tipoAcesso=" + tipoAcesso + "]";
	}

	// ===== Implementação do UserDetails =====

	//FIXME Ajustar esses métodos para os valores corretos

	@Override
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	@Override
	@Transient
	public String getPassword() {
		return getSenha();
	}

	@Override
	@Transient
	public String getUsername() {
		return getLogin();
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return Boolean.TRUE;
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return Boolean.TRUE;
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return Boolean.TRUE;
	}

	@Override
	@Transient
	public boolean isEnabled() {
		return Boolean.TRUE;
	}

	// =====

}
