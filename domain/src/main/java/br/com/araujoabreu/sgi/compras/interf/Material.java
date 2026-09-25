package br.com.araujoabreu.sgi.compras.interf;

import br.com.araujoabreu.sgi.compras.domain.CategoriaItemCompra;

import java.util.List;

public interface Material {

	Long getCodigo();

	String getSigla();

	String getNome();

	TipoMaterial getTipo();

	CategoriaItemCompra getPai();

	List<Material> getChildren();

	void setChildren(List<Material> list);

	Boolean isLoaded();

	enum TipoMaterial {AGRUPADOR, PRODUTO, EMPTY}

}
