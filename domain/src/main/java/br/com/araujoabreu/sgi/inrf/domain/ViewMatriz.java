package br.com.araujoabreu.sgi.inrf.domain;

import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "VW_INRF_MATRIZES")
public class ViewMatriz implements Serializable {

	private static final long serialVersionUID = -8691267315172600019L;

	@Id
	@Column(name = "CODIGO")
	@NotNull
	private Long codigo;

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

	@Column(name = "NATUREZA")
	private String natureza;

	@Column(name = "ATIVIDADE")
	private String atividade;

	public ViewMatriz() {
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
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

	public String getNatureza() {
		return natureza;
	}

	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}

	public String getAtividade() {
		return atividade;
	}

	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}

	public String getIm() {
		return im;
	}

	public void setIm(String im) {
		this.im = im;
	}

	public ViewFilial transformToFilial() {
		ViewFilial filial = new ViewFilial();

		filial.setCnpj(cnpj);
		filial.setCodigo(codigo);
		filial.setIbge(ibge);
		filial.setIe(ie);
		filial.setIm(im);
		filial.setNome(nome);
		filial.setSuframa(suframa);
		filial.setUf(uf);

		return filial;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
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
		if (!(ViewMatriz.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		ViewMatriz other = (ViewMatriz) obj;
		if (cnpj == null) {
			if (other.getCnpj() != null) {
				return false;
			}
		} else if (!cnpj.equals(other.getCnpj())) {
			return false;
		}
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
