package br.com.fasolti.commons.domain;

import java.io.Serializable;

public interface DomainObject extends Serializable {
	Long getId();

	void setId(Long id);
}
