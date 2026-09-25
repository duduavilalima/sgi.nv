package br.com.araujoabreu.sgi.service.reinf;

public class IdeEvento {
	private int indRetif = TipoEventoArquivo.ORIGINAL.getValor();
	private String nrRecibo; // Preencher com o número do recibo do arquivo a ser retificado. Obrigatório se indRetif = RETIFICAO
	private String perApur;  // Informar o ano/mês de referência das informações no formato AAAA-MM
	private int tpAmb = TipoAmbiente.PRODUCAO_RESTRITA.getValor();
	private int procEmi = 1; //Processo de emissão do evento: 1 - Aplicativo do contribuinte; 2 - Aplicativo governamental.
	private String verProc;

	public int getIndRetif() {
		return indRetif;
	}

	public void setIndRetif(int indRetif) {
		this.indRetif = indRetif;
	}

	public String getNrRecibo() {
		return nrRecibo;
	}

	public void setNrRecibo(String nrRecibo) {
		this.nrRecibo = nrRecibo;
	}

	public String getPerApur() {
		return perApur;
	}

	public void setPerApur(String perApur) {
		this.perApur = perApur;
	}

	public int getTpAmb() {
		return tpAmb;
	}

	public void setTpAmb(int tpAmb) {
		this.tpAmb = tpAmb;
	}

	public int getProcEmi() {
		return procEmi;
	}

	public void setProcEmi(int procEmi) {
		this.procEmi = procEmi;
	}

	public String getVerProc() {
		return verProc;
	}

	public void setVerProc(String verProc) {
		this.verProc = verProc;
	}

	/*
	Esse enum representa o mesmo que o enum TipoEvento na entidade ReinfEvento, foi renomeado apenas para não gerar
	ambiguidade e por sua utilização ser um pouco diferente do que na entidade.
	*/
	public enum TipoEventoArquivo {
		ORIGINAL(1), RETIFICACAO(2);

		final int valor;

		TipoEventoArquivo(int valor) {
			this.valor = valor;
		}

		public int getValor() {
			return valor;
		}
	}

	public enum TipoAmbiente {
		PRODUCAO(1), PRODUCAO_RESTRITA(2);

		final int valor;

		TipoAmbiente(int valor) {
			this.valor = valor;
		}

		public int getValor() {
			return valor;
		}
	}
}
