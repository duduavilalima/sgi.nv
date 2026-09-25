package br.com.fasolti.sgi.job.despesascl;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.lang.AdministracaoException;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.gerencia.GerenciaService;
import br.com.araujoabreu.sgi.service.gerencia.transfer.GerenciaTO;
import br.com.fasolti.commons.util.DozerHelper;
import br.com.fasolti.queue.email.domain.Email;
import br.com.fasolti.queue.email.helpers.InternetAddresses;
import br.com.fasolti.queue.email.services.EmailService;
import br.com.fasolti.queue.email.services.EmailTemplateProcessor;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.service.util.ConfiguracaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.internet.InternetAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static br.com.fasolti.queue.email.JobsEmails.emailTo;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DespesasCLEmailJobTask {

	public static final String JOB_DESPESAS_CL_LIMITE = "job.despesas.cl.limite";
	public static final String JOB_DESPESAS_CL_FUTURO = "job.despesas.cl.futuro";
	public static final String DATE_FORMAT_EMAIL = "MMMM/yyyy";
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String ARQUIVO_TEMPLATE_EMAIL = "mail.despesasCL.html";
	private static final String ARQUIVO_TEMPLATE_ASSUNTO = "mail.despesasCL.subject";
	private static final int TAMANHO_MAXIMO_NOME = 40;
	private static final String PERFIL = "relatorio-especial-cl";
	private static final String PERFIL_DIRETORIA = "financeiro-diretor";

	@Autowired
	private ConfiguracaoService configuracaoService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmailTemplateProcessor emailTemplateProcessor;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private CentroFinanceiroService centroFinanceiroService;

	@Autowired
	private GerenciaService gerenciaService;

	@Autowired
	private DozerHelper mapper;

	private List<ColaboradorTO> destinatariosPadrao;

	@PostConstruct
	private void init() {
		//Recupera a lista de todos os colaboradores com o perfil "relatorio-especial-cl"
		this.destinatariosPadrao = mapper.map(colaboradorRepository.findByNomePerfil(PERFIL), ColaboradorTO.class);
	}

	void executar() throws AdministracaoException {
		LocalDateTime hoje = LocalDateTime.now();
		logger.info("Iniciando JOB de despesas de CLs...");

		//Recupera a lista de todos os colaboradores com o perfil "financeiro-diretor"
		List<ColaboradorTO> diretoria = mapper.map(colaboradorRepository.findByNomePerfil(PERFIL_DIRETORIA), ColaboradorTO.class);

		//Filtra a lista de colaboradores, mantendo apenas os que possuem os dois perfis "relatorio-especial-cl" e "financeiro-diretor"
		List<ColaboradorTO> destinatariosDiretoria = this.destinatariosPadrao.stream()
			.distinct()
			.filter(diretoria::contains)
			.collect(Collectors.toList());

		//Essa nova lista de colaboradores mantém somente o perfil "relatorio-especial-cl"  e não possui o perfil "financeiro-diretor"
		List<ColaboradorTO> destinatariosGerencia = new ArrayList<>(this.destinatariosPadrao);
		destinatariosGerencia.removeAll(destinatariosDiretoria);

		//Recupera a lista de todas as gerências
		List<GerenciaTO> gerencias = gerenciaService.listAll();

		//Map para agrupar os cls com despesa acima do limite, pelo seu gerente
		Map<ColaboradorTO, List<CentroFinanceiroTO>> mapCls = new HashMap<>();

		String meses = configuracaoService.getAny(JOB_DESPESAS_CL_FUTURO).get();
		Double limite = Double.valueOf(configuracaoService.getAny(JOB_DESPESAS_CL_LIMITE).get());

		//Processa todos os CLs e retorna lista dos que estão com despesas acima do limit
		List<CentroFinanceiroTO> cls = centroFinanceiroService.cLsComDespesasAcimaLimite(Date.from(hoje.atZone(ZoneId.systemDefault()).toInstant()), limite, meses);

		//Envio de email para os Colaboradores que possuem os perfis "relatorio-especial-cl" e "financeiro-diretor". Estes recebem a lista completa de CLs
		this.enviarEmails(destinatariosDiretoria, cls, hoje);

		//Envio de email para os Colaboradores que possuem o perfil "relatorio-especial-cl" e são gerentes de algum dos CLs com despesas acima do limite
		for (GerenciaTO gerencia : gerencias) {
			if (destinatariosGerencia.contains(gerencia.getGerente())) {
				List<CentroFinanceiroTO> clsGerencia = cls.stream().filter(cl -> cl.getPai().getCodigo().equals(gerencia.getCodigo())).collect(Collectors.toList());
				List<ColaboradorTO> destinatarioGerente = new ArrayList<>();
				destinatarioGerente.add(gerencia.getGerente());

				if (clsGerencia != null && !clsGerencia.isEmpty())
					this.enviarEmails(destinatarioGerente, clsGerencia, hoje);
			}
		}

		logger.info("Iniciando JOB de despesas finalizado.");
	}

	private void enviarEmails(List<ColaboradorTO> destinatariosEmail, List<CentroFinanceiroTO> cls, LocalDateTime hoje) {
		DespesasCLEmailBean emailBean = new DespesasCLEmailBean();
		emailBean.setDataDespesas(hoje.format(DateTimeFormatter.ofPattern(DATE_FORMAT_EMAIL)));
		emailBean.setDespesasLimite(configuracaoService.getAny(JOB_DESPESAS_CL_LIMITE).get());

		emailBean.setcFList(cls);
		emailBean.setTotal(emailBean.getcFList().size());

		List<InternetAddress> destinatarios = destinatariosEmail.stream()
			.map(d -> InternetAddresses.create(d.getEmail(), d.getNome()))
			.collect(Collectors.toList());

		//Envio do Email
		String assunto = emailTemplateProcessor.process(ARQUIVO_TEMPLATE_ASSUNTO, emailBean).getContent();

		Email email = emailTo(destinatarios)
			.withSubject(assunto)
			.htmlTemplate(ARQUIVO_TEMPLATE_EMAIL, emailBean);

		logger.info(String.format("Enviando email para: %s", email.getTo()));

		emailService.send(email);

	}
}
