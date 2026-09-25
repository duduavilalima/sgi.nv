package br.com.araujoabreu.sgi.service.reinf.r2060;

public class TipoAjuste {

	public enum DESC_AJUSTE {
		NF_CANCELADA("NFs Canceladas"),
		ISS_EXCLUSAO("Exc. Receita ISS");

		private final String descricao;

		DESC_AJUSTE(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return getDescricao();
		}


	}

	private Integer tpAjuste;
	private Integer codAjuste;
	private String vlrAjuste;
	private String descAjuste = "NFs Canceladas";
	private String dtAjuste;

	public Integer getTpAjuste() {
		return tpAjuste;
	}

	public void setTpAjuste(Integer tpAjuste) {
		this.tpAjuste = tpAjuste;
	}

	public Integer getCodAjuste() {
		return codAjuste;
	}

	public void setCodAjuste(Integer codAjuste) {
		this.codAjuste = codAjuste;
	}

	public String getVlrAjuste() {
		return vlrAjuste;
	}

	public void setVlrAjuste(String vlrAjuste) {
		this.vlrAjuste = vlrAjuste;
	}

	public String getDescAjuste() {
		return descAjuste;
	}

	public void setDescAjuste(String descAjuste) {
		this.descAjuste = descAjuste;
	}

	public String getDtAjuste() {
		return dtAjuste;
	}

	public void setDtAjuste(String dtAjuste) {
		this.dtAjuste = dtAjuste;
	}


}
