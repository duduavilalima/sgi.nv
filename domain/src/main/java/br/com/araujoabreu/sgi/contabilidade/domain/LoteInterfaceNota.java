package br.com.araujoabreu.sgi.contabilidade.domain;

import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "LOTE_INTERFACE_NOTA")
@SequenceGenerator(name = "SEQ_LOTE_INTERFACE_NOTA", sequenceName = "SEQ_LOTE_INTERFACE_NOTA", allocationSize = 1)
public class LoteInterfaceNota extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -602808390391568275L;
	private LoteInterface loteInterface;
	private Nota nota;
	private Tipo tipo;
	private Integer ordem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_lote_interface", nullable = false)
	public LoteInterface getLoteInterface() {
		return loteInterface;
	}

	public void setLoteInterface(LoteInterface loteInterface) {
		this.loteInterface = loteInterface;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_nota", nullable = false)
	public Nota getNota() {
		return nota;
	}

	public void setNota(Nota nota) {
		this.nota = nota;
	}

	@Column(nullable = false)
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		LoteInterfaceNota that = (LoteInterfaceNota) o;

		if (!getLoteInterface().equals(that.getLoteInterface())) return false;
		if (!getNota().equals(that.getNota())) return false;
		return getTipo() == that.getTipo();

	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + getLoteInterface().hashCode();
		result = 31 * result + getNota().hashCode();
		result = 31 * result + getTipo().hashCode();
		return result;
	}

	public enum Tipo {
		ENVIADA("Enviada", "Enviada"),
		CANCELADA("Cancelada", "Cancelada"),
		PORTAL_OI("Portal Oi", "Portal Oi"),
		BOLETO_ITAU("Boleto (Itaú)", "Boleto (Itaú)");

		private final String descricao;
		private final String nome;

		Tipo(String descricao, String nome) {
			this.descricao = descricao;
			this.nome = nome;
		}

		public String getDescricao() {
			return descricao;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return getNome();
		}
	}
}
