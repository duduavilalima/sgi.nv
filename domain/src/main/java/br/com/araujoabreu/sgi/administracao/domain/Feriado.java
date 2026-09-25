package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Calendar;


/**
 * The persistent class for the feriado database table.
 */
@Entity
@Table(name = "feriado")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name="SEQ_FERIADO", sequenceName="SEQ_FERIADO")
public class Feriado extends AuditEntity implements DomainObject, Serializable {

	private static final long serialVersionUID = 4704022113754095753L;
	private Integer ano;
	private String descricao;
	private Integer dia;
	private Integer mes;

	@Column(name = "ANO")
	public Integer getAno() {
		return this.ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	@Column(name = "DESCRICAO", nullable = false)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "DIA", nullable = false)
	public Integer getDia() {
		return this.dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	@Column(name = "MES", nullable = false)
	public Integer getMes() {
		return this.mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	@Transient
	public String getDescricaoMes() {
		if (mes != null) {
			for (Meses mesAux : Meses.values()) {
				if (mesAux.getNumeroMesFormatado() == mes) {
					return mesAux.getNome();
				}
			}
		}
		return null;
	}

	@Transient
	public String getDiaFormatado() {
		if (dia != null) {
			if (dia < 10) {
				return "0".concat(dia.toString());
			}
			return dia.toString();
		} else {
			return "";
		}
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
		if (!(Feriado.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Feriado other = (Feriado) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	public enum Meses {

		Jan(Calendar.JANUARY, "Janeiro"),
		Fev(Calendar.FEBRUARY, "Fevereiro"),
		Mar(Calendar.MARCH, "Março"),
		Abr(Calendar.APRIL, "Abril"),
		Mai(Calendar.MAY, "Maio"),
		Jun(Calendar.JUNE, "Junho"),
		Jul(Calendar.JULY, "Julho"),
		Ago(Calendar.AUGUST, "Agosto"),
		Set(Calendar.SEPTEMBER, "Setembro"),
		Out(Calendar.OCTOBER, "Outubro"),
		Nov(Calendar.NOVEMBER, "Novembro"),
		Dez(Calendar.DECEMBER, "Dezembro");

		private final int numero;
		private final String nome;

		private Meses(int numero, String nome) {
			this.numero = numero;
			this.nome = nome;
		}

		public static Meses getMes(int numero) {
			for (Meses mes : Meses.values()) {
				if (mes.getNumero() == numero)
					return mes;
			}
			return null;
		}

		public static Meses getMes(String nome) {
			for (Meses mes : Meses.values()) {
				nome = nome.replace("&ccedil;", "ç");
				if (mes.getNome().trim().equalsIgnoreCase(nome.trim()))
					return mes;
			}
			return null;
		}

		public int getNumero() {
			return numero;
		}

		public int getNumeroMesFormatado() {
			return numero + 1;
		}

		public String getNome() {
			return nome;
		}

	}

}
