package br.com.araujoabreu.sgi.service.nota.transfer;

import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.AliquotaTO;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.CategoriaTributoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.math.BigDecimal;

public class NotaTributoAliquotaTO extends BaseTO {
	private BigDecimal baseCalculo;
	private BigDecimal percentual;
	private Integer individual;
	private NotaTO nota;
	private AliquotaTO aliquota;
	private Boolean validaPrefeitura = Boolean.FALSE;
	private CategoriaTributoTO categoriaTributo;
	private BigDecimal valor;
	private Integer ordem = 1;

	public BigDecimal getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	public Integer getIndividual() {
		return individual;
	}

	public void setIndividual(Integer individual) {
		this.individual = individual;
	}

	public NotaTO getNota() {
		return nota;
	}

	public void setNota(NotaTO nota) {
		this.nota = nota;
	}

	public AliquotaTO getAliquota() {
		return aliquota;
	}

	public void setAliquota(AliquotaTO aliquota) {
		this.aliquota = aliquota;
	}

	public Boolean getValidaPrefeitura() {
		return validaPrefeitura;
	}

	public void setValidaPrefeitura(Boolean validaPrefeitura) {
		this.validaPrefeitura = validaPrefeitura;
	}

	public CategoriaTributoTO getCategoriaTributo() {
		return categoriaTributo;
	}

	public void setCategoriaTributo(CategoriaTributoTO categoriaTributo) {
		this.categoriaTributo = categoriaTributo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}
