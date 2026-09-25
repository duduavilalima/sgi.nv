package br.com.fasolti.sgi.job.lotes.inativacao.usuarios;

import br.com.araujoabreu.sgi.lang.AdministracaoException;
import br.com.fasolti.queue.commons.api.model.annotations.ImplementsJob;
import br.com.fasolti.queue.commons.api.model.annotations.WithJobModel;
import br.com.fasolti.queue.commons.api.model.entities.JobExecutionInfo;
import br.com.fasolti.queue.commons.api.model.enumerations.JobInstantiationMode;
import br.com.fasolti.sgi.job.impl.util.SgiJobImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@ImplementsJob
@WithJobModel(
	autostart = true,
	crontab = "#{configuracaoService.getAny('usuarios.interface.demitidos.crontab').orElse('0 0,10,20,30,40,50 7-22 ? * 1-5')}",
	instantiationMode = JobInstantiationMode.ORDERED)
public class InativacaoUsuarioJob extends SgiJobImpl {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	protected void doProcess(JobExecutionInfo execution) throws Exception {
		logger.info("Iniciando serviço de inativação automática de usuários: {}", execution.toString());

		try {
			criarTarefa().executar();

		} catch (AdministracaoException e) {
			logger.error("Serviço de inativação automática de usuários finalizado com erros!", e);
			throw e;

		} catch (Exception e) {
			logger.error("Serviço de inativação automática de usuários finalizado com erro desconhecido!", e);
			throw e;
		}

		logger.info("Serviço de inativação automática de usuários '{}' finalizado.", execution.toString());
	}

	private InativacaoUsuarioJobTask criarTarefa() {
		return applicationContext.getBean(InativacaoUsuarioJobTask.class);
	}
}
