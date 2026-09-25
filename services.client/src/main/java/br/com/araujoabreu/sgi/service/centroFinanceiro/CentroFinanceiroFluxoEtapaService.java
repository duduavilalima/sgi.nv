package br.com.araujoabreu.sgi.service.centroFinanceiro;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroFluxoEtapaTO;
import br.com.araujoabreu.sgi.service.etapaFluxo.EtapaFluxoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.MotivoAplicacaoTO;
import br.com.araujoabreu.sgi.service.motivoRetorno.transfer.RetornoAssinaturaTO;
import br.com.araujoabreu.sgi.service.util.MenssagemPadraoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface CentroFinanceiroFluxoEtapaService extends CrudService<CentroFinanceiroFluxoEtapaTO> {
	List<EtapaFluxoTO> buscarTodosEtapaFluxo();

	MotivoAplicacaoTO motivoAplicacaoByFluxoEtapaId(String id) throws Exception;

	List<CentroFinanceiroFluxoEtapaTO> criarAssinaturas();

	MenssagemPadraoTO assinarCentroFinanceiro(Long codEtapaFluxo, Long codCentroFinanceiro);

	void retornarAssinaturaCentroFinanceiro(RetornoAssinaturaTO retornoAssinaturaTO);

	void retornarAssinaturaSemMotivoRetorno(RetornoAssinaturaTO retornoAssinaturaTO);
}
