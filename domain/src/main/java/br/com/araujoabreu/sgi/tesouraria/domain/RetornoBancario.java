package br.com.araujoabreu.sgi.tesouraria.domain;

import br.com.araujoabreu.sgi.tesouraria.domain.ItemRelacao.StatusItemRelacao;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "RETORNO_BANCARIO")
public class RetornoBancario extends BaseEntity implements DomainObject {

	private static final long serialVersionUID = -922475313399196784L;

	private String identificacaoErro;
	private String descricaoOriginal;
	private String descricao;
	private Boolean utilizado;
	private StatusItemRelacao statusItemRelacao;
	private List<ItemRelacao> itemRelacaos;

	@Column(name = "CODIGO_ERRO", nullable = false, length = 20)
	@NotNull
	@Length(max = 20)
	public String getIdentificacaoErro() {
		return identificacaoErro;
	}

	public void setIdentificacaoErro(String identificacaoErro) {
		this.identificacaoErro = identificacaoErro;
	}

	@Column(name = "DESCRICAO_ORIGINAL", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getDescricaoOriginal() {
		return descricaoOriginal;
	}

	public void setDescricaoOriginal(String descricaoOriginal) {
		this.descricaoOriginal = descricaoOriginal;
	}

	@Column(name = "DESCRICAO", length = 100)
	@Length(max = 100)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "UTILIZADO", nullable = false)
	@NotNull
	public Boolean getUtilizado() {
		return utilizado;
	}

	public void setUtilizado(Boolean utilizado) {
		this.utilizado = utilizado;
	}

	@Column(name = "SITUACAO_ITEM_RELACAO", nullable = false)
	@NotNull
	public StatusItemRelacao getStatusItemRelacao() {
		return statusItemRelacao;
	}

	public void setStatusItemRelacao(StatusItemRelacao statusItemRelacao) {
		this.statusItemRelacao = statusItemRelacao;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "retornoBancario", targetEntity = ItemRelacao.class)
	public List<ItemRelacao> getItemRelacaos() {
		return itemRelacaos;
	}

	public void setItemRelacaos(List<ItemRelacao> itemRelacaos) {
		this.itemRelacaos = itemRelacaos;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		RetornoBancario that = (RetornoBancario) o;
		return identificacaoErro.equals(that.identificacaoErro) &&
			descricaoOriginal.equals(that.descricaoOriginal) &&
			descricao.equals(that.descricao) &&
			utilizado.equals(that.utilizado);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), identificacaoErro, descricaoOriginal, descricao, utilizado);
	}
}
