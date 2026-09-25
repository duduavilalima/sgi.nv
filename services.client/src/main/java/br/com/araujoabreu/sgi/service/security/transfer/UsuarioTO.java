package br.com.araujoabreu.sgi.service.security.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;
import java.util.List;

public class UsuarioTO extends BaseTO {
	private String login;
	private String senha;
	private String tipoAcesso;
	private Date dataUltimoAcesso;
	private PessoaTO pessoa;
	private List<PerfilTO> perfis;

	public UsuarioTO() {
	}

	public UsuarioTO(Long codigo) {
		setCodigo(codigo);
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTipoAcesso() {
		return tipoAcesso;
	}

	public void setTipoAcesso(String tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}

	public Date getDataUltimoAcesso() {
		return dataUltimoAcesso;
	}

	public void setDataUltimoAcesso(Date dataUltimoAcesso) {
		this.dataUltimoAcesso = dataUltimoAcesso;
	}

	public PessoaTO getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaTO pessoa) {
		this.pessoa = pessoa;
	}

	public List<PerfilTO> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<PerfilTO> perfis) {
		this.perfis = perfis;
	}
}
