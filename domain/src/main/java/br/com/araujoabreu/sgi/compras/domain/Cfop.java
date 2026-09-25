package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "CFOP")
public class Cfop extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7677775191481797201L;
	private String descricao;
	private Boolean permitirNFSaida;
	private Boolean permitirNFEntrada;

	public Cfop() {
		super();
	}

	public Cfop(Long codigo) {
		setCodigo(codigo);
	}

	public Cfop(Long codigo, String descricao, Boolean permitirNFSaida, Boolean permitirNFEntrada) {
		super();
		setCodigo(codigo);
		this.descricao = descricao;
		this.permitirNFSaida = permitirNFSaida;
		this.permitirNFEntrada = permitirNFEntrada;
	}

	public Cfop(Long codigo, String descricao) {
		super();
		setCodigo(codigo);
		this.descricao = descricao;
	}

	@Column(name = "DESCRICAO", nullable = false)
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "PERMITIR_NF_SAIDA", nullable = false)
	@NotNull
	public Boolean getPermitirNFSaida() {
		return permitirNFSaida;
	}

	public void setPermitirNFSaida(Boolean permitirNFSaida) {
		this.permitirNFSaida = permitirNFSaida;
	}

	@Column(name = "PERMITIR_NF_ENTRADA", nullable = false)
	@NotNull
	public Boolean getPermitirNFEntrada() {
		return permitirNFEntrada;
	}

	public void setPermitirNFEntrada(Boolean permitirNFEntrada) {
		this.permitirNFEntrada = permitirNFEntrada;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Cfop)) return false;
		if (!super.equals(o)) return false;
		Cfop cfop = (Cfop) o;
		return Objects.equals(getDescricao(), cfop.getDescricao()) &&
			Objects.equals(getPermitirNFSaida(), cfop.getPermitirNFSaida()) &&
			Objects.equals(getPermitirNFEntrada(), cfop.getPermitirNFEntrada());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getDescricao(), getPermitirNFSaida(), getPermitirNFEntrada());
	}
}
