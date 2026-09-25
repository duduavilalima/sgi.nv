package br.com.araujoabreu.sgi.service.reinf.r1000;

public class Contato {
	private String nmCtt; /* Nome do contato no contribuinte. Pessoa responsável por ser o contato do
							contribuinte com a Receita Federal do Brasil relativamente à EFD-Reinf.
							*/

	private String cpfCtt; //Preencher com o número do CPF do contato.

	private String foneFixo;//Informar o número do telefone, com DDD.
	private String foneCel; //Telefone celular, com DDD
	private String email;    //Endereço eletrônico

	public String getNmCtt() {
		return nmCtt;
	}

	public void setNmCtt(String nmCtt) {
		this.nmCtt = nmCtt;
	}

	public String getCpfCtt() {
		return cpfCtt;
	}

	public void setCpfCtt(String cpfCtt) {
		this.cpfCtt = cpfCtt;
	}

	public String getFoneFixo() {
		return foneFixo;
	}

	public void setFoneFixo(String foneFixo) {
		this.foneFixo = foneFixo;
	}

	public String getFoneCel() {
		return foneCel;
	}

	public void setFoneCel(String foneCel) {
		this.foneCel = foneCel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
