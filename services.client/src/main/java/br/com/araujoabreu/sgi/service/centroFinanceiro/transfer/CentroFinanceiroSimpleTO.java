package br.com.araujoabreu.sgi.service.centroFinanceiro.transfer;

import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoTO;

public class CentroFinanceiroSimpleTO {
	private Long codigo;
	private String nome;
	private String identificador;
	private String identificadorFinal;
	private ContratoTO contratoTO;

	private String situacao;
	private Boolean cumulativo;
	private String observacao;

	public CentroFinanceiroSimpleTO() {
	}

	public CentroFinanceiroSimpleTO(Long codigo) {
		this.codigo = codigo;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getIdentificadorFinal() {
		return identificadorFinal;
	}

	public void setIdentificadorFinal(String identificadorFinal) {
		this.identificadorFinal = identificadorFinal;
	}

	public ContratoTO getContratoTO() {
		return contratoTO;
	}

	public void setContratoTO(ContratoTO contratoTO) {
		this.contratoTO = contratoTO;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Boolean getCumulativo() {
		return cumulativo;
	}

	public void setCumulativo(Boolean cumulativo) {
		this.cumulativo = cumulativo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}

