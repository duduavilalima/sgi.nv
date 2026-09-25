package br.com.araujoabreu.sgi.service.administracao.transfer;

import br.com.araujoabreu.sgi.service.comercial.transfer.ClassificacaoClienteTO;
import br.com.araujoabreu.sgi.service.security.transfer.UsuarioTO;

public class PessoaFisicaTO extends PessoaTO {

	private UsuarioTO usuario;
	private ClassificacaoClienteTO classificacaoClienteTO;
	private String tipoPessoaFisica;
	private EnderecoTO endereco;
	private PessoaTO homologador;

	public PessoaFisicaTO() {
	}

	public PessoaFisicaTO(Long codigo) {
		setCodigo(codigo);
	}

	public PessoaFisicaTO(String nome) {
		setNome(nome);
	}

	public UsuarioTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioTO usuario) {
		this.usuario = usuario;
	}

	public ClassificacaoClienteTO getClassificacaoClienteTO() {
		return classificacaoClienteTO;
	}

	public void setClassificacaoClienteTO(ClassificacaoClienteTO classificacaoClienteTO) {
		this.classificacaoClienteTO = classificacaoClienteTO;
	}

	public String getTipoPessoaFisica() {
		return tipoPessoaFisica;
	}

	public void setTipoPessoaFisica(String tipoPessoaFisica) {
		this.tipoPessoaFisica = tipoPessoaFisica;
	}

	public EnderecoTO getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoTO endereco) {
		this.endereco = endereco;
	}
}
