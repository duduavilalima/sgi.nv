package br.com.araujoabreu.sgi.service.administracao.transfer;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ColaboradorTO extends PessoaFisicaTO {

	private CentroFinanceiroTO centroFinanceiro;
	private ContaContabilTO contaContabilFundoFixo;
	private ContaContabilTO contaContabilEmprestimo;

	public ColaboradorTO() {
	}

	public ColaboradorTO(Long codigo) {
		setCodigo(codigo);
	}

	public ColaboradorTO(String nome) {
		setNome(nome);
	}

	public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public ContaContabilTO getContaContabilFundoFixo() {
		return contaContabilFundoFixo;
	}

	public void setContaContabilFundoFixo(ContaContabilTO contaContabilFundoFixo) {
		this.contaContabilFundoFixo = contaContabilFundoFixo;
	}

	public ContaContabilTO getContaContabilEmprestimo() {
		return contaContabilEmprestimo;
	}

	public void setContaContabilEmprestimo(ContaContabilTO contaContabilEmprestimo) {
		this.contaContabilEmprestimo = contaContabilEmprestimo;
	}

	@Override
	public String toString() {
		return this.getNome();
	}
}
