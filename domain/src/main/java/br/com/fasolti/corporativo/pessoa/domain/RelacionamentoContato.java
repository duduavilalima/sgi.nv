package br.com.fasolti.corporativo.pessoa.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.security.domain.Perfil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "RELACIONAMENTO_CONTATO")
@SequenceGenerator(name = "SEQ_RELACIONAMENTO_CONTATO", sequenceName = "SEQ_RELACIONAMENTO_CONTATO", allocationSize = 1)
public class RelacionamentoContato extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 3955265992676494944L;
	private String nome;
	private Perfil perfil;

	@Column(name = "NOME", nullable = false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_PERFIL", nullable = false)
	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RelacionamentoContato)) return false;
		if (!super.equals(o)) return false;
		RelacionamentoContato that = (RelacionamentoContato) o;
		return Objects.equals(getNome(), that.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome());
	}
}
