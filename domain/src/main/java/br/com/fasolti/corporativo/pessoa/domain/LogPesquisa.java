package br.com.fasolti.corporativo.pessoa.domain;

import br.com.fasolti.corporativo.pessoa.enums.TipoServicoPesquisa;
import org.hibernate.Hibernate;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Log das consultas realizadas ao serviço de validação de fornecedores SERASA/Experian.
 */
@Entity
@Table(name = "LOG_PESQUISA")
@SequenceGenerator(name = "seq_log_pesquisa", sequenceName = "seq_log_pesquisa", allocationSize = 1)
public class LogPesquisa implements java.io.Serializable {

	private static final long serialVersionUID = -4099861691128215976L;
	/**
	 * Código interno da pesquisa.
	 */
	private Long codigo;
	/**
	 * Código interno do solicitante (código interno da pessoa) da pesquisa.
	 */
	private Pessoa solicitante;
	/**
	 * Tipo de serviço da pesquisa.
	 */
	private TipoServicoPesquisa tipoServico;
	/**
	 * Situação da pesquisa. Cada serviço deverá ter seu fluxo e suas situações.
	 */
	private SituacaoPesquisa situacao;
	/**
	 * Nº do documento utilizado para a pesquisa (CNPJ, CPF, CEP, NF, etc).
	 */
	private String documento;
	/**
	 * Data/hora da pesquisa.
	 */
	private Date dataPesquisa;
	/**
	 * XML enviado para a pesquisa.
	 */
	private String xmlEnvio;
	/**
	 * Resultado da pesquisa.
	 */
	private String resultado;
	/**
	 * XML de retorno da pesquisa.
	 */
	private String xmlRetorno;

	public LogPesquisa() {
	}

	public LogPesquisa(long codigo) {
		this.codigo = codigo;
	}

	public LogPesquisa(long codigo, TipoServicoPesquisa tipoServico, SituacaoPesquisa situacao, String documento, Date dataPesquisa) {
		this.codigo = codigo;
		this.tipoServico = tipoServico;
		this.situacao = situacao;
		this.documento = documento;
		this.dataPesquisa = dataPesquisa;
	}

	public LogPesquisa(Pessoa solicitante,
					   TipoServicoPesquisa tipoServico,
					   SituacaoPesquisa situacao,
					   String documento,
					   Date dataPesquisa,
					   String xmlEnvio,
					   String resultado,
					   String xmlRetorno) {
		this.solicitante = solicitante;
		this.tipoServico = tipoServico;
		this.situacao = situacao;
		this.documento = documento;
		this.dataPesquisa = dataPesquisa;
		this.xmlEnvio = xmlEnvio;
		this.resultado = resultado;
		this.xmlRetorno = xmlRetorno;
	}

	@Id
	@Column(name = "codigo", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_log_pesquisa")
	public Long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_solicitante")
	public Pessoa getSolicitante() {
		return this.solicitante;
	}

	public void setSolicitante(Pessoa solicitante) {
		this.solicitante = solicitante;
	}

	@Column(name = "tipo_servico", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public TipoServicoPesquisa getTipoServico() {
		return this.tipoServico;
	}

	public void setTipoServico(TipoServicoPesquisa tipoServico) {
		this.tipoServico = tipoServico;
	}

	@Column(name = "situacao", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public SituacaoPesquisa getSituacao() {
		return this.situacao;
	}

	public void setSituacao(SituacaoPesquisa situacao) {
		this.situacao = situacao;
	}

	@Column(name = "documento", nullable = false, length = 18)
	@NotNull
	@Length(max = 18)
	public String getDocumento() {
		return this.documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_pesquisa", nullable = false, length = 29)
	@NotNull
	public Date getDataPesquisa() {
		return this.dataPesquisa;
	}

	public void setDataPesquisa(Date dataPesquisa) {
		this.dataPesquisa = dataPesquisa;
	}

	@Column(name = "xml_envio")
	public String getXmlEnvio() {
		return this.xmlEnvio;
	}

	public void setXmlEnvio(String xmlEnvio) {
		this.xmlEnvio = xmlEnvio;
	}

	@Column(name = "resultado", length = 50)
	@Length(max = 50)
	public String getResultado() {
		return this.resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	@Column(name = "xml_retorno")
	public String getXmlRetorno() {
		return this.xmlRetorno;
	}

	public void setXmlRetorno(String xmlRetorno) {
		this.xmlRetorno = xmlRetorno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (codigo ^ (codigo >>> 32));
		result = prime * result + ((documento == null) ? 0 : documento.hashCode());
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
		if (!(Pessoa.class.isAssignableFrom(Hibernate.getClass(obj)))) {
			return false;
		}
		LogPesquisa other = (LogPesquisa) obj;
		if (!Objects.equals(codigo, other.codigo))
			return false;
		if (documento == null) {
			if (other.documento != null)
				return false;
		} else if (!documento.equals(other.documento))
			return false;
		return true;
	}

	public enum SituacaoPesquisa {
		SUCESSO,
		ERRO
	}

}
