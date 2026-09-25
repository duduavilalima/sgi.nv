package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

@Entity
@Table(name = "motivo_retorno_documento_contrato", uniqueConstraints = @UniqueConstraint(columnNames = {"codigo_contrato", "codigo_motivo_retorno_documento"}))
@SequenceGenerator(name = "SEQ_MOTIVO_RETORNO_DOCUMENTO_CONTRATO", sequenceName = "SEQ_MOTIVO_RETORNO_DOCUMENTO_CONTRATO", allocationSize = 1)
public class MotivoRetornoDocumentoContrato extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -884555035964616982L;
	private Contrato contrato;
	private MotivoRetornoDocumento motivoRetornoDocumento;

	public MotivoRetornoDocumentoContrato() {
	}

	public MotivoRetornoDocumentoContrato(Contrato contrato, MotivoRetornoDocumento motivoRetornoDocumento) {
		this.contrato = contrato;
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_contrato", nullable = false)
	@NotNull
	public Contrato getContrato() {
		return this.contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_motivo_retorno_documento", nullable = false)
	@NotNull
	public MotivoRetornoDocumento getMotivoRetornoDocumento() {
		return this.motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumento motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}
}
