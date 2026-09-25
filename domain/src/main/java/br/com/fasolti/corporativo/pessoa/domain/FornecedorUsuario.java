package br.com.fasolti.corporativo.pessoa.domain;

import br.com.fasolti.corporativo.security.domain.Usuario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "fornecedor_usuario")
@SequenceGenerator(name = "seq_fornecedor_usuario", sequenceName = "seq_fornecedor_usuario", allocationSize = 1)
public class FornecedorUsuario implements Serializable {

	private static final long serialVersionUID = -4032426018780693055L;

	private Long codigo;
	private Usuario usuario;
	private Pessoa fornecedor;

	public FornecedorUsuario() {
		super();
	}

	public FornecedorUsuario(Usuario usuario, Pessoa fornecedor) {
		super();
		this.usuario = usuario;
		this.fornecedor = fornecedor;
	}

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_fornecedor_usuario")
	@Id
	@Column(name = "CODIGO", nullable = false)
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_usuario", nullable = false)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_fornecedor", nullable = false)
	public Pessoa getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Pessoa pessoa) {
		this.fornecedor = pessoa;
	}

}
