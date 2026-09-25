package br.com.araujoabreu.sgi.comercial.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class BdiVO {

	private TipoBdi bdi;
	private BigDecimal valor;
	private BigDecimal custoMensal;
	private BigDecimal total;
	private List<BdiVO> listaBdi;

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

	public List<BdiVO> getListaBdi() {
		return listaBdi;
	}

	public void setListaBdi(List<BdiVO> listaBdi) {
		this.listaBdi = listaBdi;
	}

	private BigDecimal getPercentual(TipoBdi tipo) {
		if (listaBdi != null && !listaBdi.isEmpty()) {
			for (BdiVO bdi : listaBdi) {
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
			valor = valor.setScale(4, RoundingMode.HALF_EVEN);
			DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.0000");
			return decimalFormat.format(valor);
		}
		return "0,0000";
	}

	public BigDecimal getTotal(TipoBdi bdi) {
		if (custoMensal != null && valor != null) {
			BigDecimal percentualPrincipal = getPercentual(bdi).divide(new BigDecimal("100"));
			BigDecimal despesaIndireta = getPercentual(TipoBdi.DESPESAS_INDIRETAS).divide(new BigDecimal("100"));
			BigDecimal beneficios = getPercentual(TipoBdi.BENEFICIOS).divide(new BigDecimal("100"));
			BigDecimal um = new BigDecimal("1");
			BigDecimal dividendo = (this.custoMensal.multiply(percentualPrincipal));
			BigDecimal divisor = (um.subtract(despesaIndireta.add(beneficios)));
			if (divisor.doubleValue() <= 0) {
				divisor = BigDecimal.valueOf(1d);
			}
			double totalAux = dividendo.doubleValue() / divisor.doubleValue();
			total = new BigDecimal(totalAux).setScale(2, RoundingMode.HALF_EVEN);
		} else {
			total = BigDecimal.valueOf(0L);
		}
		return total;
	}

	@Override
	public String toString() {
		return "BdiVO [bdi=" + bdi + ", valor=" + valor + ", custoMensal=" + custoMensal + ", total=" + total + ", listaBdi=" + listaBdi + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BdiVO)) return false;
		BdiVO bdiVO = (BdiVO) o;
		return getBdi() == bdiVO.getBdi() &&
			Objects.equals(getValor(), bdiVO.getValor()) &&
			Objects.equals(getCustoMensal(), bdiVO.getCustoMensal()) &&
			Objects.equals(total, bdiVO.total) &&
			Objects.equals(getListaBdi(), bdiVO.getListaBdi());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getBdi(), getValor(), getCustoMensal(), total, getListaBdi());
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
