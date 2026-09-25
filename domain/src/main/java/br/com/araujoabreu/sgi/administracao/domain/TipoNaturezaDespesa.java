package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.interf.Dominio;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TIPO_NATUREZA_DESPESA")
@SequenceGenerator(name="SEQ_TIPO_NATUREZA_DESPESA", sequenceName="SEQ_TIPO_NATUREZA_DESPESA", allocationSize=1)
public class TipoNaturezaDespesa extends AuditEntity implements Dominio {

	private static final long serialVersionUID = 6965114982811092422L;
	private String nome;
	private String chave;

	public TipoNaturezaDespesa() {
		super();
	}

	public TipoNaturezaDespesa(Long codigo) {
		setCodigo(codigo);
	}

	public TipoNaturezaDespesa(final Long codigo, final String nome) {
		setCodigo(codigo);
		this.nome = nome;
	}

	@Override
	@Column(name = "NOME")
	@NotNull
	public String getNome() {
		return nome;
	}

	@Override
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "CHAVE")
	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		if (!(TipoNaturezaDespesa.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		TipoNaturezaDespesa other = (TipoNaturezaDespesa) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (nome == null) {
			if (other.getNome() != null) {
				return false;
			}
		} else if (!nome.equals(other.getNome())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "TipoNaturezaDespesa [codigo=" + getCodigo() + ", nome=" + nome + "]";
	}

}
