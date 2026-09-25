package br.com.araujoabreu.sgi.administracao.domain;

import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "LOTE_INATIVACAO_REGISTRO")
@SequenceGenerator(name = "SEQ_LOTE_INATIVACAO_REGISTRO", sequenceName = "SEQ_LOTE_INATIVACAO_REGISTRO", allocationSize = 1)
public class LoteInativacaoRegistro extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -3875708205337660293L;
	private LoteInativacao loteInativacao;
	private Colaborador inativado;
	private CentroFinanceiro centroFinanceiro;
	private Situacao situacao;
	private String cpfArquivo;
	private String nomeArquivo;
	private TipoPrestacaoContas tipoPrestacaoContas;
	private BigDecimal valorPrestacaoContas;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH}, optional = false)
	@JoinColumn(name = "CODIGO_LOTE_INATIVACAO")
	@NotNull
	public LoteInativacao getLoteInativacao() {
		return loteInativacao;
	}

	public void setLoteInativacao(LoteInativacao loteInativacao) {
		this.loteInativacao = loteInativacao;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "CODIGO_PESSOA")
	public Colaborador getInativado() {
		return inativado;
	}

	public void setInativado(Colaborador pessoa) {
		this.inativado = pessoa;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "CODIGO_CENTRO_FINANCEIRO")
	public CentroFinanceiro getCentroFinanceiro() {
		return centroFinanceiro;
	}

	public void setCentroFinanceiro(CentroFinanceiro centroFinanceiro) {
		this.centroFinanceiro = centroFinanceiro;
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

	@Column(name = "CPF_ARQUIVO")
	@NotNull(message = "Informe o CPF do arquivo.")
	@Pattern(regex = "\\d{3}.\\d{3}.\\d{3}-\\d{2}", message = "Formato inválido de CPF.")
	public String getCpfArquivo() {
		return cpfArquivo;
	}

	public void setCpfArquivo(String cpfArquivo) {
		if (cpfArquivo.length() > 14) {
			cpfArquivo = cpfArquivo.substring(14);
		}
		String cpfNormalizado = cpfArquivo.replace(".", "").replace("-", "");
		this.cpfArquivo = String.format("%s.%s.%s-%s",
			cpfNormalizado.substring(0, 3),
			cpfNormalizado.substring(3, 6),
			cpfNormalizado.substring(6, 9),
			cpfNormalizado.substring(9));
	}

	@Column(name = "NOME_ARQUIVO")
	@NotNull
	@Length(min = 1, max = 30, message = "O nome no arquivo deve ter entre {min} e {max} caracteres.")
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		if (nomeArquivo.length() > 30) {
			nomeArquivo = nomeArquivo.substring(0, 30);
		}
		this.nomeArquivo = nomeArquivo;
	}

	@Column(name = "TIPO_PRESTACAO_CONTAS")
	@Enumerated(EnumType.ORDINAL)
	public TipoPrestacaoContas getTipoPrestacaoContas() {
		return tipoPrestacaoContas;
	}

	public void setTipoPrestacaoContas(TipoPrestacaoContas tipoPrestacaoContas) {
		this.tipoPrestacaoContas = tipoPrestacaoContas;
	}

	@Column(name = "VALOR_PRESTACAO_CONTAS", precision = 19, scale = 2)
	public BigDecimal getValorPrestacaoContas() {
		return valorPrestacaoContas;
	}

	public void setValorPrestacaoContas(BigDecimal valorPrestacaoContas) {
		this.valorPrestacaoContas = valorPrestacaoContas;
	}

	@Transient
	public Gerencia getGerencia() {
		return this.inativado != null ? this.inativado.getGerencia() : null;
	}

	@Override
	public String toString() {
		return "LoteInativacaoRegistro [codigo=" + getCodigo() + "]";
	}

	public static enum Situacao {

		NAO_CADASTRADO("Não cadastrado", "Registro não encontrado no SGI"),
		CADASTRADO_JA_INATIVADO("Já inativo", "Registro localizado no SGI, mas já estava inativo"),
		CADASTRADO_INATIVADO_SEM_PRESTACAO("Inativo", "Registro localizado no SGI, inativado e SEM prestação de contas"),
		CADASTRADO_INATIVADO_COM_PRESTACAO("Inativo", "Registro localizado no SGI, inativado e COM prestação de contas"),
		IGNORADO("Ignorado (perfil)", "Registro localizado no SGI, mas ignorado (perfil do usuário) e, portanto, não inativado");

		private final String descricao;
		private final String dica;

		private Situacao(String descricao, String dica) {
			this.descricao = descricao;
			this.dica = dica;
		}

		public String getDescricao() {
			return descricao;
		}

		public String getDica() {
			return dica;
		}

	}

	public enum TipoPrestacaoContas {

		FUNDO_FIXO("FF", "Fundo fixo"),
		REEMBOLSO("RE", "Reembolso"),
		ADIANTAMENTO("AD", "Adiantamento"),
		ADIANTAMENTO_REEMBOLSO("AR", "Adiantamento/Reembolso");

		private final String sigla;
		private final String descricao;

		TipoPrestacaoContas(String sigla, String descricao) {
			this.sigla = sigla;
			this.descricao = descricao;
		}

		public String getSigla() {
			return sigla;
		}

		public String getDescricao() {
			return descricao;
		}
	}

}
