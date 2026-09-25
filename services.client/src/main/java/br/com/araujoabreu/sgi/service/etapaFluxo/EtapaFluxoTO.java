package br.com.araujoabreu.sgi.service.etapaFluxo;

import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.administracao.transfer.FluxoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoAplicacaoTO;
import br.com.araujoabreu.sgi.service.security.transfer.PerfilTO;
import br.com.araujoabreu.sgi.service.util.BaseTO;

public class EtapaFluxoTO extends BaseTO {

	private String nome;
	private Integer ordem;
	private Integer statusAnterior;
	private Integer statusAtual;
	private Integer statusBase;
	private ColaboradorTO colaborador;
	private PerfilTO perfil;
	private MotivoAplicacaoTO motivoAplicacao;
	private FluxoTO fluxo;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Integer getStatusAtual() {
		return statusAtual;
	}

	public void setStatusAtual(Integer statusAtual) {
		this.statusAtual = statusAtual;
	}

	public Integer getStatusBase() {
		return statusBase;
	}

	public void setStatusBase(Integer statusBase) {
		this.statusBase = statusBase;
	}

	public ColaboradorTO getColaborador() {
		return colaborador;
	}

	public void setColaborador(ColaboradorTO colaborador) {
		this.colaborador = colaborador;
	}

	public PerfilTO getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilTO perfil) {
		this.perfil = perfil;
	}

	public MotivoAplicacaoTO getMotivoAplicacao() {
		return motivoAplicacao;
	}

	public void setMotivoAplicacao(MotivoAplicacaoTO motivoAplicacao) {
		this.motivoAplicacao = motivoAplicacao;
	}

	public Integer getStatusAnterior() {
		return statusAnterior;
	}

	public void setStatusAnterior(Integer statusAnterior) {
		this.statusAnterior = statusAnterior;
	}

	public FluxoTO getFluxo() {
		return fluxo;
	}

	public void setFluxo(FluxoTO fluxo) {
		this.fluxo = fluxo;
	}
}
