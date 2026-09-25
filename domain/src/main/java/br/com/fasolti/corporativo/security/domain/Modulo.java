package br.com.fasolti.corporativo.security.domain;

import br.com.fasolti.commons.domain.DomainObject;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "MODULO")
@SequenceGenerator(name = "SEQ_MODULO", sequenceName = "SEQ_MODULO", allocationSize = 1)
public class Modulo implements MenuableObject, DomainObject {

	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MODULO")
	@Column(name = "CODIGO", nullable = false)
	@Id
	private Long codigo;
	@Column(name = "NOME", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	private String nome;
	@Column(name = "ORDEM", nullable = false)
	@NotNull
	private Integer ordem;
	@Column(name = "CONTEXTO", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	private String contexto;
	@Column(name = "STATUS", nullable = false)
	@NotNull
	private Status status;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_SISTEMA")
	private Sistema sistema;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_MODULO_PAI")
	private Modulo modulo;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modulo")
	private List<Funcionalidade> funcionalidades;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modulo")
	private List<Modulo> modulos;

	@Override
	public Long getId() {
		return codigo;
	}

	@Override
	public void setId(Long id) {
		this.codigo = id;
	}

	@Override
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Override
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getContexto() {
		return contexto;
	}

	public void setContexto(String contexto) {
		this.contexto = contexto;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public List<Funcionalidade> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<Funcionalidade> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	public List<Modulo> getModulos() {
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}

	@Override
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Override
	public MenuableObject getParent() {
		return modulo;
	}

	@Override
	public List<MenuableObject> getChildren() {
		List<MenuableObject> children = new ArrayList<>();
		children.addAll(modulos);
		children.addAll(funcionalidades);
		return children;
	}

	@Override
	public boolean isAvailable() {
		return status.equals(Status.INSTALADO);
	}

	@Override
	public String getSecurityContext() {
		return null;
	}

	@Override
	public String getLink() {
		return contexto;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Modulo)) return false;
		Modulo modulo1 = (Modulo) o;
		return Objects.equals(getCodigo(), modulo1.getCodigo()) &&
			Objects.equals(getNome(), modulo1.getNome()) &&
			Objects.equals(getOrdem(), modulo1.getOrdem()) &&
			Objects.equals(getContexto(), modulo1.getContexto()) &&
			status == modulo1.status &&
			Objects.equals(getSistema(), modulo1.getSistema()) &&
			Objects.equals(getModulo(), modulo1.getModulo()) &&
			Objects.equals(getFuncionalidades(), modulo1.getFuncionalidades()) &&
			Objects.equals(getModulos(), modulo1.getModulos());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigo(), getNome(), getOrdem(), getContexto(), status, getSistema(), getModulo(), getFuncionalidades(), getModulos());
	}

	@Override
	public String toString() {
		return "Modulo{" +
			"codigo=" + codigo +
			", nome='" + nome + '\'' +
			", ordem=" + ordem +
			", contexto='" + contexto + '\'' +
			", status=" + status +
			", sistema=" + sistema +
			", modulo=" + modulo +
			", funcionalidades=" + funcionalidades +
			", modulos=" + modulos +
			'}';
	}

	public enum Status {DESINSTALADO, INSTALADO}
}
