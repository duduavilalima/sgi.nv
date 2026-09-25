package br.com.fasolti.sgi.job.lotes.inativacao.usuarios;

import br.com.araujoabreu.sgi.administracao.domain.LoteInativacao;
import br.com.araujoabreu.sgi.lang.AdministracaoException;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.lote.LoteInativacaoService;
import br.com.araujoabreu.sgi.service.lote.transfer.LoteInativacaoTO;
import br.com.araujoabreu.sgi.service.lote.util.ArquivoLoteInativacao;
import br.com.fasolti.queue.email.domain.Email;
import br.com.fasolti.queue.email.helpers.InternetAddresses;
import br.com.fasolti.queue.email.services.EmailService;
import br.com.fasolti.queue.email.services.EmailTemplateProcessor;
import br.com.fasolti.sgi.service.util.ConfiguracaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.fasolti.queue.email.JobsEmails.emailTo;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InativacaoUsuarioJobTask {

	private static final Logger logger = LoggerFactory.getLogger(InativacaoUsuarioJobTask.class);

	private static final String ARQUIVO_TEMPLATE_EMAIL = "mail.inativados.html";
	private static final String ARQUIVO_TEMPLATE_ASSUNTO = "mail.inativados.subject";

	private static final String CHAVE_DIRETORIO_IMPORTACAO = "usuarios.interface.demitidos.diretorio";
	private static final String DIRETORIO_IMPORTACAO_PADRAO = "/var/lib/sgi/interface/rh/";

	private static final String CHAVE_DIRETORIO_HISTORICO = "usuarios.interface.demitidos.diretorio.history";
	private static final String DIRETORIO_HISTORICO_PADRAO = "/var/lib/sgi/interface/rh/history/";
	@Autowired
	private ConfiguracaoService configuracaoService;


	// ---
	@Autowired
	private LoteInativacaoService loteInativacaoService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private EmailTemplateProcessor emailTemplateProcessor;
	// Configuração (fixo, determinado no ato da criação do bean)
	private Path diretorioImportacao;

	// ---
	private Path diretorioHistorico;
	private List<ColaboradorTO> destinatariosPadrao;
	// Estado (muda de acordo com a execução dos lotes de inativação)
	private LoteInativacaoTO loteInativacaoAtual;
	private ArquivoLoteInativacao arquivoLoteInativacaoAtual;
	private Path arquivoImportacaoAtual;
	private Path destinoArquivoImportacaoAtual;
	private Estado estadoProcessamento;

	@PostConstruct
	private void init() {
		this.diretorioImportacao = verificarDiretorioImportacao();
		this.diretorioHistorico = verificarDiretorioHistorico();
		this.destinatariosPadrao = loteInativacaoService.findDestinatariosEmailParaImportacaoAutomatica();
	}


	// ---
	// CONFIGURAÇÃO
	// ---

	// ---
	// EXECUÇÃO
	// ---
	void executar() throws AdministracaoException, IOException {
		List<Path> arquivosImportacao = new ArrayList<>();
		Files.newDirectoryStream(diretorioImportacao, p -> Files.isRegularFile(p)).forEach(arquivosImportacao::add);

		for (Path arquivoImportacao : arquivosImportacao) {
			arquivoImportacao = arquivoImportacao.toAbsolutePath();

			try {
				processarArquivoImportacao(arquivoImportacao);

				// Envio de e-mail feito fora do método de processamento, para garantir que o envio de e-mail não
				// cancele a transação de persistência do novo lote.
				enviarEmail();

			} catch (Exception e) {

				if (isArquivoMovido() && !isLoteProcessado()) {
					desfazerMoverArquivoImportacao();
				}

				logger.error("Erro ao processar arquivo de inativos '" + arquivoImportacao.toString() + "'", e);
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processarArquivoImportacao(Path arquivoImportacao) throws AdministracaoException {
		iniciar(arquivoImportacao);
		abrirArquivoLoteInativacao();
		importarArquivoLoteInativacao();
		moverArquivoImportacao();
	}

	//
	// TRANSIÇÃO DE ESTADO
	//

	private void iniciar(Path arquivoImportacao) {
		this.estadoProcessamento = Estado.INICIADO;
		this.arquivoImportacaoAtual = arquivoImportacao;
		this.destinoArquivoImportacaoAtual = null;
		this.arquivoLoteInativacaoAtual = null;
		this.loteInativacaoAtual = null;
	}

	private void abrirArquivoLoteInativacao() {
		this.arquivoLoteInativacaoAtual = ArquivoLoteInativacao.abrir(arquivoImportacaoAtual);
		this.estadoProcessamento = Estado.ARQUIVO_ABERTO;
	}

	private void importarArquivoLoteInativacao() {
		this.loteInativacaoAtual = loteInativacaoService.importar(arquivoLoteInativacaoAtual);
		this.estadoProcessamento = Estado.LOTE_PROCESSADO;
	}

	private void moverArquivoImportacao() throws AdministracaoException {
		destinoArquivoImportacaoAtual = construirNomeArquivoHistorico(arquivoImportacaoAtual, loteInativacaoAtual.getAnexo().getCodigo());
		moverArquivo(arquivoImportacaoAtual, destinoArquivoImportacaoAtual);
		this.estadoProcessamento = Estado.ARQUIVO_MOVIDO;
	}

	private void desfazerMoverArquivoImportacao() throws AdministracaoException {
		assert isArquivoMovido();

		moverArquivo(destinoArquivoImportacaoAtual, arquivoImportacaoAtual);
	}

	private void enviarEmail() {
		boolean arquivoImportado = loteInativacaoAtual.getSituacao().equals(LoteInativacao.Situacao.IMPORTADO.name());

		final InativacaoUsuarioEmailBean emailBean = new InativacaoUsuarioEmailBean();
		emailBean.setCodigoLoteFormatado(loteInativacaoAtual.getCodigoFormatado());
		emailBean.setNomeArquivo(loteInativacaoAtual.getAnexo().getNome());
		emailBean.setSucesso(arquivoImportado);
		emailBean.setMotivoErro("");
		arquivoLoteInativacaoAtual.getPrimeiroErro().ifPresent(e -> emailBean.setMotivoErro(e.getName()));

		emailBean.setTotalRegistros(loteInativacaoAtual.getTotalRegistros());
		emailBean.setTotalNaoLocalizado(loteInativacaoAtual.getTotalNaoLocalizado());
		emailBean.setTotalImportadoComPc(loteInativacaoAtual.getTotalImportadoComPc());
		emailBean.setTotalImportadoSemPc(loteInativacaoAtual.getTotalImportadoSemPc());
		emailBean.setTotalJaInativado(loteInativacaoAtual.getTotalJaInativado());
		emailBean.setTotalIgnorado(loteInativacaoAtual.getTotalIgnorado());

		List<InternetAddress> destinatarios = destinatariosPadrao.stream()
			.map(d -> InternetAddresses.create(d.getEmail(), d.getNome()))
			.collect(Collectors.toList());

		String assunto = emailTemplateProcessor.process(ARQUIVO_TEMPLATE_ASSUNTO, emailBean).getContent();

		Email email = emailTo(destinatarios)
			.withSubject(assunto)
			.htmlTemplate(ARQUIVO_TEMPLATE_EMAIL, emailBean);

		logger.info(String.format("Enviando email para: %s", email.getTo()));

		emailService.send(email);
		this.estadoProcessamento = Estado.EMAIL_ENVIADO;
	}

	private boolean isArquivoMovido() {
		switch (this.estadoProcessamento) {
			case INICIADO:
			case ARQUIVO_ABERTO:
			case LOTE_PROCESSADO:
				return false;
			case ARQUIVO_MOVIDO:
			case EMAIL_ENVIADO:
				return true;
		}
		return false;
	}

	//
	// UTILS
	//

	private boolean isLoteProcessado() {
		switch (this.estadoProcessamento) {
			case INICIADO:
			case ARQUIVO_ABERTO:
				return false;
			case LOTE_PROCESSADO:
			case ARQUIVO_MOVIDO:
			case EMAIL_ENVIADO:
				return true;
		}
		return false;
	}

	private Path construirNomeArquivoHistorico(Path arquivoImportacao, Long codigoAnexo) {
		return new File(diretorioHistorico.toFile(), codigoAnexo.toString() + "_" + arquivoImportacao.getFileName().toString()).toPath();
	}

	private void moverArquivo(Path arquivoOrigem, Path arquivoDestino) throws AdministracaoException {
		Path caminhoOrigem = arquivoOrigem.toAbsolutePath();
		Path caminhoDestino = arquivoDestino.toAbsolutePath();

		try {
			Files.move(caminhoOrigem, caminhoDestino);

		} catch (IOException e) {
			throw new AdministracaoException("Não foi possível mover o arquivo de lote"
				+ " de '" + caminhoOrigem.toString() + "'"
				+ " para '" + caminhoDestino.toString() + "'", e);
		}
	}

	private String getParametro(String chave, String padrao) {
		Optional<String> parametro = configuracaoService.getAny(chave);
		if (!parametro.isPresent()) {
			logger.error("Não foi possível obter o valor para o parâmetro '{}'.", chave);
		}
		return parametro.orElse(padrao);
	}

	private void validarArquivoDiretorio(
		Path arquivo,
		Boolean possuiPermissaoLeitura,
		Boolean possuiPermissaoEscrita,
		Boolean possuiPermissaoListagem,
		String mensagem) throws AdministracaoException {
		boolean valido = Files.isDirectory(arquivo)
			&& (possuiPermissaoLeitura == null || Files.isReadable(arquivo) == possuiPermissaoLeitura)
			&& (possuiPermissaoEscrita == null || Files.isWritable(arquivo) == possuiPermissaoEscrita)
			&& (possuiPermissaoListagem == null || Files.isExecutable(arquivo) == possuiPermissaoListagem);
		if (!valido) {
			// NOTE Provavelmente há algum problema na implementação da OpenJDK em que, caso o usuário
			// não exista na /etc/passwd, algumas verificações de permissão falham ou os resultados
			// incorretos são retornados. Visto que, no processo de deploy via Docker em uso atualmente,
			// (em alguns casos) algumas pastas e arquivos compartilhados através de volumes ou criadas por
			// processos externos ao container não contém um owner ou group válido para o sistema operacional
			// "convidado" (no container), é melhor não haver uma falha ou erro caso a verificação de
			// permissões nas pastas de importação falhe.
			// Resumo: não lançar exceção caso a pasta não possa ser lida, escrita ou executada. Ao invés disto,
			// limitar-se à efetuar uma entrada de log, ou não fazer nada e deixar que o erro aconteça em um
			// momento posterior do processo de importação dos lotes.
			logger.warn("{}: {}", arquivo.toString(), mensagem);
		}
	}

	private Path verificarDiretorioImportacao() throws AdministracaoException {
		String caminho = getParametro(CHAVE_DIRETORIO_IMPORTACAO, DIRETORIO_IMPORTACAO_PADRAO);
		Path arquivo = Paths.get(caminho).toAbsolutePath();
		validarArquivoDiretorio(arquivo, true, true, true, "Ignorando possíveis problemas de permissão e/ou acesso à pasta de arquivos de importação.");
		return arquivo;
	}

	private Path verificarDiretorioHistorico() throws AdministracaoException {
		String caminho = getParametro(CHAVE_DIRETORIO_HISTORICO, DIRETORIO_HISTORICO_PADRAO);
		Path arquivo = Paths.get(caminho).toAbsolutePath();

		if (!Files.exists(arquivo)) {
			try {
				Files.createDirectories(arquivo);

			} catch (IOException e) {
				throw new AdministracaoException("Não foi possível encontrar ou criar o diretório de histórico.", e);
			}

		} else {
			validarArquivoDiretorio(arquivo, true, true, null, "Ignorando possíveis problemas de permissão e/ou acesso à pasta de arquivos de histórico.");
		}

		return arquivo;
	}

	private enum Estado {
		INICIADO,
		ARQUIVO_ABERTO,
		LOTE_PROCESSADO,
		ARQUIVO_MOVIDO,
		EMAIL_ENVIADO
	}
}
