package br.com.araujoabreu.sgi.inrf.domain;

import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "CONTABILISTA")
public class Contabilista extends PessoaFisica {

	private static final long serialVersionUID = -1475195802887853031L;

	@Column(name = "CRC", nullable = false, length = 20, unique = true)
	@NotNull
	@Length(max = 20)
	private String crc;

	public Contabilista() {
	}

	public String getCrc() {
		return crc;
	}

	public void setCrc(String crc) {
		this.crc = crc;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Contabilista that = (Contabilista) o;
		return crc.equals(that.crc);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), crc);
	}
}
