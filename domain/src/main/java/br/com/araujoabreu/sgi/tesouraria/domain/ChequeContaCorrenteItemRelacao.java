package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.fasolti.commons.domain.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
// FIXME: Chave composta não funciona no Spring Repository
@Table(name = "CHEQUE_CONTA_CORRENTE_ITEM_RELACAO")
public class ChequeContaCorrenteItemRelacao implements DomainObject {

	private static final long serialVersionUID = -6555001543751617313L;

	@Id
	@Column(name = "CODIGO_CHEQUE_CONTA_CORRENTE")
	private Long codigoChequeContaCorrente;

	//FIXME: Chave composta não funciona no Spring Repository
	@Column(name = "CODIGO_ITEM_RELACAO")
	private Long codigoItemRelacao;

	public ChequeContaCorrenteItemRelacao() {

	}

	public ChequeContaCorrenteItemRelacao(Long codigoChequeContaCorrente, Long codigoItemRelacao) {
		this.codigoChequeContaCorrente = codigoChequeContaCorrente;
		this.codigoItemRelacao = codigoItemRelacao;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoChequeContaCorrente == null) ? 0 : codigoChequeContaCorrente.hashCode());
		result = prime * result + ((codigoItemRelacao == null) ? 0 : codigoItemRelacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ChequeContaCorrenteItemRelacao)) {
			return false;
		}
		ChequeContaCorrenteItemRelacao other = (ChequeContaCorrenteItemRelacao) obj;
		if (codigoChequeContaCorrente == null) {
			if (other.codigoChequeContaCorrente != null) {
				return false;
			}
		} else if (!codigoChequeContaCorrente.equals(other.codigoChequeContaCorrente)) {
			return false;
		}
		if (codigoItemRelacao == null) {
			if (other.codigoItemRelacao != null) {
				return false;
			}
		} else if (!codigoItemRelacao.equals(other.codigoItemRelacao)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return codigoChequeContaCorrente;
	}

	@Override
	public void setId(Long id) {
		this.codigoChequeContaCorrente = id;
	}
}
