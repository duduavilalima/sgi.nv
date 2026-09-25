package br.com.araujoabreu.sgi.inrf.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ViewItemNotaCompositeKey implements Serializable {

	private static final long serialVersionUID = -4731790988205865563L;

	@Column(name = "codigo_nota")
	private Long codigoNota;

	@Column(name = "codigo_item_nota")
	private Long codigoItemNota;

	public ViewItemNotaCompositeKey() {
	}

	public Long getCodigoNota() {
		return codigoNota;
	}

	public void setCodigoNota(Long codigoNota) {
		this.codigoNota = codigoNota;
	}

	public Long getCodigoItemNota() {
		return codigoItemNota;
	}

	public void setCodigoItemNota(Long codigoItemNota) {
		this.codigoItemNota = codigoItemNota;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoItemNota == null) ? 0 : codigoItemNota.hashCode());
		result = prime * result + ((codigoNota == null) ? 0 : codigoNota.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ViewItemNotaCompositeKey other = (ViewItemNotaCompositeKey) obj;
		if (codigoItemNota == null) {
			if (other.codigoItemNota != null)
				return false;
		} else if (!codigoItemNota.equals(other.codigoItemNota))
			return false;
		if (codigoNota == null) {
			if (other.codigoNota != null)
				return false;
		} else if (!codigoNota.equals(other.codigoNota))
			return false;
		return true;
	}


}
