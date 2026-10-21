package br.com.fasolti.commons;

import java.io.File;
import java.io.Serializable;

public class FileBean implements Serializable {

	private String nome;
	private String pastaDestino;
	private String extensao;

	public FileBean() {
	}

	public FileBean(String nome, String pastaDestino, String extensao) {
		this.nome = nome;
		this.pastaDestino = pastaDestino;
		this.extensao = extensao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPastaDestino() {
		return pastaDestino;
	}

	public void setPastaDestino(String pastaDestino) {
		this.pastaDestino = pastaDestino;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public String getNomeCompleto() {
		return extensao == null || extensao.isEmpty() ? nome : nome + "." + extensao;
	}

	public String getCaminhoCompleto() {
		return pastaDestino == null || pastaDestino.isEmpty()
			? getNomeCompleto()
			: pastaDestino + File.separator + getNomeCompleto();
	}
}
