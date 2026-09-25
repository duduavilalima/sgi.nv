package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.tesouraria.domain.ChequeContaCorrente;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "MOTIVO_RETORNO_DOCUMENTO_CHEQUE_CONTA_CORRENTE")
@SequenceGenerator(name = "SEQ_MOTIVO_RETORNO_DOCUMENTO_CHEQUE_CONTA_CORRENTE", sequenceName = "SEQ_MOTIVO_RETORNO_DOCUMENTO_CHEQUE_CONTA_CORRENTE", allocationSize = 1)
public class MotivoRetornoDocumentoChequeContaCorrente extends BaseEntity implements Comparable<MotivoRetornoDocumentoChequeContaCorrente>, Serializable {

	private static final long serialVersionUID = 4704543163058004357L;
	private ChequeContaCorrente chequeContaCorrente;
	private MotivoRetornoDocumento motivoRetornoDocumento;

	public MotivoRetornoDocumentoChequeContaCorrente() {
	}

	public MotivoRetornoDocumentoChequeContaCorrente(MotivoRetornoDocumento motivoRetornoDocumento, ChequeContaCorrente chequeContaCorrente) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
		this.chequeContaCorrente = chequeContaCorrente;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "codigo_cheque_conta_corrente", nullable = false)
	public ChequeContaCorrente getChequeContaCorrente() {
		return chequeContaCorrente;
	}

	public void setChequeContaCorrente(ChequeContaCorrente chequeContaCorrente) {
		this.chequeContaCorrente = chequeContaCorrente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_motivo_retorno_documento", nullable = false)
	public MotivoRetornoDocumento getMotivoRetornoDocumento() {
		return this.motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumento motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((chequeContaCorrente == null) ? 0 : chequeContaCorrente.hashCode());
		result = prime * result + ((motivoRetornoDocumento == null) ? 0 : motivoRetornoDocumento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof MotivoRetornoDocumentoChequeContaCorrente)) {
			return false;
		}
		MotivoRetornoDocumentoChequeContaCorrente other = (MotivoRetornoDocumentoChequeContaCorrente) obj;
		if (chequeContaCorrente == null) {
			if (other.chequeContaCorrente != null) {
				return false;
			}
		} else if (!chequeContaCorrente.equals(other.chequeContaCorrente)) {
			return false;
		}
		if (motivoRetornoDocumento == null) {
			if (other.motivoRetornoDocumento != null) {
				return false;
			}
		} else if (!motivoRetornoDocumento.equals(other.motivoRetornoDocumento)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(MotivoRetornoDocumentoChequeContaCorrente o) {
		return this.motivoRetornoDocumento.getDataCriacao().compareTo(o.getMotivoRetornoDocumento().getDataCriacao());
	}

}
