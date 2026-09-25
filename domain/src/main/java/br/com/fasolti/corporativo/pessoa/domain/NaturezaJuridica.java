package br.com.fasolti.corporativo.pessoa.domain;

import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import br.com.fasolti.corporativo.pessoa.interf.Dominio;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Tabela da Receita Federal:<br>
 * <a href="http://www.receita.fazenda.gov.br/pessoajuridica/cnpj/tabelas/natjurqualificaresponsavel.htm">Tabela de
 * Natureza Jurídica e Qualificação do Representante da Entidade</a>
 */
@Entity
@Table(name = "NATUREZA_JURIDICA")
@SequenceGenerator(name = "SEQ_NATUREZA_JURIDICA", sequenceName = "SEQ_NATUREZA_JURIDICA", allocationSize = 1)
public class NaturezaJuridica extends AuditEntity implements Dominio {

	private static final long serialVersionUID = 1L;

	private Long codigo;
	private String nome;
	private String codigoRfb;
	private Integer ordem;
	private boolean selecionavel;

	public NaturezaJuridica() {
	}

	public NaturezaJuridica(Long codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}

	public NaturezaJuridica(Long codigo, String nome, String codigoRfb, Integer ordem, boolean selecionavel) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.codigoRfb = codigoRfb;
		this.ordem = ordem;
		this.selecionavel = selecionavel;
	}

	public NaturezaJuridica(String nome, String codigoRfb, Integer ordem, boolean selecionavel) {
		super();
		this.nome = nome;
		this.codigoRfb = codigoRfb;
		this.ordem = ordem;
		this.selecionavel = selecionavel;
	}

	@Override
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NATUREZA_JURIDICA")
	@Id
	@Column(name = "CODIGO", nullable = false)
	@NotNull
	public Long getCodigo() {
		return this.codigo;
	}

	@Override
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Override
	@Column(name = "NOME", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getNome() {
		return this.nome;
	}

	@Override
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "CODIGO_RFB", length = 5)
	@Length(max = 5)
	public String getCodigoRfb() {
		return codigoRfb;
	}

	public void setCodigoRfb(String codigoRfb) {
		this.codigoRfb = codigoRfb;
	}

	@Column(name = "ORDEM")
	@NotNull
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@Column(name = "SELECIONAVEL", nullable = false)
	@NotNull
	public boolean isSelecionavel() {
		return selecionavel;
	}

	public void setSelecionavel(boolean selecionavel) {
		this.selecionavel = selecionavel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((codigoRfb == null) ? 0 : codigoRfb.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((ordem == null) ? 0 : ordem.hashCode());
		result = prime * result + (selecionavel ? 1231 : 1237);
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		NaturezaJuridica other = (NaturezaJuridica) obj;
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
			return false;
		}
		if (codigoRfb == null) {
			if (other.codigoRfb != null) {
				return false;
			}
		} else if (!codigoRfb.equals(other.codigoRfb)) {
			return false;
		}
		if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		if (ordem == null) {
			if (other.ordem != null) {
				return false;
			}
		} else if (!ordem.equals(other.ordem)) {
			return false;
		}
		if (selecionavel != other.selecionavel) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "NaturezaJuridica [codigo=" + codigo + ", nome=" + nome + ", codigoRfb=" + codigoRfb + ", ordem=" + ordem + ", selecionavel=" + selecionavel
			+ "]";
	}

}
