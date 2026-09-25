package br.com.fasolti.corporativo.security.domain;

import br.com.fasolti.framework.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "USUARIO_PERFIL")
@SequenceGenerator(name = "SEQ_USUARIO_PERFIL", sequenceName = "SEQ_USUARIO_PERFIL", allocationSize = 1)
public class UsuarioPerfil extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 8929374407432281840L;

	private Usuario usuario;
	private Perfil perfil;

	public UsuarioPerfil() {
	}

	public UsuarioPerfil(Usuario usuario, Perfil perfil) {
		this.usuario = usuario;
		this.perfil = perfil;
	}

	@ManyToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "CODIGO_USUARIO", nullable = false)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne(targetEntity = Perfil.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "CODIGO_PERFIL")
	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	@Override
	public String toString() {
		return "UsuarioPerfil [usuario=" + usuario + ", perfil=" + perfil + "]";
	}

}
