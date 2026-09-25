package br.com.araujoabreu.sgi.contasApagar.domain;

import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Alexandre Neves
 */
@Entity
@Table(name = "PAGAMENTO_CONTA_CONTABIL")
@SequenceGenerator(name="SEQ_PAGAMENTO_CONTA_CONTABIL", sequenceName="SEQ_PAGAMENTO_CONTA_CONTABIL", allocationSize=1)
public class PagamentoContaContabil extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5728375998272126834L;
	private Pagamento pagamento;
	private ContaContabil contaContabil;
	private Tipo tipo;

	public PagamentoContaContabil() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PAGAMENTO")
	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CONTA_CONTABIL")
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TIPO")
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public enum Tipo {

		MULTAS("Multas"),
		JUROS("Juros"),
		DESCONTOS("Descontos concedidos");

		private final String descricao;

		private Tipo(String descricao) {
			this.descricao = descricao;
		}

		public static Tipo getTipoPorNome(String nome) {
			if (nome != null) {
				for (Tipo tipo : Tipo.values()) {
					if (tipo.getDescricao().trim().equalsIgnoreCase(nome.trim())) {
						return tipo;
					}
				}
			}

			return null;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return descricao;
		}
	}

}
