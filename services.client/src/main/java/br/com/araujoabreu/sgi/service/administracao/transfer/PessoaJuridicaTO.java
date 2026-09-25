package br.com.araujoabreu.sgi.service.administracao.transfer;

import br.com.araujoabreu.sgi.service.comercial.transfer.ClassificacaoClienteTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.RamoNegocioTO;

public class PessoaJuridicaTO extends PessoaTO {

	private String inscricaoEstadual;
	private String ufInscricaoEstadual;
	private String inscricaoMunicipal;
	private String nomeFantasia;
	private String emailContato;
	private CnaeTO cnaeTO;
	private RamoNegocioTO cnae;
	private ClassificacaoClienteTO classificacaoClienteTO;
	private String tipoPessoaJuridica;
	private Boolean mei;
	private String canalPreferencial;

	public PessoaJuridicaTO() {
	}

	public PessoaJuridicaTO(Long codigo) {
		setCodigo(codigo);
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getUfInscricaoEstadual() {
		return ufInscricaoEstadual;
	}

	public void setUfInscricaoEstadual(String ufInscricaoEstadual) {
		this.ufInscricaoEstadual = ufInscricaoEstadual;
	}

	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getEmailContato() {
		return emailContato;
	}

	public void setEmailContato(String emailContato) {
		this.emailContato = emailContato;
	}

	public CnaeTO getCnaeTO() {
		return cnaeTO;
	}

	public void setCnaeTO(CnaeTO cnaeTO) {
		this.cnaeTO = cnaeTO;
	}

	public RamoNegocioTO getCnae() {
		return cnae;
	}

	public void setCnae(RamoNegocioTO cnae) {
		this.cnae = cnae;
	}

	public ClassificacaoClienteTO getClassificacaoClienteTO() {
		return classificacaoClienteTO;
	}

	public void setClassificacaoClienteTO(ClassificacaoClienteTO classificacaoClienteTO) {
		this.classificacaoClienteTO = classificacaoClienteTO;
	}

	public String getTipoPessoaJuridica() {
		return tipoPessoaJuridica;
	}

	public void setTipoPessoaJuridica(String tipoPessoaJuridica) {
		this.tipoPessoaJuridica = tipoPessoaJuridica;
	}

	public Boolean getMei() {
		return mei;
	}

	public void setMei(Boolean mei) {
		this.mei = mei;
	}

	public String getCanalPreferencial() {
		return canalPreferencial;
	}

	public void setCanalPreferencial(String canalPreferencial) {
		this.canalPreferencial = canalPreferencial;
	}
}
