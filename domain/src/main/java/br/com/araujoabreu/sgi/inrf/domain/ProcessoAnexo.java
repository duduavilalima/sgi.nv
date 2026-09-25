package br.com.araujoabreu.sgi.inrf.domain;

import br.com.araujoabreu.sgi.compras.domain.Anexo;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "PROCESSO_ANEXO")
public class ProcessoAnexo implements Serializable {

	private static final long serialVersionUID = 1123484322817969193L;

	@EmbeddedId
	private PKProcessoAnexo pkProcessoAnexo = new PKProcessoAnexo();

	@Transient
	private Long codigoBlocoProcesso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PROCESSO", insertable = false, updatable = false)
	@NotNull
	private Processo processo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_BLOCO_PROCESSO", insertable = false, updatable = false)
	@NotNull
	private BlocoProcesso blocoProcesso;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "CODIGO_ANEXO", insertable = false, updatable = false)
	@NotNull
	private Anexo anexo;

	public PKProcessoAnexo getPkProcessoAnexo() {
		return pkProcessoAnexo;
	}

	public void setPkProcessoAnexo(PKProcessoAnexo pkProcessoAnexo) {
		this.pkProcessoAnexo = pkProcessoAnexo;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public BlocoProcesso getBlocoProcesso() {
		return blocoProcesso;
	}

	public void setBlocoProcesso(BlocoProcesso blocoProcesso) {
		this.blocoProcesso = blocoProcesso;
	}

	public Anexo getAnexo() {
		return anexo;
	}

	public void setAnexo(Anexo anexo) {
		this.anexo = anexo;
	}

	public Long getCodigoBlocoProcesso() {
		return codigoBlocoProcesso;
	}

	public void setCodigoBlocoProcesso(Long codigoBlocoProcesso) {
		this.codigoBlocoProcesso = codigoBlocoProcesso;
	}

	@Transient
	public void atualizarPK() {
		if (this.pkProcessoAnexo == null)
			this.pkProcessoAnexo = new PKProcessoAnexo(this.processo.getCodigo(), this.blocoProcesso.getCodigo(), this.anexo.getCodigo());
		else {
			this.pkProcessoAnexo.setCodigoAnexo(this.anexo.getCodigo());
			this.pkProcessoAnexo.setCodigoBlocoProcesso(this.blocoProcesso.getCodigo());
			this.pkProcessoAnexo.setCodigoProcesso(this.processo.getCodigo());
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProcessoAnexo that = (ProcessoAnexo) o;
		return codigoBlocoProcesso.equals(that.codigoBlocoProcesso);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoBlocoProcesso);
	}

	@Embeddable
	public static class PKProcessoAnexo implements Serializable {

		private static final long serialVersionUID = -4790354147565397750L;

		@Column(name = "CODIGO_PROCESSO", nullable = false, updatable = false)
		private Long codigoProcesso;

		@Column(name = "CODIGO_BLOCO_PROCESSO", nullable = false, updatable = false)
		private Long codigoBlocoProcesso;

		@Column(name = "CODIGO_ANEXO", nullable = false, updatable = false)
		private Long codigoAnexo;

		public PKProcessoAnexo() {
		}

		public PKProcessoAnexo(Long codigoProcesso, Long codigoBlocoProcesso, Long codigoAnexo) {
			super();
			this.codigoProcesso = codigoProcesso;
			this.codigoBlocoProcesso = codigoBlocoProcesso;
			this.codigoAnexo = codigoAnexo;
		}

		public Long getCodigoProcesso() {
			return codigoProcesso;
		}

		public void setCodigoProcesso(Long codigoProcesso) {
			this.codigoProcesso = codigoProcesso;
		}

		public Long getCodigoBlocoProcesso() {
			return codigoBlocoProcesso;
		}

		public void setCodigoBlocoProcesso(Long codigoBlocoProcesso) {
			this.codigoBlocoProcesso = codigoBlocoProcesso;
		}

		public Long getCodigoAnexo() {
			return codigoAnexo;
		}

		public void setCodigoAnexo(Long codigoAnexo) {
			this.codigoAnexo = codigoAnexo;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			PKProcessoAnexo that = (PKProcessoAnexo) o;
			return codigoProcesso.equals(that.codigoProcesso) &&
				codigoBlocoProcesso.equals(that.codigoBlocoProcesso) &&
				codigoAnexo.equals(that.codigoAnexo);
		}

		@Override
		public int hashCode() {
			return Objects.hash(codigoProcesso, codigoBlocoProcesso, codigoAnexo);
		}

		@Override
		public String toString() {
			return "PKProcessoAnexo [codigoProcesso=" + codigoProcesso
				+ ", codigoBlocoProcesso=" + codigoBlocoProcesso
				+ ", codigoAnexo=" + codigoAnexo + "]";
		}

	}

}
