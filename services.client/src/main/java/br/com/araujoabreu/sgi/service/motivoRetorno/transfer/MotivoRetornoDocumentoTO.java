package br.com.araujoabreu.sgi.service.motivoRetorno.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;
import java.util.List;

public class MotivoRetornoDocumentoTO extends BaseTO {
	private String observacao;
	private PessoaTO pessoa;
	private MotivoAplicacaoTO aplicacao;

	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;
	private List<MotivoDocumentoTO> listaMotivoRetorno;

	public MotivoRetornoDocumentoTO() {
	}

	public MotivoRetornoDocumentoTO(Long codigo, String observacao, PessoaTO pessoa) {
		this.setCodigo(codigo);
		this.observacao = observacao;
		this.pessoa = pessoa;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public PessoaTO getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaTO pessoa) {
		this.pessoa = pessoa;
	}

	public MotivoAplicacaoTO getAplicacao() {
		return aplicacao;
	}

	public void setAplicacao(MotivoAplicacaoTO aplicacao) {
		this.aplicacao = aplicacao;
	}

	public List<MotivoDocumentoTO> getListaMotivoRetorno() {
		return listaMotivoRetorno;
	}

	public void setListaMotivoRetorno(List<MotivoDocumentoTO> listaMotivoRetorno) {
		this.listaMotivoRetorno = listaMotivoRetorno;
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

	public String getAtualizador() {
		return atualizador;
	}

	public void setAtualizador(String atualizador) {
		this.atualizador = atualizador;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
}
