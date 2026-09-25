package br.com.araujoabreu.sgi.historico.entity;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "HISTORICO_APLICACAO")
@SequenceGenerator(name="SEQ_HISTORICO_APLICACAO", sequenceName="SEQ_HISTORICO_APLICACAO", allocationSize=1)
public class HistoricoAplicacao extends BaseEntity {

	private static final long serialVersionUID = 4205311683919218743L;
	@NotNull
	@Column(name = "USUARIO", nullable = false)
	private String usuario;

	@NotNull
	@Column(name = "DATA", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@NotNull
	@Column(name = "ENTIDADE", nullable = false)
	private String entidade;

	//	TODO: corrigir a inserção do hitórico no momento da criação da entidade, o código da mesma está nulo, pois ainda não foi inserida no banco.
//	NotNull
	@Column(name = "CODIGO_ENTIDADE")
	private Long codigoEntidade;

	@NotNull
	@Column(name = "MOMENTO", nullable = false)
	private String momento;

	@NotNull
	@Column(name = "METODO", nullable = false)
	private String metodo;

	@NotNull
	@Column(name = "MENSAGEM", nullable = false)
	private String mensagem;

	@NotNull
	@Column(name = "SERIALIZACAO", nullable = false)
	private String serializacao;

	public HistoricoAplicacao() {
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getEntidade() {
		return entidade;
	}

	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	public Long getCodigoEntidade() {
		return codigoEntidade;
	}

	public void setCodigoEntidade(Long codigoEntidade) {
		this.codigoEntidade = codigoEntidade;
	}

	public String getMomento() {
		return momento;
	}

	public void setMomento(String momento) {
		this.momento = momento;
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getSerializacao() {
		return serializacao;
	}

	public void setSerializacao(String serializacao) {
		this.serializacao = serializacao;
	}

}
