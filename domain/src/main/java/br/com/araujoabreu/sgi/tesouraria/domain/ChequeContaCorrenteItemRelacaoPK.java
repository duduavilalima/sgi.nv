package br.com.araujoabreu.sgi.tesouraria.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChequeContaCorrenteItemRelacaoPK implements Serializable {

	private static final long serialVersionUID = 1454691680065659013L;

	@Column(name = "CODIGO_CHEQUE_CONTA_CORRENTE")
	private Long codigoChequeContaCorrente;

	@Column(name = "CODIGO_ITEM_RELACAO")
	private Long codigoItemRelacao;

	public ChequeContaCorrenteItemRelacaoPK() {

	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChequeContaCorrenteItemRelacaoPK that = (ChequeContaCorrenteItemRelacaoPK) o;
		return codigoChequeContaCorrente.equals(that.codigoChequeContaCorrente) &&
			codigoItemRelacao.equals(that.codigoItemRelacao);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoChequeContaCorrente, codigoItemRelacao);
	}
}
