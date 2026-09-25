package br.com.araujoabreu.sgi.service.compras.transfer;

public class DocumentoFiscalTO {

	private Long codigo;
	private String codigoReceita;
	private String nome;
	private Boolean indicadorConsumo;
	private Boolean indicadorTransporte;
	private Boolean situacao;

	public DocumentoFiscalTO() {
	}

	public DocumentoFiscalTO(long codigo) {
		setCodigo(codigo);
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getCodigoReceita() {
		return codigoReceita;
	}

	public void setCodigoReceita(String codigoReceita) {
		this.codigoReceita = codigoReceita;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getIndicadorConsumo() {
		return indicadorConsumo;
	}

	public void setIndicadorConsumo(Boolean indicadorConsumo) {
		this.indicadorConsumo = indicadorConsumo;
	}

	public Boolean getIndicadorTransporte() {
		return indicadorTransporte;
	}

	public void setIndicadorTransporte(Boolean indicadorTransporte) {
		this.indicadorTransporte = indicadorTransporte;
	}

	public Boolean getSituacao() {
		return situacao;
	}

	public void setSituacao(Boolean situacao) {
		this.situacao = situacao;
	}
}
