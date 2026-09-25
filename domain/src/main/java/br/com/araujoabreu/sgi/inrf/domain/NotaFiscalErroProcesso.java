package br.com.araujoabreu.sgi.inrf.domain;

import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "NOTA_FISCAL_ERRO_PROCESSO")
@SequenceGenerator(name="SEQ_NOTA_FISCAL_ERRO_PROCESSO", sequenceName="SEQ_NOTA_FISCAL_ERRO_PROCESSO", allocationSize=1)
public class NotaFiscalErroProcesso extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 5486993751082072509L;
	private NotaFiscal notaFiscal;
	private TipoErro tipoErro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_NOTA")
	@NotNull
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	@Column(name = "ERRO")
	@NotNull
	public TipoErro getTipoErro() {
		return tipoErro;
	}

	public void setTipoErro(TipoErro tipoErro) {
		this.tipoErro = tipoErro;
	}

	public enum TipoErro {
		NF_INVALIDA("Situação da NF inválida"),
		ITEM_NAO_CLASSIFICADO("Item não classificado");

		private final String descricao;

		private TipoErro(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}


}
