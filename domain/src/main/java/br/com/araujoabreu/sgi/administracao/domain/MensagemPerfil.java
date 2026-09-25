package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.corporativo.security.domain.Perfil;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mensagem_perfil")
@SequenceGenerator(name="SEQ_MENSAGEM_PERFIL", sequenceName="SEQ_MENSAGEM_PERFIL", allocationSize = 1)
public class MensagemPerfil extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -255554195697779529L;
	private Mensagem mensagem;
	private Perfil perfil;

	public MensagemPerfil() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_mensagem", nullable = false)
	public Mensagem getMensagem() {
		return this.mensagem;
	}

	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_perfil", nullable = false)
	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

}
