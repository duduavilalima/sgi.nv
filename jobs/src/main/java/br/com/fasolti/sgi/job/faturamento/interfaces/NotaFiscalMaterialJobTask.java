package br.com.fasolti.sgi.job.faturamento.interfaces;

import br.com.araujoabreu.sgi.administracao.domain.QTipo;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface;
import br.com.araujoabreu.sgi.contabilidade.domain.QLoteInterface;
import br.com.araujoabreu.sgi.lang.AdministracaoException;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.LoteInterfaceFaturamentoService;
import br.com.fasolti.sgi.repository.loteInterface.LoteInterfaceRepository;
import br.com.fasolti.sgi.repository.loteInterfaceFaturamento.LoteInterfaceFaturamentoRepository;
import br.com.fasolti.sgi.service.util.ConfiguracaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NotaFiscalMaterialJobTask {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ConfiguracaoService configuracaoService;

	@Autowired
	private LoteInterfaceRepository loteInterfaceRepository;

	@Autowired
	private LoteInterfaceFaturamentoService loteInterfaceFaturamentoService;

	void executar() throws AdministracaoException {
		List<LoteInterface> lotes = loteInterfaceRepository.findLoteContabilPorSituacaoETipo(LoteInterface.TipoLoteInterface.NF_MATERIAL, LoteInterface.SituacaoLoteInterface.CRIADO);
		if (lotes != null && !lotes.isEmpty()) {
			for(LoteInterface loteInterface : lotes){
				try{
					logger.info(String.format("Encontrado lote %s 'para geração' ", LoteInterface.TipoLoteInterface.NF_MATERIAL.getNome()));
					loteInterfaceFaturamentoService.getClass();
					loteInterfaceFaturamentoService.gerarLoteAPartirDeJobs(loteInterface.getId());
					logger.info(String.format("Job de lote %s finalizado.", LoteInterface.TipoLoteInterface.NF_MATERIAL.getNome()));
				}catch(Exception e){
					logger.info("Erro ao gerar o lote. " + e.getMessage());
					e.printStackTrace();
				}
			}
		} else {
			logger.info("Não foram encontrados lotes com o status 'em geração' ");
		}
	}
}
