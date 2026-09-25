package br.com.fasolti.sgi.job.contasapagar;

import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiro;
import br.com.araujoabreu.sgi.compras.domain.QNotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.QNotaPagamento;
import br.com.araujoabreu.sgi.lang.AdministracaoException;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.compras.NotaPagamentoService;
import br.com.araujoabreu.sgi.service.nota.NotaFiscalService;
import br.com.fasolti.commons.util.DozerHelper;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.security.domain.QUsuario;
import br.com.fasolti.queue.email.domain.Email;
import br.com.fasolti.queue.email.helpers.InternetAddresses;
import br.com.fasolti.queue.email.services.EmailService;
import br.com.fasolti.queue.email.services.EmailTemplateProcessor;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.service.util.ConfiguracaoService;
import com.querydsl.core.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.internet.InternetAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.fasolti.queue.email.JobsEmails.emailTo;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NotasEmailJobTask {
	/**
	 * Job para enviar por e-mail a listagem de NPs e NFs cadastradas no sistema,
	 * no dia imediatamente anterior a data de execução do JOB.
	 *
	 * Está agendado para executar apenas uma vez por dia, às 02:00h de terça a sábado.
	 *
	 * O e-mail é enviado a todos os usuários que possuem o perfil "@Relatorio especial (NP)"
	 */

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String ARQUIVO_TEMPLATE_EMAIL = "mail.npscriadas.html";
	private static final String ARQUIVO_TEMPLATE_ASSUNTO = "mail.npscriadas.subject";
	private static final int TAMANHO_MAXIMO_NOME = 40;
	private static final String PERFIL = "relatorio-especial-np";

	@Autowired
	private ConfiguracaoService configuracaoService;

	@Autowired
	private NotaPagamentoService notaPagamentoService;

	@Autowired
	private NotaFiscalService notaFiscalService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmailTemplateProcessor emailTemplateProcessor;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private DozerHelper mapper;

	private List<ColaboradorTO> destinatariosPadrao;

	@PostConstruct
	private void init() {
		List<Colaborador> colaboradores = colaboradorRepository.findByNomePerfil(PERFIL);
		this.destinatariosPadrao = mapper.map(colaboradores, ColaboradorTO.class);
	}

	void executar() throws AdministracaoException {
		QNotaFiscal qNotaFiscal = QNotaFiscal.notaFiscal;
		QNotaPagamento qNotaPagamento = QNotaPagamento.notaPagamento;
		QUsuario qUsuario = QUsuario.usuario;
		QPessoa qPessoa = QPessoa.pessoa;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;

		Calendar dia = Calendar.getInstance();
		dia.add(Calendar.DAY_OF_MONTH, -1); //recupera o dia anterior a execução do Job
//		dia.set(Calendar.YEAR, 2019);
//		dia.set(Calendar.MONTH, Calendar.DECEMBER);
//		dia.set(Calendar.DAY_OF_MONTH, 6);

		//Seleciona as NPs cadastradas
		List<Tuple> notas = notaPagamentoService.listarNPsPorData(dia.getTime());
		final List<NotasEmailBean> listaNps = new ArrayList<>();

		DecimalFormat df = new DecimalFormat("###,###,###.00");
		String numeroAux = "";
		String pessoaAux = "";

		/*
		Os dois próximos loops são utilizado para popular a lista de beans com o resultado
		e serão passados para o template de e-mail do thymeleaf.
		Para simplificar a query utilizada na seleção, as NPs e NFs rateadas para mais de um
		CC/CL serão exibidas em uma única linha, com a indacação "(+)", para informar
		a existência deste tipo de rateio.
		 */
		for (Tuple tuple : notas) {
			if (!numeroAux.equals(tuple.get(qNotaPagamento.numero))) {
				NotasEmailBean emailBean = new NotasEmailBean();
				emailBean.setNumero(tuple.get(qNotaPagamento.numero));
				emailBean.setValor(tuple.get(qNotaPagamento.valor));
				emailBean.setStrValor(df.format(emailBean.getValor()));

				String nome = tuple.get(qPessoa.nome)==null? "" : tuple.get(qPessoa.nome);
				emailBean.setNomeCredor((nome.length() > TAMANHO_MAXIMO_NOME ? nome.substring(0, TAMANHO_MAXIMO_NOME) : nome));
				emailBean.setDocumentoCredor(tuple.get(qPessoa.documento));

				nome = tuple.get(qUsuario.pessoaFisica.nome)==null? "" : tuple.get(qUsuario.pessoaFisica.nome);
				emailBean.setCriador(nome.length() > TAMANHO_MAXIMO_NOME ? nome.substring(0, TAMANHO_MAXIMO_NOME) : nome);

				emailBean.setCl(tuple.get(qCentroFinanceiro.identificador).substring(7));

				listaNps.add(emailBean);
				numeroAux = tuple.get(qNotaPagamento.numero);
			}
			else {
				String cl = listaNps.get(listaNps.size()-1).getCl();
				if (!cl.contains("+")) {
					listaNps.get(listaNps.size() - 1).setCl(cl + "(+)");
				}
			}
		}

		//Seleciona as NFs cadastradas
		numeroAux = "";
		pessoaAux = "";
		notas = notaFiscalService.listarNFsPorData(dia.getTime());
		final List<NotasEmailBean> listaNfs = new ArrayList<>();
		for (Tuple tuple : notas) {
			if (!numeroAux.equals(tuple.get(qNotaFiscal.numero)) && !pessoaAux.equals(tuple.get(qPessoa.nome))) {
				NotasEmailBean emailBean = new NotasEmailBean();
				emailBean.setNumero(tuple.get(qNotaFiscal.numero));
				emailBean.setValor(tuple.get(qNotaFiscal.valor));
				emailBean.setStrValor(df.format(emailBean.getValor()));

				String nome = tuple.get(qPessoa.nome)==null? "" : tuple.get(qPessoa.nome);
				emailBean.setNomeCredor((nome.length() > TAMANHO_MAXIMO_NOME ? nome.substring(0, TAMANHO_MAXIMO_NOME) : nome));
				emailBean.setDocumentoCredor(tuple.get(qPessoa.documento));

				nome = tuple.get(qUsuario.pessoaFisica.nome)==null? "" : tuple.get(qUsuario.pessoaFisica.nome);
				emailBean.setCriador(nome.length() > TAMANHO_MAXIMO_NOME ? nome.substring(0, TAMANHO_MAXIMO_NOME) : nome);

				emailBean.setCl(tuple.get(qCentroFinanceiro.identificador).substring(7));

				listaNfs.add(emailBean);
				numeroAux = tuple.get(qNotaFiscal.numero);
				pessoaAux = tuple.get(qPessoa.nome);
			}
			else {
				String cl = listaNfs.get(listaNfs.size()-1).getCl();
				if (!cl.contains("+")) {
					listaNfs.get(listaNfs.size() - 1).setCl(cl + "(+)");
				}
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		NotasEmailBean emailBean = new NotasEmailBean();
		emailBean.setTotal(listaNps.size());
		emailBean.setTotalNfs(listaNfs.size());
		emailBean.setStrDataCriacao(sdf.format(dia.getTime()));

		emailBean.setNps(listaNps);
		emailBean.setNfs(listaNfs);

		List<InternetAddress> destinatarios = destinatariosPadrao.stream()
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
