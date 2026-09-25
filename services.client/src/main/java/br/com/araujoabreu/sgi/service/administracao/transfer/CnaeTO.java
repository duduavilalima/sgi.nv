package br.com.araujoabreu.sgi.service.administracao.transfer;

public class CnaeTO {

	private Long codigo;
	private String sigla;
	private String descricao;

	public CnaeTO() {
	}

	public CnaeTO(Long codigo) {
		this.codigo = codigo;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
