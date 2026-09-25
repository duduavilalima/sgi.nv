package br.com.araujoabreu.sgi.comercial.domain;


import java.util.Objects;

public class LogImportacaoFecho {

	private TipoLogCF tipo;
	private String descricaoTipo;
	private String categoria;
	private String contratacao;
	private String valor;
	private String percentual;
	private String quantidade;
	private String adicionalPericulosidade;
	private String adicionalInsalubridade;
	private String adicionalDissidio;
	private String adicionalHorasExtras;
	private String adicionalSobreaviso;
	private String adicionalNoturno;
	private StringBuffer situacao;

	public TipoLogCF getTipo() {
		return tipo;
	}

	public void setTipo(TipoLogCF tipo) {
		this.tipo = tipo;
	}

	public String getDescricaoTipo() {
		return descricaoTipo;
	}

	public void setDescricaoTipo(String descricaoTipo) {
		this.descricaoTipo = descricaoTipo;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getContratacao() {
		return contratacao;
	}

	public void setContratacao(String contratacao) {
		this.contratacao = contratacao;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getPercentual() {
		return percentual;
	}

	public void setPercentual(String percentual) {
		this.percentual = percentual;
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	public StringBuffer getSituacao() {
		return situacao;
	}

	public void setSituacao(StringBuffer situacao) {
		this.situacao = situacao;
	}

	public String getAdicionalPericulosidade() {
		return adicionalPericulosidade;
	}

	public void setAdicionalPericulosidade(String adicionalPericulosidade) {
		this.adicionalPericulosidade = adicionalPericulosidade;
	}

	public String getAdicionalInsalubridade() {
		return adicionalInsalubridade;
	}

	public void setAdicionalInsalubridade(String adicionalInsalubridade) {
		this.adicionalInsalubridade = adicionalInsalubridade;
	}

	public String getAdicionalDissidio() {
		return adicionalDissidio;
	}

	public void setAdicionalDissidio(String adicionalDissidio) {
		this.adicionalDissidio = adicionalDissidio;
	}

	public String getAdicionalHorasExtras() {
		return adicionalHorasExtras;
	}

	public void setAdicionalHorasExtras(String adicionalHorasExtras) {
		this.adicionalHorasExtras = adicionalHorasExtras;
	}

	public String getAdicionalSobreaviso() {
		return adicionalSobreaviso;
	}

	public void setAdicionalSobreaviso(String adicionalSobreaviso) {
		this.adicionalSobreaviso = adicionalSobreaviso;
	}

	public String getAdicionalNoturno() {
		return adicionalNoturno;
	}

	public void setAdicionalNoturno(String adicionalNoturno) {
		this.adicionalNoturno = adicionalNoturno;
	}

	public void addSituacao(String situacao) {
		if (this.situacao != null) {
			this.situacao.append("• ");
			this.situacao.append(situacao);
			this.situacao.append("<br/>");
		}
	}

	public boolean valido() {
		if (this.situacao != null && this.situacao.length() > 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LogImportacaoFecho)) return false;
		LogImportacaoFecho that = (LogImportacaoFecho) o;
		return getTipo() == that.getTipo() &&
			Objects.equals(getDescricaoTipo(), that.getDescricaoTipo()) &&
			Objects.equals(getCategoria(), that.getCategoria()) &&
			Objects.equals(getContratacao(), that.getContratacao()) &&
			Objects.equals(getValor(), that.getValor()) &&
			Objects.equals(getPercentual(), that.getPercentual()) &&
			Objects.equals(getQuantidade(), that.getQuantidade()) &&
			Objects.equals(getAdicionalPericulosidade(), that.getAdicionalPericulosidade()) &&
			Objects.equals(getAdicionalInsalubridade(), that.getAdicionalInsalubridade()) &&
			Objects.equals(getAdicionalDissidio(), that.getAdicionalDissidio()) &&
			Objects.equals(getAdicionalHorasExtras(), that.getAdicionalHorasExtras()) &&
			Objects.equals(getAdicionalSobreaviso(), that.getAdicionalSobreaviso()) &&
			Objects.equals(getAdicionalNoturno(), that.getAdicionalNoturno()) &&
			Objects.equals(getSituacao(), that.getSituacao());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getTipo(), getDescricaoTipo(), getCategoria(), getContratacao(), getValor(), getPercentual(), getQuantidade(), getAdicionalPericulosidade(), getAdicionalInsalubridade(), getAdicionalDissidio(), getAdicionalHorasExtras(), getAdicionalSobreaviso(), getAdicionalNoturno(), getSituacao());
	}

	public enum TipoLogCF {
		MAO_DE_OBRA("Mão de obra"),
		ENCARGOS("Encargos"),
		GASTOS_GERAIS("Gastos gerais"),
		MATERIAIS("Materiais"),
		SERVICOS_TERCEIROS("Serviços terceiros"),
		BDI("BDI"),
		IMPOSTOS("Impostos");

		private final String nome;

		private TipoLogCF(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}
	}
}
