package br.com.fasolti.sgi.job.controlefiscal.reinf;

import br.com.araujoabreu.sgi.lang.AdministracaoException;
import br.com.fasolti.sgi.job.util.JobSchedule;
import br.com.fasolti.sgi.job.util.JobExecutionInfo;
import br.com.fasolti.sgi.job.impl.util.SgiJobImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
@JobSchedule(
	autostart = true,
	crontab = "#{configuracaoService.getAny('controlefiscal.reinf.gerar.crontab').orElse('0 0/10 5-23 ? * 1-7')}")
public class ReinfGerarJob extends SgiJobImpl {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String MSG = "Geração dos arquivos do Reinf para envio à Receita Federal";

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	protected void doProcess(JobExecutionInfo execution) throws Exception {
		Calendar dia = Calendar.getInstance();
		dia.add(Calendar.DAY_OF_MONTH, -1);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		logger.info("Iniciando - {} {}: {}", MSG, sdf.format(dia.getTime()), execution.toString());

		try {
			criarTarefa().executar();

		} catch (AdministracaoException e) {
			logger.error("{} {} finalizado com erros!", MSG, sdf.format(dia.getTime()), e);
			throw e;

		} catch (Exception e) {
			logger.error("{} {} finalizado com erro desconhecido!", MSG, sdf.format(dia.getTime()), e);
			throw e;
		}

		logger.info("Finalizado - {} {}: {}", MSG, sdf.format(dia.getTime()), execution.toString());
	}

	private ReinfGerarJobTask criarTarefa() {
		return applicationContext.getBean(ReinfGerarJobTask.class);
	}

}
