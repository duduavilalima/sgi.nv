package br.com.araujoabreu.sgi.service.fecho.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.BdiVOTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FechoTO extends BaseTO {

	private Integer numero;
	private Date dataReajuste;
	private BigDecimal valorMaoObra;
	private BigDecimal valorGastosGerais;
	private BigDecimal valorMaterial;
	private BigDecimal valorServicoTerceiro;
	private BigDecimal percentualImposto;
	private BigDecimal percentualBdiBeneficios;
	private BigDecimal percentualBdiDespesas;
	private BigDecimal percentualEncargosSociais;
	private BigDecimal percentualReajusteMaoDeObra;
	private BigDecimal percentualReajusteGastosGerais;
	private BigDecimal percentualReajusteMateriais;
	private BigDecimal percentualReajusteServicos;
	private ColaboradorTO responsavelReajuste;
	private CentroFinanceiroTO centroFinanceiro;
	private String indiceRajuste;
	private List<FechoMaoDeObraTO> listaFechosMaoDeObra = new ArrayList<>();
	private List<FechoDespesaGeralTO> listaFechosDespesasGerais = new ArrayList<>();
	private List<BdiVOTO> listaBdi = new ArrayList<>();

	public FechoTO() {
	}

	public FechoTO(long codigo) {
		setCodigo(codigo);
	}

	public Date getDataReajuste() {
		return dataReajuste;
	}

	public void setDataReajuste(Date dataReajuste) {
		this.dataReajuste = dataReajuste;
	}

	public BigDecimal getPercentualImposto() {
		return percentualImposto;
	}

	public void setPercentualImposto(BigDecimal percentualImposto) {
		this.percentualImposto = percentualImposto;
	}

	public BigDecimal getPercentualBdiBeneficios() {
		return percentualBdiBeneficios;
	}

	public void setPercentualBdiBeneficios(BigDecimal percentualBdiBeneficios) {
		this.percentualBdiBeneficios = percentualBdiBeneficios;
	}

	public BigDecimal getPercentualBdiDespesas() {
		return percentualBdiDespesas;
	}

	public void setPercentualBdiDespesas(BigDecimal percentualBdiDespesas) {
		this.percentualBdiDespesas = percentualBdiDespesas;
	}

	public BigDecimal getPercentualEncargosSociais() {
		return percentualEncargosSociais;
	}

	public void setPercentualEncargosSociais(BigDecimal percentualEncargosSociais) {
		this.percentualEncargosSociais = percentualEncargosSociais;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public BigDecimal getPercentualReajusteMaoDeObra() {
		return percentualReajusteMaoDeObra;
	}

	public void setPercentualReajusteMaoDeObra(BigDecimal percentualReajusteMaoDeObra) {
		this.percentualReajusteMaoDeObra = percentualReajusteMaoDeObra;
	}

	public BigDecimal getPercentualReajusteGastosGerais() {
		return percentualReajusteGastosGerais;
	}

	public void setPercentualReajusteGastosGerais(BigDecimal percentualReajusteGastosGerais) {
		this.percentualReajusteGastosGerais = percentualReajusteGastosGerais;
	}

	public BigDecimal getPercentualReajusteMateriais() {
		return percentualReajusteMateriais;
	}

	public void setPercentualReajusteMateriais(BigDecimal percentualReajusteMateriais) {
		this.percentualReajusteMateriais = percentualReajusteMateriais;
	}

	public BigDecimal getPercentualReajusteServicos() {
		return percentualReajusteServicos;
	}

	public void setPercentualReajusteServicos(BigDecimal percentualReajusteServicos) {
		this.percentualReajusteServicos = percentualReajusteServicos;
	}

	public ColaboradorTO getResponsavelReajuste() {
		return responsavelReajuste;
	}

	public void setResponsavelReajuste(ColaboradorTO responsavelReajuste) {
		this.responsavelReajuste = responsavelReajuste;
	}

	public CentroFinanceiroTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public String getIndiceRajuste() {
		return indiceRajuste;
	}

	public void setIndiceRajuste(String indiceRajuste) {
		this.indiceRajuste = indiceRajuste;
	}

	public List<FechoMaoDeObraTO> getListaFechosMaoDeObra() {
		return listaFechosMaoDeObra;
	}

	public void setListaFechosMaoDeObra(List<FechoMaoDeObraTO> listaFechosMaoDeObra) {
		this.listaFechosMaoDeObra = listaFechosMaoDeObra;
	}

	public List<FechoDespesaGeralTO> getListaFechosDespesasGerais() {
		return listaFechosDespesasGerais;
	}

	public void setListaFechosDespesasGerais(List<FechoDespesaGeralTO> listaFechosDespesasGerais) {
		this.listaFechosDespesasGerais = listaFechosDespesasGerais;
	}

	public BigDecimal getValorMaoObra() {
		return valorMaoObra;
	}

	public void setValorMaoObra(BigDecimal valorMaoObra) {
		this.valorMaoObra = valorMaoObra;
	}

	public BigDecimal getValorGastosGerais() {
		return valorGastosGerais;
	}

	public void setValorGastosGerais(BigDecimal valorGastosGerais) {
		this.valorGastosGerais = valorGastosGerais;
	}

	public BigDecimal getValorMaterial() {
		return valorMaterial;
	}

	public void setValorMaterial(BigDecimal valorMaterial) {
		this.valorMaterial = valorMaterial;
	}

	public BigDecimal getValorServicoTerceiro() {
		return valorServicoTerceiro;
	}

	public void setValorServicoTerceiro(BigDecimal valorServicoTerceiro) {
		this.valorServicoTerceiro = valorServicoTerceiro;
	}

	private BigDecimal getTotalMaoDeObraPorTipoContratacao(String tipo) {
		BigDecimal total = new BigDecimal("0");
		if (this.listaFechosMaoDeObra != null && !this.listaFechosMaoDeObra.isEmpty()) {
			for (FechoMaoDeObraTO maoDeObra : this.listaFechosMaoDeObra) {
				if (maoDeObra.getTipoContratacao().equals(tipo.toString())) {
					total = total.add(maoDeObra.getValorTotal());
				}
			}
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	public BigDecimal getTotalMaoDeObra() {
		BigDecimal total = new BigDecimal("0");
		if (this.listaFechosMaoDeObra != null && !this.listaFechosMaoDeObra.isEmpty()) {
			for (FechoMaoDeObraTO maoDeObra : this.listaFechosMaoDeObra) {
				total = total.add(maoDeObra.getValorTotal());
			}
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	public BigDecimal getTotalMaoDeObraComEncargos() {
		BigDecimal total = new BigDecimal("0");
		total = total.add(this.getTotalMaoDeObra()).add(getTotalEncargosSociais());
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	private BigDecimal getTotalEncargosSociais() {
		BigDecimal total = new BigDecimal("0");
		if (percentualEncargosSociais != null) {
			total = this.getTotalMaoDeObra().multiply(percentualEncargosSociais);
			total = total.divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	public BigDecimal getTotalDespesaGeral(String tipoDespesa) {
		BigDecimal total = new BigDecimal("0");
		if (this.listaFechosDespesasGerais != null && !this.listaFechosDespesasGerais.isEmpty()) {
			for (FechoDespesaGeralTO despesa : this.listaFechosDespesasGerais) {
				if (despesa.getTipoDespesaGeral().equals(tipoDespesa.toString()))
					total = total.add(despesa.getValorTotalItem());
			}
		}
		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

	public List<BdiVOTO> getListaBdi() {
		return listaBdi;
	}

	public void setListaBdi(List<BdiVOTO> listaBdi) {
		this.listaBdi = listaBdi;
	}
}
