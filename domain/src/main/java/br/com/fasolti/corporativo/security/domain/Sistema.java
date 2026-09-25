package br.com.fasolti.corporativo.security.domain;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Representa o sistema corrente. Usado para implementar regras de segurança e para implementar o componete de menu.
 *
 * @author Rodrigo Barreiros
 * @since 0.2; 14/02/2009
 */
@Entity
@Table(name = "SISTEMA")
@SequenceGenerator(name = "SEQ_SISTEMA", sequenceName = "SEQ_SISTEMA", allocationSize = 1)
public class Sistema implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SISTEMA")
	@Column(name = "CODIGO", nullable = false)
	@Id
	private Long codigo;

	@Column(name = "NOME", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	private String nome;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sistema")
	private List<Modulo> modulos;

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

	public List<Modulo> getModulos() {
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}
}
