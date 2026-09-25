package br.com.araujoabreu.sgi.inrf.domain;

import br.com.araujoabreu.sgi.administracao.domain.Feriado.Meses;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.compras.domain.Anexo;
import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "PROCESSO")
@SequenceGenerator(name="SEQ_PROCESSO", sequenceName="SEQ_PROCESSO", allocationSize=1)
public class Processo extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 2186522128282723510L;

	private Unidade unidade;
	private Anexo anexo;
	private Integer mes;
	private Integer ano;
	private StatusProcesso status;
	private Date inicio;
	private Date termino;
	private String codigoEnvio;
	private Meses eMes;
	private String path;
	private List<NotaFiscal> listaNotaFiscal = new ArrayList<>();
	private List<ProcessoAnexo> listaProcessoAnexo = new ArrayList<>();
	private List<ProcessoSituacao> listaProcessoSituacao = new ArrayList<>();

	public Processo() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_UNIDADE")
	@NotNull
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ARQUIVO_RFB")
	public Anexo getAnexo() {
		return anexo;
	}

	public void setAnexo(Anexo anexo) {
		this.anexo = anexo;
	}

	@Column(name = "MES")
	@NotNull
	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	@Column(name = "ANO")
	@NotNull
	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	@Column(name = "SITUACAO")
	@NotNull
	public StatusProcesso getStatus() {
		return status;
	}

	public void setStatus(StatusProcesso status) {
		this.status = status;
	}

	@Column(name = "INICIO")
	@Temporal(TemporalType.DATE)
	@NotNull
	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	@Column(name = "TERMINO")
	@Temporal(TemporalType.DATE)
	@NotNull
	public Date getTermino() {
		return termino;
	}

	public void setTermino(Date termino) {
		this.termino = termino;
	}

	@Transient
	public StatusProcesso[] getStatusProcessos() {
		return StatusProcesso.values();
	}

	@Column(name = "CODIGO_ENVIO")
	public String getCodigoEnvio() {
		return codigoEnvio;
	}

	public void setCodigoEnvio(String codigoEnvio) {
		this.codigoEnvio = codigoEnvio;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "processo")
	public List<NotaFiscal> getListaNotaFiscal() {
		return listaNotaFiscal;
	}

	public void setListaNotaFiscal(List<NotaFiscal> listaNotaFiscal) {
		this.listaNotaFiscal = listaNotaFiscal;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "processo")
	public List<ProcessoAnexo> getListaProcessoAnexo() {
		return listaProcessoAnexo;
	}

	public void setListaProcessoAnexo(List<ProcessoAnexo> listaProcessoAnexo) {
		this.listaProcessoAnexo = listaProcessoAnexo;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "processo")
	public List<ProcessoSituacao> getListaProcessoSituacao() {
		return listaProcessoSituacao;
	}

	public void setListaProcessoSituacao(List<ProcessoSituacao> listaProcessoSituacao) {
		this.listaProcessoSituacao = listaProcessoSituacao;
	}

	@Transient
	public String getReferencia() {
		StringBuilder buffer = new StringBuilder();

		if (mes < 10) {
			buffer.append("0");
		}
		buffer.append("").append(mes).append("/");
		buffer.append(ano).append(" - ");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		buffer.append(sdf.format(inicio));
		buffer.append(" até ");
		buffer.append(sdf.format(termino));

		return buffer.toString();
	}

	@Transient
	public String getMesAno() {
		StringBuilder buffer = new StringBuilder();
		if (mes != null && ano != null) {
			if (mes < 9) {
				buffer.append("0");
			}
			buffer.append(mes).append("/").append(ano);
		}
		return buffer.toString();
	}

	@Transient
	public String getAnoMes() {
		StringBuilder buffer = new StringBuilder();
		if (mes != null && ano != null) {
			if (mes < 9)
				buffer.append(ano).append("0").append(mes);
			else
				buffer.append(ano).append(mes);
		}
		return buffer.toString();
	}

	@Transient
	public String getMesAnoExtenso() {
		StringBuilder buffer = new StringBuilder();
		if (mes != null && ano != null) {
			String mesExtenso = Meses.getMes(mes - 1).getNome();
			buffer.append(mesExtenso).append("/").append(ano);
		}
		return buffer.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Processo processo = (Processo) o;
		return mes.equals(processo.mes) &&
			ano.equals(processo.ano) &&
			inicio.equals(processo.inicio) &&
			termino.equals(processo.termino) &&
			codigoEnvio.equals(processo.codigoEnvio) &&
			path.equals(processo.path);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), mes, ano, inicio, termino, codigoEnvio, path);
	}

	@Override
	public String toString() {
		return "Processo [codigo=" + getCodigo() + ", unidade=" + unidade
			+ ", anexo=" + anexo + ", mes=" + mes + ", ano=" + ano
			+ ", status=" + status + ", inicio=" + inicio + ", termino="
			+ termino + ", codigoEnvio=" + codigoEnvio + ", eMes=" + eMes
			+ ", listaNotaFiscal=" + listaNotaFiscal
			+ ", listaProcessoAnexo=" + listaProcessoAnexo
			+ ", listaProcessoSituacao=" + listaProcessoSituacao + "]";
	}

	@Transient
	public int getTotalAnexos() {
		int i = 0;
		if (listaProcessoAnexo != null && !listaProcessoAnexo.isEmpty()) {
			i = listaProcessoAnexo.size();
		}
		return i;
	}

	@Transient
	public int getTotalNFs() {
		int i = 0;
		if (listaNotaFiscal != null && !listaNotaFiscal.isEmpty()) {
			i = listaNotaFiscal.size();
		}
		return i;
	}

	@Transient
	public int getTotalNFComErro() {
		int erros = 0;
		if (listaNotaFiscal != null && !listaNotaFiscal.isEmpty()) {
			for (NotaFiscal nf : listaNotaFiscal) {
				if (nf.notaFiscalComErro())
					erros++;
			}
		}
		return erros;
	}

	@Transient
	public int getTotalNFSemErro() {
		int total = 0;
		if (listaNotaFiscal != null && !listaNotaFiscal.isEmpty()) {
			for (NotaFiscal nf : listaNotaFiscal) {
				if (!nf.notaFiscalComErro())
					total++;
			}
		}
		return total;
	}

	@Transient
	public String getDescricaoErrosNF() {
		StringBuilder label = new StringBuilder();
		label.append(getTotalNFSemErro());
		label.append(" / ");
		label.append("<font style=\"color:red;\">");
		label.append(getTotalNFComErro());
		label.append("<font/>");
		return label.toString();
	}

	@Transient
	public Meses geteMes() {
		return eMes;
	}

	public void seteMes(Meses eMes) {
		this.eMes = eMes;
	}

	@Transient
	public String getCodigoExibicao() {
		if (getCodigo() != null) {
			return "EFD/" + getCodigo();
		}
		return null;
	}

	@Transient
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public enum StatusProcesso {
		ABERTO("Aberto"),
		PARA_VALIDACAO("Para validação de NFs"),
		EM_VALIDACAO("Em validação"),
		VALIDACAO_COM_ERRO("NFs validadas (com erro)"),
		LIBERADO_PROCESSAMENTO("Liberado para processamento"),
		A_PROCESSAR("A processar"),
		EM_PROCESSAMENTO("Em processamento"),
		PRECESSADO_COM_ERRO("Processado com erros"),
		ARQUIVO_GERADO("Arquivo gerado"),
		ENVIADO("Arquivo enviado à RFB");

		private final String descricao;

		private StatusProcesso(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}

}
