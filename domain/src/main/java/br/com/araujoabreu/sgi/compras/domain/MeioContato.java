package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "MEIO_CONTATO")
@SequenceGenerator(name="SEQ_MEIO_CONTATO", sequenceName="SEQ_MEIO_CONTATO", allocationSize=1)
public class MeioContato extends AuditEntity {

	private static final long serialVersionUID = -4699532796987055482L;
	private Tipo tipo;
	private InformacaoContato informacaoContato;

	/**
	 * Construtor default
	 */
	public MeioContato() {
	}

	/**
	 * Construtor para um tipo de endereço
	 *
	 * @param tipo de endereço
	 */
	public MeioContato(Tipo tipo) {
		this.tipo = tipo;
	}

	@Column(name = "TIPO")
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CODIGO_INFORMACAO_CONTATO")
	public InformacaoContato getInformacaoContato() {
		return informacaoContato;
	}

	public void setInformacaoContato(InformacaoContato informacaoContato) {
		this.informacaoContato = informacaoContato;
	}

	@Transient
	public String getDescricaoContato() {
		StringBuilder builder = new StringBuilder();
		if (this.tipo.equals(Tipo.OUTRO)) {
			builder.append(informacaoContato.getEndereco().getEndereco()).append(" - ");
			builder.append(informacaoContato.getEndereco().getBairro()).append(" - ");
			builder.append(informacaoContato.getEndereco().getCidade()).append(" - ");
			builder.append(" A/C ").append(informacaoContato.getNome());

		}
		if (this.informacaoContato != null) {
			if (informacaoContato.getEndereco().getTipoLogradouro() != null) {
				builder.append(informacaoContato.getEndereco().getTipoLogradouro().getNome().trim());
				builder.append(" ");
			}


			builder.append(informacaoContato.getEndereco().getEndereco().trim());


			if (informacaoContato.getEndereco().getNumero() != null) {
				builder.append(" - ");
				builder.append(informacaoContato.getEndereco().getNumero().trim());
			}
			if (informacaoContato.getEndereco().getBairro() != null) {
				builder.append(" - ");
				builder.append(informacaoContato.getEndereco().getBairro().trim());
			}
			if (informacaoContato.getEndereco().getCidade() != null) {
				builder.append(" - ");
				builder.append(informacaoContato.getEndereco().getCidade().getNome().trim());
			}
			if (informacaoContato.getEndereco().getCep() != null) {
				builder.append(" - CEP: ").append(informacaoContato.getEndereco().getCep().trim());
			}
		} else {
			builder.append(this.tipo);
		}

		return builder.toString();
	}

	@Transient
	public String getDescricaoContatoPlanilhaPedidos() {
		StringBuilder builder = new StringBuilder();
		if (this.informacaoContato != null) {
			if (informacaoContato.getEndereco().getTipoLogradouro() != null) {
				builder.append(informacaoContato.getEndereco().getTipoLogradouro().getNome().trim());
				builder.append(" ");
			}


			builder.append(informacaoContato.getEndereco().getEndereco().trim());


			if (informacaoContato.getEndereco().getNumero() != null) {
				builder.append(" - ");
				builder.append(informacaoContato.getEndereco().getNumero().trim());
			}
		} else {
			builder.append(this.tipo);
		}

		return builder.toString();
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
		if (!(MeioContato.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		MeioContato other = (MeioContato) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	public enum Tipo {
		ARAUJOABREU, OBRA, OUTRO;

		@Override
		public String toString() {
			switch (ordinal()) {
				case 0:
					return "Endereço da gerência";
				case 1:
					return "Endereço principal do contrato";
				case 2:
					return "Outro (selecionar)";
				default:
					return "";
			}
		}
	}
}
