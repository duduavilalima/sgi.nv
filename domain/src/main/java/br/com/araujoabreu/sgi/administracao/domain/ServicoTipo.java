package br.com.araujoabreu.sgi.administracao.domain;


import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "SERVICO_TIPO")
@SequenceGenerator(name="SEQ_SERVICO_TIPO", sequenceName="SEQ_SERVICO_TIPO", allocationSize=1)
public class ServicoTipo extends AuditEntity implements DomainObject, Serializable {

	private static final long serialVersionUID = 7755515382313799892L;
	public static final String NOME_TF_NAO_HABILITADO = "Não habilitado";
	public static final String NOME_TF_HABILITADO = "Habilitado";
	public static final String NOME_TF_HABILITADO_CIDADE = "Habilitado com cidade";
	public static final String DESCRICAO_TF_NAO_HABILITADO = "Não habilitado para o faturamento";
	public static final String DESCRICAO_TF_HABILITADO = "Habilitado para o faturamento";
	public static final String DESCRICAO_TF_HABILITADO_CIDADE = "Habilitado para o faturamento E permite seleção da cidade da matriz do contrato como local de prestação dos serviços";
	public static final String NOME_SITUACAO_ATIVO = "Ativo";
	public static final String NOME_SITUACAO_INATIVO = "Inativo";
	private ServicoTipo servicoTipoPai;
	private String sigla;
	private String nome;
	private Situacao situacao;
	private TipoFaturamento tipoFaturamento;
	private ReinfServico reinfServico;
	private String faturamentoCodigoObra;

	@Transient
	public static ServicoTipo.TipoFaturamento getTipoFaturamentoByNome(String nome) {
		ServicoTipo.TipoFaturamento tipo = null;
		String nomePesquisa = new String(nome);

		switch (nomePesquisa) {
			case NOME_TF_NAO_HABILITADO:
				tipo = ServicoTipo.TipoFaturamento.NAO_HABILITADO;
				break;
			case NOME_TF_HABILITADO:
				tipo = ServicoTipo.TipoFaturamento.HABILITADO;
				break;
			case NOME_TF_HABILITADO_CIDADE:
				tipo = ServicoTipo.TipoFaturamento.HABILITADO_CIDADE;
				break;
			default:
				tipo = null;
				break;
		}

		return tipo;
	}

	@Transient
	public static ServicoTipo.Situacao getSituacaoByNome(String nome) {
		ServicoTipo.Situacao situacao = null;
		String nomePesquisa = new String(nome);

		switch (nomePesquisa) {
			case ServicoTipo.NOME_SITUACAO_ATIVO:
				situacao = Situacao.ATIVO;
				break;
			case ServicoTipo.NOME_SITUACAO_INATIVO:
				situacao = Situacao.INATIVO;
				break;
			default:
				situacao = null;
				break;
		}

		return situacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_pai")
	public ServicoTipo getServicoTipoPai() {
		return servicoTipoPai;
	}

	public void setServicoTipoPai(ServicoTipo servicoTipoPai) {
		this.servicoTipoPai = servicoTipoPai;
	}

	@Column(length = 30)
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Column(length = 1000)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(nullable = false)
	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Column(name = "tipo_faturamento", nullable = false)
	public TipoFaturamento getTipoFaturamento() {
		return tipoFaturamento;
	}

	public void setTipoFaturamento(TipoFaturamento tipoFaturamento) {
		this.tipoFaturamento = tipoFaturamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_reinf")
	public ReinfServico getReinfServico() {
		return reinfServico;
	}

	public void setReinfServico(ReinfServico reinfServico) {
		this.reinfServico = reinfServico;
	}

	@Column(name = "faturamento_codigo_obra")
	public String getFaturamentoCodigoObra() {
		return faturamentoCodigoObra;
	}

	public void setFaturamentoCodigoObra(String faturamentoCodigoObra) {
		this.faturamentoCodigoObra = faturamentoCodigoObra;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ServicoTipo)) return false;
		if (!super.equals(o)) return false;
		ServicoTipo that = (ServicoTipo) o;
		return Objects.equals(getSigla(), that.getSigla()) &&
			Objects.equals(getNome(), that.getNome()) &&
			getSituacao() == that.getSituacao();
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getSigla(), getNome(), getSituacao());
	}

	public enum Situacao {
		INATIVO(NOME_SITUACAO_INATIVO),
		ATIVO(NOME_SITUACAO_ATIVO);

		private final String descricao;

		private Situacao(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return getDescricao();
		}
	}

	public enum TipoFaturamento {
		NAO_HABILITADO(DESCRICAO_TF_NAO_HABILITADO, NOME_TF_NAO_HABILITADO),
		HABILITADO(DESCRICAO_TF_HABILITADO, NOME_TF_HABILITADO),
		HABILITADO_CIDADE(DESCRICAO_TF_HABILITADO_CIDADE, NOME_TF_HABILITADO_CIDADE);

		private final String descricao;
		private final String nome;

		TipoFaturamento(String descricao, String nome) {
			this.descricao = descricao;
			this.nome = nome;
		}

		public String getDescricao() {
			return descricao;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return getNome();
		}

	}
}
