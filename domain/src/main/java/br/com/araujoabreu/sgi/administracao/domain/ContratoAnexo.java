package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.administracao.enums.TipoAnexo;
import br.com.araujoabreu.sgi.comercial.domain.Aditivo;
import br.com.araujoabreu.sgi.compras.domain.Anexo;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "contrato_anexo")
@SequenceGenerator(	name = "SEQ_CONTRATO_ANEXO",	sequenceName = "SEQ_CONTRATO_ANEXO",	allocationSize = 1)
public class ContratoAnexo extends BaseEntity {

	private static final long serialVersionUID = -7777564084562091005L;
	private Contrato contrato;
	private Aditivo aditivo;
	private Anexo anexo;
	private Integer ordem;
	private TipoAnexo tipo;

	private Long codigoAditivo;

	public ContratoAnexo() {
	}

	public ContratoAnexo(Contrato contrato, Anexo anexo, Integer ordem, TipoAnexo tipo) {
		this.contrato = contrato;
		this.anexo = anexo;
		this.ordem = ordem;
		this.tipo = tipo;
	}

	public ContratoAnexo(Contrato contrato, Aditivo aditivo, Anexo anexo, Integer ordem, TipoAnexo tipo) {
		this.contrato = contrato;
		this.aditivo = aditivo;
		this.anexo = anexo;
		this.ordem = ordem;
		this.tipo = tipo;
	}

	public ContratoAnexo(Anexo anexo, Contrato contrato, Aditivo aditivo) {
		this.anexo = anexo;
		this.contrato = contrato;
		this.aditivo = aditivo;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_contrato", nullable = false)
	@NotNull
	public Contrato getContrato() {
		return this.contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_aditivo")
	public Aditivo getAditivo() {
		return this.aditivo;
	}

	public void setAditivo(Aditivo aditivo) {
		this.aditivo = aditivo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_anexo", nullable = false)
	@NotNull
	public Anexo getAnexo() {
		return this.anexo;
	}

	public void setAnexo(Anexo anexo) {
		this.anexo = anexo;
	}

	@Column(name = "ordem")
	public Integer getOrdem() {
		return this.ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Column(name = "tipo", nullable = false)
	public TipoAnexo getTipo() {
		return this.tipo;
	}

	public void setTipo(TipoAnexo tipo) {
		this.tipo = tipo;
	}

	@Transient
	public Long getCodigoAditivo() {
		if (this.aditivo == null) {
			return codigoAditivo;
		} else {
			codigoAditivo = aditivo.getCodigo();
			return codigoAditivo;
		}
	}

	public void setCodigoAditivo(Long codigoAditivo) {
		if (this.aditivo == null) {
			aditivo = new Aditivo(codigoAditivo);
		}
		this.codigoAditivo = codigoAditivo;
	}
}
