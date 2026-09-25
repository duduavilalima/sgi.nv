package br.com.fasolti.corporativo.pessoa.domain;

import br.com.araujoabreu.sgi.administracao.enums.TipoEnderecoContrato;
import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ENDERECO")
@SequenceGenerator(name = "SEQ_ENDERECO", sequenceName = "SEQ_ENDERECO", allocationSize = 1)
public class Endereco extends AuditEntity implements DomainObject, Detail {

	private static final long serialVersionUID = 3515701030200679029L;
	private String endereco;
	private String numero;
	private String complemento;
	private String uf;
	private String bairro;
	private String cep;
	private TipoLogradouro tipoLogradouro;
	private Cidade cidade;
	private TipoEndereco tipoEndereco;
	private TipoEnderecoContrato tipoEnderecoContrato;
	private Boolean principal;

	public Endereco() {

	}

	public Endereco(Cidade cidade) {
		this.cidade = cidade;
	}

	public Endereco(Long codigo) {
		setCodigo(codigo);
	}

	public Endereco(String cep) {
		this.cep = cep;
	}

	public Endereco(Long codigo, String endereco, String uf, String cidade, String cep) {
		setCodigo(codigo);
		this.endereco = endereco;
		this.uf = uf;
		this.cep = cep;
	}

	public Endereco(Long codigo, String endereco, String uf, String cidade, String bairro, String cep) {
		setCodigo(codigo);
		this.endereco = endereco;
		this.uf = uf;
		this.bairro = bairro;
		this.cep = cep;
	}

	@Column(name = "ENDERECO", nullable = false)
	@NotNull
	public String getEndereco() {
		return this.endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@Column(name = "UF", nullable = false)
	@NotNull
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	@Column(name = "BAIRRO")
	public String getBairro() {
		return this.bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	@Column(name = "CEP", nullable = false)
	@NotNull
	public String getCep() {
		return this.cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@Override
	@Column(name = "PRINCIPAL")
	public Boolean getPrincipal() {
		return principal == null ? Boolean.FALSE : principal;
	}

	@Override
	public void setPrincipal(Boolean principal) {
		this.principal = principal == null ? Boolean.FALSE : principal;
	}

	@Column(name = "NUMERO")
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "COMPLEMENTO")
	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_TIPO_LOGRADOURO")
	public TipoLogradouro getTipoLogradouro() {
		return tipoLogradouro;
	}

	public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_MUNICIPIO")
	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		if (cidade != null) {
			this.uf = cidade.getUf();
		}
		this.cidade = cidade;
	}

	@Transient
	public TipoEnderecoContrato getTipoEnderecoContrato() {
		return tipoEnderecoContrato;
	}

	public void setTipoEnderecoContrato(TipoEnderecoContrato tipoEnderecoContrato) {
		this.tipoEnderecoContrato = tipoEnderecoContrato;
	}

	@Transient
	public String getEnderecoCompleto() {
		StringBuilder end = new StringBuilder();

		if (tipoLogradouro != null) {
			if (!tipoLogradouro.getNome().trim().equals("")) {
				end.append(tipoLogradouro.getNome().trim()).append(" ");
			}
		}

		if (endereco != null) {
			if (!endereco.trim().equals("")) {
				end.append(endereco.trim());
			}
		}

		if (numero != null) {
			if (!numero.trim().equals("")) {
				end.append(", ").append(numero.trim());
			}
		}

		if (complemento != null) {
			if (!complemento.trim().equals("")) {
				end.append(" - ").append(complemento.trim());
			}
		}

		return end.toString();
	}

	@Transient
	public String getCidadeUf() {
		if (cidade != null) {
			if (cidade.getUf() != null && !cidade.getUf().isEmpty()) {
				return cidade + "/" + cidade.getUf();
			}
		} else if (uf != null && !uf.isEmpty()) {
			return cidade + "/" + uf;
		}

		return "";

	}

	@Transient
	public String getCepFormatado() {
		if (cep != null) {
			return cep.length() >= 9 ? cep : cep.substring(0, 5).concat("-").concat(cep.substring(5, 8));
		}
		return "";
	}

	@Override
	public String toString() {
		return "Endereco [codigo=" + getCodigo() + ", endereco=" + endereco + ", numero=" + numero + ", uf=" + uf + ", cep=" + cep + ", tipoLogradouro="
			+ tipoLogradouro + ", tipoEnderecoContrato=" + tipoEnderecoContrato + ", principal=" + principal + "]";
	}

	@Transient
	public TipoEndereco getTipoEndereco() {
		return tipoEndereco;
	}

	public void setTipoEndereco(TipoEndereco tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}

	public enum TipoEndereco {
		PRESTACAO_SERVICO("Prestação de serviço"),
		CONTRATO("Contrato"),
		OUTROS("Outros");

		private final String descricao;

		private TipoEndereco(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		@Override
		public String toString() {
			return descricao;
		}
	}

}
