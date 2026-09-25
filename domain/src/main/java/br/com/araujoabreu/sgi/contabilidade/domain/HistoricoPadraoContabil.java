package br.com.araujoabreu.sgi.contabilidade.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "HISTORICO_PADRAO_CONTABIL")
@SequenceGenerator(name = "SEQ_HISTORICO_PADRAO_CONTABIL", sequenceName = "SEQ_HISTORICO_PADRAO_CONTABIL", allocationSize = 1)
public class HistoricoPadraoContabil extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2437001696016453727L;
	@Column(name = "NUMERO")
	@NotNull
	private Integer numero;

	@Column(name = "DESCRICAO")
	@NotNull
	private String descricao;

	@Column(name = "TIPO_ESPECIAL")
	private Integer tipoEspecial;

	public HistoricoPadraoContabil() {
	}

	public HistoricoPadraoContabil(Long codigo) {
		setCodigo(codigo);
	}

	public HistoricoPadraoContabil(long codigo) {
		setCodigo(codigo);
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getTipoEspecial() {
		return tipoEspecial;
	}

	public void setTipoEspecial(Integer tipoEspecial) {
		this.tipoEspecial = tipoEspecial;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((tipoEspecial == null) ? 0 : tipoEspecial.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		HistoricoPadraoContabil other = (HistoricoPadraoContabil) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (getDescricao() == null) {
			if (other.getDescricao() != null) {
				return false;
			}
		} else if (!getDescricao().equals(other.getDescricao())) {
			return false;
		}
		if (getNumero() == null) {
			if (other.getNumero() != null) {
				return false;
			}
		} else if (!getNumero().equals(other.getNumero())) {
			return false;
		}
		if (getTipoEspecial() == null) {
			if (other.getTipoEspecial() != null) {
				return false;
			}
		} else if (!getTipoEspecial().equals(other.getTipoEspecial())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "HistoricoPadraoContabil [codigo=" + getCodigo() + ", numero=" + numero + ", descricao=" + descricao + ", tipoEspecial=" + tipoEspecial + "]";
	}

}
