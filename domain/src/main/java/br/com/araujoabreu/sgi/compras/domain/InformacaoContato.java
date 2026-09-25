package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "INFORMACAO_CONTATO")
@SequenceGenerator(name="SEQ_INFORMACAO_CONTATO", sequenceName="SEQ_INFORMACAO_CONTATO", allocationSize=1)
public class InformacaoContato extends AuditEntity {

	private static final long serialVersionUID = 3541479233118660569L;
	private String nome;
	private String email;
	private String telefone;
	private Endereco endereco;

	@Column(name = "NOME")
	@Length(max = 50)
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "EMAIL")
	@Length(max = 100)
	@NotNull
	@Email
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "TELEFONE")
	@Length(max = 9)
	@NotNull
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CODIGO_ENDERECO")
	@NotNull
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof InformacaoContato)) return false;
		if (!super.equals(o)) return false;
		InformacaoContato that = (InformacaoContato) o;
		return Objects.equals(getNome(), that.getNome()) &&
			Objects.equals(getEmail(), that.getEmail()) &&
			Objects.equals(getTelefone(), that.getTelefone());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome(), getEmail(), getTelefone());
	}
}
