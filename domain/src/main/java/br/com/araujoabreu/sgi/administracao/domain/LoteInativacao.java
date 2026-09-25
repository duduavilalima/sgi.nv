package br.com.araujoabreu.sgi.administracao.domain;

import br.com.araujoabreu.sgi.compras.domain.Anexo;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "LOTE_INATIVACAO")
@SequenceGenerator(name = "SEQ_LOTE_INATIVACAO", sequenceName = "SEQ_LOTE_INATIVACAO", allocationSize = 1)
public class LoteInativacao extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6900450179661882229L;
	private Anexo anexo;
	private Colaborador responsavel;
	private Tipo tipo;
	private Situacao situacao;
	private Date dataImportacao;
	private Integer totalRegistros = 0;
	private Integer totalNaoLocalizado = 0;
	private Integer totalImportadoComPc = 0;
	private Integer totalImportadoSemPc = 0;
	private Integer totalJaInativado = 0;
	private Integer totalIgnorado = 0;
	private String criador;
	private Date dataCriacao;
	private List<LoteInativacaoRegistro> registros = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinColumn(name = "CODIGO_ANEXO")
	@NotNull(message = "O anexo do lote de inativação deve ser informado.")
	public Anexo getAnexo() {
		return anexo;
	}

	public void setAnexo(Anexo anexo) {
		this.anexo = anexo;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "CODIGO_IMPORTACAO")
	public Colaborador getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Colaborador responsavel) {
		this.responsavel = responsavel;
	}

	@Column(name = "TIPO")
	@Enumerated(EnumType.ORDINAL)
	@NotNull(message = "O tipo do lote de inativação deve ser informado.")
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Column(name = "SITUACAO")
	@NotNull(message = "A situação do lote de informação deve ser informada.")
	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_IMPORTACAO")
	@NotNull(message = "A data de importação do lote de inativação deve ser informada.")
	public Date getDataImportacao() {
		return dataImportacao;
	}

	public void setDataImportacao(Date dataImportacao) {
		this.dataImportacao = dataImportacao;
	}

	@Column(name = "TOTAL_REGISTROS")
	@NotNull(message = "O total de registros do lote de informação deve ser informado.")
	public Integer getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(Integer totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	@Column(name = "TOTAL_NAO_LOCALIZADO")
	@NotNull(message = "O total de registros não localizados do lote de informação deve ser informado.")
	public Integer getTotalNaoLocalizado() {
		return totalNaoLocalizado;
	}

	public void setTotalNaoLocalizado(Integer totalNaoLocalizado) {
		this.totalNaoLocalizado = totalNaoLocalizado;
	}

	@Column(name = "TOTAL_IMPORTADO_COM_PC")
	@NotNull(message = "O total de registros importados com PC no lote de informação deve ser informado.")
	public Integer getTotalImportadoComPc() {
		return totalImportadoComPc;
	}

	public void setTotalImportadoComPc(Integer totalImportadoComPc) {
		this.totalImportadoComPc = totalImportadoComPc;
	}

	@Column(name = "TOTAL_IMPORTADO_SEM_PC")
	@NotNull(message = "O total de registros importados sem PC no lote de informação deve ser informado.")
	public Integer getTotalImportadoSemPc() {
		return totalImportadoSemPc;
	}

	public void setTotalImportadoSemPc(Integer totalImportadoSemPc) {
		this.totalImportadoSemPc = totalImportadoSemPc;
	}

	@Column(name = "total_nao_inativado")
	@NotNull(message = "O total de usuários ignorados por perfil deve ser informado.")
	public Integer getTotalIgnorado() {
		return totalIgnorado;
	}

	public void setTotalIgnorado(Integer totalIgnorado) {
		this.totalIgnorado = totalIgnorado;
	}

	@Column(name = "total_ja_inativo")
	@NotNull(message = "O total de usuários já inativos deve ser informado.")
	public Integer getTotalJaInativado() {
		return totalJaInativado;
	}

	public void setTotalJaInativado(Integer totalJaInativado) {
		this.totalJaInativado = totalJaInativado;
	}

	@Column(name = "CRIADOR")
	@Length(max = 255, message = "O nome do criador do lote deve ter entre {min} e {max} caracteres.")
	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CRIACAO")
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "loteInativacao")
	public List<LoteInativacaoRegistro> getRegistros() {
		return registros;
	}

	public void setRegistros(List<LoteInativacaoRegistro> registros) {
		this.registros = registros;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoteInativacao other = (LoteInativacao) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!getCodigo().equals(other.getCodigo()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LoteInativacao [codigo=" + getCodigo() + "]";
	}

	@Transient
	public boolean isImportado() {
		return situacao == Situacao.IMPORTADO;
	}

	@Transient
	public boolean isImportacaoVazia() {
		return isImportado() && Objects.equals(totalNaoLocalizado, totalRegistros);
	}

	public void ordenarRegistros() {
		if (!registros.isEmpty()) {
			Comparator<LoteInativacaoRegistro> comparator = (arg0, arg1) -> arg0.getNomeArquivo().compareToIgnoreCase(arg1.getNomeArquivo());
			registros.sort(comparator);
		}
	}

	public LoteInativacao addRegistro(LoteInativacaoRegistro registro) {
		registro.setLoteInativacao(this);
		registros.add(registro);
		totalRegistros += 1;
		switch (registro.getSituacao()) {
			case CADASTRADO_INATIVADO_SEM_PRESTACAO:
				totalImportadoSemPc += 1;
				break;
			case CADASTRADO_INATIVADO_COM_PRESTACAO:
				totalImportadoComPc += 1;
				break;
			case NAO_CADASTRADO:
				totalNaoLocalizado += 1;
				break;
			case IGNORADO:
				totalIgnorado += 1;
				break;
			case CADASTRADO_JA_INATIVADO:
				totalJaInativado += 1;
				break;
		}
		return this;
	}

	public enum Situacao {

		NAO_IMPORTADO,
		IMPORTADO
	}

	public enum Tipo {

		MANUAL,
		AUTOMATICO
	}
}
