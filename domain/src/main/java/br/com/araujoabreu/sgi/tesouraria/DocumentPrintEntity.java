package br.com.araujoabreu.sgi.tesouraria;

import br.com.fasolti.framework.domain.BaseEntity;
import br.com.fasolti.sgi.repository.tesouraria.enumerations.PrintStatus;
import br.com.fasolti.sgi.repository.tesouraria.enumerations.TiposImpressao;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "documentos_impressao")
public class DocumentPrintEntity extends BaseEntity implements DocumentPrint {

	private static final long serialVersionUID = -600898366319137793L;
	private String textoImpressao;
	private PrintStatus status;
	private TiposImpressao tipoImpressao;
	private Date dataCriacao;
	private Date dataImpressao;
	private String criador;

	public DocumentPrintEntity() {
		super();
	}

	public DocumentPrintEntity(final Long codigo) {
		super();
		setCodigo(codigo);
	}

	public DocumentPrintEntity(final Long codigo, final String impressao, final PrintStatus status,
							   final TiposImpressao tipoImpressao, final Date dataCriacao, final Date dataImpressao, final String criador) {
		super();
		setCodigo(codigo);
		this.textoImpressao = impressao;
		this.status = status;
		this.tipoImpressao = tipoImpressao;
		this.dataCriacao = dataCriacao;
		this.dataImpressao = dataImpressao;
		this.criador = criador;
	}

	@Column(name = "TEXTO_IMPRESSAO", nullable = false)
	@NotNull
	@Override
	public String getTextoImpressao() {
		return textoImpressao;
	}

	public void setTextoImpressao(final String impressao) {
		this.textoImpressao = impressao;
	}

	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	@Override
	public PrintStatus getStatus() {
		return status;
	}

	public void setStatus(final PrintStatus status) {
		this.status = status;
	}

	@Column(name = "TIPO", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	@Override
	public TiposImpressao getTipoImpressao() {
		return tipoImpressao;
	}

	public void setTipoImpressao(final TiposImpressao tipoImpressao) {
		this.tipoImpressao = tipoImpressao;
	}

	@Column(name = "DATA_CRIACAO", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Override
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(final Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Column(name = "DATA_IMPRESSAO")
	@Temporal(TemporalType.TIMESTAMP)
	@Override
	public Date getDataImpressao() {
		return dataImpressao;
	}

	public void setDataImpressao(final Date dataImpressao) {
		this.dataImpressao = dataImpressao;
	}

	@Column(name = "CRIADOR", nullable = false)
	@NotNull
	@Override
	public String getCriador() {
		return criador;
	}

	public void setCriador(final String criador) {
		this.criador = criador;
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
		if (!(DocumentPrintEntity.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		DocumentPrintEntity other = (DocumentPrintEntity) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Print [codigo=" + getCodigo() + ", impressao=" + textoImpressao + ", status=" + status + ", tipoImpressao="
			+ tipoImpressao + ", dataCriacao=" + dataCriacao + ", dataImpressao=" + dataImpressao + ", criador="
			+ criador + "]";
	}

}

