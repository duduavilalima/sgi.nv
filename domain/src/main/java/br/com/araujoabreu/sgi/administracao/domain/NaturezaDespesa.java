package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.araujoabreu.sgi.enumerators.Situacao;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Representa os possíveis tipos de uma despesa.
 *
 * @author Marcus Lacerda
 */
@Entity
@Table(name = "NATUREZA_DESPESA")
@SequenceGenerator(name = "SEQ_NATUREZA_DESPESA", sequenceName = "SEQ_NATUREZA_DESPESA", allocationSize = 1)
public class NaturezaDespesa extends AuditEntity {

	private static final long serialVersionUID = -2563812268080493626L;
	private Colaborador inativador;
	private Date dataInativacao;
	private List<NaturezaDespesaContaContabil> listaContaContabil = new ArrayList<>();
	private Long codigo;
	private String nome;
	private TipoNaturezaDespesa tipoNaturezaDespesa;
	private ContaContabil contaContabil;
	private Situacao situacao;
	private String chave;

	public NaturezaDespesa() {
	}

	public NaturezaDespesa(final Long codigo) {
		this.codigo = codigo;
	}

	public NaturezaDespesa(final Long codigo, final String nome) {
		super();
		this.codigo = codigo;
		this.nome = nome;
	}

	@Column(name = "NOME")
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_TIPO_NATUREZA_DESPESA")
	public TipoNaturezaDespesa getTipoNaturezaDespesa() {
		return tipoNaturezaDespesa;
	}

	public void setTipoNaturezaDespesa(TipoNaturezaDespesa tipoNaturezaDespesa) {
		this.tipoNaturezaDespesa = tipoNaturezaDespesa;
	}

	@Transient
	public ContaContabil getContaContabil(Unidade empresa) {
		for (NaturezaDespesaContaContabil naturezaDespesaContaContabil : listaContaContabil) {
			if (naturezaDespesaContaContabil.getContaContabil().getEmpresa().equals(empresa)) {
				return naturezaDespesaContaContabil.getContaContabil();
			}
		}
		return null;
	}

	@Transient
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@Column(name = "SITUACAO")
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_INATIVACAO")
	public Colaborador getInativador() {
		return inativador;
	}

	public void setInativador(Colaborador inativador) {
		this.inativador = inativador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_INATIVACAO")
	public Date getDataInativacao() {
		return dataInativacao;
	}

	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "naturezaDespesa")
	public List<NaturezaDespesaContaContabil> getListaContaContabil() {
		return listaContaContabil;
	}

	public void setListaContaContabil(List<NaturezaDespesaContaContabil> listaContaContabil) {
		this.listaContaContabil = listaContaContabil;
	}

	@Column(name = "CHAVE", length = 25)
	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		if (!(obj instanceof NaturezaDespesa)) {
			return false;
		}
		NaturezaDespesa other = (NaturezaDespesa) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (getNome() == null) {
			if (other.getNome() != null) {
				return false;
			}
		} else if (!getNome().equals(other.getNome())) {
			return false;
		}
		if (getSituacao() != other.getSituacao()) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "NaturezaDespesa [codigo=" + codigo + ", nome=" + nome + "]";
	}

}
