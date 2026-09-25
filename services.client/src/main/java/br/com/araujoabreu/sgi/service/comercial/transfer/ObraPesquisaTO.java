package br.com.araujoabreu.sgi.service.comercial.transfer;

import br.com.araujoabreu.sgi.service.cidade.transfer.CidadeTO;
import br.com.araujoabreu.sgi.service.util.BaseSearchTO;

import java.util.Date;

public class ObraPesquisaTO extends BaseSearchTO {
	private String nome;
	private TipoObraTO tipoObraTO;
	private String situacao;
	private CidadeTO cidade;
	private String uf;
	private Date inicioVigencia;
	private Date fimVigencia;
	private String setor;
	private String cliente;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoObraTO getTipoObraTO() {
		return tipoObraTO;
	}

	public void setTipoObraTO(TipoObraTO tipoObraTO) {
		this.tipoObraTO = tipoObraTO;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public Date getInicioVigencia() {
		return inicioVigencia;
	}

	public void setInicioVigencia(Date inicioVigencia) {
		this.inicioVigencia = inicioVigencia;
	}

	public Date getFimVigencia() {
		return fimVigencia;
	}

	public void setFimVigencia(Date fimVigencia) {
		this.fimVigencia = fimVigencia;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public CidadeTO getCidade() {
		return cidade;
	}

	public void setCidade(CidadeTO cidade) {
		this.cidade = cidade;
	}

}
