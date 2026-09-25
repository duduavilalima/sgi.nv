package br.com.araujoabreu.sgi.service.custos.transfer;

import br.com.araujoabreu.sgi.service.Diretoria.transfer.DiretoriaSimpleTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroSimpleTO;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaSimpleTO;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeSimpleTO;

import java.util.Objects;

public class ImportacaoOrcamentoEmpresarialTO {

	public enum TipoPlanilha {
		DIRETORIA(2, "Diretoria"),
		MATRIZ_FILIAL(1, "Matriz/Filial"),
		GERENCIA(3, "Gerência"),
		CENTRO_DE_LUCRO(4, "Centro de Lucro");

		private Integer codigo;
		private String descricao;

		TipoPlanilha(Integer codigo, String descricao) {
			this.codigo = codigo;
			this.descricao = descricao;
		}

		public Integer getCodigo() {
			return codigo;
		}

		public String getDescricao() {
			return descricao;
		}

		public static TipoPlanilha getEnum(Integer code) {
			switch (code) {
				case 1:
					return MATRIZ_FILIAL;
				case 2:
					return DIRETORIA;
				case 3:
					return GERENCIA;
				case 4:
					return CENTRO_DE_LUCRO;
				default:
					return null;
			}
		}
	}

	private Integer tipoPlanilha;
	private UnidadeSimpleTO unidade;
	private DiretoriaSimpleTO diretoria;
	private GerenciaSimpleTO gerencia;
	private CentroFinanceiroSimpleTO centroFinanceiro;
	private Integer referenciaAno;
	private byte[] file;

	public Integer getTipoPlanilha() {
		return tipoPlanilha;
	}

	public void setTipoPlanilha(Integer tipoPlanilha) {
		this.tipoPlanilha = tipoPlanilha;
	}

	public UnidadeSimpleTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeSimpleTO unidade) {
		this.unidade = unidade;
	}

	public DiretoriaSimpleTO getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(DiretoriaSimpleTO diretoria) {
		this.diretoria = diretoria;
	}

	public GerenciaSimpleTO getGerencia() {
		return gerencia;
	}

	public void setGerencia(GerenciaSimpleTO gerencia) {
		this.gerencia = gerencia;
	}

	public CentroFinanceiroSimpleTO getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiroSimpleTO centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
	}

	public Integer getReferenciaAno() {
		return referenciaAno;
	}

	public void setReferenciaAno(Integer referenciaAno) {
		this.referenciaAno = referenciaAno;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ImportacaoOrcamentoEmpresarialTO)) return false;
		ImportacaoOrcamentoEmpresarialTO that = (ImportacaoOrcamentoEmpresarialTO) o;
		return Objects.equals(getTipoPlanilha(), that.getTipoPlanilha()) &&
			Objects.equals(getUnidade(), that.getUnidade()) &&
			Objects.equals(getDiretoria(), that.getDiretoria()) &&
			Objects.equals(getGerencia(), that.getGerencia()) &&
			Objects.equals(getCentroFinanceiro(), that.getCentroFinanceiro()) &&
			Objects.equals(getReferenciaAno(), that.getReferenciaAno());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getTipoPlanilha(), getUnidade(), getDiretoria(), getGerencia(), getCentroFinanceiro(), getReferenciaAno());
	}

	@Override
	public String toString() {
		return "ImportacaoOrcamentoEmpresarialTO{" +
			"tipoPlanilha=" + tipoPlanilha +
			", referenciaAno=" + referenciaAno +
			'}';
	}
}
