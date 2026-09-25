package br.com.fasolti.corporativo.pessoa.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ENDERECO_HORARIO_FUNCIONAMENTO")
@SequenceGenerator(name = "SEQ_ENDERECO_HORARIO_FUNCIONAMENTO", sequenceName = "SEQ_ENDERECO_HORARIO_FUNCIONAMENTO", allocationSize = 1)
public class EnderecoHorario extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = -4003152617590031830L;
	private Endereco endereco;
	private Integer diaSemana;
	private String horarioInicio;
	private String horarioTermino;
	private String almocoInicio;
	private String almocoTermino;

	@ManyToOne
	@JoinColumn(name = "CODIGO_ENDERECO")
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Column(name = "DIA_SEMANA")
	public Integer getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(Integer diaSemana) {
		this.diaSemana = diaSemana;
	}

	@Column(name = "HORARIO_INICIO")
	public String getHorarioInicio() {
		return horarioInicio;
	}

	public void setHorarioInicio(String horarioInicio) {
		this.horarioInicio = horarioInicio;
	}

	@Column(name = "HORARIO_TERMINO")
	public String getHorarioTermino() {
		return horarioTermino;
	}

	public void setHorarioTermino(String horarioTermino) {
		this.horarioTermino = horarioTermino;
	}

	@Column(name = "ALMOCO_INICIO")
	public String getAlmocoInicio() {
		return almocoInicio;
	}

	public void setAlmocoInicio(String almocoInicio) {
		this.almocoInicio = almocoInicio;
	}

	@Column(name = "ALMOCO_TERMINO")
	public String getAlmocoTermino() {
		return almocoTermino;
	}

	public void setAlmocoTermino(String almocoTermino) {
		this.almocoTermino = almocoTermino;
	}

}
