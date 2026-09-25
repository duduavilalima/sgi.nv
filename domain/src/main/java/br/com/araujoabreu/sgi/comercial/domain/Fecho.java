package br.com.araujoabreu.sgi.comercial.domain;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.comercial.vo.BdiVO;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FECHO")
@SequenceGenerator(name = "SEQ_FECHO", sequenceName = "SEQ_FECHO", allocationSize = 1)
public class Fecho extends AuditEntity implements DomainObject, Serializable {

	private static final long serialVersionUID = -1856293835926134890L;
	private CentroFinanceiro centroFinanceiro = new CentroFinanceiro();
	private Integer numero;
	private BigDecimal valorMaoObra;
	private BigDecimal percentualEncargosSociais;
	private BigDecimal valorGastosGerais;
	private BigDecimal valorMaterial;
	private BigDecimal valorServicoTerceiro;
	private BigDecimal percentualBdiBeneficios;
	private BigDecimal percentualBdiDespesas;
	private BigDecimal percentualImposto;
	private List<FechoMaoDeObra> listaFechosMaoDeObra = new ArrayList<>();
	private List<FechoDespesaGeral> listaFechosDespesasGerais = new ArrayList<>();

	private Colaborador responsavelReajuste;
	private Date dataReajuste;
	private String indiceRajuste;
	private BigDecimal percentualReajusteMaoDeObra;
	private BigDecimal percentualReajusteGastosGerais;
	private BigDecimal percentualReajusteMateriais;
	private BigDecimal percentualReajusteServicos;

	public Fecho() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO", nullable = false)
	@NotNull
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	@Column(name = "NUMERO", nullable = false)
	@NotNull
	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	@Column(name = "VALOR_MAO_OBRA")
	public BigDecimal getValorMaoObra() {
		return valorMaoObra;
	}

	public void setValorMaoObra(BigDecimal valorMaoObra) {
		this.valorMaoObra = valorMaoObra;
	}

	@Column(name = "PERCENTUAL_ENCARGOS_SOCIAIS")
	public BigDecimal getPercentualEncargosSociais() {
		return percentualEncargosSociais;
	}

	public void setPercentualEncargosSociais(BigDecimal percentualEncargosSociais) {
		this.percentualEncargosSociais = percentualEncargosSociais;
	}

	@Column(name = "VALOR_GASTOS_GERAIS")
	public BigDecimal getValorGastosGerais() {
		return valorGastosGerais;
	}

	public void setValorGastosGerais(BigDecimal valorGastosGerais) {
		this.valorGastosGerais = valorGastosGerais;
	}

	@Column(name = "VALOR_MATERIAL")
	public BigDecimal getValorMaterial() {
		return valorMaterial;
	}

	public void setValorMaterial(BigDecimal valorMaterial) {
		this.valorMaterial = valorMaterial;
	}

	@Column(name = "VALOR_SERVICO_TERCEIRO")
	public BigDecimal getValorServicoTerceiro() {
		return valorServicoTerceiro;
	}

	public void setValorServicoTerceiro(BigDecimal valorServicoTerceiro) {
		this.valorServicoTerceiro = valorServicoTerceiro;
	}

	@Column(name = "PERCENTUAL_IMPOSTO")
	public BigDecimal getPercentualImposto() {
		return percentualImposto;
	}

	public void setPercentualImposto(BigDecimal percentualImposto) {
		this.percentualImposto = percentualImposto;
	}

	@Column(name = "PERCENTUAL_BDI_BENEFICIOS", precision = 7, scale = 4)
	public BigDecimal getPercentualBdiBeneficios() {
		return percentualBdiBeneficios;
	}

	public void setPercentualBdiBeneficios(BigDecimal percentualBdiBeneficios) {
		this.percentualBdiBeneficios = percentualBdiBeneficios;
	}

	@Column(name = "PERCENTUAL_BDI_DESPESAS", precision = 7, scale = 4)
	public BigDecimal getPercentualBdiDespesas() {
		return percentualBdiDespesas;
	}

	public void setPercentualBdiDespesas(BigDecimal percentualBdiDespesas) {
		this.percentualBdiDespesas = percentualBdiDespesas;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fecho")
	@Fetch(FetchMode.SUBSELECT)
	public List<FechoDespesaGeral> getListaFechosDespesasGerais() {
		return listaFechosDespesasGerais;
	}

	public void setListaFechosDespesasGerais(List<FechoDespesaGeral> listaFechosDespesasGerais) {
		this.listaFechosDespesasGerais = listaFechosDespesasGerais;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fecho")
	@Fetch(FetchMode.SUBSELECT)
	public List<FechoMaoDeObra> getListaFechosMaoDeObra() {
		return listaFechosMaoDeObra;
	}

	public void setListaFechosMaoDeObra(List<FechoMaoDeObra> listaFechosMaoDeObra) {
		this.listaFechosMaoDeObra = listaFechosMaoDeObra;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_RESPONSAVEL_REAJUSTE")
	public Colaborador getResponsavelReajuste() {
		return responsavelReajuste;
	}

	public void setResponsavelReajuste(Colaborador responsavelReajuste) {
		this.responsavelReajuste = responsavelReajuste;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_REAJUSTE")
	public Date getDataReajuste() {
		return dataReajuste;
	}

	public void setDataReajuste(Date dataReajuste) {
		this.dataReajuste = dataReajuste;
	}

	@Column(name = "INDICE_REAJUSTE")
	public String getIndiceRajuste() {
		return indiceRajuste;
	}

	public void setIndiceRajuste(String indiceRajuste) {
		this.indiceRajuste = indiceRajuste;
	}

	@Column(name = "PERCENTUAL_REAJUSTE_MAO_OBRA")
	public BigDecimal getPercentualReajusteMaoDeObra() {
		return percentualReajusteMaoDeObra;
	}

	public void setPercentualReajusteMaoDeObra(BigDecimal percentualReajusteMaoDeObra) {
		this.percentualReajusteMaoDeObra = percentualReajusteMaoDeObra;
	}

	@Column(name = "PERCENTUAL_REAJUSTE_GASTOS_GERAIS")
	public BigDecimal getPercentualReajusteGastosGerais() {
		return percentualReajusteGastosGerais;
	}

	public void setPercentualReajusteGastosGerais(BigDecimal percentualReajusteGastosGerais) {
		this.percentualReajusteGastosGerais = percentualReajusteGastosGerais;
	}

	@Column(name = "PERCENTUAL_REAJUSTE_MATERIAIS")
	public BigDecimal getPercentualReajusteMateriais() {
		return percentualReajusteMateriais;
	}

	public void setPercentualReajusteMateriais(BigDecimal percentualReajusteMateriais) {
		this.percentualReajusteMateriais = percentualReajusteMateriais;
	}

	@Column(name = "PERCENTUAL_REAJUSTE_SERVICOS")
	public BigDecimal getPercentualReajusteServicos() {
		return percentualReajusteServicos;
	}

	public void setPercentualReajusteServicos(BigDecimal percentualReajusteServicos) {
		this.percentualReajusteServicos = percentualReajusteServicos;
	}

	@Transient //TODO: fazer o cálculo
	public BigDecimal getTotalBdiItem() {
		return new BigDecimal("0");
	}

	@Transient //TODO: fazer o cálculo
	public BigDecimal getTotalBdi() {
		return new BigDecimal("0");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((dataReajuste == null) ? 0 : dataReajuste.hashCode());
		result = prime * result + ((indiceRajuste == null) ? 0 : indiceRajuste.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((percentualBdiBeneficios == null) ? 0 : percentualBdiBeneficios.hashCode());
		result = prime * result + ((percentualBdiDespesas == null) ? 0 : percentualBdiDespesas.hashCode());
		result = prime * result + ((percentualEncargosSociais == null) ? 0 : percentualEncargosSociais.hashCode());
		result = prime * result + ((percentualImposto == null) ? 0 : percentualImposto.hashCode());
		result = prime * result + ((percentualReajusteGastosGerais == null) ? 0 : percentualReajusteGastosGerais.hashCode());
		result = prime * result + ((percentualReajusteMaoDeObra == null) ? 0 : percentualReajusteMaoDeObra.hashCode());
		result = prime * result + ((percentualReajusteMateriais == null) ? 0 : percentualReajusteMateriais.hashCode());
		result = prime * result + ((percentualReajusteServicos == null) ? 0 : percentualReajusteServicos.hashCode());
		result = prime * result + ((responsavelReajuste == null) ? 0 : responsavelReajuste.hashCode());
		result = prime * result + ((valorGastosGerais == null) ? 0 : valorGastosGerais.hashCode());
		result = prime * result + ((valorMaoObra == null) ? 0 : valorMaoObra.hashCode());
		result = prime * result + ((valorMaterial == null) ? 0 : valorMaterial.hashCode());
		result = prime * result + ((valorServicoTerceiro == null) ? 0 : valorServicoTerceiro.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(Fecho.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Fecho other = (Fecho) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (getDataReajuste() == null) {
			if (other.getDataReajuste() != null) {
				return false;
			}
		} else if (!getDataReajuste().equals(other.getDataReajuste())) {
			return false;
		}
		if (getIndiceRajuste() == null) {
			if (other.getIndiceRajuste() != null) {
				return false;
			}
		} else if (!getIndiceRajuste().equals(other.getIndiceRajuste())) {
			return false;
		}
		if (getNumero() == null) {
			if (other.getNumero() != null) {
				return false;
			}
		} else if (!getNumero().equals(other.getNumero())) {
			return false;
		}
		if (getPercentualBdiBeneficios() == null) {
			if (other.getPercentualBdiBeneficios() != null) {
				return false;
			}
		} else if (!getPercentualBdiBeneficios().equals(other.getPercentualBdiBeneficios())) {
			return false;
		}
		if (getPercentualBdiDespesas() == null) {
			if (other.getPercentualBdiDespesas() != null) {
				return false;
			}
		} else if (!getPercentualBdiDespesas().equals(other.getPercentualBdiDespesas())) {
			return false;
		}
		if (getPercentualEncargosSociais() == null) {
			if (other.getPercentualEncargosSociais() != null) {
				return false;
			}
		} else if (!getPercentualEncargosSociais().equals(other.getPercentualEncargosSociais())) {
			return false;
		}
		if (getPercentualImposto() == null) {
			if (other.getPercentualImposto() != null) {
				return false;
			}
		} else if (!getPercentualImposto().equals(other.getPercentualImposto())) {
			return false;
		}
		if (getPercentualReajusteGastosGerais() == null) {
			if (other.getPercentualReajusteGastosGerais() != null) {
				return false;
			}
		} else if (!getPercentualReajusteGastosGerais().equals(other.getPercentualReajusteGastosGerais())) {
			return false;
		}
		if (getPercentualReajusteMaoDeObra() == null) {
			if (other.getPercentualReajusteMaoDeObra() != null) {
				return false;
			}
		} else if (!getPercentualReajusteMaoDeObra().equals(other.getPercentualReajusteMaoDeObra())) {
			return false;
		}
		if (getPercentualReajusteMateriais() == null) {
			if (other.getPercentualReajusteMateriais() != null) {
				return false;
			}
		} else if (!getPercentualReajusteMateriais().equals(other.getPercentualReajusteMateriais())) {
			return false;
		}
		if (getPercentualReajusteServicos() == null) {
			if (other.getPercentualReajusteServicos() != null) {
				return false;
			}
		} else if (!getPercentualReajusteServicos().equals(other.getPercentualReajusteServicos())) {
			return false;
		}
		if (getResponsavelReajuste() == null) {
			if (other.getResponsavelReajuste() != null) {
				return false;
			}
		} else if (!getResponsavelReajuste().equals(other.getResponsavelReajuste())) {
			return false;
		}
		if (getValorGastosGerais() == null) {
			if (other.getValorGastosGerais() != null) {
				return false;
			}
		} else if (!getValorGastosGerais().equals(other.getValorGastosGerais())) {
			return false;
		}
		if (getValorMaoObra() == null) {
			if (other.getValorMaoObra() != null) {
				return false;
			}
		} else if (!getValorMaoObra().equals(other.getValorMaoObra())) {
			return false;
		}
		if (getValorMaterial() == null) {
			if (other.getValorMaterial() != null) {
				return false;
			}
		} else if (!getValorMaterial().equals(other.getValorMaterial())) {
			return false;
		}
		if (getValorServicoTerceiro() == null) {
			if (other.getValorServicoTerceiro() != null) {
				return false;
			}
		} else if (!getValorServicoTerceiro().equals(other.getValorServicoTerceiro())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Fecho [codigo=" + getCodigo() + ", centroFinanceiro="
			+ centroFinanceiro + ", numero=" + numero + ", valorMaoObra="
			+ valorMaoObra + ", percentualEncargosSociais="
			+ percentualEncargosSociais + ", valorGastosGerais="
			+ valorGastosGerais + ", valorMaterial=" + valorMaterial
			+ ", valorServicoTerceiro=" + valorServicoTerceiro
			+ ", percentualBdiBeneficios=" + percentualBdiBeneficios + ", percentualImposto="
			+ percentualImposto + "]";
	}

	@Transient
	public List<BdiVO> getListaBdi() {
		List<BdiVO> listaBdi = new ArrayList<>();
		BdiVO bdi = new BdiVO();
		bdi.setBdi(BdiVO.TipoBdi.DESPESAS_INDIRETAS);
		if (this.percentualBdiDespesas != null)
			bdi.setValor(this.percentualBdiDespesas);
		else
			bdi.setValor(new BigDecimal("0"));
		listaBdi.add(bdi);
		bdi = new BdiVO();
		bdi.setBdi(BdiVO.TipoBdi.BENEFICIOS);
		if (this.percentualBdiBeneficios != null)
			bdi.setValor(this.percentualBdiBeneficios);
		else
			bdi.setValor(new BigDecimal("0"));
		listaBdi.add(bdi);
		return listaBdi;
	}

	@Transient
	public BigDecimal getTotalMaoDeObraDireta() {
		return this.getTotalMaoDeObraPorTipoContratacao(FechoMaoDeObra.TipoContratacao.DIRETA);
	}

	@Transient
	public BigDecimal getTotalMaoDeObraIndireta() {
		return this.getTotalMaoDeObraPorTipoContratacao(FechoMaoDeObra.TipoContratacao.INDIRETA);
	}

	private BigDecimal getTotalMaoDeObraPorTipoContratacao(FechoMaoDeObra.TipoContratacao tipo) {
		BigDecimal total = new BigDecimal("0");
		if (this.listaFechosMaoDeObra != null && !this.listaFechosMaoDeObra.isEmpty()) {
			for (FechoMaoDeObra maoDeObra : this.listaFechosMaoDeObra) {
				if (maoDeObra.getTipoContratacao().equals(tipo)) {
					total = total.add(maoDeObra.getValorTotal());
				}
			}
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	@Transient
	public BigDecimal getTotalMaoDeObra() {
		BigDecimal total = new BigDecimal("0");
		if (this.listaFechosMaoDeObra != null && !this.listaFechosMaoDeObra.isEmpty()) {
			for (FechoMaoDeObra maoDeObra : this.listaFechosMaoDeObra) {
				total = total.add(maoDeObra.getValorTotal());
			}
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	@Transient
	public BigDecimal getTotalMaoDeObraComEncargos() {
		BigDecimal total = new BigDecimal("0");
		total = total.add(this.getTotalMaoDeObra()).add(getTotalEncargosSociais());
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	@Transient
	private BigDecimal getTotalEncargosSociais() {
		BigDecimal total = new BigDecimal("0");
		if (percentualEncargosSociais != null) {
			total = this.getTotalMaoDeObra().multiply(percentualEncargosSociais);
			total = total.divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	@Transient
	public BigDecimal getTotalDespesaGeral(FechoDespesaGeral.TipoDespesaGeral tipoDespesa) {
		BigDecimal total = new BigDecimal("0");
		if (this.listaFechosDespesasGerais != null && !this.listaFechosDespesasGerais.isEmpty()) {
			for (FechoDespesaGeral despesa : this.listaFechosDespesasGerais) {
				if (despesa.getTipoDespesaGeral().equals(tipoDespesa))
					total = total.add(despesa.getValorTotalItem());
			}
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

}

