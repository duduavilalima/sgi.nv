package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.enumerators.Setor;
import br.com.araujoabreu.sgi.enumerators.Situacao;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Endereco;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "OBRA")
@SequenceGenerator(name = "SEQ_OBRA", sequenceName = "SEQ_OBRA", allocationSize = 1)
public class Obra extends AuditEntity implements DomainObject, Serializable {

	private static final long serialVersionUID = -8787966400564665229L;
	private TipoObra tipoObra;
	private Colaborador inativador;
	private Endereco endereco;
	private String nome;
	private Situacao situacao;
	private Setor setor;
	private Date inicioVigencia;
	private Date fimVigencia;
	private BigDecimal m2Total;
	private Short numPavimentos;
	private Short numSalas;
	private Short numLojas;
	private String descricao;
	private Date dataInativacao;
	private Set<ObraCliente> obraClientes = new HashSet<>(0);

	public Obra() {
	}

	public Obra(TipoObra tipoObra, Endereco endereco, Situacao situacao) {
		this.tipoObra = tipoObra;
		this.endereco = endereco;
		this.situacao = situacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_tipo_obra", nullable = false)
	@NotNull
	public TipoObra getTipoObra() {
		return this.tipoObra;
	}

	public void setTipoObra(TipoObra tipoObra) {
		this.tipoObra = tipoObra;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_inativador")
	public Colaborador getInativador() {
		return this.inativador;
	}

	public void setInativador(Colaborador inativador) {
		this.inativador = inativador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_endereco", nullable = false)
	@NotNull
	public Endereco getEndereco() {
		return this.endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Column(name = "nome", unique = true, nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "situacao", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public Situacao getSituacao() {
		return this.situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Column(name = "setor", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public Setor getSetor() {
		return this.setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "inicio_vigencia", nullable = false, length = 13)
	@NotNull
	public Date getInicioVigencia() {
		return this.inicioVigencia;
	}

	public void setInicioVigencia(Date inicioVigencia) {
		this.inicioVigencia = inicioVigencia;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fim_vigencia", nullable = false, length = 13)
	@NotNull
	public Date getFimVigencia() {
		return this.fimVigencia;
	}

	public void setFimVigencia(Date fimVigencia) {
		this.fimVigencia = fimVigencia;
	}

	@Column(name = "m2_total", precision = 8)
	public BigDecimal getM2Total() {
		return this.m2Total;
	}

	public void setM2Total(BigDecimal m2Total) {
		this.m2Total = m2Total;
	}

	@Column(name = "num_pavimentos")
	public Short getNumPavimentos() {
		return this.numPavimentos;
	}

	public void setNumPavimentos(Short numPavimentos) {
		this.numPavimentos = numPavimentos;
	}

	@Column(name = "num_salas")
	public Short getNumSalas() {
		return this.numSalas;
	}

	public void setNumSalas(Short numSalas) {
		this.numSalas = numSalas;
	}

	@Column(name = "num_lojas")
	public Short getNumLojas() {
		return this.numLojas;
	}

	public void setNumLojas(Short numLojas) {
		this.numLojas = numLojas;
	}

	@Column(name = "descricao", length = 2000)
	@Length(max = 2000)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inativacao", length = 29)
	public Date getDataInativacao() {
		return this.dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "obra", cascade = CascadeType.ALL)
	public Set<ObraCliente> getObraClientes() {
		return this.obraClientes;
	}

	public void setObraClientes(Set<ObraCliente> obraClientes) {
		this.obraClientes = obraClientes;
	}
}
