package br.com.araujoabreu.sgi.service.administracao;

import br.com.araujoabreu.sgi.service.etapaFluxo.EtapaFluxoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface EtapaFluxoService extends CrudService<EtapaFluxoTO> {
	List<EtapaFluxoTO> obterEtapaFluxoColaboradorEPerfil();

	EtapaFluxoTO obterEtapaFLuxoComMotivoAplicacao(Long idEtapaFLuxo);

	EtapaFluxoTO findByCodFluxoAndStatusAtual(Long codigoFluxo, Integer statusAtual);

}
