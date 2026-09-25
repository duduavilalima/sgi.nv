package br.com.araujoabreu.sgi.contabilidade.domain;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.compras.domain.Anexo;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.contabilidade.enums.TipoMovimentoFinanceiro;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "LOTE_INTERFACE")
@SequenceGenerator(name = "SEQ_LOTE_INTERFACE", sequenceName = "SEQ_LOTE_INTERFACE", allocationSize = 1)
public class LoteInterface extends BaseEntity implements Serializable, Cloneable {

	private static final long serialVersionUID = -5754659793879384354L;
	private SituacaoLoteInterface situacao;
	private Unidade unidade;
	private TipoLoteInterface tipoLoteInterface;
	private String criador;
	private Date dataCriacao;
	private Date dataEvento;
	private Date dataImportacao;
	private Colaborador colaborador;
	private List<ItemLoteContabil> itens;
	private List<Anexo> anexos = new ArrayList<>();
	private List<PessoaContaContabil> pessoaContaContabils = new ArrayList<>();
	private List<ItemLoteInterfaceCap> listaItemLoteInterfaceCap = new ArrayList<>();
	private String mensagemErro;
	private List<LoteInterfaceNota> notas = new ArrayList<>();

	public LoteInterface() {
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "loteInterface", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy(value = "codigo")
	public List<LoteInterfaceNota> getNotas() {
		return notas;
	}

	public void setNotas(List<LoteInterfaceNota> notas) {
		this.notas = notas;
	}

	public LoteInterfaceNota addNota(LoteInterfaceNota.Tipo tipo, Nota nota, Integer numeroParcela) {
		Objects.requireNonNull(tipo);
		Objects.requireNonNull(nota);

		LoteInterfaceNota lia = new LoteInterfaceNota();
		lia.setTipo(tipo);
		lia.setLoteInterface(this);
		lia.setNota(nota);
		lia.setOrdem(numeroParcela);
		getNotas().add(lia);

		return lia;
	}

	public LoteInterfaceNota addNota(LoteInterfaceNota.Tipo tipo, Nota nota) {
		return addNota(tipo, nota, 1);
	}

	public <T extends Nota> void addNotas(LoteInterfaceNota.Tipo tipo, List<T> notas) {
		for (Nota nota : notas) {
			addNota(tipo, nota);
		}
	}

	@Column(name = "SITUACAO")
	@NotNull
	public SituacaoLoteInterface getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoLoteInterface situacao) {
		this.situacao = situacao;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "loteInterface", cascade = CascadeType.ALL)
	@OrderBy(value = "codigo")
	public List<ItemLoteContabil> getItens() {
		return itens;
	}

	public void setItens(List<ItemLoteContabil> itens) {
		this.itens = itens;
	}

	@Transient
	public ItemLoteContabil addItemLoteContabil(ItemLoteContabil itemLoteContabil) {
		getItens().add(itemLoteContabil);
		itemLoteContabil.setLoteInterface(this);
		return itemLoteContabil;
	}

	@Transient
	public ItemLoteContabil removeItemLoteContabil(ItemLoteContabil itemLoteContabil) {
		getItens().remove(itemLoteContabil);
		itemLoteContabil.setLoteInterface(null);
		return itemLoteContabil;
	}

	@Transient
	public void addItem(ItemLoteContabil itemLoteContabil) {
		if (itens == null) {
			itens = new ArrayList<>();
		}
		this.itens.add(itemLoteContabil);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_UNIDADE")
	@NotNull
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_IMPORTACAO")
	//TODO Trocar nome para Importador
	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	@Column(name = "TIPO")
	public TipoLoteInterface getTipoLoteInterface() {
		return tipoLoteInterface;
	}

	public void setTipoLoteInterface(TipoLoteInterface tipoLoteInterface) {
		this.tipoLoteInterface = tipoLoteInterface;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
		name = "LOTE_INTERFACE_ANEXO",
		joinColumns = {@JoinColumn(name = "CODIGO_LOTE_INTERFACE", nullable = false)},
		inverseJoinColumns = {@JoinColumn(name = "CODIGO_ANEXO", nullable = false)})
	public List<Anexo> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<Anexo> anexos) {
		this.anexos = anexos;
	}

	@OneToMany(mappedBy = "loteInterface", fetch = FetchType.LAZY)
	public List<PessoaContaContabil> getPessoaContaContabils() {
		return pessoaContaContabils;
	}

	public void setPessoaContaContabils(List<PessoaContaContabil> pessoaContaContabils) {
		this.pessoaContaContabils = pessoaContaContabils;
	}

	@Column()
	public String getCriador() {
		return this.criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	@Column(name = "DATA_CRIACAO")
	public Date getDataCriacao() {
		return this.dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_EVENTO")
	public Date getDataEvento() {
		return this.dataEvento;
	}

	public void setDataEvento(Date dataEvento) {
		this.dataEvento = dataEvento;
	}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_IMPORTACAO")
    public Date getDataImportacao() {
		return dataImportacao;
	}

	public void setDataImportacao(Date dataImportacao) {
		this.dataImportacao = dataImportacao;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "loteInterface", cascade = CascadeType.ALL)
	@OrderBy(value = "codigo")
	public List<ItemLoteInterfaceCap> getListaItemLoteInterfaceCap() {
		return listaItemLoteInterfaceCap;
	}

	public void setListaItemLoteInterfaceCap(List<ItemLoteInterfaceCap> listaItemLoteInterfaceCap) {
		this.listaItemLoteInterfaceCap = listaItemLoteInterfaceCap;
	}

	@Column(name = "mensagem_erro", length = 2000)
	public String getMensagemErro() {
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	@Transient
	public PessoaContaContabil addPessoaContaContabil(PessoaContaContabil pessoaContaContabil) {
		getPessoaContaContabils().add(pessoaContaContabil);
		pessoaContaContabil.setLoteInterface(this);
		return pessoaContaContabil;
	}

	@Transient
	public PessoaContaContabil removePessoaContaContabil(PessoaContaContabil pessoaContaContabil) {
		getPessoaContaContabils().remove(pessoaContaContabil);
		pessoaContaContabil.setLoteInterface(null);
		return pessoaContaContabil;
	}

	@Transient
	public boolean isCriado() {
		return SituacaoLoteInterface.CRIADO.equals(situacao);
	}

	@Transient
	public boolean isEmGeracao() {
		return SituacaoLoteInterface.EMGERACAO.equals(situacao);
	}

	@Transient
	public BigDecimal getValorTotalDebito() {
		BigDecimal valorTotal = new BigDecimal(0);
		if (this.getItens() != null && !this.getItens().isEmpty()) {

			for (ItemLoteContabil itemLoteContabil : this.getItens()) {
				if (itemLoteContabil.getDebitoCredito().equals(TipoMovimentoFinanceiro.DEBITO.getConstante())) {
					valorTotal = valorTotal.add(itemLoteContabil.getValor());
				}
			}
		}
		return valorTotal;
	}

	@Transient
	public BigDecimal getValorTotalCredito() {
		BigDecimal valorTotal = new BigDecimal(0);
		if (this.getItens() != null && !this.getItens().isEmpty()) {

			for (ItemLoteContabil itemLoteContabil : this.getItens()) {
				if (itemLoteContabil.getDebitoCredito().equals(TipoMovimentoFinanceiro.CREDITO.getConstante())) {
					valorTotal = valorTotal.add(itemLoteContabil.getValor());
				}
			}
		}
		return valorTotal;
	}

	@Transient
	public BigDecimal getValorTotalDiferenca() {
		BigDecimal valorTotal = new BigDecimal(0);
		valorTotal = getValorTotalCredito().abs().subtract(getValorTotalDebito().abs());
		return valorTotal;
	}

	@Transient
	public BigDecimal getValorTotalDebitoFormatado() {
		return getValorTotalDebito().abs();
	}

	@Transient
	public BigDecimal getValorTotalCreditoFormatado() {
		return getValorTotalDebito().abs();
	}

	@Transient
	public BigDecimal getValorTotalDiferencaFormatado() {
		return getValorTotalDiferenca().abs();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((criador == null) ? 0 : criador.hashCode());
		result = prime * result + ((dataCriacao == null) ? 0 : dataCriacao.hashCode());
		result = prime * result + ((dataEvento == null) ? 0 : dataEvento.hashCode());
		result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
		result = prime * result + ((tipoLoteInterface == null) ? 0 : tipoLoteInterface.hashCode());
		result = prime * result + ((unidade == null) ? 0 : unidade.hashCode());
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
		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LoteInterface other = (LoteInterface) obj;
		if (criador == null) {
			if (other.criador != null) {
				return false;
			}
		} else if (!criador.equals(other.criador)) {
			return false;
		}
		if (dataCriacao == null) {
			if (other.dataCriacao != null) {
				return false;
			}
		} else if (!dataCriacao.equals(other.dataCriacao)) {
			return false;
		}
		if (dataEvento == null) {
			if (other.dataEvento != null) {
				return false;
			}
		} else if (!dataEvento.equals(other.dataEvento)) {
			return false;
		}
		if (situacao != other.situacao) {
			return false;
		}
		if (tipoLoteInterface != other.tipoLoteInterface) {
			return false;
		}
		if (unidade == null) {
			if (other.unidade != null) {
				return false;
			}
		} else if (!unidade.equals(other.unidade)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "LoteInterface [situacao=" + situacao
			+ ", unidade=" + unidade + ", tipoLoteInterface=" + tipoLoteInterface + ", criador=" + criador + ", dataCriacao=" + dataCriacao
			+ ", dataEvento=" + dataEvento + "]";
	}

	@Override
	public Object clone() {
		LoteInterface loteInterfaceNovo = new LoteInterface();
		loteInterfaceNovo.setTipoLoteInterface(getTipoLoteInterface());
		loteInterfaceNovo.setSituacao(getSituacao());
		loteInterfaceNovo.setUnidade(getUnidade());
		loteInterfaceNovo.setItens(getItens() == null ? new ArrayList<>() : getItens());
		loteInterfaceNovo.setCriador(getCriador());
		loteInterfaceNovo.setDataCriacao(getDataCriacao());
		loteInterfaceNovo.setDataEvento(getDataEvento());
		return loteInterfaceNovo;
	}

	public enum SituacaoLoteInterface {
		CRIADO("Aguardando geração do arquivo"),
		EMGERACAO("Gerando arquivo"),
		GERADO("Arquivo gerado"),
		IMPORTADO("Importado na contabilidade"),
		PROCESSADOCOMERROS("Processado com erros");

		private final String nome;

		private SituacaoLoteInterface(String nome) {
			this.nome = nome;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return nome;
		}
	}

	public enum TipoLoteInterface {
		Provisionamento("Provisionamento", "Provisionamento"), // 0
		Tesouraria("Tesouraria", "Tesouraria"), // 1
		Custos("Custos", "Custos"), // 2
		Novos_fornecedores("Novos fornecedores", "Novos fornecedores"), // 3
		Fluxo_de_caixa("Fluxo de caixa", "Fluxo de caixa"), // 4
		NF_Devolucao("NF/Devolução", "NF/Devolução"), // 5
		DIRF("DIRF (anual)", "DIRF (anual)"), // 6
		FATN("FATN - Faturamento emitido no período", "FATN"), // 7
		FATC("FATC - Cancelamentos fora do período", "FATC"), // 8
		DSFP("DSFP - 2% sobre faturamento (desoneração da folha de pagamento)", "DSFP"), // 9
		Baixas_diarias("Baixas diárias", "Baixas diárias"), // 10
		SEFIP("SEFIP", "SEFIP"), // 11
		SISTEMA_FISCAL("Sistema Fiscal", "Sistema Fiscal"), // 12
		Fluxo_de_caixa_Faturamento("Fluxo de caixa (faturamento)", "Fluxo de caixa (faturamento)"), // 13
		Custos_Faturamento("Custos (faturamento)", "Custos (faturamento)"), // 14
		ISS_PIS_COFINS_CSLL("Sistema Fiscal (PIS/COFINS/ISS)", "PIS/COFINS/ISS"), // 15
		Nota_Carioca("Nota carioca", "Nota carioca"), // 16
		Boletos("Boletos (Itaú)", "Boletos (Itaú)"), // 17
		Portal_Oi("Portal Oi", "Portal Oi"), // 18
		NOTA_NORTE("Nota Norte (Belém)", "Nota Norte"), // 19
		NOTA_PAULISTANA("Nota Paulistana", "Nota Paulistana"), // 20
		NOTA_BH("Nota BH", "Nota BH"), // 21
		NOTA_BRASILIA("Nota Brasília", "Nota Brasília"), // 22
		PER_DCOMP("PER/DCOMP (R29/R36)", "PER/DCOMP"), // 23
		ECF("ECF (Bloco Y570)", "ECF"), // 24
		NF_MATERIAL("NF/Material", "NF/Material"), //25
		CUSTOS_DIARIO("Custos (diário)", "Custos (diário)"); //26

		private final String nome;
		private final String abrv;

		private TipoLoteInterface(String nome, String abrv) {
			this.nome = nome;
			this.abrv = abrv;
		}

		public String getNome() {
			return nome;
		}

		public String getAbrv() {
			return abrv;
		}

		@Override
		public String toString() {
			return nome;
		}
	}

}
