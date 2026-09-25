package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "REINF_EVENTO")
@SequenceGenerator(name="SEQ_REINF_EVENTO", sequenceName="SEQ_REINF_EVENTO", allocationSize=1)
public class ReinfEvento extends AuditEntity {
	private static final long serialVersionUID = 6166312331525283339L;
	private TipoEvento tipoEvento;
	private Date dataEnvio;
	private Pessoa prestador;
	private Pessoa cliente;
	private ReinfRegistroEvento reinfRegistroEvento;
	private Reinf reinf;
	private List<ReinfEventoNotaFiscal> eventoNotasFiscais;
	private List<ReinfEventoAnexo> anexos;
	private String idTransmissao;
	private SituacaoEventoReinf situacao;
	private String numeroRecibo;
	private List<ReinfEventoErro> reinfEventoErros;

	@Column(name = "tipo_evento")
	@NotNull
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_reinf")
	@NotNull
	public Reinf getReinf() {
		return reinf;
	}

	public void setReinf(Reinf reinf) {
		this.reinf = reinf;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reinfEvento")
	public List<ReinfEventoNotaFiscal> getEventoNotasFiscais() {
		return eventoNotasFiscais;
	}

	public void setEventoNotasFiscais(List<ReinfEventoNotaFiscal> eventoNotasFiscais) {
		this.eventoNotasFiscais = eventoNotasFiscais;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_registro_evento")
	@NotNull
	public ReinfRegistroEvento getReinfRegistroEvento() {
		return reinfRegistroEvento;
	}

	public void setReinfRegistroEvento(ReinfRegistroEvento reinfRegistroEvento) {
		this.reinfRegistroEvento = reinfRegistroEvento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_envio")
	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_prestador")
	public Pessoa getPrestador() {
		return prestador;
	}

	public void setPrestador(Pessoa prestador) {
		this.prestador = prestador;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reinfEvento")
	public List<ReinfEventoAnexo> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<ReinfEventoAnexo> anexos) {
		this.anexos = anexos;
	}

	@Column(name = "id_transmissao")
	@NotNull
	public String getIdTransmissao() {
		return idTransmissao;
	}

	public void setIdTransmissao(String idTransmissao) {
		this.idTransmissao = idTransmissao;
	}

	@Column(name = "situacao")
	@NotNull
	public SituacaoEventoReinf getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoEventoReinf situacao) {
		this.situacao = situacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_cliente")
	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	@Column(name = "numero_recibo")
	public String getNumeroRecibo() {
		return numeroRecibo;
	}

	public void setNumeroRecibo(String numeroRecibo) {
		this.numeroRecibo = numeroRecibo;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reinfEvento")
	public List<ReinfEventoErro> getReinfEventoErros() {
		return reinfEventoErros;
	}

	public void setReinfEventoErros(List<ReinfEventoErro> reinfEventoErros) {
		this.reinfEventoErros = reinfEventoErros;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ReinfEvento{");
		sb.append("tipoEvento=").append(tipoEvento);
		sb.append(", dataEnvio=").append(dataEnvio);
		sb.append(", prestador=").append(prestador);
		sb.append(", cliente=").append(cliente);
		sb.append(", reinfRegistroEvento=").append(reinfRegistroEvento);
		sb.append(", reinf=").append(reinf);
		sb.append(", eventoNotasFiscais=").append(eventoNotasFiscais);
		sb.append(", anexos=").append(anexos);
		sb.append(", idTransmissao='").append(idTransmissao).append('\'');
		sb.append(", situacao=").append(situacao);
		sb.append(", numeroRecibo='").append(numeroRecibo).append('\'');
		sb.append(", reinfEventoErros=").append(reinfEventoErros);
		sb.append('}');
		return sb.toString();
	}

	public enum TipoEvento {
		ORIGINAL(1, "Original"), RETIFICACAO(2, "Retificação");

		final int valor;
		final String nome;

		TipoEvento(int valor, String nome) {
			this.valor = valor;
			this.nome = nome;
		}

		public int getValor() {
			return valor;
		}

		public String getNome() {
			return nome;
		}
	}

	public enum SituacaoEventoReinf {
		ARQUIVO_GERADO("Arquivo gerado", "Evento possui arquivo gerado e aguardando envio"),
		ENVIADO_SUCESSO("Enviado com sucesso", "Evento enviado com sucesso"),
		ENVIADO_ERRO("Enviado com erros", "Evento enviado, porém contém erros de validação"),
		ERRO_ENVIO("Erro durante a transmissão", "Ocorreu algum erro na aplicação ou na recepção na receita"),
		EXCLUIDO("Evento excluído do Reinf", "O evento foi excluído do Reinf");

		private String nome;
		private String descricao;

		SituacaoEventoReinf(String nome, String descricao) {
			this.nome = nome;
			this.descricao = descricao;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
	}
}
