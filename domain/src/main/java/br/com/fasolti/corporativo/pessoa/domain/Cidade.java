package br.com.fasolti.corporativo.pessoa.domain;

import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface;
import br.com.fasolti.framework.domain.BaseEntity;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name = "MUNICIPIO_ECT")
public class Cidade extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8939615929407567578L;
	private String nome;
	private String uf;
	private boolean interfacePrefeitura;
	private LoteInterface.TipoLoteInterface tipoLoteInterface;

	@Column(name = "NOME")
	@Length(max = 70)
	@NotNull
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "UF", length = 2)
	@Length(max = 2)
	@NotNull
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}


	@Column(name = "interface_prefeitura")
	@NotNull
	public boolean getInterfacePrefeitura() {
		return interfacePrefeitura;
	}

	public void setInterfacePrefeitura(boolean interfacePrefeitura) {
		this.interfacePrefeitura = interfacePrefeitura;
	}

	@Transient
	public String getCidadeEstado() {
		return nome + "/" + uf;
	}

	@Transient
	public String getCidadeUf() {
		return nome + "/" + uf;
	}

	@Transient
	public String getEstadoCidade() {
		return uf + "/" + nome;
	}

	@Column(name = "TIPO_LOTE_INTERFACE")
	public LoteInterface.TipoLoteInterface getTipoLoteInterface() {
		return tipoLoteInterface;
	}

	public void setTipoLoteInterface(LoteInterface.TipoLoteInterface tipoLoteInterface) {
		this.tipoLoteInterface = tipoLoteInterface;
	}

	@Override
	public String toString() {
		return nome;
	}

}
