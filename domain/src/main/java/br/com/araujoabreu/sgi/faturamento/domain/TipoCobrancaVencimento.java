package br.com.araujoabreu.sgi.faturamento.domain;

import br.com.araujoabreu.sgi.administracao.domain.Contrato.TipoVencimento;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TIPO_COBRANCA_VENCIMENTO")
public class TipoCobrancaVencimento extends BaseEntity {

	private static final long serialVersionUID = 644450817694013377L;
	private TipoDataReferencia dataReferencia;
	private Integer diaVencimento;
	private TipoVencimento diaVencimentoTipo;
	private TipoMesReferencia mesReferencia;
	private Boolean solicitaData;
	private TipoCobrancaFaturamento tipoCobrancaFaturamento;

	public TipoCobrancaVencimento() {
	}

	@Column(name = "DATA_REFERENCIA")
	public TipoDataReferencia getDataReferencia() {
		return this.dataReferencia;
	}

	public void setDataReferencia(TipoDataReferencia dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	@Column(name = "DIA_VENCIMENTO")
	public Integer getDiaVencimento() {
		return this.diaVencimento;
	}

	public void setDiaVencimento(Integer diaVencimento) {
		this.diaVencimento = diaVencimento;
	}

	@Column(name = "DIA_VENCIMENTO_TIPO")
	public TipoVencimento getDiaVencimentoTipo() {
		return this.diaVencimentoTipo;
	}

	public void setDiaVencimentoTipo(TipoVencimento diaVencimentoTipo) {
		this.diaVencimentoTipo = diaVencimentoTipo;
	}

	@Column(name = "MES_REFERENCIA")
	public TipoMesReferencia getMesReferencia() {
		return this.mesReferencia;
	}

	public void setMesReferencia(TipoMesReferencia mesReferencia) {
		this.mesReferencia = mesReferencia;
	}

	@Column(name = "SOLICITA_DATA", nullable = false)
	public Boolean getSolicitaData() {
		return this.solicitaData;
	}

	public void setSolicitaData(Boolean solicitaData) {
		this.solicitaData = solicitaData;
	}

	//bi-directional many-to-one association to TipoCobrancaFaturamento
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_tipo_cobranca_faturamento", nullable = false)
	public TipoCobrancaFaturamento getTipoCobrancaFaturamento() {
		return this.tipoCobrancaFaturamento;
	}

	public void setTipoCobrancaFaturamento(TipoCobrancaFaturamento tipoCobrancaFaturamento) {
		this.tipoCobrancaFaturamento = tipoCobrancaFaturamento;
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

		if (!(obj instanceof TipoCobrancaVencimento)) {
			return false;
		}

		TipoCobrancaVencimento other = (TipoCobrancaVencimento) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TipoCobrancaVencimento [codigo=" + getCodigo() + ", dataReferencia=" + dataReferencia + ", diaVencimento=" + diaVencimento + ", diaVencimentoTipo=" + diaVencimentoTipo + ", mesReferencia=" + mesReferencia + ", solicitaData=" + solicitaData + ", tipoCobrancaFaturamento=" + tipoCobrancaFaturamento + "]";
	}

	public enum TipoDataReferencia {
		EMISSAO("Emissão"),
		ENTREGA("Entrega");

		private final String descricao;

		private TipoDataReferencia(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}


	public enum TipoMesReferencia {
		ATUAL("Atual"),
		PROXIMO("Próximo");

		private final String descricao;

		private TipoMesReferencia(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}

}
