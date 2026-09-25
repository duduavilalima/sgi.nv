package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "FAIXA_AVALIACAO")
@SequenceGenerator(name = "SEQ_FAIXA_AVALIACAO", sequenceName = "SEQ_FAIXA_AVALIACAO", allocationSize = 1)
public class FaixaAvaliacao extends AuditEntity implements Serializable {

	private static final long serialVersionUID = 6029766885040625503L;
	private Double faixaInicial;
	private Double faixaFinal;
	private String descricao;
	private String rgb;
	private String legenda;

	@Column(name = "FAIXA_INICIAL", nullable = false)
	@NotNull
	public Double getFaixaInicial() {
		return faixaInicial;
	}

	public void setFaixaInicial(Double faixaInicial) {
		this.faixaInicial = faixaInicial;
	}

	@Column(name = "FAIXA_FINAL", nullable = false)
	@NotNull
	public Double getFaixaFinal() {
		return faixaFinal;
	}

	public void setFaixaFinal(Double faixaFinal) {
		this.faixaFinal = faixaFinal;
	}

	@Column(name = "DESCRICAO", nullable = false)
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "RGB", nullable = false)
	@NotNull
	public String getRgb() {
		return rgb;
	}

	public void setRgb(String rgb) {
		this.rgb = rgb;
	}

	@Column(name = "LEGENDA", nullable = false)
	@NotNull
	public String getLegenda() {
		return legenda;
	}

	public void setLegenda(String legenda) {
		this.legenda = legenda;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((faixaFinal == null) ? 0 : faixaFinal.hashCode());
		result = prime * result + ((faixaInicial == null) ? 0 : faixaInicial.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}
		if (!(obj instanceof FaixaAvaliacao)) {
			return false;
		}
		FaixaAvaliacao other = (FaixaAvaliacao) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (descricao == null) {
			if (other.descricao != null) {
				return false;
			}
		} else if (!descricao.equals(other.descricao)) {
			return false;
		}
		return true;
	}

}
