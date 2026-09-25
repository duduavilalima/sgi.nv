package br.com.araujoabreu.sgi.compras.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "DOCUMENTO_FISCAL")
@SequenceGenerator(name="SEQ_DOCUMENTO_FISCAL", sequenceName="SEQ_DOCUMENTO_FISCAL", allocationSize=1)
public class DocumentoFiscal extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1437097433160111475L;
	private String codigoReceita;
	private String nome;
	private Boolean indicadorConsumo;
	private Boolean indicadorTransporte;
	private Boolean situacao;

	@Column(name = "CODIGO_RECEITA")
	@Length(max = 10)
	@NotNull
	public String getCodigoReceita() {
		return codigoReceita;
	}

	public void setCodigoReceita(String codigoReceita) {
		this.codigoReceita = codigoReceita;
	}

	@Column(name = "NOME")
	@Length(max = 100)
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "INDICADOR_CONSUMO", nullable = false, length = 1)
	@NotNull
	public Boolean getIndicadorConsumo() {
		return indicadorConsumo;
	}

	public void setIndicadorConsumo(Boolean indicadorConsumo) {
		this.indicadorConsumo = indicadorConsumo;
	}

	@Column(name = "INDICADOR_TRANSPORTE", nullable = false, length = 1)
	@NotNull
	public Boolean getIndicadorTransporte() {
		return indicadorTransporte;
	}

	public void setIndicadorTransporte(Boolean indicadorTransporte) {
		this.indicadorTransporte = indicadorTransporte;
	}

	@Column(name = "SITUACAO", nullable = false, length = 1)
	@NotNull
	public Boolean getSituacao() {
		return situacao;
	}

	public void setSituacao(Boolean situacao) {
		this.situacao = situacao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DocumentoFiscal)) return false;
		if (!super.equals(o)) return false;
		DocumentoFiscal that = (DocumentoFiscal) o;
		return Objects.equals(getCodigoReceita(), that.getCodigoReceita()) &&
			Objects.equals(getNome(), that.getNome()) &&
			Objects.equals(getIndicadorConsumo(), that.getIndicadorConsumo()) &&
			Objects.equals(getIndicadorTransporte(), that.getIndicadorTransporte()) &&
			Objects.equals(getSituacao(), that.getSituacao());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getCodigoReceita(), getNome(), getIndicadorConsumo(), getIndicadorTransporte(), getSituacao());
	}
}
