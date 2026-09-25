package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.araujoabreu.sgi.enumerators.Situacao;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "HISTORICO_CONCILIACAO")
@SequenceGenerator(name = "SEQ_HISTORICO_CONCILIACAO", sequenceName = "SEQ_HISTORICO_CONCILIACAO", allocationSize = 1)
public class HistoricoConciliacao extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -4643311296610601056L;

	private String descricao;
	private Situacao situacao;
	private TipoAcao tipoAcao;

	public HistoricoConciliacao() {
	}

	@Column(name = "DESCRICAO", nullable = false)
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "SITUACAO")
	@Enumerated(EnumType.ORDINAL)
	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Column(name = "TIPO_ACAO")
	@Enumerated(EnumType.ORDINAL)
	public TipoAcao getTipoAcao() {
		return tipoAcao;
	}

	public void setTipoAcao(TipoAcao tipoAcao) {
		this.tipoAcao = tipoAcao;
	}

	public enum TipoAcao {

		NENHUMA_ACAO("Nenhuma ação"),
		LANCAMENTO_TESOURARIA("Lançamento de tesouraria"),
		PAGAMENTO("Pagamento"),
		PAGAMENTO_CHEQUE("Pagamento (cheque)"),
		NF_SAIDA_FATURAMENTO("NF/Saída (faturamento)"),
		OUTROS_RECEBIMENTOS("Outros recebimentos");

		private final String nome;

		TipoAcao(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return nome;
		}
	}
}
