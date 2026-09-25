package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "REINF")
@SequenceGenerator(name="SEQ_REINF", sequenceName="SEQ_REINF", allocationSize=1)
public class Reinf extends AuditEntity {

	private static final long serialVersionUID = -5229569130346515377L;
	private String referencia;
	private List<ReinfEvento> eventos;
	private List<ReinfAnexo> anexos;
	private Unidade unidade;
	private ReinfStatus reinfStatus;
	private String versao;


	@Column(name = "referencia")
	@NotNull
	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reinf")
	@OrderBy(value = "codigo ASC")
	public List<ReinfEvento> getEventos() {
		return eventos;
	}

	public void setEventos(List<ReinfEvento> eventos) {
		this.eventos = eventos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reinf")
	@OrderBy(value = "codigo ASC")
	public List<ReinfAnexo> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<ReinfAnexo> anexos) {
		this.anexos = anexos;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_unidade")
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@Column(name = "situacao")
	@NotNull
	public ReinfStatus getReinfStatus() {
		return reinfStatus;
	}

	public void setReinfStatus(ReinfStatus reinfStatus) {
		this.reinfStatus = reinfStatus;
	}

	@Column(name = "versao")
	@NotNull
	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public enum ReinfStatus {
		EM_GERACAO_EVENTOS("Em geração", "Em geração de eventos"),
		EVENTOS_GERADOS("Eventos gerados", "Eventos gerados e aguardando transmissão"),
		EM_TRAMISSAO("Em transmissão", "Em transmissão de eventos"),
		EM_PROCESSAMENTO_RECEITA("Enviado a Receita Federal", "Evento de fechamento enviado e em processamento pela Receita"),
		PROCESSADO("Processado pela receita", "Processado pela receita com sucesso"),
		PROCESSADO_COM_PENDENCIAS("Processado pela Receita com pendências", "Processado pela Receita com pendências/erros"),
		REABERTO("Reaberto", "Reaberto para a retransmissão de eventos"),
		ARQUIVO_IMPORTADO("Arquivo importado", "Arquivo com os informes da RFB importado."),
		PROCESSANDO_RETORNO("Processando retorno", "Processando os arquivos após envio à RFB"),
		EXCLUIDO("Selecionado para exclusão", "Agendado para exclusão");

		final String nome;
		final String descricao;

		ReinfStatus(String nome, String descricao) {
			this.nome = nome;
			this.descricao = descricao;
		}

		public String getNome() {
			return nome;
		}

		public String getDescricao() {
			return descricao;
		}

	}
}
