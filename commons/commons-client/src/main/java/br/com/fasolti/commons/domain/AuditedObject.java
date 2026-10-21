package br.com.fasolti.commons.domain;

import java.util.Date;

public interface AuditedObject {

	String getCreatedBy();

	void setCreatedBy(String name);

	Date getCreationDate();

	void setCreationDate(Date date);

	String getModifiedBy();

	void setModifiedBy(String name);

	Date getModificationDate();

	void setModificationDate(Date date);

}
