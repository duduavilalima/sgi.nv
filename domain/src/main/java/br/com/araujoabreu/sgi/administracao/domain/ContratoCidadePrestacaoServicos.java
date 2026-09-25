package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CONTRATO_CIDADE_PRESTACAO_SERVICOS")
@SequenceGenerator(name="SEQ_CONTRATO_CIDADE_PRESTACAO_SERVICOS", sequenceName="SEQ_CONTRATO_CIDADE_PRESTACAO_SERVICOS", allocationSize = 1)
public class ContratoCidadePrestacaoServicos extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 507914371074095218L;
	private Contrato contrato;
	private Cidade cidade;

	public ContratoCidadePrestacaoServicos() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_contrato", nullable = false)
	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_municipio_ect", nullable = false)
	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
}
