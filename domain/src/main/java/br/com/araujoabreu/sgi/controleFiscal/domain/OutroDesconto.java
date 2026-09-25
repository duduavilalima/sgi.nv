package br.com.araujoabreu.sgi.controleFiscal.domain;

import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.contabilidade.domain.ContaContabil;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.enums.ESituacao;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OUTRO_DESCONTO")
@SequenceGenerator(name="SEQ_OUTRO_DESCONTO", sequenceName="SEQ_OUTRO_DESCONTO", allocationSize=1)
public class OutroDesconto extends AuditEntity {

	private static final long serialVersionUID = 5957773324992137438L;
	private String nome;
	private String descricao;
	private ContaContabil contaContabil;
	private ESituacao situacao;
	private List<ContaContabil> listaContabil = new ArrayList<>();

	@Column(name = "NOME")
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "descricao")
	@NotNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Transient
	public ContaContabil getContaContabil() {
		return contaContabil;
	}

	public void setContaContabil(ContaContabil contaContabil) {
		this.contaContabil = contaContabil;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "situacao")
	@NotNull
	public ESituacao getSituacao() {
		return situacao;
	}

	public void setSituacao(ESituacao situacao) {
		this.situacao = situacao;
	}

	@ManyToMany(targetEntity = ContaContabil.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
		name = "OUTRO_DESCONTO_CONTA_CONTABIL",
		joinColumns = @JoinColumn(name = "CODIGO_OUTRO_DESCONTO"),
		inverseJoinColumns = @JoinColumn(name = "CODIGO_CONTA_CONTABIL")
	)
	public List<ContaContabil> getListaContabil() {
		return listaContabil;
	}

	public void setListaContabil(List<ContaContabil> listaContabil) {
		this.listaContabil = listaContabil;
	}

	@Transient
	public ContaContabil getContaContabilPorEmpresa(Unidade empresa) {
		for (ContaContabil cc : this.listaContabil) {
			if (cc.getEmpresa().equals(empresa))
				return cc;
		}
		return null;
	}

	@Transient
	public ContaContabil getContaContabilPorEmpresaTipo(Unidade empresa, ContaContabil.TipoContaContabil tipoContaContabil) {
		for (ContaContabil cc : this.listaContabil) {
			if (cc.getEmpresa().equals(empresa)) {
				if (cc.getTipo().equals(tipoContaContabil)) {
					return cc;
				}
			}
		}
		return null;
	}


	@Transient
	public String getLabel() {
		return nome + " - " + descricao;
	}


	@Override
	public String toString() {
		return "OutroDesconto [codigo=" + getCodigo() + ", nome=" + nome + ", descricao=" + descricao + "]";
	}

}
