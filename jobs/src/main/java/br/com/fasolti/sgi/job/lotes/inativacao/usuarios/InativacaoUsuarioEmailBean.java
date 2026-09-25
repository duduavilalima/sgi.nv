package br.com.fasolti.sgi.job.lotes.inativacao.usuarios;

public class InativacaoUsuarioEmailBean {

	private String codigoLoteFormatado;
	private String nomeArquivo;
	private boolean sucesso;
	private String motivoErro = "";

	private int totalRegistros;
	private int totalNaoLocalizado;
	private int totalImportadoComPc;
	private int totalImportadoSemPc;
	private int totalJaInativado;
	private int totalIgnorado;

	public boolean isSucesso() {
		return sucesso;
	}

	public void setSucesso(boolean arquivoImportado) {
		this.sucesso = arquivoImportado;
	}

	public String getCodigoLoteFormatado() {
		return codigoLoteFormatado;
	}

	public void setCodigoLoteFormatado(String codigoLoteFormatado) {
		this.codigoLoteFormatado = codigoLoteFormatado;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getMotivoErro() {
		return motivoErro;
	}

	public void setMotivoErro(String motivoErro) {
		this.motivoErro = motivoErro;
	}

	public int getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(int totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	public int getTotalNaoLocalizado() {
		return totalNaoLocalizado;
	}

	public void setTotalNaoLocalizado(int totalNaoLocalizado) {
		this.totalNaoLocalizado = totalNaoLocalizado;
	}

	public int getTotalImportadoComPc() {
		return totalImportadoComPc;
	}

	public void setTotalImportadoComPc(int totalImportadoComPc) {
		this.totalImportadoComPc = totalImportadoComPc;
	}

	public int getTotalImportadoSemPc() {
		return totalImportadoSemPc;
	}

	public void setTotalImportadoSemPc(int totalImportadoSemPc) {
		this.totalImportadoSemPc = totalImportadoSemPc;
	}

	public int getTotalJaInativado() {
		return totalJaInativado;
	}

	public void setTotalJaInativado(int totalJaInativado) {
		this.totalJaInativado = totalJaInativado;
	}

	public int getTotalIgnorado() {
		return totalIgnorado;
	}

	public void setTotalIgnorado(int totalIgnorado) {
		this.totalIgnorado = totalIgnorado;
	}
}
