package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "REINF_EVENTO_NOTA_FISCAL")
@SequenceGenerator(name="SEQ_REINF_EVENTO_NOTA_FISCAL", sequenceName="SEQ_REINF_EVENTO_NOTA_FISCAL", allocationSize=1)
public class ReinfEventoNotaFiscal extends BaseEntity {

	private static final long serialVersionUID = -5877530528255079507L;
	private ReinfEvento reinfEvento;
	private NotaFiscal notaFiscal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_reinf_evento")
	@NotNull
	public ReinfEvento getReinfEvento() {
		return reinfEvento;
	}

	public void setReinfEvento(ReinfEvento reinfEvento) {
		this.reinfEvento = reinfEvento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_nota_fiscal")
	@NotNull
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
}
