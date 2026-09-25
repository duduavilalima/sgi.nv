package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "CHEQUE_CONTA_CORRENTE_DOC_IMPRESSAO")
@SequenceGenerator(name = "SEQ_CHEQUE_CONTA_CORRENTE_DOC_IMPRESSAO", sequenceName = "SEQ_CHEQUE_CONTA_CORRENTE_DOC_IMPRESSAO", allocationSize = 1)
public class ChequeContaCorrenteDocumentosImpressao extends BaseEntity implements DomainObject {

	private static final long serialVersionUID = 4005205620192006699L;

	private Long codigoChequeContaCorrente;
	private Long codigoDocumentosImpressao;

	public ChequeContaCorrenteDocumentosImpressao() {
	}

	public ChequeContaCorrenteDocumentosImpressao(Long codigoChequeContaCorrente, Long codigoDocumentosImpressao) {
		this.codigoChequeContaCorrente = codigoChequeContaCorrente;
		this.codigoDocumentosImpressao = codigoDocumentosImpressao;
	}

	@Column(name = "CODIGO_CHEQUE_CONTA_CORRENTE")
	public Long getCodigoChequeContaCorrente() {
		return codigoChequeContaCorrente;
	}

	public void setCodigoChequeContaCorrente(Long codigoChequeContaCorrente) {
		this.codigoChequeContaCorrente = codigoChequeContaCorrente;
	}

	@Column(name = "CODIGO_DOCUMENTOS_IMPRESSAO")
	public Long getCodigoDocumentosImpressao() {
		return codigoDocumentosImpressao;
	}

	public void setCodigoDocumentosImpressao(Long codigoDocumentosImpressao) {
		this.codigoDocumentosImpressao = codigoDocumentosImpressao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ChequeContaCorrenteDocumentosImpressao)) return false;
		if (!super.equals(o)) return false;
		ChequeContaCorrenteDocumentosImpressao that = (ChequeContaCorrenteDocumentosImpressao) o;
		return Objects.equals(getCodigoChequeContaCorrente(), that.getCodigoChequeContaCorrente()) &&
			Objects.equals(getCodigoDocumentosImpressao(), that.getCodigoDocumentosImpressao());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getCodigoChequeContaCorrente(), getCodigoDocumentosImpressao());
	}

	@Override
	public String toString() {
		return "ChequeContaCorrenteDocumentosImpressao{" +
			"codigoChequeContaCorrente=" + codigoChequeContaCorrente +
			", codigoDocumentosImpressao=" + codigoDocumentosImpressao +
			'}';
	}
}
