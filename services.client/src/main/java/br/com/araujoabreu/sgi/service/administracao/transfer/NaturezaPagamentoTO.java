package br.com.araujoabreu.sgi.service.administracao.transfer;

public class NaturezaPagamentoTO {

	private Long codigo;
	private String nome;
	private String contaContabil;
	private String tipoDespesa;

	public NaturezaPagamentoTO() {
	}

	public NaturezaPagamentoTO(Long codigo) {
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

	public String getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(String contaContabil) {
		this.contaContabil = contaContabil;
	}

	public String getTipoDespesa() {
		return tipoDespesa;
	}

	public void setTipoDespesa(String tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}
}
