package br.com.fasolti.corporativo.pessoa.domain;

import br.com.fasolti.corporativo.security.domain.Usuario;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Uma <code>PessoaFísica</code> herda os atributos de uma entidade abstrada <code>Pessoa</code>
 * <br>
 * Uma pessoa física pode ter um usuário de acesso configurado, neste caso, podendo acessar o sistema
 * para utilizá-lo.
 *
 * @author Marcus Lacerda
 * @see br.com.araujoabreu.sgi.administracao.domain.Colaborador
 */
@Entity
@Table(name = "PESSOA_FISICA")
public class PessoaFisica extends Pessoa {

	private static final long serialVersionUID = 8776205709295516404L;

	private Usuario usuario;
	private TipoPessoaFisica tipoPessoaFisica;

	public PessoaFisica() {
		setTipoPessoa(TipoPessoa.PESSOA_FISICA);
	}

	/**
	 * Usuário utilizado para acesso ao sistema
	 *
	 * @return usuário com as suas permissões de acesso
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CODIGO_USUARIO")
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Column(name = "TIPO", nullable = false)
	@NotNull
	public TipoPessoaFisica getTipoPessoaFisica() {
		return tipoPessoaFisica;
	}

	public void setTipoPessoaFisica(TipoPessoaFisica tipoPessoaFisica) {
		this.tipoPessoaFisica = tipoPessoaFisica;
	}

	@Override
	public String toString() {
		return "PessoaFisica{" +
			"tipoPessoaFisica=" + getTipoPessoaFisica() +
			'}';
	}

	public enum TipoPessoaFisica {
		USUARIO("Usuário"),
		CLIENTE("Cliente"),
		USUARIO_CLIENTE("Usuário/Cliente"),
		FORNECEDOR("Fornecedor"),
		USUARIO_FORNECEDOR("Usuário/Fornecedor"),
		FORNECEDOR_CLIENTE("Fornecedor/Cliente"),
		USUARIO_FORNECEDOR_CLIENTE("Usuário/Fornecedor/Cliente");

		private final String nome;

		TipoPessoaFisica(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}
	}

}
