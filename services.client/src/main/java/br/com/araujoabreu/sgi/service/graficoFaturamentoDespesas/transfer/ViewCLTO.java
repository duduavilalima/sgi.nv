package br.com.araujoabreu.sgi.service.graficoFaturamentoDespesas.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

/**
 * Objeto de visualização do item CL (Centro Financeiro) para a tela "Custos > Gráfico Faturamento/Despesas", no campo "CL"
 */
public class ViewCLTO extends BaseTO {

	private String nome;
	private String identificadorFinal;
	private String cor;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIdentificadorFinal() {
		return identificadorFinal;
	}

	public void setIdentificadorFinal(String identificadorFinal) {
		this.identificadorFinal = identificadorFinal;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

}
