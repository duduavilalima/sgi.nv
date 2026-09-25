package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "REGIONAL_GERENCIA")
@SequenceGenerator(name="SEQ_REGIONAL_GERENCIA", sequenceName="SEQ_REGIONAL_GERENCIA", allocationSize=1)
public class RegionalGerencia extends BaseEntity {

	private static final long serialVersionUID = -841233286258955017L;
	private Regional regional;
	private Gerencia gerencia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_regional", nullable = false)
	public Regional getRegional() {
		return regional;
	}

	public void setRegional(Regional regional) {
		this.regional = regional;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_gerencia", nullable = false)
	public Gerencia getGerencia() {
		return gerencia;
	}

	public void setGerencia(Gerencia gerencia) {
		this.gerencia = gerencia;
	}
}
