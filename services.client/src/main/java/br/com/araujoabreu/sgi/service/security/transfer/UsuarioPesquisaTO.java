package br.com.araujoabreu.sgi.service.security.transfer;

import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.util.Date;

public class UsuarioPesquisaTO extends BaseSearchTO {


	private String situacao;
	private String tipo;
	private String login;
	private Date dataUltimoAcesso;

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Date getDataUltimoAcesso() {
		return dataUltimoAcesso;
	}

	public void setDataUltimoAcesso(Date dataUltimoAcesso) {
		this.dataUltimoAcesso = dataUltimoAcesso;
	}
}
