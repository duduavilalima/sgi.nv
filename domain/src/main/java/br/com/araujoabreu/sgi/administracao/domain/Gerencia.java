package br.com.araujoabreu.sgi.administracao.domain;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GERENCIA")
public class Gerencia extends CentroFinanceiro implements Serializable {

	private static final long serialVersionUID = 4057279320766992531L;
	private String numero;
	private Diretoria diretoria;
	private Unidade unidade;
	private Colaborador gerente;
	private Boolean obrigaAvaliacao;
	private Boolean habilitaFaturamento;
	private List<Contrato> listaContratos = new ArrayList<>();
	private Integer proximaOC;

	private List<RegionalGerencia> listaRegionais;

	public Gerencia() {
		setTipo(TipoCentroFinanceiro.CENTRO_CUSTO);
	}

	@Override
	@Column(name = "NUMERO", nullable = false)
	@NotNull
	public String getNumero() {
		return numero;
	}

	public void setNumero(final String numero) {
		this.numero = numero;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CODIGO_DIRETORIA", nullable = false)
	@NotNull
	public Diretoria getDiretoria() {
		return diretoria;
	}

	public void setDiretoria(final Diretoria diretoria) {
		this.diretoria = diretoria;
	}

	@Override
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CODIGO_UNIDADE", insertable = false, updatable = false)
	public Unidade getUnidade() {
		return this.unidade;
	}

	@Override
	public void setUnidade(final Unidade unidade) {
		this.unidade = unidade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_GERENTE")
	public Colaborador getGerente() {
		return gerente;
	}

	public void setGerente(final Colaborador gerente) {
		this.gerente = gerente;
	}

	@Column(name = "obriga_avaliacao", nullable = false)
	@NotNull
	public Boolean getObrigaAvaliacao() {
		return obrigaAvaliacao;
	}

	public void setObrigaAvaliacao(Boolean obrigaAvaliacao) {
		this.obrigaAvaliacao = obrigaAvaliacao;
	}

	@ManyToMany(targetEntity = Contrato.class, fetch = FetchType.LAZY)
	@JoinTable(
		name = "CONTRATO_GERENCIA",
		joinColumns = @JoinColumn(name = "CODIGO_GERENCIA"),
		inverseJoinColumns = @JoinColumn(name = "CODIGO_CONTRATO")
	)
	public List<Contrato> getListaContratos() {
		return listaContratos;
	}

	public void setListaContratos(List<Contrato> listaContratos) {
		this.listaContratos = listaContratos;
	}

	@Column(name = "PROXIMA_OC", nullable = false, insertable = false, updatable = false)
	public Integer getProximaOC() {
		return proximaOC;
	}

	public void setProximaOC(Integer proximaOC) {
		this.proximaOC = proximaOC;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gerencia")
	public List<RegionalGerencia> getListaRegionais() {
		return listaRegionais;
	}

	public void setListaRegionais(List<RegionalGerencia> listaRegionais) {
		this.listaRegionais = listaRegionais;
	}

	@Column(name = "habilita_faturamento", nullable = false)
	@NotNull
	public Boolean getHabilitaFaturamento() {
		return habilitaFaturamento;
	}

	public void setHabilitaFaturamento(Boolean habilitaFaturamento) {
		this.habilitaFaturamento = habilitaFaturamento;
	}

	@Transient
	public String getObrigaAvaliacaoTxt() {
		if (obrigaAvaliacao != null && obrigaAvaliacao)
			return "Sim";
		return "Não";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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

		/*
		 * O teste "!super.equals(obj)" somente deve ser utilizado se a classe HERDAR de uma outra classe da aplicação,
		 * com exceção das classes de auditoria
		 */
		if (!super.equals(obj)) {
			return false;
		}

		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		if (!(obj instanceof Gerencia)) {
			return false;
		}

		Gerencia other = (Gerencia) obj;

		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}

		if (getNumero() == null) {
			if (other.getNumero() != null) {
				return false;
			}
		} else if (!getNumero().equals(other.getNumero())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Gerencia [numero=" + numero + ", getCodigo()=" + getCodigo() + ", getNome()=" + getNome() + "]";
	}

}
