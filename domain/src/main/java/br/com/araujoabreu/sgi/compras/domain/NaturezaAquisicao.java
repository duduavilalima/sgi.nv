package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "NATUREZA_AQUISICAO")
@SequenceGenerator(name="SEQ_NATUREZA_AQUISICAO", sequenceName="SEQ_NATUREZA_AQUISICAO", allocationSize=1)
public class NaturezaAquisicao extends BaseEntity {

	private static final long serialVersionUID = 8858809569921662933L;
	private String nome;
	private String contaContabil;
	private BaseCalculoCredito baseCalculoCredito;
	private TipoAquisicao tipo;

	@Column(name = "NOME")
	@Length(max = 50)
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "CONTA_CONTABIL")
	@Length(max = 50)
	@NotNull
	public String getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(String contaContabil) {
		this.contaContabil = contaContabil;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_BASE_CALCULO_CREDITO")
	@NotNull
	public BaseCalculoCredito getBaseCalculoCredito() {
		return baseCalculoCredito;
	}

	public void setBaseCalculoCredito(BaseCalculoCredito baseCalculoCredito) {
		this.baseCalculoCredito = baseCalculoCredito;
	}

	@Enumerated(EnumType.ORDINAL)
	@JoinColumn(name = "tipo")
	public TipoAquisicao getTipo() {
		return tipo;
	}

	public void setTipo(TipoAquisicao tipo) {
		this.tipo = tipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
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
		if (!(NaturezaAquisicao.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		NaturezaAquisicao other = (NaturezaAquisicao) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	public enum TipoAquisicao {
		MATERIAIS, SERVICOS
	}

}
