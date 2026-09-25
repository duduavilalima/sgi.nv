package br.com.araujoabreu.sgi.faturamento.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "TIPO_COBRANCA_FATURAMENTO")
public class TipoCobrancaFaturamento extends BaseEntity {

	private static final long serialVersionUID = 3241535409069392705L;
	private String descricao;
	private List<Faturamento> faturamentos;
	private List<TipoCobrancaVencimento> tipoCobrancaVencimentos;

	public TipoCobrancaFaturamento() {
	}

	@Column(nullable = false, length = 30)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	//bi-directional many-to-one association to Faturamento
	@OneToMany(mappedBy = "tipoCobrancaFaturamento", fetch = FetchType.LAZY)
	public List<Faturamento> getFaturamentos() {
		return this.faturamentos;
	}

	public void setFaturamentos(List<Faturamento> faturamentos) {
		this.faturamentos = faturamentos;
	}

	public Faturamento addFaturamento(Faturamento faturamento) {
		getFaturamentos().add(faturamento);
		faturamento.setTipoCobrancaFaturamento(this);

		return faturamento;
	}

	public Faturamento removeFaturamento(Faturamento faturamento) {
		getFaturamentos().remove(faturamento);
		faturamento.setTipoCobrancaFaturamento(null);

		return faturamento;
	}


	//bi-directional many-to-one association to TipoCobrancaVencimento
	@OneToMany(mappedBy = "tipoCobrancaFaturamento", fetch = FetchType.LAZY)
	public List<TipoCobrancaVencimento> getTipoCobrancaVencimentos() {
		return this.tipoCobrancaVencimentos;
	}

	public void setTipoCobrancaVencimentos(List<TipoCobrancaVencimento> tipoCobrancaVencimentos) {
		this.tipoCobrancaVencimentos = tipoCobrancaVencimentos;
	}

	public TipoCobrancaVencimento addTipoCobrancaVencimento(TipoCobrancaVencimento tipoCobrancaVencimento) {
		getTipoCobrancaVencimentos().add(tipoCobrancaVencimento);
		tipoCobrancaVencimento.setTipoCobrancaFaturamento(this);

		return tipoCobrancaVencimento;
	}

	public TipoCobrancaVencimento removeTipoCobrancaVencimento(TipoCobrancaVencimento tipoCobrancaVencimento) {
		getTipoCobrancaVencimentos().remove(tipoCobrancaVencimento);
		tipoCobrancaVencimento.setTipoCobrancaFaturamento(null);

		return tipoCobrancaVencimento;
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

		if (!(obj instanceof TipoCobrancaFaturamento)) {
			return false;
		}

		TipoCobrancaFaturamento other = (TipoCobrancaFaturamento) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;
		return true;
	}


}
