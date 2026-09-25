package br.com.araujoabreu.sgi.service.motivoRetorno.transfer;

import br.com.araujoabreu.sgi.service.nota.transfer.NotaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;
import java.util.Objects;

public class MotivoRetornoNotaTO extends BaseTO {

	private NotaTO nota;
	private MotivoRetornoDocumentoTO motivoRetornoDocumento;
	private Date dataCancelamento;

	public NotaTO getNota() {
		return nota;
	}

	public void setNota(NotaTO nota) {
		this.nota = nota;
	}

	public MotivoRetornoDocumentoTO getMotivoRetornoDocumento() {
		return motivoRetornoDocumento;
	}

	public void setMotivoRetornoDocumento(MotivoRetornoDocumentoTO motivoRetornoDocumento) {
		this.motivoRetornoDocumento = motivoRetornoDocumento;
	}

	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MotivoRetornoNotaTO)) return false;
		if (!super.equals(o)) return false;
		MotivoRetornoNotaTO that = (MotivoRetornoNotaTO) o;
		return Objects.equals(getNota(), that.getNota()) &&
			Objects.equals(getMotivoRetornoDocumento(), that.getMotivoRetornoDocumento()) &&
			Objects.equals(getDataCancelamento(), that.getDataCancelamento());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNota(), getMotivoRetornoDocumento(), getDataCancelamento());
	}

	@Override
	public String toString() {
		return "MotivoRetornoNotaTO{" +
			"nota=" + nota +
			", motivoRetornoDocumento=" + motivoRetornoDocumento +
			", dataCancelamento=" + dataCancelamento +
			'}';
	}
}
