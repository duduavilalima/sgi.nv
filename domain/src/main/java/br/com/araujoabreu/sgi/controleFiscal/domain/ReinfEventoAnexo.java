package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.araujoabreu.sgi.compras.domain.Anexo;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "REINF_EVENTO_ANEXO")
@SequenceGenerator(name="SEQ_REINF_EVENTO_ANEXO", sequenceName="SEQ_REINF_EVENTO_ANEXO", allocationSize=1)
public class ReinfEventoAnexo extends BaseEntity {

	private static final long serialVersionUID = 6744313014565745919L;
	private ReinfEvento reinfEvento;
	private Anexo anexo;
	private TipoArquivoReinf tipo;

	@ManyToOne
	@JoinColumn(name = "codigo_reinf_evento", nullable = false)
	public ReinfEvento getReinfEvento() {
		return reinfEvento;
	}

	public void setReinfEvento(ReinfEvento reinfEvento) {
		this.reinfEvento = reinfEvento;
	}

	@ManyToOne
	@JoinColumn(name = "codigo_anexo", nullable = false)
	public Anexo getAnexo() {
		return anexo;
	}

	public void setAnexo(Anexo anexo) {
		this.anexo = anexo;
	}

	@Column(name = "tipo", nullable = false)
	public TipoArquivoReinf getTipo() {
		return tipo;
	}

	public void setTipo(TipoArquivoReinf tipo) {
		this.tipo = tipo;
	}

	public enum TipoArquivoReinf {
		ENVIO("Arquivo de envio"),
		RETORNO("Arquivo de retorno"),
		ASSINADO("Arquivo de envio assinado");

		private String nome;

		TipoArquivoReinf(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}
	}
}
