package br.com.araujoabreu.sgi.service.util;

import br.com.fasolti.commons.client.transfer.TransferObject;

import java.util.Objects;

public class BaseTO implements TransferObject {
	private Long id;

	public BaseTO() {
	}

	public BaseTO(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	//TODO getter e setter de "codigo" no lugar de "id" para não quebrar o código

	public Long getCodigo() {
		return id;
	}

	public void setCodigo(Long codigo) {
		this.id = codigo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BaseTO)) return false;
		BaseTO baseTO = (BaseTO) o;
		return Objects.equals(getId(), baseTO.getId());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getId());
	}

	@Override
	public String toString() {
		return "BaseTO{" +
			"id=" + id +
			'}';
	}
}
