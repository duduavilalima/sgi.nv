package br.com.araujoabreu.sgi.service.lote.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.AnexoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoteInativacaoTO extends BaseTO {


	private AnexoTO anexo;
	private ColaboradorTO responsavel;
	private String tipo;
	private String situacao;
	private Date dataImportacao;
	private Integer totalRegistros;
	private Integer totalNaoLocalizado;
	private Integer totalImportadoComPc;
	private Integer totalImportadoSemPc;
	private Integer totalJaInativado;
	private Integer totalIgnorado;
	private String criador;
	private Date dataCriacao;
	private List<LoteInativacaoRegistroTO> registros = new ArrayList<>();

	public AnexoTO getAnexo() {
		return anexo;
	}

	public void setAnexo(AnexoTO anexo) {
		this.anexo = anexo;
	}

	public ColaboradorTO getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(ColaboradorTO responsavel) {
		this.responsavel = responsavel;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Date getDataImportacao() {
		return dataImportacao;
	}

	public void setDataImportacao(Date dataImportacao) {
		this.dataImportacao = dataImportacao;
	}

	public Integer getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(Integer totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	public Integer getTotalNaoLocalizado() {
		return totalNaoLocalizado;
	}

	public void setTotalNaoLocalizado(Integer totalNaoLocalizado) {
		this.totalNaoLocalizado = totalNaoLocalizado;
	}

	public Integer getTotalImportadoComPc() {
		return totalImportadoComPc;
	}

	public void setTotalImportadoComPc(Integer totalImportadoComPc) {
		this.totalImportadoComPc = totalImportadoComPc;
	}

	public Integer getTotalImportadoSemPc() {
		return totalImportadoSemPc;
	}

	public void setTotalImportadoSemPc(Integer totalImportadoSemPc) {
		this.totalImportadoSemPc = totalImportadoSemPc;
	}

	public Integer getTotalJaInativado() {
		return totalJaInativado;
	}

	public void setTotalJaInativado(Integer totalJaInativado) {
		this.totalJaInativado = totalJaInativado;
	}

	public Integer getTotalIgnorado() {
		return totalIgnorado;
	}

	public void setTotalIgnorado(Integer totalIgnorado) {
		this.totalIgnorado = totalIgnorado;
	}

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public List<LoteInativacaoRegistroTO> getRegistros() {
		return registros;
	}

	public void setRegistros(List<LoteInativacaoRegistroTO> registros) {
		this.registros = registros;
	}

	public String getCodigoFormatado() {
		return String.format("LU/%06d", getCodigo());
	}

}
