package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ALIQUOTA_AGREGADA")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name="SEQ_ALIQUOTA_AGREGADA", sequenceName="SEQ_ALIQUOTA_AGREGADA", allocationSize=1)
public class AliquotaAgregada extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5947342125231237404L;
	private String nome;
	private BigDecimal percentual;
	private Aliquota aliquota;

	private List<ContaContabil> listaContaContabil = new ArrayList<>();

	@Column(name = "NOME")
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "PERCENTUAL", precision = 5, scale = 2)
	@NotNull
	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_ALIQUOTA")
	@NotNull
	public Aliquota getAliquota() {
		return aliquota;
	}

	public void setAliquota(Aliquota aliquota) {
		this.aliquota = aliquota;
	}

	@ManyToMany(targetEntity = ContaContabil.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
		name = "ALIQUOTA_AGREGADA_CONTA_CONTABIL",
		joinColumns = @JoinColumn(name = "CODIGO_ALIQUOTA_AGREGADA"),
		inverseJoinColumns = @JoinColumn(name = "CODIGO_CONTA_CONTABIL")
	)
	public List<ContaContabil> getListaContaContabil() {
		return listaContaContabil;
	}

	public void setListaContaContabil(List<ContaContabil> listaContaContabil) {
		this.listaContaContabil = listaContaContabil;
	}

	@Transient
	public ContaContabil getContaContabilPorEmpresa(Unidade empresa) {
		for (ContaContabil contaContabil : this.listaContaContabil) {
			if (contaContabil.getEmpresa().equals(empresa))
				return contaContabil;
		}

		return null;
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

		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		if (!(obj instanceof AliquotaAgregada)) {
			return false;
		}

		AliquotaAgregada other = (AliquotaAgregada) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

}
