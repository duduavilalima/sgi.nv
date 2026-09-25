package br.com.araujoabreu.sgi.inrf.domain;

import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "BLOCO_PROCESSO")
@SequenceGenerator(name = "SEQ_BLOCO_PROCESSO", sequenceName = "SEQ_BLOCO_PROCESSO", allocationSize = 1)
public class BlocoProcesso extends AuditEntity implements DomainObject {

	private static final long serialVersionUID = 34888199163674472L;

	private String bloco;
	private String nome;
	private TipoBlocoProcesso tipo;
	private List<ProcessoAnexo> listaProcessoAnexo = new ArrayList<>();

	@Column(name = "BLOCO")
	@NotNull
	public String getBloco() {
		return bloco;
	}

	public void setBloco(String bloco) {
		this.bloco = bloco;
	}

	@Column(name = "NOME")
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "TIPO")
	@NotNull
	public TipoBlocoProcesso getTipo() {
		return tipo;
	}

	public void setTipo(TipoBlocoProcesso tipo) {
		this.tipo = tipo;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "blocoProcesso")
	public List<ProcessoAnexo> getListaProcessoAnexo() {
		return listaProcessoAnexo;
	}

	public void setListaProcessoAnexo(List<ProcessoAnexo> listaProcessoAnexo) {
		this.listaProcessoAnexo = listaProcessoAnexo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		BlocoProcesso that = (BlocoProcesso) o;
		return bloco.equals(that.bloco) &&
			nome.equals(that.nome);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), bloco, nome);
	}

	@Override
	public String toString() {
		return "BlocoProcesso [codigo=" + getCodigo() + ", bloco=" + bloco
			+ ", nome=" + nome + ", tipo=" + tipo + ", listaProcessoAnexo="
			+ listaProcessoAnexo + "]";
	}

	public enum TipoBlocoProcesso {
		APLICACAO("Gerado pela aplicação"),
		ANEXO("Possível de receber anexo"),
		INATIVO("Inativo (não será mais permitido gerar ou anexar)");

		private final String descricao;

		private TipoBlocoProcesso(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}


}
