package br.com.araujoabreu.sgi.service.impl.administracao;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro.TipoCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.Colaborador;
import br.com.araujoabreu.sgi.administracao.domain.ColaboradorCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.EntityStatus;
import br.com.araujoabreu.sgi.administracao.domain.LoteInativacao;
import br.com.araujoabreu.sgi.administracao.domain.LoteInativacaoRegistro;
import br.com.araujoabreu.sgi.compras.domain.Anexo;
import br.com.araujoabreu.sgi.fundoFixo.domain.FundoFixo;
import br.com.araujoabreu.sgi.lang.AdministracaoException;
import br.com.araujoabreu.sgi.lang.AdministracaoPersistenceException;
import br.com.araujoabreu.sgi.lang.AdministracaoValidationException;
import br.com.araujoabreu.sgi.service.administracao.transfer.ColaboradorTO;
import br.com.araujoabreu.sgi.service.lote.LoteInativacaoService;
import br.com.araujoabreu.sgi.service.lote.transfer.LoteInativacaoPesquisaTO;
import br.com.araujoabreu.sgi.service.lote.transfer.LoteInativacaoTO;
import br.com.araujoabreu.sgi.service.lote.util.ArquivoLoteInativacao;
import br.com.araujoabreu.sgi.service.util.GlobalBean;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.security.domain.Perfil;
import br.com.fasolti.corporativo.security.domain.TipoAcesso;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.ColaboradorRepository;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroRepository;
import br.com.fasolti.sgi.repository.lote.LoteInativacaoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class LoteInativacaoServiceImpl extends
	BaseSearchService<
		LoteInativacao,
		LoteInativacaoTO,
		LoteInativacaoPesquisaTO,
		LoteInativacaoRepository>
	implements LoteInativacaoService {

	private static final Log logger = LogFactory.getLog(LoteInativacaoServiceImpl.class);

	private static final List<String> NOMES_PERFIS_IGNORADOS = Arrays.asList(
		"consultor-terceiro", "admin-usuario"
	);
	@Autowired
	private ColaboradorRepository colaboradorRepository;
	@Autowired
	private CentroFinanceiroRepository centroFinanceiroRepository;

	public LoteInativacaoServiceImpl(LoteInativacaoRepository repository) {
		super(repository);
	}

	@Override
	public List<ColaboradorTO> findDestinatariosEmailParaImportacaoAutomatica() {
		List<Colaborador> lista = colaboradorRepository.findDestinatariosEmailParaImportacaoArquivoInativacao();
		return lista.stream().map(c -> getMapper().map(c, ColaboradorTO.class)).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public LoteInativacaoTO importar(ArquivoLoteInativacao arquivoImportacao, ColaboradorTO usuarioLogado) throws AdministracaoException {
		Objects.requireNonNull(arquivoImportacao);
		Objects.requireNonNull(usuarioLogado);
		Objects.requireNonNull(usuarioLogado.getCodigo());

		LoteInativacao loteInativacao = new LoteInativacao();

		if (usuarioLogado == null || usuarioLogado.getCodigo() == null) {
			throw new AdministracaoValidationException("A importação manual de lote de inativação deve conter um usuário responsável.");
		}

		Colaborador responsavel = colaboradorRepository.findById(usuarioLogado.getCodigo())
			.orElseThrow(() -> new AdministracaoValidationException("Colaborador ID:" + usuarioLogado.getCodigo() + " não existe."));

		loteInativacao.setTipo(LoteInativacao.Tipo.MANUAL);
		loteInativacao.setResponsavel(responsavel);
		loteInativacao.setCriador(responsavel.getNome());
		return doImportar(loteInativacao, arquivoImportacao);
	}

	@Override
	@Transactional
	public LoteInativacaoTO importar(ArquivoLoteInativacao arquivoLoteImportacao) throws AdministracaoException {
		Objects.requireNonNull(arquivoLoteImportacao);

		LoteInativacao loteInativacao = new LoteInativacao();
		loteInativacao.setTipo(LoteInativacao.Tipo.AUTOMATICO);
		loteInativacao.setResponsavel(null);
		loteInativacao.setCriador("admin");
		return doImportar(loteInativacao, arquivoLoteImportacao);
	}

	private LoteInativacaoTO doImportar(LoteInativacao resultado, ArquivoLoteInativacao arquivoLoteInativacao) throws AdministracaoException {

		Calendar calAtual = Calendar.getInstance();
		String nomeCriador = resultado.getCriador();
		Colaborador usuarioLogado = resultado.getResponsavel();
		LoteInativacao.Tipo tipoImportacao = resultado.getTipo();

		LoteInativacao.Situacao situacaoImportacao = LoteInativacao.Situacao.IMPORTADO;
		Path caminhoArquivoImportacao = arquivoLoteInativacao.getCaminhoArquivo();
		File arquivoImportacao = caminhoArquivoImportacao.toFile();
		String nomeAnexo = arquivoLoteInativacao.getNomeArquivo();
		String tipoAnexo = arquivoLoteInativacao.getTipoArquivo();
		boolean arquivoValido = arquivoLoteInativacao.isValido();

		if (arquivoValido) {
			Set<ArquivoLoteInativacao.Registro> registros = new TreeSet<>((a, b) -> a.getCpf().compareToIgnoreCase(b.getCpf()));
			registros.addAll(arquivoLoteInativacao.getRegistros());

			for (ArquivoLoteInativacao.Registro entrada : registros) {
				importarUsuario(resultado, usuarioLogado, entrada);
			}

			resultado.ordenarRegistros();

		} else {
			situacaoImportacao = LoteInativacao.Situacao.NAO_IMPORTADO;

			if (tipoImportacao != LoteInativacao.Tipo.AUTOMATICO) {
				throw new AdministracaoValidationException("Arquivo inválido para importação. " + gerarMensagemErro(arquivoLoteInativacao));
			}
		}

		Anexo anexo = new Anexo();
		anexo.setArquivo(arquivoImportacao);
		anexo.setPath(arquivoImportacao.getParent());
		anexo.setNome(nomeAnexo);
		anexo.setNomeTemporario(arquivoImportacao.getName());
		anexo.setContentType(tipoAnexo);
		anexo.setDescricao("Arquivo de Lote de Importação");
		anexo.setCriador(nomeCriador);
		anexo.setDataCriacao(calAtual.getTime());

		resultado.setAnexo(anexo);
		resultado.setSituacao(situacaoImportacao);
		resultado.setDataImportacao(calAtual.getTime());
		resultado.setDataCriacao(calAtual.getTime());

		resultado = getRepository().save(resultado);

		return map(resultado);
	}

	private void importarUsuario(LoteInativacao resultado, Colaborador responsavel, ArquivoLoteInativacao.Registro linha) throws AdministracaoException {
		String cpfNaoFormatado = linha.getCpf();
		LoteInativacaoRegistro colaboradorArquivo = new LoteInativacaoRegistro();
		colaboradorArquivo.setCpfArquivo(linha.getCpf());
		colaboradorArquivo.setNomeArquivo(linha.getNome());
		Colaborador colaboradorEncontrado = colaboradorRepository.findOneByDocumento(cpfNaoFormatado).orElse(null);
		boolean usuarioIgnorado = false;

		if (colaboradorEncontrado == null) {
			String cpfFormatado = GlobalBean.formatarCPFComPonto(cpfNaoFormatado);
			colaboradorEncontrado = colaboradorRepository.findOneByDocumento(cpfFormatado).orElse(null);
		}

		if (colaboradorEncontrado != null) {
			logger.info("Inativando usuário: " + colaboradorEncontrado.getId());
			colaboradorArquivo.setCentroFinanceiro(colaboradorEncontrado.getCentroFinanceiro());
			colaboradorArquivo.setInativado(colaboradorEncontrado);
			usuarioIgnorado = isUsuarioIgnorado(resultado, colaboradorArquivo);

			if (!usuarioIgnorado) {
				if (colaboradorEncontrado.getSituacao() == Pessoa.Situacao.DISABLED) {
					colaboradorArquivo.setSituacao(LoteInativacaoRegistro.Situacao.CADASTRADO_JA_INATIVADO);

				} else {
					calcularFundoFixo(colaboradorArquivo);
					colaboradorEncontrado = inativarColaborador(colaboradorEncontrado, responsavel, responsavel != null);
					// NOTE: Previne erros do JPA relacionados ao ambiente de persistência e entidades em modo detached.
					colaboradorArquivo.setInativado(colaboradorEncontrado);
				}
			}

		} else {
			colaboradorArquivo.setSituacao(LoteInativacaoRegistro.Situacao.NAO_CADASTRADO);
		}

		if (usuarioIgnorado) {
			logger.info("Ignorando usuário: " + colaboradorArquivo.getInativado());
			colaboradorArquivo.setSituacao(LoteInativacaoRegistro.Situacao.IGNORADO);
		}

		resultado.addRegistro(colaboradorArquivo);
	}

	private boolean calcularFundoFixo(LoteInativacaoRegistro colaboradorArquivo) {
		boolean fundoFixoEncontrado = false;
		Colaborador colaboradorEncontrado = colaboradorArquivo.getInativado();

		if (colaboradorEncontrado.getFundosFixos() != null && !colaboradorEncontrado.getFundosFixos().isEmpty()) {
			fundoFixoEncontrado = true;

			BigDecimal totalFundoFixo = BigDecimal.ZERO;
			List<FundoFixo> fundosFixos = new ArrayList<>();
			List<FundoFixo> adiantamentos = new ArrayList<>();
			List<FundoFixo> reembolsos = new ArrayList<>();

			for (FundoFixo fundoFixo : colaboradorEncontrado.getFundosFixos()) {
				switch (fundoFixo.getTipo()) {
					case FUNDO_FIXO:
						fundosFixos.add(fundoFixo);
						break;
					case ADIANTAMENTO:
						adiantamentos.add(fundoFixo);
						break;
					case REEMBOLSO:
						reembolsos.add(fundoFixo);
						break;
				}
			}

			// NOTE Duas sequências de `for`s para melhorar a visualização do processo (em código).
			if (!fundosFixos.isEmpty()) {
				colaboradorArquivo.setTipoPrestacaoContas(LoteInativacaoRegistro.TipoPrestacaoContas.FUNDO_FIXO);
				for (FundoFixo fundo : fundosFixos) totalFundoFixo = totalFundoFixo.add(fundo.getSaldo());
				// NOTE (!adiantamentos.isEmpty() || !reembolsos.empty()) ? <GAME OVER>.

			} else {

				if (!adiantamentos.isEmpty() && !reembolsos.isEmpty()) {
					colaboradorArquivo.setTipoPrestacaoContas(LoteInativacaoRegistro.TipoPrestacaoContas.ADIANTAMENTO_REEMBOLSO);

				} else if (!adiantamentos.isEmpty()) {
					colaboradorArquivo.setTipoPrestacaoContas(LoteInativacaoRegistro.TipoPrestacaoContas.ADIANTAMENTO);

				} else {
					colaboradorArquivo.setTipoPrestacaoContas(LoteInativacaoRegistro.TipoPrestacaoContas.REEMBOLSO);
					// NOTE Uma das listas de fundos deve ser não-vazia.
				}

				for (FundoFixo fundo : adiantamentos) totalFundoFixo = totalFundoFixo.add(fundo.getSaldo());
				for (FundoFixo fundo : reembolsos) totalFundoFixo = totalFundoFixo.subtract(fundo.getSaldoPendente());
			}

			colaboradorArquivo.setSituacao(LoteInativacaoRegistro.Situacao.CADASTRADO_INATIVADO_COM_PRESTACAO);
			colaboradorArquivo.setValorPrestacaoContas(totalFundoFixo);

		} else {
			colaboradorArquivo.setSituacao(LoteInativacaoRegistro.Situacao.CADASTRADO_INATIVADO_SEM_PRESTACAO);
		}

		return fundoFixoEncontrado;
	}

	private boolean isUsuarioIgnorado(LoteInativacao loteInativacao, LoteInativacaoRegistro registro) throws AdministracaoPersistenceException {
		boolean ignorado = false;

		Colaborador colaborador = registro.getInativado();
		Usuario usuario = colaborador.getUsuario();

		for (Perfil perfil : usuario.getPerfis()) {
			if (NOMES_PERFIS_IGNORADOS.contains(perfil.getNome())) {
				ignorado = true;
				break;
			}
		}

		return ignorado;
	}

	private Colaborador inativarColaborador(Colaborador colaborador, Colaborador usuarioLogado, boolean manual) throws AdministracaoException {
		colaborador.setSituacao(br.com.fasolti.corporativo.pessoa.domain.Pessoa.Situacao.DISABLED);
		CentroFinanceiro cfPessoalAfastado = getCentroFinanceiroPessoalAfastado();
		colaborador.setCentroFinanceiro(cfPessoalAfastado);

		boolean colaboradorAfastado = false;
		List<ColaboradorCentroFinanceiro> cfsRemover = new ArrayList<>();
		for (ColaboradorCentroFinanceiro ccf : colaborador.getCentrosFinanceiros()) {
			if (ccf.getCentroFinanceiro().equals(cfPessoalAfastado)) {
				colaboradorAfastado = true;
			} else {
				cfsRemover.add(ccf);
			}
		}

		if (TipoAcesso.INTERNO.equals(colaborador.getUsuario().getTipoAcesso())) {
			colaborador.getCentrosFinanceiros().removeAll(cfsRemover);

			if (!colaboradorAfastado) {
				ColaboradorCentroFinanceiro colaboradorCentroFinanceiro = new ColaboradorCentroFinanceiro();
				colaboradorCentroFinanceiro.setColaborador(colaborador);
				colaboradorCentroFinanceiro.setCentroFinanceiro(cfPessoalAfastado);

				colaborador.getCentrosFinanceiros().add(colaboradorCentroFinanceiro);
			}
		}

		colaborador.setInativacaoManual(manual);
		colaborador.setInativador(usuarioLogado);
		colaborador.setDataInativacao(new Date());
		for (FundoFixo fundoFixo : colaborador.getFundosFixos()) {
			fundoFixo.setInativador(usuarioLogado);
			fundoFixo.setDataInativacao(new Date());
			fundoFixo.setStatus(EntityStatus.DISABLED);
		}

		colaborador.getUsuario().getPerfis().clear();
		colaborador = colaboradorRepository.save(colaborador);

		return colaborador;
	}

	private CentroFinanceiro getCentroFinanceiroPessoalAfastado() {
		return centroFinanceiroRepository.findOneCentroFinanceiroPessoaAfastadoByTipo(TipoCentroFinanceiro.CENTRO_CUSTO);
	}

	private String gerarMensagemErro(ArquivoLoteInativacao arquivo) {

		if (arquivo.isVazio()) return "O arquivo não possui registros.";
		if (arquivo.getRegistrosInvalidos().isEmpty()) return "";

		ArquivoLoteInativacao.Registro primeiroRegistroErro = arquivo.getRegistrosInvalidos().stream()
			.filter(r -> !r.getErros().isEmpty()).findFirst()
			.orElse(null);

		if (primeiroRegistroErro == null) return "";
		ArquivoLoteInativacao.TipoErro tipoPrimeiroErro = primeiroRegistroErro.getErros().get(0);

		switch (tipoPrimeiroErro) {
			case ARQUIVO_VAZIO:
				return "O arquivo não possui registros.";
			case CPF_NAO_NUMERICO:
				return "O arquivo não corresponde ao padrão definido.";
			case FORMATO_LINHA_INVALIDO:
				return "O arquivo possui um CPF a ser inativado não numérico.";
			case NOME_VAZIO:
				return "O arquivo possui um nome a ser inativado em branco.";
		}

		// Not really needed
		return "";
	}
}
