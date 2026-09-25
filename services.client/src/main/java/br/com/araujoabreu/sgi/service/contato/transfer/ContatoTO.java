package br.com.araujoabreu.sgi.service.contato.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.telefone.transfer.TelefoneTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class ContatoTO extends BaseTO {

	private String nome;
	private PessoaTO pessoa;
	private TelefoneTO telefone1;
	private TelefoneTO telefone2;
	private String email;
	private RelacionamentoContatoTO relacionamentoContato;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public PessoaTO getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaTO pessoa) {
		this.pessoa = pessoa;
	}

	public TelefoneTO getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(TelefoneTO telefone1) {
		this.telefone1 = telefone1;
	}

	public TelefoneTO getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(TelefoneTO telefone2) {
		this.telefone2 = telefone2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public RelacionamentoContatoTO getRelacionamentoContato() {
		return relacionamentoContato;
	}

	public void setRelacionamentoContato(RelacionamentoContatoTO relacionamentoContato) {
		this.relacionamentoContato = relacionamentoContato;
	}
}
