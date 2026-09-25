package br.com.araujoabreu.sgi.service.comercial.transfer;

public class ClassificacaoClienteTO {

	private Long codigo;
	private String nome;

	public ClassificacaoClienteTO() {
	}

	public ClassificacaoClienteTO(Long codigo) {
		this.codigo = codigo;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
