package br.com.araujoabreu.sgi.service.menu.transfer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ModuloTO {

	private String contexto;

	private String status;

	private Long codigo;
	private String nome;
	private Integer ordem;
	private String securityContext;
	private String link;
	private String url;

	private List<ModuloTO> funcionalidades;

	private List<ModuloTO> modulos;

	private List<ModuloTO> children;

	public List<ModuloTO> getChildren() {
		this.children = new ArrayList<>();
		if (modulos != null) {
			children.addAll(modulos);
		}
		if (funcionalidades != null) {
			children.addAll(funcionalidades);
		}

		children.sort((o1, o2) -> o1.getOrdem().compareTo(o2.getOrdem()));

		return this.children;
	}

	public void setChildren(List<ModuloTO> children) {
		this.children = children;
	}

	public String getContexto() {
		return contexto;
	}

	public void setContexto(String contexto) {
		this.contexto = contexto;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getSecurityContext() {
		return securityContext;
	}

	public void setSecurityContext(String securityContext) {
		this.securityContext = securityContext;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<ModuloTO> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<ModuloTO> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	public List<ModuloTO> getModulos() {
		return modulos;
	}

	public void setModulos(List<ModuloTO> modulos) {
		this.modulos = modulos;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ModuloTO)) return false;
		ModuloTO moduloTO = (ModuloTO) o;
		return Objects.equals(getContexto(), moduloTO.getContexto()) &&
			Objects.equals(getStatus(), moduloTO.getStatus()) &&
			Objects.equals(getCodigo(), moduloTO.getCodigo()) &&
			Objects.equals(getNome(), moduloTO.getNome()) &&
			Objects.equals(getOrdem(), moduloTO.getOrdem()) &&
			Objects.equals(getSecurityContext(), moduloTO.getSecurityContext()) &&
			Objects.equals(getLink(), moduloTO.getLink()) &&
			Objects.equals(getUrl(), moduloTO.getUrl()) &&
			Objects.equals(getFuncionalidades(), moduloTO.getFuncionalidades()) &&
			Objects.equals(getModulos(), moduloTO.getModulos()) &&
			Objects.equals(getChildren(), moduloTO.getChildren());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getContexto(), getStatus(), getCodigo(), getNome(), getOrdem(), getSecurityContext(), getLink(), getUrl(), getFuncionalidades(), getModulos(), getChildren());
	}

	@Override
	public String toString() {
		return "ModuloTO{" +
			"contexto='" + contexto + '\'' +
			", status='" + status + '\'' +
			", codigo=" + codigo +
			", nome='" + nome + '\'' +
			", ordem=" + ordem +
			", securityContext='" + securityContext + '\'' +
			", link='" + link + '\'' +
			", url='" + url + '\'' +
			", funcionalidades=" + funcionalidades +
			", modulos=" + modulos +
			", children=" + children +
			'}';
	}
}
