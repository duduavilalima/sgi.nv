package br.com.araujoabreu.sgi.inrf.domain;

import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "VW_INRF_FILIAIS")
public class ViewFilial implements Serializable {

	private static final long serialVersionUID = 6808591198670387368L;

	@Id
	@Column(name = "CODIGO")
	@NotNull
	private Long codigo;

	@Column(name = "CODIGO_MATRIZ")
	private Long matriz;

	@Column(name = "CNPJ")
	private String cnpj;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "UF")
	private String uf;

	@Column(name = "IE")
	private String ie;

	@Column(name = "IM")
	private String im;

	@Column(name = "IBGE")
	private String ibge;

	@Column(name = "SUFRAMA")
	private String suframa;

	public ViewFilial() {
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Long getMatriz() {
		return matriz;
	}

	public void setMatriz(Long matriz) {
		this.matriz = matriz;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getIe() {
		return ie;
	}

	public void setIe(String ie) {
		this.ie = ie;
	}

	public String getIm() {
		return im;
	}

	public void setIm(String im) {
		this.im = im;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((matriz == null) ? 0 : matriz.hashCode());
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
		if (!(ViewFilial.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		ViewFilial other = (ViewFilial) obj;
		if (codigo == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!codigo.equals(other.getCodigo())) {
			return false;
		}
		if (matriz == null) {
			if (other.getMatriz() != null) {
				return false;
			}
		} else if (!matriz.equals(other.getMatriz())) {
			return false;
		}
		return true;
	}

}
