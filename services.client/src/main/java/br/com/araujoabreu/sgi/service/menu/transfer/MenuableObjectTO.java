package br.com.araujoabreu.sgi.service.menu.transfer;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MenuableObjectTO {

	private Long codigo;
	private String nome;
	private Integer ordem;
	private List<MenuableObjectTO> children;
	private String securityContext;
	private String link;
	private String url;

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

	public List<MenuableObjectTO> getChildren() {
		if (children != null) {
			children.sort((o1, o2) -> o1.getOrdem().compareTo(o2.getOrdem()));
		}
		return children;
	}

	public void setChildren(List<MenuableObjectTO> children) {
		this.children = children;
	}

	public String getSecurityContext() {
		return url;
	}

	public void setSecurityContext(String securityContext) {
		this.securityContext = securityContext;
	}

	public String getLink() {
		return url;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MenuableObjectTO)) return false;
		MenuableObjectTO that = (MenuableObjectTO) o;
		return Objects.equals(getCodigo(), that.getCodigo()) &&
			Objects.equals(getNome(), that.getNome()) &&
			Objects.equals(getOrdem(), that.getOrdem()) &&
			Objects.equals(getChildren(), that.getChildren()) &&
			Objects.equals(getSecurityContext(), that.getSecurityContext()) &&
			Objects.equals(getLink(), that.getLink()) &&
			Objects.equals(getUrl(), that.getUrl());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigo(), getNome(), getOrdem(), getChildren(), getSecurityContext(), getLink(), getUrl());
	}

	@Override
	public String toString() {
		return "MenuableObjectTO{" +
			"codigo=" + codigo +
			", nome='" + nome + '\'' +
			", ordem=" + ordem +
			", children=" + children +
			", securityContext='" + securityContext + '\'' +
			", link='" + link + '\'' +
			", url='" + url + '\'' +
			'}';
	}
}
