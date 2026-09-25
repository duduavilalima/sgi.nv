package br.com.fasolti.corporativo.auditoria.domain;

import br.com.fasolti.commons.domain.AuditedObject;
import br.com.fasolti.framework.Constants;
import br.com.fasolti.framework.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class AuditEntity extends BaseEntity implements Serializable, AuditedObject {

	private static final long serialVersionUID = 7779462927582343105L;

	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

	@Column(name = "CRIADOR", updatable = false)
	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CRIACAO", updatable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Column(name = "ATUALIZADOR", insertable = false)
	public String getAtualizador() {
		return atualizador;
	}

	public void setAtualizador(String atualizador) {
		this.atualizador = atualizador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_ATUALIZACAO", insertable = false)
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@Transient
	@Override
	public String getCreatedBy() {
		return getCriador();
	}

	@Override
	public void setCreatedBy(String name) {
		setCriador(name);
	}

	@Transient
	@Override
	public Date getCreationDate() {
		return getDataCriacao();
	}

	@Override
	public void setCreationDate(Date date) {
		setDataCriacao(date);
	}

	@Transient
	@Override
	public String getModifiedBy() {
		return getAtualizador();
	}

	@Override
	public void setModifiedBy(String name) {
		setAtualizador(name);
	}

	@Transient
	@Override
	public Date getModificationDate() {
		return getDataAtualizacao();
	}

	@Override
	public void setModificationDate(Date date) {
		setDataAtualizacao(date);
	}
}
