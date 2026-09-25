package br.com.araujoabreu.sgi.service.comercial.transfer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class BdiVOTO {

	private TipoBdi bdi;
	private BigDecimal valor;
	private BigDecimal custoMensal;
	private BigDecimal total;
	private List<BdiVOTO> listaBdi;

	public TipoBdi getBdi() {
		return bdi;
	}

	public void setBdi(TipoBdi bdi) {
		this.bdi = bdi;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getCustoMensal() {
		return custoMensal;
	}

	public void setCustoMensal(BigDecimal custoMensal) {
		this.custoMensal = custoMensal;
	}

	public List<BdiVOTO> getListaBdi() {
		return listaBdi;
	}

	public void setListaBdi(List<BdiVOTO> listaBdi) {
		this.listaBdi = listaBdi;
	}

	private BigDecimal getPercentual(TipoBdi tipo) {
		if (listaBdi != null && !listaBdi.isEmpty()) {
			for (BdiVOTO bdi : listaBdi) {
				if (bdi.getBdi().equals(tipo)) {
					if (bdi.getValor() == null) {
						return new BigDecimal("0");
					}
					return bdi.getValor();
				}
			}
		}
		return new BigDecimal("0");

	}

	public String getValorLabel() {
		if (valor != null) {
			valor = valor.setScale(3, RoundingMode.HALF_EVEN);
			DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.000");
			return decimalFormat.format(valor);
		}
		return "0,000";
	}

	public BigDecimal getTotal(TipoBdi bdi) {
		if (custoMensal != null && valor != null) {
			BigDecimal percentualPrincipal = getPercentual(bdi).divide(new BigDecimal("100")).setScale(5, RoundingMode.HALF_EVEN);
			BigDecimal despesaIndireta = getPercentual(TipoBdi.DESPESAS_INDIRETAS).divide(new BigDecimal("100")).setScale(5, RoundingMode.HALF_EVEN);
			BigDecimal beneficios = getPercentual(TipoBdi.BENEFICIOS).divide(new BigDecimal("100")).setScale(5, RoundingMode.HALF_EVEN);
			BigDecimal um = new BigDecimal("1");
			BigDecimal dividendo = (this.custoMensal.multiply(percentualPrincipal)).setScale(5, RoundingMode.HALF_EVEN);
			BigDecimal divisor = (um.subtract(despesaIndireta.add(beneficios))).setScale(5, RoundingMode.HALF_EVEN);
			if (divisor.doubleValue() <= 0) {
				divisor = new BigDecimal(1d);
			}
			double totalAux = dividendo.doubleValue() / divisor.doubleValue();
			total = new BigDecimal(totalAux).setScale(2, RoundingMode.HALF_EVEN);
		} else {
			total = new BigDecimal(0L);
		}
		return total;
	}

	@Override
	public String toString() {
		return "BdiVO [bdi=" + bdi + ", valor=" + valor + ", custoMensal=" + custoMensal + ", total=" + total + ", listaBdi=" + listaBdi + "]";
	}

	public enum TipoBdi {
		DESPESAS_INDIRETAS("Despesas indiretas"),
		BENEFICIOS("Benefícios");

		private final String descricao;

		private TipoBdi(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}

}
