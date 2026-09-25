package br.com.fasolti.corporativo.pessoa.domain;

import br.com.araujoabreu.sgi.administracao.domain.ContratoContato;
import br.com.araujoabreu.sgi.administracao.interf.Detail;
import br.com.fasolti.commons.domain.DomainObject;
import br.com.fasolti.corporativo.auditoria.domain.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CONTATO")
@SequenceGenerator(name = "SEQ_CONTATO", sequenceName = "SEQ_CONTATO", allocationSize = 1)
public class Contato extends AuditEntity implements DomainObject, Detail {

	private static final long serialVersionUID = 7852050481447363110L;
	private String nome;
	private Pessoa pessoa;
	private RelacionamentoContato relacionamentoContato;
	private Cargo cargo;
	private Long codigoCargo;
	private Long codigoRelacionamentoContato;
	private Telefone telefone1;
	private Telefone telefone2;
	private String email;

	//    private List<Telefone> listaTelefone;
	private Set<ContratoContato> contratoContatos = new HashSet<>(0);


	@Column(name = "NOME", nullable = false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_PESSOA")
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_RELACIONAMENTO_CONTATO", nullable = false)
	public RelacionamentoContato getRelacionamentoContato() {
		return relacionamentoContato;
	}

	public void setRelacionamentoContato(RelacionamentoContato relacionamentoContato) {
		this.relacionamentoContato = relacionamentoContato;
	}

	@Column(name = "EMAIL", length = 100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Transient
	public Telefone getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(Telefone telefone1) {
		this.telefone1 = telefone1;
	}

	@Transient
	public Telefone getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(Telefone telefone2) {
		this.telefone2 = telefone2;
	}

	@ManyToOne
	@JoinColumn(name = "CODIGO_CARGO")
	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	@Transient
	public Long getCodigoCargo() {
		if (codigoCargo == null && cargo != null) {
			codigoCargo = cargo.getCodigo();
		}

		return codigoCargo;
	}

	public void setCodigoCargo(Long codigoCargo) {
		this.codigoCargo = codigoCargo;
	}

	@Transient
	public Long getCodigoRelacionamentoContato() {
		if (codigoRelacionamentoContato == null && relacionamentoContato != null) {
			codigoRelacionamentoContato = relacionamentoContato.getCodigo();
		}

		return codigoRelacionamentoContato;
	}

	public void setCodigoRelacionamentoContato(Long codigoRelacionamentoContato) {
		this.codigoRelacionamentoContato = codigoRelacionamentoContato;
	}

	//    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "contato")
//    @OrderBy(value = "codigo ASC")
//    @Cascade({ org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
//    public List<Telefone> getListaTelefone() {
//        return listaTelefone;
//    }
//
//    public void setListaTelefone(List<Telefone> listaTelefone) {
//        this.listaTelefone = listaTelefone;
//    }
//
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contato")
	public Set<ContratoContato> getContratoContatos() {
		return this.contratoContatos;
	}

	public void setContratoContatos(Set<ContratoContato> contratoContatos) {
		this.contratoContatos = contratoContatos;
	}

	@Override
	@Transient
	public Boolean getPrincipal() {
		return false;
	}

	@Override
	public void setPrincipal(Boolean principal) {
	}

	@Override
	public String toString() {
		return "Contato [codigo=" + getCodigo() + ", nome=" + nome + ", cargo=" + cargo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getCodigo() == null) ? 0 : getCodigo().hashCode());
		result = prime * result + ((codigoCargo == null) ? 0 : codigoCargo.hashCode());
		result = prime * result + ((codigoRelacionamentoContato == null) ? 0 : codigoRelacionamentoContato.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Contato other = (Contato) obj;
		if (getCodigo() == null) {
			if (other.getCodigo() != null) {
				return false;
			}
		} else if (!getCodigo().equals(other.getCodigo())) {
			return false;
		}
		if (codigoCargo == null) {
			if (other.codigoCargo != null) {
				return false;
			}
		} else if (!codigoCargo.equals(other.codigoCargo)) {
			return false;
		}
		if (codigoRelacionamentoContato == null) {
			if (other.codigoRelacionamentoContato != null) {
				return false;
			}
		} else if (!codigoRelacionamentoContato.equals(other.codigoRelacionamentoContato)) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		return true;
	}

//    @Transient
//    public String getTelefonesContato(){
//    	StringBuffer telefones = new StringBuffer();
//    	if (listaTelefone != null && !listaTelefone.isEmpty()){
//    		telefone1 = listaTelefone.get(0);
//    		if (listaTelefone.size() > 1){
//    			telefone2 = listaTelefone.get(1);
//    		}
//    		telefones.append(telefone1.getDddTelefone());
//    		if (telefone2 != null && telefone2.getCodigo() != null){
//    			telefones.append(" / ");
//    			telefones.append(telefone2.getDddTelefone());
//    		}
//    	}
//    	return telefones.toString();
//    }

}
