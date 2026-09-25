package br.com.araujoabreu.sgi.service.reinf;

public class IdeContri { //Informações de identificação do contribuinte
	private int tpInsc = TipoInscricao.CNPJ.getValor(); //Preencher com o código correspondente ao tipo de inscrição. Deve ser igual a [1] (CNPJ) ou [2] (CPF)
	private String nrInsc; /*Informar o número de inscrição do contribuinte de acordo com o tipo de
							inscrição indicado no campo {tpInsc}. Se for um CNPJ deve ser informada
							apenas a Raiz/Base de oito posições, exceto se natureza jurídica de
							administração pública direta federal ([101-5], [104-0], [107-4], [116-3],
							situação em que o campo deve ser preenchido com o CNPJ completo (14
							posições)
							*/

	public int getTpInsc() {
		return tpInsc;
	}

	public void setTpInsc(int tpInsc) {
		this.tpInsc = tpInsc;
	}

	public String getNrInsc() {
		return nrInsc;
	}

	public void setNrInsc(String nrInsc) {
		this.nrInsc = nrInsc;
	}

	public enum TipoInscricao {
		CNPJ(1), CPF(2), CNO(4);

		final int valor;

		TipoInscricao(int valor) {
			this.valor = valor;
		}

		public int getValor() {
			return valor;
		}
	}
}
