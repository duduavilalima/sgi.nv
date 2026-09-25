package br.com.araujoabreu.sgi.service.reinf.r2010;

public class InfoServTom { //Serviços Tomados - Cessão de Mão de Obra ou Empreitada
	private IdeEstabObra ideEstabObra; //Identificação do Estabelecimento/obra contratante dos Serviços

	public IdeEstabObra getIdeEstabObra() {
		return ideEstabObra;
	}

	public void setIdeEstabObra(IdeEstabObra ideEstabObra) {
		this.ideEstabObra = ideEstabObra;
	}
}
