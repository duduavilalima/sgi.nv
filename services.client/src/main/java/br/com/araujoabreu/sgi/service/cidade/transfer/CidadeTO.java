package br.com.araujoabreu.sgi.service.cidade.transfer;

import br.com.araujoabreu.sgi.service.util.BaseTO;

public class CidadeTO extends BaseTO {

	private String nome;
	private String nomeComAcento;
	private String uf;
	private boolean interfacePrefeitura;

	public CidadeTO() {
	}

	public CidadeTO(Long codigo) {
		setCodigo(codigo);
	}

	public CidadeTO(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public boolean getInterfacePrefeitura() {
		return interfacePrefeitura;
	}

	public void setInterfacePrefeitura(boolean interfacePrefeitura) {
		this.interfacePrefeitura = interfacePrefeitura;
	}

	public String getNomeComAcento() {
		return nomeComAcento;
	}

	public void setNomeComAcento(String nomeComAcento) {
		this.nomeComAcento = nomeComAcento;
	}

	public String getCidadeUf() {
		String cidadeUf = this.nome != null ? this.nome : null;

		cidadeUf = cidadeUf == null ? "" : cidadeUf + (this.uf == null ? "" : "/" + this.uf);

		return cidadeUf;

	}
}
