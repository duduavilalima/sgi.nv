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
@Table(name = "MENSAGEM_CENTRO_FINANCEIRO")
@SequenceGenerator(name = "SEQ_MENSAGEM_GERENCIA", sequenceName = "SEQ_MENSAGEM_GERENCIA", allocationSize = 1)
public class MensagemGerencia extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 2658453905668618586L;
	private Mensagem mensagem;
	private CentroFinanceiro centroFinanceiro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_MENSAGEM", nullable = false)
	public Mensagem getMensagem() {
		return mensagem;
	}

	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO", nullable = false)
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

}
