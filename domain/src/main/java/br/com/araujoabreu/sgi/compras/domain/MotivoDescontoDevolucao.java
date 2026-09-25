package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "MOTIVO_DESCONTO_DEVOLUCAO")
@SequenceGenerator(name="SEQ_MOTIVO_DESCONTO_DEVOLUCAO", sequenceName="SEQ_MOTIVO_DESCONTO_DEVOLUCAO", allocationSize=1)
public class MotivoDescontoDevolucao extends BaseEntity {

	private static final long serialVersionUID = -1162581696920874617L;
	private String descricao;

	@Column(name = "DESCRICAO", length = 50)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		MotivoDescontoDevolucao that = (MotivoDescontoDevolucao) o;
		return Objects.equals(getDescricao(), that.getDescricao());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getDescricao());
	}
}
