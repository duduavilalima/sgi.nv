package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.domain.Cidade;
import org.hibernate.annotations.Cascade;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Representa uma categoria do tributo. Tributos pussuem categorias em funções dos códigos
 * de receita. Para cada código de receita, existe uma categoria de tributo.
 *
 * @author Rodrigo Barreiros
 */
@Entity
@Table(name = "CATEGORIA_TRIBUTO")
@SequenceGenerator(name = "SEQ_CATEGORIA_TRIBUTO", sequenceName = "SEQ_CATEGORIA_TRIBUTO", allocationSize = 1)
public class CategoriaTributo extends AuditEntity implements DomainObject, Detail {

	private static final long serialVersionUID = -8089707788239557626L;
	private Integer codigoReceita;
	private String descricao;
	private TipoAliquota tipoAliquota;
	private String observacao;
	private Tributo tributo;
	private List<Aliquota> aliquotas = new ArrayList<>();
	private Cidade cidade;
	private Boolean principal = false;
	private BigDecimal minimoGuia;
	private DataReferencia referenciaData;
	private Integer periodoApuracao;
	private Antecipacao antecipaPosterga;
	private Integer primeiroDia;
	private Date vigenciaInicio;
	private Date vigenciaFim;
	private boolean categoriaPrincipal;
	private Boolean habilitaFaturamento;
	private Boolean desoneracaoFolha;

	// O campo abaixo foi criado pois o campo 'principal' não está obedecendo o valor setado
	// ao gravar um tributo. Após selecionar o campo na tela e salvar, ele seta o valor correto,
	// mas na hora de gravar o tributo, por algum motivo ele está voltando ao valor anterior.
	@Transient
	public boolean isCategoriaPrincipal() {
		return categoriaPrincipal;
	}

	public void setCategoriaPrincipal(boolean categoriaPrincipal) {
		this.categoriaPrincipal = categoriaPrincipal;
	}

	public void addAliquota(Aliquota aliquota) {
		aliquota.setCategoriaTributo(this);
		aliquotas.add(aliquota);
	}

	/**
	 * Determina se a Alíquota corrente é ou não Fixa
	 *
	 * @return true se Fixa, false caso contrário
	 */
	@Transient
	public boolean isFixaComposta() {
		if (tipoAliquota != null) {
			return tipoAliquota.equals(TipoAliquota.FIXA_COMPOSTA);
		}
		return false;
	}

	/**
	 * Determina se a Alíquota corrente é ou não Fixa
	 *
	 * @return true se Fixa, false caso contrário
	 */
	@Transient
	public boolean isFixa() {
		if (tipoAliquota != null) {
			return tipoAliquota.equals(TipoAliquota.FIXA);
		}
		return false;
	}

	/**
	 * Determina se a Alíquota corrente é ou não Variavel
	 *
	 * @return true se Variavel, false caso contrário
	 */
	@Transient
	public boolean isVariavel() {
		if (tipoAliquota != null) {
			return tipoAliquota.equals(TipoAliquota.VARIAVEL);
		}
		return false;
	}

	/**
	 * Retorna todos os possíveis tipos de alíquotas.
	 *
	 * @return os tipos
	 */
	@Transient
	public TipoAliquota[] getTipos() {
		return TipoAliquota.values();
	}

	@Column(name = "CODIGO_RECEITA")
	@NotNull
	public Integer getCodigoReceita() {
		return codigoReceita;
	}

	public void setCodigoReceita(Integer codigoReceita) {
		this.codigoReceita = codigoReceita;
	}

	@Column(name = "DESCRICAO")
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "TIPO_ALIQUOTA")
	@NotNull
	public TipoAliquota getTipoAliquota() {
		return tipoAliquota;
	}

	public void setTipoAliquota(TipoAliquota tipoAliquota) {
		this.tipoAliquota = tipoAliquota;
	}

	@Column(name = "OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_TRIBUTO")
	@NotNull
	public Tributo getTributo() {
		return tributo;
	}

	public void setTributo(Tributo tributo) {
		this.tributo = tributo;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "categoriaTributo")
	@Cascade({org.hibernate.annotations.CascadeType.ALL})
	@OrderBy(value = "limiteInferior")
	public List<Aliquota> getAliquotas() {
		return aliquotas;
	}

	public void setAliquotas(List<Aliquota> aliquotas) {
		this.aliquotas = aliquotas;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_municipio_ect")
	@Cascade({org.hibernate.annotations.CascadeType.DETACH})
	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	@Override
	@Column(name = "principal")
	@NotNull
	public Boolean getPrincipal() {
		return principal;
	}

	@Override
	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	@Column(name = "minimo_guia")
	@NotNull
	public BigDecimal getMinimoGuia() {
		return minimoGuia;
	}

	public void setMinimoGuia(BigDecimal minimoGuia) {
		this.minimoGuia = minimoGuia;
	}

	@Column(name = "referencia_data")
	@NotNull
	public DataReferencia getReferenciaData() {
		return referenciaData;
	}

	public void setReferenciaData(DataReferencia referenciaData) {
		this.referenciaData = referenciaData;
	}

	@Column(name = "periodo_apuracao")
	@NotNull
	public Integer getPeriodoApuracao() {
		return periodoApuracao;
	}

	public void setPeriodoApuracao(Integer periodoApuracao) {
		this.periodoApuracao = periodoApuracao;
	}

	@Column(name = "antecipa_posterga")
	@NotNull
	public Antecipacao getAntecipaPosterga() {
		return antecipaPosterga;
	}

	public void setAntecipaPosterga(Antecipacao antecipaPosterga) {
		this.antecipaPosterga = antecipaPosterga;
	}

	@Column(name = "primeiro_dia")
	@NotNull
	public Integer getPrimeiroDia() {
		return primeiroDia;
	}

	public void setPrimeiroDia(Integer primeiroDia) {
		this.primeiroDia = primeiroDia;
	}

	@Column(name = "vigencia_inicio")
	public Date getVigenciaInicio() {
		return vigenciaInicio;
	}

	public void setVigenciaInicio(Date vigenciaInicio) {
		this.vigenciaInicio = vigenciaInicio;
	}

	@Column(name = "vigencia_fim")
	public Date getVigenciaFim() {
		return vigenciaFim;
	}

	public void setVigenciaFim(Date vigenciaFim) {
		this.vigenciaFim = vigenciaFim;
	}

	@Column(name = "habilita_faturamento")
	@NotNull
	public Boolean getHabilitaFaturamento() {
		return habilitaFaturamento;
	}

	public void setHabilitaFaturamento(Boolean habilitaFaturamento) {
		this.habilitaFaturamento = habilitaFaturamento;
	}

	@Column(name = "desoneracao_folha")
	public Boolean getDesoneracaoFolha() {
		return desoneracaoFolha;
	}

	public void setDesoneracaoFolha(Boolean desoneracaofolha) {
		this.desoneracaoFolha = desoneracaofolha;
	}

	@Transient
	public String getPercentualAliquota() {
		if (aliquotas != null && !aliquotas.isEmpty()) {
			if (aliquotas.size() == 1) {
				BigDecimal percentual = BigDecimal.valueOf(aliquotas.get(0).getPercentual().doubleValue()).setScale(2, RoundingMode.HALF_UP);
				if (percentual != null) {
					String percentualStr = percentual.toString();
					percentualStr = percentualStr.replace(".", ",");
					return percentualStr;
				}
			}
			return "Vários";
		}
		return "0,00";
	}

	@Transient
	public String getDescricaoPeriodo() {
		if (periodoApuracao != null) {
			for (Periodo periodo : Periodo.values()) {
				if (periodo.valorPeriodo.equals(periodoApuracao)) {
					return periodo.nomePeriodo;
				}
			}
		}
		return null;
	}

	@Transient
	public String getDescricaoAliquota() {
		if (getTributo() != null && !Tributo.ISS.equals(getTributo().getNome()) && getDescricao() != null && getPercentualAliquota() != null) {
			if (getDescricao() != null && getPercentualAliquota() != null) {
				return getDescricao() + " - " + getPercentualAliquota() + "%";
			}
		}
		return "";
	}

	@Transient
	public String getDescricaoAliquotaISS() {
		if (getTributo() != null && Tributo.ISS.equals(getTributo().getNome()) && getDescricao() != null && getPercentualAliquota() != null && getCidade() != null) {
			return getCidade().getNome() + " - " + getPercentualAliquota() + "% - " + getDescricao();
		}
		return "";
	}

	@Transient
	public String getLabel() {
		StringBuilder sbLabel = new StringBuilder();
		if (this.cidade != null && this.cidade.getUf() != null && !this.cidade.getUf().isEmpty()) {
			sbLabel.append(this.cidade.getUf()).append("/");
			sbLabel.append(this.cidade.getNome()).append(" - ");
		}

		if (this.getPercentualAliquota() != null) {
			sbLabel.append(this.getPercentualAliquota())
				.append("%");
			sbLabel.append(" - ");
		}

		if (this.codigoReceita != null) {
			sbLabel.append(this.codigoReceita).append(" - ");
		}

		if (this.descricao != null && !this.descricao.isEmpty()) {
			sbLabel.append(this.descricao);
		}
		return sbLabel.toString();
	}

	@Transient
	public String getLabelTA() {
		StringBuilder sbLabel = new StringBuilder();
		if (this.cidade != null && this.cidade.getUf() != null && !this.cidade.getUf().isEmpty()) {
			sbLabel.append(this.cidade.getUf()).append("/");
			sbLabel.append(this.cidade.getNome()).append(" - ");
		}

		if (this.codigoReceita != null) {
			sbLabel.append(this.codigoReceita).append(" - ");
		}

		if (this.descricao != null && !this.descricao.isEmpty()) {
			sbLabel.append(this.descricao);
		}
		return sbLabel.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((codigoReceita == null) ? 0 : codigoReceita.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((minimoGuia == null) ? 0 : minimoGuia.hashCode());
		result = prime * result + ((observacao == null) ? 0 : observacao.hashCode());
		result = prime * result + ((periodoApuracao == null) ? 0 : periodoApuracao.hashCode());
		result = prime * result + ((principal == null) ? 0 : principal.hashCode());
		result = prime * result + ((vigenciaFim == null) ? 0 : vigenciaFim.hashCode());
		result = prime * result + ((vigenciaInicio == null) ? 0 : vigenciaInicio.hashCode());
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
		if (!(obj instanceof CategoriaTributo)) {
			return false;
		}
		CategoriaTributo other = (CategoriaTributo) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (getCodigoReceita() == null) {
			if (other.getCodigoReceita() != null) {
				return false;
			}
		} else if (!getCodigoReceita().equals(other.getCodigoReceita())) {
			return false;
		}
		if (getDescricao() == null) {
			if (other.getDescricao() != null) {
				return false;
			}
		} else if (!getDescricao().equals(other.getDescricao())) {
			return false;
		}
		if (getMinimoGuia() == null) {
			if (other.getMinimoGuia() != null) {
				return false;
			}
		} else if (!getMinimoGuia().equals(other.getMinimoGuia())) {
			return false;
		}
		if (getObservacao() == null) {
			if (other.getObservacao() != null) {
				return false;
			}
		} else if (!getObservacao().equals(other.getObservacao())) {
			return false;
		}
		if (getPeriodoApuracao() == null) {
			if (other.getPeriodoApuracao() != null) {
				return false;
			}
		} else if (!getPeriodoApuracao().equals(other.getPeriodoApuracao())) {
			return false;
		}
		if (getPrincipal() == null) {
			if (other.getPrincipal() != null) {
				return false;
			}
		} else if (!getPrincipal().equals(other.getPrincipal())) {
			return false;
		}
		if (getVigenciaFim() == null) {
			if (other.getVigenciaFim() != null) {
				return false;
			}
		} else if (!getVigenciaFim().equals(other.getVigenciaFim())) {
			return false;
		}
		if (getVigenciaInicio() == null) {
			if (other.getVigenciaInicio() != null) {
				return false;
			}
		} else if (!getVigenciaInicio().equals(other.getVigenciaInicio())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "CategoriaTributo [codigo=" + getCodigo() + ", codigoReceita=" + codigoReceita + ", descricao=" + descricao + ", observacao=" + observacao
			+ ", cidade=" + cidade + ", principal=" + principal + ", vigenciaInicio=" + vigenciaInicio + ", vigenciaFim=" + vigenciaFim + "]";
	}

	public enum TipoAliquota {
		FIXA,
		FIXA_COMPOSTA,
		VARIAVEL
	}

	public enum DataReferencia {
		PAGAMENTO("Pagamento"),
		EMISSAO("Emissão");

		private final String descricao;

		private DataReferencia(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

	}

	public enum Periodo {

		DIARIO("Diário", 1),
		SEMANAL("Semanal", 7),
		DECENDIAL("Decendial", 10),
		QUINZENAL("Quinzenal", 15),
		MENSAL("Mensal", 30),
		BIMESTRAL("Bimestral", 60),
		TRIMESTRAL("Trimestral", 90);

		private final String nomePeriodo;
		private final Integer valorPeriodo;

		Periodo(String nomePeriodo, Integer valorPeriodo) {
			this.nomePeriodo = nomePeriodo;
			this.valorPeriodo = valorPeriodo;
		}

		public static Periodo getPeriodo(Integer valor) {
			for (Periodo periodo : Periodo.values()) {
				if (Objects.equals(periodo.getValorPeriodo(), valor)) {
					return periodo;
				}
			}

			return MENSAL;

		}

		public String getNomePeriodo() {
			return nomePeriodo;
		}

		public Integer getValorPeriodo() {
			return valorPeriodo;
		}
	}

	public enum Antecipacao {

		ANTECIPA("Antecipado"),
		POSTERGA("Postergado");

		private final String descricao;

		private Antecipacao(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

	}

}
