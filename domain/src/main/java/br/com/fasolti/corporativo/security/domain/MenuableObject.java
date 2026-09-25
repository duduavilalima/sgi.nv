package br.com.fasolti.corporativo.security.domain;

import java.io.Serializable;
import java.util.List;

public interface MenuableObject extends Serializable {
	Long getCodigo();

	String getNome();

	Integer getOrdem();

	boolean isAvailable();

	MenuableObject getParent();

	List<MenuableObject> getChildren();

	String getSecurityContext();

	String getLink();
}
