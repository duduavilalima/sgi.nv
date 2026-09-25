package br.com.araujoabreu.sgi.service.telefone.transfer;

import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.contato.transfer.ContatoTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

import java.util.Date;

public class TelefoneTO extends BaseTO {

	private PessoaTO pessoa;
	private ContatoTO contato;
	private String ddd;
	private String numero;
	private String ramal;
	private String tipo;
	private Boolean principal;
	private OperadoraTO operadora;
	private Boolean selecao;
	private Long codigoOperadora;
	private String dddTelefoneRamal;
	private String criador;
	private Date dataCriacao;
	private String atualizador;
	private Date dataAtualizacao;

	public TelefoneTO() {
	}

	public TelefoneTO(Long codigo, String ddd, String numero, String tipo, Boolean principal, ContatoTO contatoTO) {
		this.setCodigo(codigo);
		this.ddd = ddd;
		this.numero = numero;
		this.tipo = tipo;
		this.principal = principal;
	}

	public PessoaTO getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaTO pessoa) {
		this.pessoa = pessoa;
	}

	public ContatoTO getContato() {
		return contato;
	}

	public void setContato(ContatoTO contato) {
		this.contato = contato;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public OperadoraTO getOperadora() {
		return operadora;
	}

	public void setOperadora(OperadoraTO operadora) {
		this.operadora = operadora;
	}

	public Boolean getSelecao() {
		return selecao;
	}

	public void setSelecao(Boolean selecao) {
		this.selecao = selecao;
	}

	public Long getCodigoOperadora() {
		return codigoOperadora;
	}

	public void setCodigoOperadora(Long codigoOperadora) {
		this.codigoOperadora = codigoOperadora;
	}

	public String getDddTelefoneRamal() {
		StringBuilder sb = new StringBuilder();
		if (numero != null) {
			if (ddd != null) {
				sb.append("(").append(ddd).append(") ").append(numero);
			}
		}

		dddTelefoneRamal = sb.toString();

		return dddTelefoneRamal;
	}

	public void setDddTelefoneRamal(String dddTelefoneRamal) {
		this.dddTelefoneRamal = dddTelefoneRamal;
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
