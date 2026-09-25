package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.Hibernate;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um tributo que deve ser aplicado à compra itens ou
 * serviços.
 *
 * @author Rodrigo Barreiros
 */
@Entity
@Table(name = "TRIBUTO")
@SequenceGenerator(name="SEQ_TRIBUTO", sequenceName="SEQ_TRIBUTO", allocationSize=1)
public class Tributo extends AuditEntity implements DomainObject {

	public static final String INSS = "INSS";
	public static final String PIS_COFINS_CSLL = "PIS/COFINS/CSLL";
	public static final String ISS = "ISS";
	public static final String IRRF = "IRRF";
	public static final String INSS_NP = "INSS (NP)";
	public static final String IRRF_NP_PJ = "IRRF/PJ (NP)";
	public static final String IRRF_NP_PF = "IRRF/PF (NP)";
	public static final String IRRF_ALUGUEL = "IRRF ALUGUEL";
	public static final String ASSISTENCIA_NP = "Assistência(NP)";
	public static final Long INSS_CODIGO = 6L;
	public static final Long PIS_COFINS_CSLL_CODIGO = 1L;
	public static final Long ISS_CODIGO = 4L;
	public static final Long IRRF_CODIGO = 5L;
	public static final Long INSS_NP_CODIGO = 12L;
	public static final Long IRRF_NP_CODIGO = 10L;
	public static final Long IRRF_PF_CODIGO = 11L;
	public static final Long IRRF_ALUGUEL_CODIGO = 14L;
	public static final Long ASSISTENCIA_NP_CODIGO = 13L;
	private static final long serialVersionUID = 1L;
	//    private Long codigo;
	private String nome;
	private String descricao;
	private Tipo tipo;
	private List<CategoriaTributo> categorias = new ArrayList<>();
	private ContaContabil contaContabil;
	/**
	 * Indicação se o tributo será exibido na lista de tributos selecionáveis ao se cadastrar uma nota fiscal.
	 */
	private Boolean exibeNota = false;
	private List<ContaContabil> listaContaContabil = new ArrayList<>();

	public Tributo() {
		super();
	}

	public Tributo(Long codigo, String nome) {
		super();
		super.setCodigo(codigo);
		this.nome = nome;
	}

	/**
	 * Adiciona uma dada Categoria de Tributo à lista de categorias.
	 *
	 * @param categoria a categoria a ser adicionada
	 */
	public void addCategoria(CategoriaTributo categoria) {
		categoria.setTributo(this);
		categorias.add(categoria);
	}

	/**
	 * Retorna todos os possíveis tipos de tributos.
	 *
	 * @return os tipos
	 */
	@Transient
	public Tipo[] getTipos() {
		return Tipo.values();
	}

	/**
	 * Determina se o Tributo corrente é ou não Federal
	 *
	 * @return true se Federal, false caso contrário
	 */
	@Transient
	public boolean isFederal() {
		if (tipo != null) {
			return tipo.equals(Tipo.FEDERAL);
		}
		return false;
	}

	/**
	 * Determina se o Tributo corrente é ou não Estadual
	 *
	 * @return true se Estadual, false caso contrário
	 */
	@Transient
	public boolean isEstadual() {
		if (tipo != null) {
			return tipo.equals(Tipo.ESTADUAL);
		}
		return false;
	}

	/**
	 * Determina se o Tributo corrente é ou não Municipal
	 *
	 * @return true se Municipal, false caso contrário
	 */
	@Transient
	public boolean isMunicipal() {
		if (tipo != null) {
			return tipo.equals(Tipo.MUNICIPAL);
		}
		return false;
	}

	@Column(name = "NOME")
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "DESCRICAO")
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "TIPO")
	@NotNull
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tributo")
	@OrderBy(value = "principal, codigo ASC")
	public List<CategoriaTributo> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<CategoriaTributo> categorias) {
		this.categorias = categorias;
	}

	@Transient
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@Transient
	public ContaContabil getContaContabil(Unidade empresa) {
		if (listaContaContabil != null) {
			for (ContaContabil contaContabil : listaContaContabil) {
				if (contaContabil.getEmpresa().equals(empresa)) {
					return contaContabil;
				}
			}
		}
		return null;
	}

	@Column(name = "exibe_nota")
	@NotNull
	public Boolean getExibeNota() {
		return exibeNota;
	}

	public void setExibeNota(Boolean exibeNota) {
		this.exibeNota = exibeNota;
	}

	@ManyToMany(targetEntity = ContaContabil.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
		name = "TRIBUTO_CONTA_CONTABIL",
		joinColumns = @JoinColumn(name = "CODIGO_TRIBUTO"),
		inverseJoinColumns = @JoinColumn(name = "CODIGO_CONTA_CONTABIL")
	)
	public List<ContaContabil> getListaContaContabil() {
		return listaContaContabil;
	}

	public void setListaContaContabil(List<ContaContabil> listaContaContabil) {
		this.listaContaContabil = listaContaContabil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		if (!(Tributo.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		Tributo other = (Tributo) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (nome == null) {
			if (other.getNome() != null) {
				return false;
			}
		} else if (!nome.equals(other.getNome())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Tributo [codigo=" + getCodigo() + ", nome=" + nome + ", descricao="
			+ descricao + ", tipo=" + tipo + ", categorias=" + categorias
			+ ", contaContabil=" + contaContabil + ", exibeNota="
			+ exibeNota + "]";
	}

	public enum Tipo {
		FEDERAL("Federal"),
		ESTADUAL("Estadual"),
		MUNICIPAL("Municipal");

		private final String descricao;

		private Tipo(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

	}

	public enum IdentificaoTributo { //utilizado na interface bancária
		DARF(16, "DARF Normal"),
		DARF_SIMPLES(18, "DARF Simples"),
		GPS(17, "GPS (Guia da Previdência Social)"),
		DARJ(21, "DARJ");

		private final int codigo;
		private final String nome;

		IdentificaoTributo(int codigo, String nome) {
			this.codigo = codigo;
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		public int getCodigo() {
			return codigo;
		}

	}

}
