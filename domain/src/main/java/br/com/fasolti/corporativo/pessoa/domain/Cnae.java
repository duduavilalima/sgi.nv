package br.com.fasolti.corporativo.pessoa.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Classificação Nacional de Atividades Econômicas.<br>
 * <a href="http://www.receita.fazenda.gov.br/pessoajuridica/cnaefiscal/cnaef.htm">Classificação Nacional de Atividades Econômicas / CNAE</a>
 */
@Entity
@Table(name = "CNAE")
public class Cnae extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 6446378214530198538L;
	private String sigla;
	private String descricao;
	private Boolean selecionavel = Boolean.TRUE;

	private Cnae cnaePai;

	@Column(name = "SIGLA", nullable = false)
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Column(name = "DESCRICAO", nullable = false)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "SELECIONAVEL", nullable = false)
	public Boolean getSelecionavel() {
		return selecionavel;
	}

	public void setSelecionavel(Boolean selecionavel) {
		this.selecionavel = selecionavel;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_PAI")
	public Cnae getCnaePai() {
		return cnaePai;
	}

	public void setCnaePai(Cnae cnaePai) {
		this.cnaePai = cnaePai;
	}

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cnaePai")
//    @OrderBy(value = "sigla")
//	public List<Cnae> getCnaeFilhos() {
//		return cnaeFilhos;
//	}
//
//	public void setCnaeFilhos(List<Cnae> cnaeFilhos) {
//		this.cnaeFilhos = cnaeFilhos;
//	}
}
