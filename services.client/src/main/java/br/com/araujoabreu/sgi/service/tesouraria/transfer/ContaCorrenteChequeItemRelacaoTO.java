package br.com.araujoabreu.sgi.service.tesouraria.transfer;


import br.com.araujoabreu.sgi.service.util.BaseTO;

public class ContaCorrenteChequeItemRelacaoTO extends BaseTO {
	//FIXME Tem ID?
	private Long codigoChequeContaCorrente;
	private Long codigoItemRelacao;

	public Long getCodigoChequeContaCorrente() {
		return codigoChequeContaCorrente;
	}

	public void setCodigoChequeContaCorrente(Long codigoChequeContaCorrente) {
		this.codigoChequeContaCorrente = codigoChequeContaCorrente;
	}

	public Long getCodigoItemRelacao() {
		return codigoItemRelacao;
	}

	public void setCodigoItemRelacao(Long codigoItemRelacao) {
		this.codigoItemRelacao = codigoItemRelacao;
	}
}
