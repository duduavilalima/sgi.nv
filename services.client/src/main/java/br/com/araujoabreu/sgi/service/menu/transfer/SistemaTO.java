package br.com.araujoabreu.sgi.service.menu.transfer;

import java.util.Objects;

public class SistemaTO {

	private Long codigo;
	private String nome;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SistemaTO)) return false;
		SistemaTO sistemaTO = (SistemaTO) o;
		return Objects.equals(getCodigo(), sistemaTO.getCodigo()) &&
			Objects.equals(getNome(), sistemaTO.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigo(), getNome());
	}

	@Override
	public String toString() {
		return "SistemaTO{" +
			"codigo=" + codigo +
			", nome='" + nome + '\'' +
			'}';
	}
}
