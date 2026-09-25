package br.com.araujoabreu.sgi.inrf.domain;

import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "VW_INRF_PARTICIPANTES")
public class ViewParticipante implements Serializable {

	private static final long serialVersionUID = 6808591198670387368L;

	@Id
	@Column(name = "CODIGO")
	@NotNull
	private Long codigo;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "PAIS")
	private String pais;

	@Column(name = "CNPJ")
	private String cnpj;

	@Column(name = "CPF")
	private String cpf;

	@Column(name = "IE")
	private String ie;

	@Column(name = "IBGE")
	private String ibge;

	@Column(name = "SUFRAMA")
	private String suframa;

	@Column(name = "ENDERECO")
	private String endereco;

	@Column(name = "NUMERO")
	private String numero;

	@Column(name = "COMPLEMENTO")
	private String complemento;

	@Column(name = "BAIRRO")
	private String bairro;

	public ViewParticipante() {
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

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getIe() {
		return ie;
	}

	public void setIe(String ie) {
		this.ie = ie;
	}

	public String getIbge() {
		return ibge;
	}

	public void setIbge(String ibge) {
		this.ibge = ibge;
	}

	public String getSuframa() {
		return suframa;
	}

	public void setSuframa(String suframa) {
		this.suframa = suframa;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(ViewParticipante.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		ViewParticipante other = (ViewParticipante) obj;
		if (codigo == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!codigo.equals(other.getCodigo())) {
			return false;
		}
		return true;
	}

}
