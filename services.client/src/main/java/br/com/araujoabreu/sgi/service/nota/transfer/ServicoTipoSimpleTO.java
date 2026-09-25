package br.com.araujoabreu.sgi.service.nota.transfer;

public class ServicoTipoSimpleTO {
	private Long codigo;
	private Long codigoPai;
	private String sigla;
	private String nome;
	private String situacao;
	private String tipoFaturamento;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Long getCodigoPai() {
		return codigoPai;
	}

	public void setCodigoPai(Long codigoPai) {
		this.codigoPai = codigoPai;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getTipoFaturamento() {
		return tipoFaturamento;
	}

	public void setTipoFaturamento(String tipoFaturamento) {
		this.tipoFaturamento = tipoFaturamento;
	}
}
