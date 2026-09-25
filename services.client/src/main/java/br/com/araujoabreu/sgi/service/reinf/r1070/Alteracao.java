package br.com.araujoabreu.sgi.service.reinf.r1070;

public class Alteracao {
	private IdeProcesso ideProcesso;
	private InfoSusp infoSusp;
	private DadosProcJud dadosProcJud;
	private NovaValidade novaValidade;

	public IdeProcesso getIdeProcesso() {
		return ideProcesso;
	}

	public void setIdeProcesso(IdeProcesso ideProcesso) {
		this.ideProcesso = ideProcesso;
	}

	public InfoSusp getInfoSusp() {
		return infoSusp;
	}

	public void setInfoSusp(InfoSusp infoSusp) {
		this.infoSusp = infoSusp;
	}

	public DadosProcJud getDadosProcJud() {
		return dadosProcJud;
	}

	public void setDadosProcJud(DadosProcJud dadosProcJud) {
		this.dadosProcJud = dadosProcJud;
	}

	public NovaValidade getNovaValidade() {
		return novaValidade;
	}

	public void setNovaValidade(NovaValidade novaValidade) {
		this.novaValidade = novaValidade;
	}
}
