package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.araujoabreu.sgi.compras.domain.Anexo;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "REINF_ANEXO")
@SequenceGenerator(name="SEQ_REINF_ANEXO", sequenceName="SEQ_REINF_ANEXO", allocationSize=1)
public class ReinfAnexo extends BaseEntity implements Serializable {

	public enum TipoAnexoReinf {
		DOWNLOAD, UPLOAD;
	}

	public enum StatusAnexoReinf {
		GERADO, BAIXADO, PROCESSADO;
	}

	private static final long serialVersionUID = -4343716926761134582L;
	private Reinf reinf;
	private Anexo anexo;
	private TipoAnexoReinf tipoAnexo;
	private StatusAnexoReinf status;

	public ReinfAnexo() {
		super();
	}

	public ReinfAnexo(Reinf reinf, Anexo anexo) {
		super();
		this.reinf = reinf;
		this.anexo = anexo;
	}

	public ReinfAnexo(Long codigo) {
		super();
		setCodigo(codigo);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "codigo_reinf", nullable = false)
	public Reinf getReinf() {
		return reinf;
	}

	public void setReinf(Reinf reinf) {
		this.reinf = reinf;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "codigo_anexo", nullable = false)
	@Cascade(value = CascadeType.DELETE)
	public Anexo getAnexo() {
		return anexo;
	}

	public void setAnexo(Anexo anexo) {
		this.anexo = anexo;
	}

	@Column(name = "tipo_anexo_reinf")
	public TipoAnexoReinf getTipoAnexo() {
		return tipoAnexo;
	}

	public void setTipoAnexo(TipoAnexoReinf tipoAnexo) {
		this.tipoAnexo = tipoAnexo;
	}

	@Column(name = "status_anexo_reinf")
	public StatusAnexoReinf getStatus() {
		return status;
	}

	public void setStatus(StatusAnexoReinf status) {
		this.status = status;
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
		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}
		if (!(obj instanceof ReinfAnexo)) {
			return false;
		}
		ReinfAnexo other = (ReinfAnexo) obj;
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
		return "ReinfAnexo [codigo=" + getCodigo() + "]";
	}

}
