package br.com.fasolti.sgi.job.controlefiscal.reinf;

import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface;
import br.com.araujoabreu.sgi.controleFiscal.domain.Reinf;
import br.com.araujoabreu.sgi.lang.AdministracaoException;
import br.com.araujoabreu.sgi.service.controlefiscal.ReinfService;
import br.com.araujoabreu.sgi.service.controlefiscal.transfer.ReinfTO;
import br.com.fasolti.sgi.service.util.ConfiguracaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReinfGerarJobTask {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ConfiguracaoService configuracaoService;

	@Autowired
	ReinfService reinfService;

	void executar() throws AdministracaoException {
		List<ReinfTO> reinfs = reinfService.findReinfBySituacao(Reinf.ReinfStatus.EM_GERACAO_EVENTOS.ordinal());
		if (reinfs != null && !reinfs.isEmpty()) {
			for(ReinfTO reinfTO : reinfs){
				try{
					logger.info(String.format("Encontrado Reinf %s da empresa %s para geração", reinfTO.getReferencia(), reinfTO.getUnidade().getCodnome()));
					reinfService.gerarReinf(reinfTO.getReferencia(), reinfTO.getUnidade().getCodigo());
					logger.info("Job de geração de arquivos do Reinf finalizado.");
				}
				catch(Exception e){
					logger.info("Erro ao gerar o lote. " + e.getMessage());
					e.printStackTrace();
				}
			}
		} else {
			logger.info("Não foram encontrados Reinfs com o status 'Em Geração' ");
		}
	}
}
