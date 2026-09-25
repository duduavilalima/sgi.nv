package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "MENSAGEM_DIRETORIA")
@SequenceGenerator(name="SEQ_MENSAGEM_DIRETORIA", sequenceName="SEQ_MENSAGEM_DIRETORIA", allocationSize=1)
public class MensagemDiretoria extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4732085487116262840L;
	private Mensagem mensagem;
	private Diretoria diretoria;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_MENSAGEM", nullable = false)
	public Mensagem getMensagem() {
		return mensagem;
	}

	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_DIRETORIA", nullable = false)
	public Diretoria getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(Diretoria diretoria) {
		this.diretoria = diretoria;
	}

}
