package br.com.fasolti.corporativo.security.domain;

import org.hibernate.validator.NotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;


@Entity
@Table(name = "PERMISSAO")
public class Permissao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private String alvo;
	private String acao;
	private Receptor receptor;

	@Id
	@GeneratedValue
	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@Transient
	public String getChaveReceptor() {
		return receptor.getChave();
	}

	public void setChaveReceptor(String receptor) {
	}

	public String getAlvo() {
		return alvo;
	}

	public void setAlvo(String target) {
		this.alvo = target;
	}


	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	@Transient
	public String getDiscriminador() {
		return receptor.getTipo().toString();
	}

	public void setDiscriminador(String discriminador) {
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CODIGO_RECEPTOR", nullable = false)
	@NotNull
	public Receptor getReceptor() {
		return receptor;
	}

	public void setReceptor(Receptor receptor) {
		this.receptor = receptor;
	}


}
