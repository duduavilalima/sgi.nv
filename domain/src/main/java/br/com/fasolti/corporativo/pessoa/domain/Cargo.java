package br.com.fasolti.corporativo.pessoa.domain;

import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.interf.Dominio;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CARGO")
@SequenceGenerator(name = "SEQ_CARGO", sequenceName = "SEQ_CARGO", allocationSize = 1)
public class Cargo extends AuditEntity implements Dominio {

	private static final long serialVersionUID = 1L;

	private Long codigo = 0L;
	private String nome;

	private List<Contato> listaContato;

	public Cargo() {
	}

	public Cargo(Long codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CARGO")
	@Id
	@Column(name = "CODIGO", nullable = false)
	@NotNull
	public Long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Column(name = "NOME", nullable = false, length = 50)
	@NotNull
	@Length(max = 50)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cargo")
	public List<Contato> getListaContato() {
		return listaContato;
	}

	public void setListaContato(List<Contato> listaContato) {
		this.listaContato = listaContato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}

		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		if (!(obj instanceof Cargo)) {
			return false;
		}

		Cargo other = (Cargo) obj;

		if (codigo == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!codigo.equals(other.getCodigo())) {
			return false;
		}

		return true;
	}

}
