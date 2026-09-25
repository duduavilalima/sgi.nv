package br.com.araujoabreu.sgi.compras.domain;

import br.com.araujoabreu.sgi.administracao.domain.ContratoAnexo;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface;
import br.com.araujoabreu.sgi.controleFiscal.domain.ReinfEventoAnexo;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ANEXO")
@SequenceGenerator(name = "SEQ_ANEXO", sequenceName = "SEQ_ANEXO", allocationSize = 1)
public class Anexo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8931066992381082741L;
	private String nome;
	private String descricao;
	private String contentType;
	private List<NotaAnexo> notas = new ArrayList<>();
	private Set<ContratoAnexo> contratoAnexos = new HashSet<>(0);
	private List<LoteInterface> loteInterfaces = new ArrayList<>();
	private List<ReinfEventoAnexo> eventos;
	private String criador;
	private Date dataCriacao;

	private File arquivo;
	private String path;
	private String nomeTemporario;

	public Anexo() {

	}

	public Anexo(String nome, String contentType, String descricao, String criador, Date dataCriacao) {
		this.nome = nome;
		this.descricao = descricao;
		setCriador(criador);
		setDataCriacao(dataCriacao);
		this.contentType = contentType;
	}

	public Anexo(String nome, String contentType, String descricao, String criador, Date dataCriacao, File arquivo) {
		this.nome = nome;
		this.descricao = descricao;
		setCriador(criador);
		setDataCriacao(dataCriacao);
		this.contentType = contentType;
		this.arquivo = arquivo;
	}

	public Anexo(Long codigo) {
		setCodigo(codigo);
	}

	public Anexo(String nome,
				 String contentType,
				 String descricao,
				 String criador,
				 Date dataCriacao,
				 File arquivo,
				 String path) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		setCriador(criador);
		setDataCriacao(dataCriacao);
		this.contentType = contentType;
		this.arquivo = arquivo;
		this.path = path;
	}

	@Column(name = "NOME", nullable = false)
	@NotNull
	@Length(max = 255)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "DESCRICAO", nullable = false, length = 2000)
	@NotNull
	@Length(max = 2000)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "CONTENT_TYPE", nullable = false, length = 150)
	@NotNull
	@Length(max = 150)
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "anexo")
	public List<NotaAnexo> getNotas() {
		return notas;
	}

	public void setNotas(List<NotaAnexo> notas) {
		this.notas = notas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "anexo")
	public Set<ContratoAnexo> getContratoAnexos() {
		return contratoAnexos;
	}

	public void setContratoAnexos(Set<ContratoAnexo> contratoAnexos) {
		this.contratoAnexos = contratoAnexos;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
		name = "LOTE_INTERFACE_ANEXO",
		joinColumns = {@JoinColumn(name = "CODIGO_ANEXO", nullable = false)},
		inverseJoinColumns = {@JoinColumn(name = "CODIGO_LOTE_INTERFACE", nullable = false)})
	public List<LoteInterface> getLoteInterfaces() {
		return loteInterfaces;
	}

	public void setLoteInterfaces(List<LoteInterface> loteInterfaces) {
		this.loteInterfaces = loteInterfaces;
	}

	public void addLoteInterfaces(LoteInterface loteInterface) {
		if (loteInterfaces != null) {
			loteInterfaces.add(loteInterface);
		}
	}

	@OneToMany(mappedBy = "anexo")
	@LazyCollection(LazyCollectionOption.TRUE)
	public List<ReinfEventoAnexo> getEventos() {
		return eventos;
	}

	public void setEventos(List<ReinfEventoAnexo> eventos) {
		this.eventos = eventos;
	}

	@Column(name = "CRIADOR", updatable = false)
	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CRIACAO", updatable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Transient
	public File getArquivo() {
		return arquivo;
	}

	public void setArquivo(File arquivo) {
		this.arquivo = arquivo;
	}

	@Transient
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Transient
	public String getNomeTemporario() {
		return nomeTemporario;
	}

	public void setNomeTemporario(String nomeTemporario) {
		this.nomeTemporario = nomeTemporario;
	}

	@Transient
	public String getNomeCompleto() {
		return this.getCodigo() + "_" + this.getNome();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((getContentType() == null) ? 0 : getContentType().hashCode());
		result = prime * result + ((getCriador() == null) ? 0 : getCriador().hashCode());
		result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
		result = prime * result + ((getDescricao() == null) ? 0 : getDescricao().hashCode());
		result = prime * result + ((getNome() == null) ? 0 : getNome().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(Anexo.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Anexo other = (Anexo) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (getContentType() == null) {
			if (other.getContentType() != null) {
				return false;
			}
		} else if (!getContentType().equals(other.getContentType())) {
			return false;
		}
		if (getCriador() == null) {
			if (other.getCriador() != null) {
				return false;
			}
		} else if (!getCriador().equals(other.getCriador())) {
			return false;
		}
		if (getDataCriacao() == null) {
			if (other.getDataCriacao() != null) {
				return false;
			}
		} else if (!getDataCriacao().equals(other.getDataCriacao())) {
			return false;
		}
		if (getDescricao() == null) {
			if (other.getDescricao() != null) {
				return false;
			}
		} else if (!getDescricao().equals(other.getDescricao())) {
			return false;
		}
		if (getNome() == null) {
			if (other.getNome() != null) {
				return false;
			}
		} else if (!getNome().equals(other.getNome())) {
			return false;
		}
		return true;
	}

}
