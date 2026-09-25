package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "MENSAGEM_USUARIO")
@SequenceGenerator(name = "SEQ_MENSAGEM_USUARIO", sequenceName = "SEQ_MENSAGEM_USUARIO", allocationSize = 1)
public class MensagemUsuario extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 846206819274756063L;
	private Mensagem mensagem;
	private Usuario usuario;
	private Date dataAceite;
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
	@JoinColumn(name = "CODIGO_USUARIO", nullable = false)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_ACEITE")
	public Date getDataAceite() {
		return dataAceite;
	}

	public void setDataAceite(Date dataAceite) {
		this.dataAceite = dataAceite;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@Transient
	public Colaborador getColaborador() {
		if (this.getUsuario() == null)
			return null;

		return (Colaborador) this.getUsuario().getPessoaFisica();
	}


}
