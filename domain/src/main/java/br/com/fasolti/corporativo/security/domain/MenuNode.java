package br.com.fasolti.corporativo.security.domain;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * Representa um nó de menu e e criado a partir de um MenuableObject.
 * Implementa a interface Comparable e se ordena pela propriedade ordem do MenuableObject.
 */
public class MenuNode implements Serializable, Comparable<MenuNode> {
	private static final long serialVersionUID = 1L;

	private final MenuNode parent;

	private final Set<MenuNode> children;

	private final Integer order;

	private final Long id;

	private final String name;

	private final String link;

	public MenuNode(MenuableObject menuInfo) {
		this(null, menuInfo);
	}

	public MenuNode(MenuNode parent, MenuableObject object) {
		this.parent = parent;

		this.order = object.getOrdem();
		this.id = object.getCodigo();
		this.name = object.getNome();
		this.link = object.getLink();

		children = new TreeSet<>();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLink() {
		return link;
	}

	public Set<MenuNode> getChildren() {
		return children;
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public MenuNode getParent() {
		return parent;
	}

	public boolean hasParent() {
		return parent != null;
	}

	public Integer getOrder() {
		return order;
	}

	@Override
	public int compareTo(MenuNode that) {
		if (that == null) return 1;
		return this.getOrder().compareTo(that.getOrder());
	}
}
