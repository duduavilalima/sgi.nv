package br.com.araujoabreu.sgi.service.impl.controlefiscal;

import br.com.araujoabreu.sgi.administracao.domain.QReinfServico;
import br.com.araujoabreu.sgi.administracao.domain.QServicoTipo;
import br.com.araujoabreu.sgi.administracao.domain.QUnidade;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.compras.domain.Anexo;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.QAnexo;
import br.com.araujoabreu.sgi.compras.domain.QNotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.QNotaSaida;
import br.com.araujoabreu.sgi.compras.domain.QParcelaNota;
import br.com.araujoabreu.sgi.contasApagar.domain.QPagamento;
import br.com.araujoabreu.sgi.controleFiscal.domain.*;
import br.com.araujoabreu.sgi.controleFiscal.domain.lang.ReinfEventoDuplicadoException;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamento;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamentoEtapa;
import br.com.araujoabreu.sgi.service.controlefiscal.ReinfService;
import br.com.araujoabreu.sgi.service.controlefiscal.transfer.ReinfEventoTO;
import br.com.araujoabreu.sgi.service.controlefiscal.transfer.ReinfPesquisaTO;
import br.com.araujoabreu.sgi.service.controlefiscal.transfer.ReinfTO;
import br.com.araujoabreu.sgi.service.reinf.IdeContri;
import br.com.araujoabreu.sgi.service.reinf.IdePeriodo;
import br.com.araujoabreu.sgi.service.reinf.r1000.*;
import br.com.araujoabreu.sgi.service.reinf.r1070.EvtTabProcesso;
import br.com.araujoabreu.sgi.service.reinf.r1070.InfoProcesso;
import br.com.araujoabreu.sgi.service.reinf.r1070.ReinfR1070;
import br.com.araujoabreu.sgi.service.reinf.r2010.EvtServTom;
import br.com.araujoabreu.sgi.service.reinf.r2010.IdeEstabObra;
import br.com.araujoabreu.sgi.service.reinf.r2010.IdePrestServ;
import br.com.araujoabreu.sgi.service.reinf.r2010.InfoServTom;
import br.com.araujoabreu.sgi.service.reinf.r2010.InfoTpServ;
import br.com.araujoabreu.sgi.service.reinf.r2010.Nfs;
import br.com.araujoabreu.sgi.service.reinf.r2010.ReinfR2010;
import br.com.araujoabreu.sgi.service.reinf.r2020.EvtServPrest;
import br.com.araujoabreu.sgi.service.reinf.r2020.IdeEstabPrest;
import br.com.araujoabreu.sgi.service.reinf.r2020.IdeTomador;
import br.com.araujoabreu.sgi.service.reinf.r2020.InfoServPrest;
import br.com.araujoabreu.sgi.service.reinf.r2020.ReinfR2020;
import br.com.araujoabreu.sgi.service.reinf.r2060.EvtCPRB;
import br.com.araujoabreu.sgi.service.reinf.r2060.IdeEstab;
import br.com.araujoabreu.sgi.service.reinf.r2060.InfoCPRB;
import br.com.araujoabreu.sgi.service.reinf.r2060.ReinfR2060;
import br.com.araujoabreu.sgi.service.reinf.r2060.TipoAjuste;
import br.com.araujoabreu.sgi.service.reinf.r2060.TipoCod;
import br.com.araujoabreu.sgi.service.reinf.r2098.ReinfR2098;
import br.com.araujoabreu.sgi.service.reinf.r2099.EvtFechaEvPer;
import br.com.araujoabreu.sgi.service.reinf.r2099.IdeEvento;
import br.com.araujoabreu.sgi.service.reinf.r2099.IdeRespInf;
import br.com.araujoabreu.sgi.service.reinf.r2099.InfoFech;
import br.com.araujoabreu.sgi.service.reinf.r2099.ReinfR2099;
import br.com.araujoabreu.sgi.service.reinf.retorno.Evento;
import br.com.araujoabreu.sgi.service.reinf.retorno.RegOcorrs;
import br.com.araujoabreu.sgi.service.reinf.retorno.RetornoLoteEventos;
import br.com.araujoabreu.sgi.service.unidade.UnidadeService;
import br.com.araujoabreu.sgi.service.unidade.transfer.UnidadeTO;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.configuration.domain.Parametro;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.QNaturezaJuridica;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaJuridica;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.administracao.PessoaJuridicaRepository;
import br.com.fasolti.sgi.repository.anexo.AnexoRepository;
import br.com.fasolti.sgi.repository.compras.NotaFiscalRepository;
import br.com.fasolti.sgi.repository.controlefiscal.ReinfAnexoRepository;
import br.com.fasolti.sgi.repository.controlefiscal.ReinfEventoAnexoRepository;
import br.com.fasolti.sgi.repository.controlefiscal.ReinfEventoErroRepository;
import br.com.fasolti.sgi.repository.controlefiscal.ReinfEventoNotaFiscalRepository;
import br.com.fasolti.sgi.repository.controlefiscal.ReinfEventoRepository;
import br.com.fasolti.sgi.repository.controlefiscal.ReinfRegistroEventoRepository;
import br.com.fasolti.sgi.repository.controlefiscal.ReinfRepository;
import br.com.fasolti.sgi.repository.parametro.ParametroRepository;
import br.com.fasolti.sgi.repository.unidade.UnidadeRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.MaskFormatter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
public class ReinfServiceImpl extends BaseSearchService<Reinf, ReinfTO, ReinfPesquisaTO, ReinfRepository> implements ReinfService {
	/*
    Os arquivos de documentação para a gerar o Reinf foram baixados originalmente do endereço : http://sped.rfb.gov.br/pasta/show/1495
    -- 16/04/2018 - versão 1.3.02 da documentação
	*/

	/*
    Se for um CNPJ deve ser informada apenas a Raiz/Base de oito posições, exceto se natureza jurídica de administração pública direta
    federal ([101-5], [104-0], [107-4], [116-3], situação em que o campo deve ser preenchido com o CNPJ completo (14 posições).
 	*/
	private static final String[] CODIGOS_RFB = {"101-5", "104-0", "107-4", "116-3"};

	private static final String REINF_NAMESPACE = "<Reinf xmlns=\"http://www.reinf.esocial.gov.br/schemas/%s/%s\">";

	private static final String VER_PROCESSO = "controlefiscal.reinf.versao";

	private static final String CAMINHO_ARQUIVO = "controlefiscal.reinf.anexo.diretorio";

	private static final String DOUBLE_PATTERN = "##############0.00";

	private static final Double ALIQUOTA_CPRB = 0.045;

	private static final Long CODIGO_ISS = 4L;

	@Autowired
	private UnidadeService unidadeService;

	@Autowired
	private ReinfEventoRepository reinfEventoRepository;

	@Autowired
	private ReinfEventoNotaFiscalRepository reinfEventoNotaFiscalRepository;

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private UnidadeRepository unidadeRepository;

	@Autowired
	private ReinfEventoAnexoRepository reinfEventoAnexoRepository;

	@Autowired
	private ReinfEventoErroRepository reinfEventoErroRepository;

	@Autowired
	private AnexoRepository anexoRepository;

	@Autowired
	private ReinfAnexoRepository reinfAnexoRepository;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	private ReinfRegistroEventoRepository reinfRegistroEventoRepository;

	@Autowired
	private NotaFiscalRepository notaFiscalRepository;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	//Parametros para a geração do REINF
	private String referencia;
	private Long codigoEmpresa;
	private Calendar calDtIni;
	private Calendar calDtFim;
	private int contadorEventos;
	private Date hoje;
	private Parametro destino;
	private Parametro versaoProcesso;
	private ReinfRegistroEvento reinfRegistroEvento;
	private DecimalFormat decimalFormat = new DecimalFormat(DOUBLE_PATTERN);
	private final NumberFormat numberFormat = new DecimalFormat(DOUBLE_PATTERN, new DecimalFormatSymbols(new Locale("pt", "BR")));
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String criador;

	@Autowired
	public ReinfServiceImpl(ReinfRepository repository) {
		super(repository);
	}

	@Override
	public ReinfTO findReinf(String referencia, Long codigoEmpresa) throws Exception {
		QReinf qReinf = QReinf.reinf;
		QUnidade qUnidade = QUnidade.unidade;

		QBean<Reinf> qBean = Projections.bean(
			Reinf.class,
			qReinf.codigo,
			qReinf.referencia,
			qReinf.reinfStatus,
			qReinf.dataCriacao,
			qReinf.versao,
			Projections.bean(
				Unidade.class,
				qUnidade.codigo,
				qUnidade.nome,
				qUnidade.codnome,
				qUnidade.tipoUnidade
			).as("unidade")
		);

		JPAQuery<Reinf> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qReinf)
			.join(qReinf.unidade, qUnidade)
			.where(
				qUnidade.codigo.eq(codigoEmpresa)
				.and(qReinf.referencia.eq(referencia))
			)
			.orderBy(qUnidade.codigo.asc());

		return map(jpaQuery.fetchOne());
	}

	/* ***************************************************************************************************
    Somente as classes do pacote br.com.araujoabreu.sgi.controleFiscal.domain.Reinf serão pesistidas.

    As classes do pacote br.com.araujoabreu.sgi.service.reinf.r2010.Reinf são utlizadas somente para
    representar cada um dos blocos que compõe os registros do Reinf e não são persistidas.
	* ***************************************************************************************************/

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ReinfTO gerarReinf(String referencia, Long codigoEmpresa) throws Exception {
		ReinfTO reinfTO = null;
		String[] eventos = {"R-2010", "R-2020", "R-2060", "R-2099"};
//		String[] eventos = {"R-2060"};

		for (String evento : eventos) {
			reinfTO = this.gerarReinfPorEvento(evento, referencia, codigoEmpresa);
		}

		this.compactarArquivosReinf(reinfTO);

		return reinfTO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ReinfTO gerarReinfPorEvento(String registro, String referencia, Long codigoEmpresa) throws Exception {

		br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade = getRepository().findOneByReferenciaAndUnidadeCodigo(referencia, codigoEmpresa);
		if (reinfEntidade == null) {
			this.iniciarParametros(registro, referencia, codigoEmpresa, securityService.getUser().getLogin());
			reinfEntidade = new br.com.araujoabreu.sgi.controleFiscal.domain.Reinf();
			reinfEntidade.setReferencia(referencia);
			reinfEntidade.setUnidade(unidadeRepository.findById(codigoEmpresa).get());
			reinfEntidade.setDataCriacao(this.hoje);
			reinfEntidade.setCriador(this.criador);
		}
		else
			this.iniciarParametros(registro, referencia, codigoEmpresa, reinfEntidade.getCriador());

		reinfEntidade.setReinfStatus(Reinf.ReinfStatus.EVENTOS_GERADOS);
		reinfEntidade.setVersao(this.versaoProcesso.getValor());
		reinfEntidade.setDataAtualizacao(this.hoje);
 		getRepository().save(reinfEntidade);

		if ("R-1000".equals(registro))
			gerarRegistroR1000(reinfEntidade);

		else if ("R-1070".equals(registro))
			gerarRegistroR1070(reinfEntidade);

		else if ("R-2010".equals(registro))
			gerarRegistroR2010(reinfEntidade);

		else if ("R-2020".equals(registro))
			gerarRegistroR2020(reinfEntidade);

		else if ("R-2030".equals(registro))
			gerarRegistroR2030(reinfEntidade);

		else if ("R-2040".equals(registro))
			gerarRegistroR2040(reinfEntidade);

		else if ("R-2050".equals(registro))
			gerarRegistroR2050(reinfEntidade);

		else if ("R-2060".equals(registro))
			gerarRegistroR2060(reinfEntidade);

		else if ("R-2070".equals(registro))
			gerarRegistroR2070(reinfEntidade);

		else if ("R-2098".equals(registro))
			gerarRegistroR2098(reinfEntidade);

		else if ("R-2099".equals(registro))
			gerarRegistroR2099(reinfEntidade);

		else if ("R-3010".equals(registro))
			gerarRegistroR3010(reinfEntidade);

		else if ("R-9000".equals(registro))
			gerarRegistroR9000(reinfEntidade);

		return map(reinfEntidade);
	}

	/* **************************************************** MÉTODOS UTILITÁRIOS ******************************************************** */
	private void iniciarParametros(String registro, String referencia, Long codigoEmpresa, String criador) {
		this.referencia = referencia;
		this.codigoEmpresa = codigoEmpresa;
		this.contadorEventos = 0;
		this.hoje = new Date(System.currentTimeMillis());
		this.criador = criador;

		this.calDtIni = Calendar.getInstance();
		this.calDtFim = Calendar.getInstance();

		int ano = Integer.valueOf(this.referencia.substring(0, 4));
		int mes = Integer.valueOf(this.referencia.substring(5));

		this.calDtIni.set(ano, mes - 1, 1);
		this.calDtFim.set(ano, mes - 1, calDtIni.getActualMaximum(Calendar.DAY_OF_MONTH));

		this.versaoProcesso = parametroRepository.findById(VER_PROCESSO).get();
		this.destino = parametroRepository.findById(CAMINHO_ARQUIVO).get();
//		this.destino = new Parametro();
//		this.destino.setValor("/home/eavila/Desenv/arquivos/sgi/reinf");

		this.reinfRegistroEvento = reinfRegistroEventoRepository.findByNome(registro);
	}

	/*
	A regra de formação abaixo foi extraída do Manual de orientação do desenvolvedor (ManualOrientacaoDesenvolvedor-REINF v1.03.02.pdf - item 7.2. Identificação do evento)
	Este arquivo foi baixado do site descrito no topo da classe.

	T - Tipo de Inscrição do Contribuinte (1 - CNPJ; 2 - CPF);
	NNNNNNNNNNNNNN - Número do CNPJ ou CPF do empregador. Completar com zeros à direita;
	AAAAMMDD - Ano, mês e dia da geração do evento;
	HHMMSS - Hora, minuto e segundo da geração do evento;
	QQQQQ - Número sequencial da chave. Incrementar somente quando ocorrer 2 posições geração de eventos na mesma data/hora.

	Exemplo: ID2333901700001892014020213424700001. (36 posições)
	*/
	private String getIdTransmissao(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//AAAAMMDD + HHMMSS
		NumberFormat numberFormat = new DecimalFormat("00000");

		StringBuilder id = new StringBuilder("ID");
		id.append("1"); //T
		id.append(reinfEntidade.getUnidade().getDocumento().replaceAll("[^0-9]", "").substring(0, 8)).append("000000"); //NNNNNNNNNNNNNN
		id.append(sdf.format(hoje));//AAAAMMDD + HHMMSS
		id.append(numberFormat.format(++contadorEventos));//QQQQQ

		return id.toString();
	}

	private br.com.araujoabreu.sgi.service.reinf.r1000.IdeEvento getIdeEventoR1000(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
		br.com.araujoabreu.sgi.service.reinf.r1000.IdeEvento ideEvento = new br.com.araujoabreu.sgi.service.reinf.r1000.IdeEvento();
		ideEvento.setTpAmb(br.com.araujoabreu.sgi.service.reinf.IdeEvento.TipoAmbiente.PRODUCAO.getValor()); // TODO : PARAMETRIZAR ESTE VALOR
		ideEvento.setProcEmi(1);
		ideEvento.setVerProc(this.versaoProcesso.getValor());

		return ideEvento;
	}

	private IdeEvento getIdeEventoR2099(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
		IdeEvento ideEvento = new IdeEvento();
		ideEvento.setPerApur(reinfEntidade.getReferencia());
		ideEvento.setTpAmb(br.com.araujoabreu.sgi.service.reinf.IdeEvento.TipoAmbiente.PRODUCAO.getValor()); // TODO : PARAMETRIZAR ESTE VALOR
		ideEvento.setVerProc(this.versaoProcesso.getValor());
		ideEvento.setProcEmi(1);

		return ideEvento;
	}

	private br.com.araujoabreu.sgi.service.reinf.IdeEvento getIdeEvento(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
		br.com.araujoabreu.sgi.service.reinf.IdeEvento ideEvento = new br.com.araujoabreu.sgi.service.reinf.IdeEvento();
		ideEvento.setIndRetif(br.com.araujoabreu.sgi.service.reinf.IdeEvento.TipoEventoArquivo.ORIGINAL.getValor());
		ideEvento.setNrRecibo(null);
		ideEvento.setPerApur(reinfEntidade.getReferencia());
		ideEvento.setTpAmb(br.com.araujoabreu.sgi.service.reinf.IdeEvento.TipoAmbiente.PRODUCAO.getValor()); // TODO : PARAMETRIZAR ESTE VALOR
		ideEvento.setVerProc(this.versaoProcesso.getValor());
		ideEvento.setProcEmi(1);

		return ideEvento;
	}

	private IdeContri getIdeContri(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
		IdeContri ideContri = new IdeContri();
		ideContri.setTpInsc(IdeContri.TipoInscricao.CNPJ.getValor());

		String nrInsc = reinfEntidade.getUnidade().getDocumento().replaceAll("[^0-9]", "");
		ideContri.setTpInsc(IdeContri.TipoInscricao.CNPJ.getValor());
		ideContri.setNrInsc(nrInsc.substring(0, 8));

		return ideContri;
	}

	private void salvarArquivo(Object arquivo, ReinfEvento reinfEvento) {
		try {
			String nomeArquivo = this.referencia + "_" + reinfEvento.getReinfRegistroEvento().getNome() + ".xml";
			String descricao = "Referente ao evento: " + reinfEvento.getReinfRegistroEvento().getNome() + " do Reinf de referêcia: " + this.referencia;

			Anexo anexo = new Anexo();
			anexo.setNome(nomeArquivo);
			anexo.setDescricao(descricao);
			anexo.setCriador(this.criador);
			anexo.setDataCriacao(new Date(System.currentTimeMillis()));
			anexo.setContentType("text/xml");
			anexoRepository.save(anexo);

			File diretorio = new File(this.destino.getValor());
			if (!diretorio.exists())
				diretorio.mkdirs();

			diretorio = new File(this.destino.getValor() + "/" + this.referencia);
			if (!diretorio.exists())
				diretorio.mkdirs();

			diretorio = new File(this.destino.getValor() + "/" + this.referencia + "/" + reinfEvento.getReinf().getUnidade().getCodigo());
			if (!diretorio.exists())
				diretorio.mkdirs();

			diretorio = new File(this.destino.getValor() + "/" + this.referencia + "/" + reinfEvento.getReinf().getUnidade().getCodigo() + "/" + reinfEvento.getReinfRegistroEvento().getNome());
			if (!diretorio.exists())
				diretorio.mkdirs();

			XmlMapper xmlMapper = new XmlMapper();
			xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			xmlMapper.getFactory().getXMLOutputFactory().setProperty("javax.xml.stream.isRepairingNamespaces", true);
			String xml = xmlMapper.writeValueAsString(arquivo).replace("<Reinf>", String.format(REINF_NAMESPACE, reinfEvento.getReinfRegistroEvento().getNamespace(), this.versaoProcesso.getValor()));

			BufferedWriter writer = new BufferedWriter(new FileWriter(diretorio + "/" + anexo.getCodigo() + "_" + anexo.getNome()));
			writer.write(xml);
			writer.close();

			anexo.setNome(anexo.getCodigo() + "_" + anexo.getNome());
			anexoRepository.save(anexo);

			ReinfEventoAnexo reinfEventoAnexo = new ReinfEventoAnexo();
			reinfEventoAnexo.setReinfEvento(reinfEvento);
			reinfEventoAnexo.setAnexo(anexo);
			reinfEventoAnexo.setTipo(ReinfEventoAnexo.TipoArquivoReinf.ENVIO);
			reinfEventoAnexoRepository.save(reinfEventoAnexo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ReinfEvento getReinfEvento(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade, ReinfRegistroEvento registroEvento) {
		ReinfEvento reinfEvento = new ReinfEvento();

		reinfEvento.setReinf(reinfEntidade);
		reinfEvento.setTipoEvento(ReinfEvento.TipoEvento.ORIGINAL);
		reinfEvento.setReinfRegistroEvento(registroEvento);
		reinfEvento.setDataCriacao(this.hoje);
		reinfEvento.setCriador(this.criador);
		reinfEvento.setPrestador(null);
		reinfEvento.setIdTransmissao(this.getIdTransmissao(reinfEntidade));
		reinfEvento.setSituacao(ReinfEvento.SituacaoEventoReinf.ARQUIVO_GERADO);

		return reinfEvento;
	}

	/* ************************************************** FIM MÉTODOS UTILITÁRIOS ****************************************************** */


	/* **************************************************** R-1000 ******************************************************** */
	private void gerarRegistroR1000(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) throws Exception {
		List<ReinfEvento> eventos = reinfEventoRepository.findAllByReinfRegistroEventoCodigoAndReinfCodigo(reinfRegistroEvento.getCodigo(), reinfEntidade.getCodigo());

		if (eventos != null && !eventos.isEmpty())
			throw new ReinfEventoDuplicadoException("Evento R1000 já cadastrado!"); //TODO ajustar a mensagem de erro;

		ReinfEvento reinfEvento = this.getReinfEvento(reinfEntidade, reinfRegistroEvento);
		reinfEventoRepository.save(reinfEvento);

		ReinfR1000 reinf = new ReinfR1000();
		EvtInfoContri evtInfoContri = new EvtInfoContri();

		evtInfoContri.setId(reinfEvento.getIdTransmissao());
		evtInfoContri.setIdeEvento(this.getIdeEventoR1000(reinfEntidade));
		evtInfoContri.setIdeContri(this.getIdeContri(reinfEntidade));

		InfoContri infoContri = new InfoContri();
		Inclusao inclusao = new Inclusao();
		Alteracao alteracao = null;
		Exclusao exclusao = null;

		IdePeriodo idePeriodo = new IdePeriodo();
		idePeriodo.setIniValid(this.referencia);

		InfoCadastro infoCadastro = new InfoCadastro();
		infoCadastro.setClassTrib("99");
		infoCadastro.setIndEscrituracao(1);
		infoCadastro.setIndDesoneracao(1);
		infoCadastro.setIndAcordoIsenMulta(0);
		infoCadastro.setIndSitPJ(0);

		Contato contato = new Contato();
		contato.setNmCtt("João Carlos da Silva");
		contato.setCpfCtt("74124285787");
		contato.setFoneFixo("2138652531");
		contato.setFoneCel(null);
		contato.setEmail("joaocarlos@araujoabreu.com.br");

		infoCadastro.setContato(contato);

		SoftHouse softHouse = new SoftHouse();
		softHouse.setNmRazao("FABRICA DE SOLUCÕES EM TECNOLOGIA DE INFORMACAO LTDA");
		softHouse.setCnpjSoftHouse("19431263000183");
		softHouse.setEmail("ricardo.valadares@fasolti.com.br");
		softHouse.setNmCont("Ricardo Copony Valadares");
		softHouse.setTelefone("21992226692");

		infoCadastro.setSoftHouse(softHouse);

		inclusao.setIdePeriodo(idePeriodo);
		inclusao.setInfoCadastro(infoCadastro);

		infoContri.setInclusao(inclusao);
		infoContri.setAlteracao(alteracao);
		infoContri.setExclusao(exclusao);

		evtInfoContri.setInfoContri(infoContri);

		reinf.setEvtInfoContri(evtInfoContri);

		this.salvarArquivo(reinf, reinfEvento);
	}


	/* **************************************************** R-1070 ******************************************************** */
	private void gerarRegistroR1070(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
		ReinfEvento reinfEvento = this.getReinfEvento(reinfEntidade, reinfRegistroEvento);
		reinfEventoRepository.save(reinfEvento);

		ReinfR1070 reinf = new ReinfR1070();
		EvtTabProcesso evtTabProcesso = new EvtTabProcesso();
		evtTabProcesso.setId(String.valueOf(reinfEvento.getIdTransmissao()));

		evtTabProcesso.setIdeEvento(this.getIdeEventoR1000(reinfEntidade));
		evtTabProcesso.setIdeContri(this.getIdeContri(reinfEntidade));

		InfoProcesso infoProcesso = new InfoProcesso();
		evtTabProcesso.setInfoProcesso(infoProcesso);

		reinf.setEvtTabProcesso(evtTabProcesso);

		this.salvarArquivo(reinf, reinfEvento);
	}


	/* **************************************************** R-2010 ******************************************************** */
	private void gerarRegistroR2010(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
		/*
		Seleção da NFs de fornecedores a serem informadas, base para a geração do REINF - 2010 - Serviços tomados
		 */
		QNotaFiscal qNotaFiscal = QNotaFiscal.notaFiscal;
		JPAQuery jpaQuery = getJPAQueryFactory().from(qNotaFiscal).distinct();

		QNotaTributoAliquota qNotaTributoAliquota = QNotaTributoAliquota.notaTributoAliquota;
		QAliquota qAliquota = QAliquota.aliquota;
		QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
		QTributo qTributo = QTributo.tributo;
		QPessoa qPessoa = QPessoa.pessoa;
		QNaturezaJuridica qNaturezaJuridica = QNaturezaJuridica.naturezaJuridica;
		QParcelaNota qPagamentoNF = new QParcelaNota("qPagamentoNF");
		QPagamento qPagamentoTR = new QPagamento("qPagamentoTR");
		QUnidade qEmpresa = QUnidade.unidade;

		jpaQuery
			.join(qNotaFiscal.listaNotaTributoAliquota, qNotaTributoAliquota)
			.join(qNotaTributoAliquota.aliquota, qAliquota)
			.join(qAliquota.categoriaTributo, qCategoriaTributo)
			.join(qCategoriaTributo.tributo, qTributo)
			.join(qNotaFiscal.parcelas, qPagamentoNF)
			.join(qNotaTributoAliquota.pagamento, qPagamentoTR)
			.join(qNotaFiscal.empresa, qEmpresa)
			.leftJoin(qPagamentoNF.credor, qPessoa)
			.leftJoin(qPessoa.as(QPessoaJuridica.class).naturezaJuridica, qNaturezaJuridica)

			.where(
				qTributo.nome.containsIgnoreCase("INSS")
					.and(qNotaFiscal.dataEmissao.between(this.calDtIni.getTime(), this.calDtFim.getTime()))
					.and(qEmpresa.matriz.codigo.eq(this.codigoEmpresa))
					.and(qPagamentoNF.ordem.eq(0))
			)

			.orderBy(qPessoa.documento.asc(), qNotaFiscal.dataEmissao.asc())
		;

		jpaQuery.select(
			qNotaFiscal.codigo,
			qNotaFiscal.numero,
			qNotaFiscal.dataEmissao,
			qNotaFiscal.valor,
			qNotaFiscal.serie,
			qPessoa.codigo,
			qPessoa.documento,
			qPessoa.tipoPessoa,
			qEmpresa.documento,
			qEmpresa.nome,
			qNaturezaJuridica.codigo,
			qNaturezaJuridica.codigoRfb,
			qNotaTributoAliquota.baseCalculo,
			qNotaTributoAliquota.aliquota.codigo,
			qNotaTributoAliquota.aliquota.percentual,
			qNotaTributoAliquota.valor
		)
		;

		List<Tuple> resultado = jpaQuery.fetch();
		/*
		Fim da seleção de NFs
		*/

		/*
		Inicio da coleta de informações para a geração do arquivo.
		Para cada bloco do evento foi criada uma classe, seguindo a estrutura do xml a ser gerado.
		*/

		//Bloco base do registro e que será utilizado para gerar o xml de envio a receita
		Map<ReinfEvento, ReinfR2010> mapReinf = new HashMap<>();

		/*
		Para cada fornecedor é gerado um evento. Contendo as "N" notas associadas a este fornecedor.
		As entidades gravadas no banco contém apenas o controle do que foi enviado.
		*/
		ReinfEvento reinfEvento = null;
		ReinfR2010 reinfArquivo = new ReinfR2010();
		Map<Long, List<Tuple>> mapNfs = new HashMap<>();

		//Agrupando as NFs por prestador
		for (Tuple tuple : resultado) {
			Long codigoPrestador = tuple.get(qPessoa.codigo);
			if (!mapNfs.keySet().contains(codigoPrestador))
				mapNfs.put(codigoPrestador, new ArrayList<Tuple>());

			mapNfs.get(codigoPrestador).add(tuple);
		}

		//Para cada fornecedor
		for (Long codigoPrestador : mapNfs.keySet()) {
			double vlrTotalBruto = 0.0;
			double vlrTotalBaseRet = 0.0;
			double vlrTotalRetPrinc = 0.0;
			Double vlrTotalRetAdic = 0.0;
			Double vlrTotalNRetPrinc = 0.0;
			Double vlrTotalNRetAdic = 0.0;
			Double percentual = 0.0;
			List<Nfs> nfs = new ArrayList<>();

			//Cria um evento para o prestador a ser gravado no banco.
			reinfEvento = this.getReinfEvento(reinfEntidade, reinfRegistroEvento);
			Pessoa prestador = new Pessoa();
			prestador.setCodigo(codigoPrestador);
			reinfEvento.setPrestador(prestador);
			reinfEventoRepository.save(reinfEvento);

			//Inicia o preencimento dos objetos utilizados para gerar os arquivos
			reinfArquivo = new ReinfR2010();
			//SubBloco evtServTom
			EvtServTom evtServTom = new EvtServTom();

			evtServTom.setId(reinfEvento.getIdTransmissao());
			reinfArquivo.setEvtServTom(evtServTom);

			//SubBloco ideEvento
			evtServTom.setIdeEvento(this.getIdeEvento(reinfEntidade));

			//SubBloco ideContri
			evtServTom.setIdeContri(this.getIdeContri(reinfEntidade));

			//SubBloco infoServTom
			InfoServTom infoServTom = new InfoServTom();

			//SubBloco ideEstabObra
			IdeEstabObra ideEstabObra = new IdeEstabObra();

			ideEstabObra.setTpInscEstab(IdeContri.TipoInscricao.CNPJ.getValor());
			ideEstabObra.setNrInscEstab(mapNfs.get(codigoPrestador).get(0).get(qEmpresa.documento).replaceAll("[^0-9]", ""));
			ideEstabObra.setIndObra(IdeEstabObra.TipoObra.NAO_CIVIL.getValor());

			infoServTom.setIdeEstabObra(ideEstabObra);
			evtServTom.setInfoServTom(infoServTom);

			for (Tuple linha : mapNfs.get(codigoPrestador)) {
				vlrTotalBruto += linha.get(qNotaFiscal.valor).doubleValue();
				vlrTotalBaseRet += linha.get(qNotaTributoAliquota.baseCalculo).doubleValue();

				if (linha.get(qNotaTributoAliquota.valor) == null || linha.get(qNotaTributoAliquota.valor).doubleValue() == 0)
					vlrTotalRetPrinc += (linha.get(qNotaTributoAliquota.baseCalculo).doubleValue() * linha.get(qNotaTributoAliquota.aliquota.percentual).doubleValue()) / 100;
				else
					vlrTotalRetPrinc += linha.get(qNotaTributoAliquota.valor).doubleValue();

				Nfs nf = new Nfs();
				if (StringUtils.isEmpty(linha.get(qNotaFiscal.serie)))
					nf.setSerie("0");
				else
					nf.setSerie(linha.get(qNotaFiscal.serie));

				nf.setNumDocto(linha.get(qNotaFiscal.numero).replaceAll("[^0-9]", ""));
				nf.setVlrBruto(this.numberFormat.format(linha.get(qNotaFiscal.valor)));

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				nf.setDtEmissaoNF(sdf.format(linha.get(qNotaFiscal.dataEmissao)));

				InfoTpServ infoTpServ = new InfoTpServ();
				infoTpServ.setTpServico("100000003");
				infoTpServ.setVlrBaseRet(this.numberFormat.format(linha.get(qNotaTributoAliquota.baseCalculo)));
				if (linha.get(qNotaTributoAliquota.valor) == null || linha.get(qNotaTributoAliquota.valor).doubleValue() == 0)
					infoTpServ.setVlrRetencao(
						this.numberFormat.format(
							(linha.get(qNotaTributoAliquota.baseCalculo).doubleValue() * linha.get(qNotaTributoAliquota.aliquota.percentual).doubleValue()) / 100)
					);
				else
					infoTpServ.setVlrRetencao(this.numberFormat.format(linha.get(qNotaTributoAliquota.valor)));

				nf.setInfoTpServ(infoTpServ);
				nfs.add(nf);

				NotaFiscal nfEvento = new NotaFiscal();
				nfEvento.setCodigo(linha.get(qNotaFiscal.codigo));

				ReinfEventoNotaFiscal reinfEventoNotaFiscal = new ReinfEventoNotaFiscal();
				reinfEventoNotaFiscal.setReinfEvento(reinfEvento);
				reinfEventoNotaFiscal.setNotaFiscal(nfEvento);

				reinfEventoNotaFiscalRepository.save(reinfEventoNotaFiscal);

				percentual = linha.get(qNotaTributoAliquota.aliquota.percentual).doubleValue();

			}

			IdePrestServ idePrestServ = new IdePrestServ();
			idePrestServ.setCnpjPrestador(mapNfs.get(codigoPrestador).get(0).get(qPessoa.documento).replaceAll("[^0-9]", ""));
			idePrestServ.setVlrTotalBruto(this.numberFormat.format(vlrTotalBruto));
			idePrestServ.setVlrTotalBaseRet(this.numberFormat.format(vlrTotalBaseRet));
			idePrestServ.setVlrTotalRetPrinc(this.numberFormat.format(vlrTotalRetPrinc));
			idePrestServ.setVlrTotalRetAdic(this.numberFormat.format(vlrTotalRetAdic));
			idePrestServ.setVlrTotalNRetPrinc(this.numberFormat.format(vlrTotalNRetPrinc));
			idePrestServ.setVlrTotalNRetAdic(this.numberFormat.format(vlrTotalNRetAdic));

			idePrestServ.setIndCPRB(
				percentual.equals(new Double(11.00))
					? IdePrestServ.TipoContribuicao.NAO_RECEITA_BRUTA.getValor()
					: IdePrestServ.TipoContribuicao.RECEITA_BRUTA.getValor());

			idePrestServ.setNfs(nfs);

			reinfArquivo.getEvtServTom().getInfoServTom().getIdeEstabObra().setIdePrestServ(idePrestServ);

			mapReinf.put(reinfEvento, reinfArquivo);
		}

		for (ReinfEvento evento : mapReinf.keySet()) {
			this.salvarArquivo(mapReinf.get(evento), evento);
		}

	}

	/* **************************************************** R-2020 ******************************************************** */
	private void gerarRegistroR2020(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
		/*
		Seleção da NFs de fornecedores a serem informadas, base para a geração do REINF - 2010 - Serviços tomados
		*/
		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
		JPAQuery jpaQuery = getJPAQueryFactory().from(qNotaSaida).distinct();

		QNotaTributoAliquota qNotaTributoAliquota = QNotaTributoAliquota.notaTributoAliquota;
		QAliquota qAliquota = QAliquota.aliquota;
		QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
		QTributo qTributo = QTributo.tributo;
		QPessoaJuridica qPessoaJuridica = QPessoaJuridica.pessoaJuridica;
		QNaturezaJuridica qNaturezaJuridica = QNaturezaJuridica.naturezaJuridica;

		QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;
		QFaturamento qFaturamento = QFaturamento.faturamento;
		QPessoa qCliente = QPessoa.pessoa;

		QServicoTipo qServicoTipo = QServicoTipo.servicoTipo;
		QReinfServico qReinfServico = QReinfServico.reinfServico;

		QUnidade qUnidade = QUnidade.unidade;

		jpaQuery
			.join(qNotaSaida.listaNotaTributoAliquota, qNotaTributoAliquota)
			.join(qNotaTributoAliquota.aliquota, qAliquota)
			.join(qAliquota.categoriaTributo, qCategoriaTributo)
			.join(qCategoriaTributo.tributo, qTributo)
			.join(qNotaSaida.credor, qPessoaJuridica)
			.join(qPessoaJuridica.naturezaJuridica, qNaturezaJuridica)
			.join(qNotaSaida.faturamentoEtapas, qFaturamentoEtapa)
			.join(qFaturamentoEtapa.faturamento, qFaturamento)
			.join(qFaturamento.cliente, qCliente)
			.join(qNotaSaida.servicoTipo, qServicoTipo)
			.join(qServicoTipo.reinfServico, qReinfServico)
			.join(qNotaSaida.empresa, qUnidade)

			.where(
				qTributo.nome.containsIgnoreCase("INSS")
					.and(qNotaSaida.dataEmissao.between(this.calDtIni.getTime(), this.calDtFim.getTime()))
					.and(qUnidade.matriz.codigo.eq(this.codigoEmpresa))
					.and(qNotaSaida.tipoNotaFiscal.eq(NotaFiscal.TipoNotaFiscal.FATURAMENTO))
					.and(qNotaSaida.status.in(Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO, Nota.StatusNota.RECEBIDA_FATURAMENTO))
			)
			.orderBy(qUnidade.codigo.asc(), qCliente.documento.asc(), qNotaSaida.dataEmissao.asc())
		;

		jpaQuery.select(
			qNotaSaida.codigo,
			qNotaSaida.numero,
			qNotaSaida.dataEmissao,
			qNotaSaida.valor,
			qNotaSaida.serie,
			qUnidade.codigo,
			qUnidade.documento,
			qUnidade.tipoPessoa,
			qCliente.codigo,
			qCliente.documento,
			qCliente.nome,
			qNaturezaJuridica.codigo,
			qNaturezaJuridica.codigoRfb,
			qNotaTributoAliquota.baseCalculo,
			qNotaTributoAliquota.aliquota.codigo,
			qNotaTributoAliquota.aliquota.percentual,
			qNotaTributoAliquota.valor,
			qReinfServico.codigoClassificacao
		)
		;

		List<Tuple> resultado = jpaQuery.fetch();
		/*
		Fim da seleção de NFs
		*/

		/*
		Inicio da coleta de informações para a geração do arquivo.
		Para cada bloco do evento foi criada uma classe, seguindo a estrutura do xml a ser gerado.
		*/

		//Bloco base do registro e que será utilizado para gerar o xml de envio a receita
		Map<ReinfEvento, br.com.araujoabreu.sgi.service.reinf.r2020.ReinfR2020> mapReinf = new HashMap<>();

		/*
		Para cada cliente (tomador do serviço) é gerado um evento. Contendo as "N" notas emitidas para este cliente.
		As entidades gravadas no banco contém apenas o controle do que foi enviado.
		*/
		ReinfEvento reinfEvento = null;

		Long codigoCliente = 0L;
		String documentoCliente = null;
		double vlrTotalBruto = 0.0;
		double vlrTotalBaseRet = 0.0;
		double vlrTotalRetPrinc = 0.0;
		Double vlrTotalRetAdic = 0.0;
		Double vlrTotalNRetPrinc = 0.0;
		Double vlrTotalNRetAdic = 0.0;
		List<ReinfEventoNotaFiscal> nfsReinf = new ArrayList<>();
		List<Nfs> nfs = new ArrayList<>();

		br.com.araujoabreu.sgi.service.reinf.r2020.ReinfR2020 reinfArquivo = new br.com.araujoabreu.sgi.service.reinf.r2020.ReinfR2020();

		for (int indice = 0; indice < resultado.size(); indice++) {
			Tuple linha = resultado.get(indice);
			if (!codigoCliente.equals(linha.get(qCliente.codigo))) {
				if (codigoCliente != 0L) { //Finalizando as informações do prestador anterior
					for (ReinfEventoNotaFiscal evtNF : nfsReinf)
						reinfEventoNotaFiscalRepository.save(evtNF);

					IdeTomador ideTomador = new IdeTomador();
					ideTomador.setTpInscTomador(1); //SEMPRE CNPJ
					ideTomador.setNrInscTomador(documentoCliente.replaceAll("[^0-9]", ""));
					ideTomador.setIndObra(0); //0 - Não é obra de construção civil ou não está sujeita a matrícula de obra;
					ideTomador.setVlrTotalBruto(this.numberFormat.format(vlrTotalBruto));
					ideTomador.setVlrTotalBaseRet(this.numberFormat.format(vlrTotalBaseRet));
					ideTomador.setVlrTotalRetPrinc(this.numberFormat.format(vlrTotalRetPrinc));
					ideTomador.setVlrTotalRetAdic(this.numberFormat.format(vlrTotalRetAdic));
					ideTomador.setVlrTotalNRetPrinc(this.numberFormat.format(vlrTotalNRetPrinc));
					ideTomador.setVlrTotalNRetAdic(this.numberFormat.format(vlrTotalNRetAdic));

					ideTomador.setNfs(nfs);

					reinfArquivo.getEvtServPrest().getInfoServPrest().getIdeEstabPrest().setIdeTomador(ideTomador);

					vlrTotalBruto = 0.0;
					vlrTotalBaseRet = 0.0;
					vlrTotalRetPrinc = 0.0;
					vlrTotalRetAdic = 0.0;
					vlrTotalNRetPrinc = 0.0;
					vlrTotalNRetAdic = 0.0;
					nfsReinf = new ArrayList<>();
					nfs = new ArrayList<>();
					reinfArquivo = new br.com.araujoabreu.sgi.service.reinf.r2020.ReinfR2020();
				}

				reinfEvento = this.getReinfEvento(reinfEntidade, reinfRegistroEvento);

				Pessoa cliente = new Pessoa();
				cliente.setCodigo(linha.get(qCliente.codigo));
				reinfEvento.setCliente(cliente);

				reinfEventoRepository.save(reinfEvento);

				//SubBloco do bloco evtServTom
				EvtServPrest evtServPrest = new EvtServPrest();

				evtServPrest.setId(reinfEvento.getIdTransmissao());
				reinfArquivo.setEvtServPrest(evtServPrest);

				evtServPrest.setIdeEvento(this.getIdeEvento(reinfEntidade));
				evtServPrest.setIdeContri(this.getIdeContri(reinfEntidade));

				InfoServPrest infoServPrest = new InfoServPrest();

				IdeEstabPrest ideEstabPrest = new IdeEstabPrest();

				ideEstabPrest.setTpInscEstabPrest(IdeContri.TipoInscricao.CNPJ.getValor());
				ideEstabPrest.setNrInscEstabPrest(linha.get(qUnidade.documento).replaceAll("[^0-9]", ""));

				infoServPrest.setIdeEstabPrest(ideEstabPrest);

				evtServPrest.setInfoServPrest(infoServPrest);

				mapReinf.put(reinfEvento, reinfArquivo);

				codigoCliente = linha.get(qCliente.codigo);
				documentoCliente = linha.get(qCliente.documento);

			}

			vlrTotalBruto += linha.get(qNotaSaida.valor).doubleValue();
			vlrTotalBaseRet += linha.get(qNotaTributoAliquota.baseCalculo).doubleValue();

			if (linha.get(qNotaTributoAliquota.valor) == null || linha.get(qNotaTributoAliquota.valor).doubleValue() == 0)
				vlrTotalRetPrinc += (linha.get(qNotaTributoAliquota.baseCalculo).doubleValue() * linha.get(qNotaTributoAliquota.aliquota.percentual).doubleValue()) / 100;
			else
				vlrTotalRetPrinc += linha.get(qNotaTributoAliquota.valor).doubleValue();

			Nfs nf = new Nfs();

			if (StringUtils.isEmpty(linha.get(qNotaSaida.serie)))
				nf.setSerie("0");
			else
				nf.setSerie(linha.get(qNotaSaida.serie));

			nf.setNumDocto(linha.get(qNotaSaida.numero).replaceAll("[^0-9]", ""));
			nf.setVlrBruto(this.numberFormat.format(linha.get(qNotaSaida.valor).doubleValue()));

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			nf.setDtEmissaoNF(sdf.format(linha.get(qNotaSaida.dataEmissao)));


			InfoTpServ infoTpServ = new InfoTpServ();
			infoTpServ.setTpServico(linha.get(qReinfServico.codigoClassificacao));

			infoTpServ.setVlrBaseRet(this.numberFormat.format(linha.get(qNotaTributoAliquota.baseCalculo)));
			if (linha.get(qNotaTributoAliquota.valor) == null || linha.get(qNotaTributoAliquota.valor).doubleValue() == 0)
				infoTpServ.setVlrRetencao(
					this.numberFormat.format(
						(linha.get(qNotaTributoAliquota.baseCalculo).doubleValue() * linha.get(qNotaTributoAliquota.aliquota.percentual).doubleValue()) / 100)
				);
			else
				infoTpServ.setVlrRetencao(this.numberFormat.format(linha.get(qNotaTributoAliquota.valor)));

			nf.setInfoTpServ(infoTpServ);
			nfs.add(nf);

			NotaFiscal nfEvento = new NotaFiscal();
			nfEvento.setCodigo(linha.get(qNotaSaida.codigo));

			ReinfEventoNotaFiscal reinfEventoNotaFiscal = new ReinfEventoNotaFiscal();
			reinfEventoNotaFiscal.setReinfEvento(reinfEvento);
			reinfEventoNotaFiscal.setNotaFiscal(nfEvento);

			nfsReinf.add(reinfEventoNotaFiscal);


			if (indice == (resultado.size() - 1)) { //Finalizando as informações da ultima linha
				for (ReinfEventoNotaFiscal evtNF : nfsReinf)
					reinfEventoNotaFiscalRepository.save(evtNF);

				IdeTomador ideTomador = new IdeTomador();
				ideTomador.setTpInscTomador(1); //SEMPRE CNPJ
				ideTomador.setNrInscTomador(linha.get(qCliente.documento).replaceAll("[^0-9]", ""));
				ideTomador.setIndObra(0); //0 - Não é obra de construção civil ou não está sujeita a matrícula de obra;
				ideTomador.setVlrTotalBruto(this.numberFormat.format(vlrTotalBruto));
				ideTomador.setVlrTotalBaseRet(this.numberFormat.format(vlrTotalBaseRet));
				ideTomador.setVlrTotalRetPrinc(this.numberFormat.format(vlrTotalRetPrinc));
				ideTomador.setVlrTotalRetAdic(this.numberFormat.format(vlrTotalRetAdic));
				ideTomador.setVlrTotalNRetPrinc(this.numberFormat.format(vlrTotalNRetPrinc));
				ideTomador.setVlrTotalNRetAdic(this.numberFormat.format(vlrTotalNRetAdic));

				ideTomador.setNfs(nfs);

				reinfArquivo.getEvtServPrest().getInfoServPrest().getIdeEstabPrest().setIdeTomador(ideTomador);
			}
		}

		for (ReinfEvento evento : mapReinf.keySet()) {
			this.salvarArquivo(mapReinf.get(evento), evento);
		}
	}


	/* **************************************************** R-2030 ******************************************************** */
	private void gerarRegistroR2030(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
	}


	/* **************************************************** R-2040 ******************************************************** */
	private void gerarRegistroR2040(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
	}


	/* **************************************************** R-2050 ******************************************************** */
	private void gerarRegistroR2050(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
	}


	/* **************************************************** R-2060 ******************************************************** */
	private void gerarRegistroR2060(Reinf reinfEntidade) {
		/*
		Seleção da NFs de fornecedores a serem informadas, base para a geração do REINF - 2010 - Serviços tomados
		*/
		QNotaSaida qNotaSaida = QNotaSaida.notaSaida;
		JPAQuery jpaQuery = getJPAQueryFactory().from(qNotaSaida).distinct();
		JPAQuery jpaQueryCancelamentos = getJPAQueryFactory().from(qNotaSaida).distinct();
		JPAQuery jpaQueryISS = getJPAQueryFactory().from(qNotaSaida).distinct();

		QUnidade qUnidade = QUnidade.unidade;
		QNotaTributoAliquotaEmissao qNotaTributoAliquotaEmissao = QNotaTributoAliquotaEmissao.notaTributoAliquotaEmissao;
		QAliquota qAliquota = QAliquota.aliquota;
		QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;

		DateExpression<Date> dtCancelamento = Expressions.dateTemplate(Date.class, "date({0})", qNotaSaida.dataCancelamento);
		DateExpression<Date> dtEmissao = Expressions.dateTemplate(Date.class, "date({0})", qNotaSaida.dataEmissao);

		DateExpression<Date> dtInicial = Expressions.dateTemplate(Date.class, "date({0})", this.calDtIni.getTime());
		DateExpression<Date> dtFinal = Expressions.dateTemplate(Date.class, "date({0})", this.calDtFim.getTime());

		//Cálculo do faturamento
		jpaQuery
			.join(qNotaSaida.empresa, qUnidade)
			.where(
				qNotaSaida.dataEmissao.goe(dtInicial)
					.and(qNotaSaida.dataEmissao.loe(dtFinal))
					.and(qUnidade.matriz.codigo.eq(this.codigoEmpresa))
					.and(qNotaSaida.tipoNotaFiscal.eq(NotaFiscal.TipoNotaFiscal.FATURAMENTO))
					.andAnyOf(
						qNotaSaida.status.in(Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO, Nota.StatusNota.RECEBIDA_FATURAMENTO),
						(qNotaSaida.status.eq(Nota.StatusNota.CANCELADA_FATURAMENTO).and(dtCancelamento.after(this.calDtFim.getTime())))
					)
			)
		;

		//Cálculo do valor de NFs canceladas
		jpaQueryCancelamentos
			.join(qNotaSaida.empresa, qUnidade)
			.where(
				qNotaSaida.dataCancelamento.goe(dtInicial)
					.and(qNotaSaida.dataCancelamento.loe(dtFinal))
					.and(qUnidade.matriz.codigo.eq(this.codigoEmpresa))
					.and(qNotaSaida.tipoNotaFiscal.eq(NotaFiscal.TipoNotaFiscal.FATURAMENTO))
					.and(qNotaSaida.status.eq(Nota.StatusNota.CANCELADA_FATURAMENTO)
					.and(dtEmissao.before(this.calDtIni.getTime()))
				)
			)
		;

		//Cálculo do valor do ISS não retido na fonte, ou seja, pago pela AA. O total será abatido da base de cálculo da CPRB
		jpaQueryISS
			.join(qNotaSaida.empresa, qUnidade)
			.join(qNotaSaida.listaNotaTributoAliquotaEmissao, qNotaTributoAliquotaEmissao)
			.join(qNotaTributoAliquotaEmissao.aliquota, qAliquota)
			.join(qAliquota.categoriaTributo, qCategoriaTributo)
			.where(
				qNotaSaida.dataEmissao.goe(dtInicial)
					.and(qNotaSaida.dataEmissao.loe(dtFinal))
					.and(qUnidade.matriz.codigo.eq(this.codigoEmpresa))
					.and(qNotaSaida.tipoNotaFiscal.eq(NotaFiscal.TipoNotaFiscal.FATURAMENTO))
					.and(
						qNotaSaida.status.in(Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO, Nota.StatusNota.RECEBIDA_FATURAMENTO)
						.or(qNotaSaida.status.eq(Nota.StatusNota.CANCELADA_FATURAMENTO).and(dtCancelamento.after(this.calDtFim.getTime())))
					)
					.and(qNotaSaida.issRetidoCliente.eq(Boolean.FALSE))
					.and(qCategoriaTributo.tributo.codigo.eq(CODIGO_ISS))
			)
		;

		BigDecimal receitaBruta = (BigDecimal) jpaQuery.select(qNotaSaida.valor.sum()).fetch().get(0);
		BigDecimal cancelamentos = (BigDecimal) jpaQueryCancelamentos.select(qNotaSaida.valor.sum()).fetch().get(0);
		BigDecimal issNaoRetido = (BigDecimal) jpaQueryISS.select(qNotaTributoAliquotaEmissao.valor.sum()).fetch().get(0);
		cancelamentos = (cancelamentos == null || cancelamentos.doubleValue() == 0.00) ? BigDecimal.ZERO : cancelamentos;
		issNaoRetido = (issNaoRetido == null || issNaoRetido.doubleValue() == 0.00) ? BigDecimal.ZERO : issNaoRetido;

		BigDecimal totalReducao = cancelamentos.add(issNaoRetido);

		Double cprb = (receitaBruta.subtract(totalReducao)).doubleValue() * new Double(ALIQUOTA_CPRB);

		ReinfEvento reinfEvento = this.getReinfEvento(reinfEntidade, reinfRegistroEvento);
		reinfEventoRepository.save(reinfEvento);

		ReinfR2060 reinf = new ReinfR2060();
		EvtCPRB evtCPRB = new EvtCPRB();
		evtCPRB.setId(String.valueOf(reinfEvento.getIdTransmissao()));

		evtCPRB.setIdeEvento(this.getIdeEvento(reinfEntidade));
		evtCPRB.setIdeContri(this.getIdeContri(reinfEntidade));

		IdeEstab ideEstab = new IdeEstab();

		ideEstab.setTpInscEstab(1);
		ideEstab.setNrInscEstab(reinfEntidade.getUnidade().getDocumento().replaceAll("[^0-9]", ""));
		ideEstab.setVlrRecBrutaTotal(this.numberFormat.format(receitaBruta));
		ideEstab.setVlrCPApurTotal(this.numberFormat.format(cprb));

		TipoCod tipoCod = new TipoCod();
		tipoCod.setCodAtivEcon("00000120");
		tipoCod.setVlrRecBrutaAtiv(this.numberFormat.format(receitaBruta));
		tipoCod.setVlrExcRecBruta(this.numberFormat.format(totalReducao));
		tipoCod.setVlrAdicRecBruta("0,00");
		tipoCod.setVlrBcCPRB(this.numberFormat.format(receitaBruta.subtract(totalReducao)));
		tipoCod.setVlrCPRBapur(this.numberFormat.format(cprb));

		if (!BigDecimal.ZERO.equals(cancelamentos)) {
			if (tipoCod.getTipoAjuste() == null)
				tipoCod.setTipoAjuste(new ArrayList<>());

			TipoAjuste tipoAjuste = new TipoAjuste();
			tipoAjuste.setTpAjuste(0); //Ajuste de redução
			tipoAjuste.setCodAjuste(6); //NFs Canceladas
			tipoAjuste.setVlrAjuste(this.numberFormat.format(cancelamentos));
			tipoAjuste.setDtAjuste(reinfEntidade.getReferencia());
			tipoAjuste.setDescAjuste(TipoAjuste.DESC_AJUSTE.NF_CANCELADA.getDescricao());

			tipoCod.getTipoAjuste().add (tipoAjuste);
		}

		if (!BigDecimal.ZERO.equals(issNaoRetido)) {
			if (tipoCod.getTipoAjuste() == null)
				tipoCod.setTipoAjuste(new ArrayList<>());

			TipoAjuste tipoAjuste = new TipoAjuste();
			tipoAjuste.setTpAjuste(0); //Ajuste de redução
			tipoAjuste.setCodAjuste(11); //Outros Ajustes
			tipoAjuste.setVlrAjuste(this.numberFormat.format(issNaoRetido));
			tipoAjuste.setDtAjuste(reinfEntidade.getReferencia());
			tipoAjuste.setDescAjuste(TipoAjuste.DESC_AJUSTE.ISS_EXCLUSAO.getDescricao());

			tipoCod.getTipoAjuste().add (tipoAjuste);
		}

		ideEstab.setTipoCod(tipoCod);

		InfoCPRB infoCPRB = new InfoCPRB();
		infoCPRB.setIdeEstab(ideEstab);

		evtCPRB.setInfoCPRB(infoCPRB);
		reinf.setEvtCPRB(evtCPRB);

		this.salvarArquivo(reinf, reinfEvento);
	}


	/* **************************************************** R-2070 ******************************************************** */
	private void gerarRegistroR2070(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
	}


	/* **************************************************** R-2098 ******************************************************** */
	private void gerarRegistroR2098(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
	}


	/* **************************************************** R-2099 ******************************************************** */
	private void gerarRegistroR2099(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
		ReinfEvento reinfEvento = this.getReinfEvento(reinfEntidade, reinfRegistroEvento);
		reinfEventoRepository.save(reinfEvento);

		ReinfR2099 reinf = new ReinfR2099();
		EvtFechaEvPer evtFechaEvPer = new EvtFechaEvPer();
		evtFechaEvPer.setId(String.valueOf(reinfEvento.getIdTransmissao()));

		evtFechaEvPer.setIdeEvento(this.getIdeEventoR2099(reinfEntidade));
		evtFechaEvPer.setIdeContri(this.getIdeContri(reinfEntidade));
		evtFechaEvPer.setIdeRespInf(new IdeRespInf());
		evtFechaEvPer.setInfoFech(new InfoFech());
		evtFechaEvPer.getInfoFech().setEvtServTm(this.possuiR2010(reinfEntidade));

		reinf.setEvtFechaEvPer(evtFechaEvPer);
		this.salvarArquivo(reinf, reinfEvento);

	}

	private String possuiR2010(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
		QReinf qReinf = QReinf.reinf;
		QReinfEvento qReinfEvento =  QReinfEvento.reinfEvento;
		QReinfRegistroEvento qReinfRegistroEvento = QReinfRegistroEvento.reinfRegistroEvento;

		QBean<ReinfEvento> qBean = Projections.bean(
			ReinfEvento.class,
			qReinfEvento.codigo
		);

		JPAQuery<ReinfEvento> jpaQuery = select(qBean);

		jpaQuery
			.from(qReinfEvento)
			.join(qReinfEvento.reinf, qReinf)
			.join(qReinfEvento.reinfRegistroEvento, qReinfRegistroEvento)
			.where(
				qReinf.codigo.eq(reinfEntidade.getCodigo())
					.and(qReinfRegistroEvento.nome.eq("R-2010"))
			);

		List<ReinfEvento> eventos = jpaQuery.fetch();
		return (eventos == null || eventos.isEmpty()) ? "N" : "S";
	}


	/* **************************************************** R-3010 ******************************************************** */
	private void gerarRegistroR3010(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
	}


	/* **************************************************** R-9000 ******************************************************** */
	private void gerarRegistroR9000(br.com.araujoabreu.sgi.controleFiscal.domain.Reinf reinfEntidade) {
	}


	@Override
	public List<ReinfTO> findReinfBySituacao(Integer situacao) {
		return map(getRepository().findByReinfStatus(Reinf.ReinfStatus.values()[situacao]));
	}

	/* ********************************************************************************************************************
													PROCESSAR ARQUIVO DE RETORNO
		 ********************************************************************************************************************** */
	@Transactional(propagation = Propagation.REQUIRED)
	public void processarTodosRetornos(Integer ano) throws Exception {
		String[] meses = {"01", "02","03","04","05","06","07","08","09","10","11","12" };

		SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date dtInicial = new Date(System.currentTimeMillis());

		for (String mes : meses) {
			referencia = ano + "-" + mes;
			if (!referencia.equals("2019-01") && !referencia.equals("2019-02") && !referencia.equals("2019-03") && !referencia.equals("2019-04")) {
				List<UnidadeTO> matrizes =  unidadeService.listarUnidadeMatrizAtiva();
				for (UnidadeTO unidadeTO : matrizes) {
					this.processarRetorno(referencia, unidadeTO.getCodigo());
					System.gc();
				}
			}
		}

		System.out.println(String.format("Importação de arquivos REINF do ano de %d - iniciado às ", ano, sdfHora.format(dtInicial.getTime())));

		Date dtFinal = new Date(System.currentTimeMillis());
		System.out.println(String.format("Importação de arquivos REINF do ano de %d - finalizado às ", ano, sdfHora.format(dtFinal.getTime())));
	}


	@Transactional(propagation = Propagation.REQUIRED)
	public void processarRetorno(String referencia, Long codigoUnidade) throws Exception{
		this.referencia = referencia;
		Reinf reinf = map(this.findReinf(referencia, codigoUnidade));

		Calendar dia = Calendar.getInstance();
		String msgErro = "";
		SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		logger.info("Iniciando - {} {} - Referência : {}", "Importação de arquivos REINF", sdfHora.format(dia.getTime()), referencia);
		System.out.println(String.format("Iniciando - Importação de arquivos REINF %s - Referência : %s", sdfHora.format(dia.getTime()), referencia));

		this.destino = parametroRepository.findById(CAMINHO_ARQUIVO).get();
		File dirReinf = new File(String.format("%s/%s/%d", this.destino.getValor(), referencia, codigoUnidade));
//		File dirReinf = new File("/home/eavila/Desenv/arquivos/sgi/desenv/reinf/2019-10/1/R-2010/enviado/02");

		//Separa todos os arquivos de repostas da Receita
		List<File> arqRespostas = this.recuperarRespostas(dirReinf);

		//Processamento dos arquivos de resposta
		for (File arquivo : arqRespostas) {
			if (!arquivo.getAbsolutePath().contains("R-5011")) {//Não processar arquivos de consulta - este evento foi descontinuado
				logger.info("Processando arquivo de resposta:  {}", arquivo.getName());
				System.out.println(String.format("Processando arquivo de resposta:  %s", arquivo.getAbsolutePath()));

				String[] st = arquivo.getAbsolutePath().split("/");
				String dirResposta = st[st.length - 2];
				String dirEvento = st[st.length - 4];
				try {
					Scanner scanner = new Scanner(arquivo);
					//Realiza o unmarshall do arquivo de resposta. Ele contém a lista de eventos enviados.
					XmlMapper xmlMapper = new XmlMapper();
					xmlMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
					String xml = scanner.nextLine();
					br.com.araujoabreu.sgi.service.reinf.retorno.Reinf retorno = xmlMapper.readValue(xml, br.com.araujoabreu.sgi.service.reinf.retorno.Reinf.class);

					RetornoLoteEventos loteEventos = retorno.getRetornoLoteEventos();

					QReinfEvento qReinfEvento = QReinfEvento.reinfEvento;
					JPAQuery jpaQuery = getJPAQueryFactory().from(qReinfEvento);
					QAnexo qAnexo = QAnexo.anexo;

					for (Evento evento : loteEventos.getRetornoEventos()) {
						String idEvento = evento.getId();
						logger.info("Processando evento:  idTransmissão {}", idEvento);
//						System.out.println(String.format("Processando evento:  idTransmissão %s", idEvento));

						List<ReinfEvento> eventos = reinfEventoRepository.findByIdTransmissao(idEvento);
						ReinfEvento reinfEvento = !eventos.isEmpty() ? eventos.get(0) : null;

						//TODO: Remover HardCode deste if
						if (!eventos.isEmpty() && eventos.size() > 1) {
							if (dirEvento.equals("R-2060"))
								reinfEvento = reinfEventoRepository.findByIdTransmissaoAndReinfRegistroEventoCodigo(idEvento, 8L);
							else
								reinfEvento = reinfEventoRepository.findByIdTransmissaoAndReinfRegistroEventoCodigo(idEvento, 11L);
						}

						if (reinfEvento == null) {
							//TRATAR OS ARQUIVOS QUE FORAM GERADOS MANUALMENTE E REMOVER QUANDO A VERSÃO FINAL ENTRAR EM PRODUÇÃO
							//Buscar arquivo físico pelo id do evento
							reinfEvento = this.recuperarArquivoReinfEventoPorIdEvento(arquivo, idEvento, reinf);
						}

						Anexo anexoEnvio = this.buscarPorIdTransmissaoETipo(idEvento, ReinfEventoAnexo.TipoArquivoReinf.ENVIO);
						String nomeAnexoEnvio = anexoEnvio.getNome();
						if (nomeAnexoEnvio.contains("/"))
							nomeAnexoEnvio = nomeAnexoEnvio.substring(nomeAnexoEnvio.lastIndexOf('/'));

						getJPAQueryFactory().update(qAnexo)
							.where(qAnexo.codigo.eq(anexoEnvio.getCodigo()))
							.set(qAnexo.nome, dirResposta + "/" + nomeAnexoEnvio)
							.execute();

						Anexo anexoResposta = this.buscarPorIdTransmissaoETipo(idEvento, ReinfEventoAnexo.TipoArquivoReinf.RETORNO);
						msgErro = String.format("ERRO - Processando evento:  idTransmissão %s - Arquivo: %s", idEvento, arquivo.getAbsolutePath());
						//Adicionar o arquivo de resposta
						if (anexoResposta == null) {
							anexoResposta = new Anexo();
							if (referencia.equals("2018-05") && evento.equals("R-2010"))
								anexoResposta.setNome(arquivo.getName());
							else {
								if (arquivo.getName().contains("/"))
									anexoResposta.setNome(dirResposta + "/" + arquivo.getName().substring(arquivo.getName().lastIndexOf('/')));
								else
									anexoResposta.setNome(dirResposta + "/" + arquivo.getName());
							}
							anexoResposta.setDescricao(String.format("Resposta referente ao evento : %s(%s) do Reinf de referêcia: %s", reinfEvento.getReinfRegistroEvento().getNome(), idEvento, referencia));
							anexoResposta.setContentType("text/xml");
							anexoResposta.setCriador("eavila");
							anexoResposta.setDataCriacao(new Date(System.currentTimeMillis()));
							anexoRepository.save(anexoResposta);

							ReinfEventoAnexo reinfEventoAnexo = new ReinfEventoAnexo();
							reinfEventoAnexo.setReinfEvento(reinfEvento);
							reinfEventoAnexo.setAnexo(anexoResposta);
							reinfEventoAnexo.setTipo(ReinfEventoAnexo.TipoArquivoReinf.RETORNO);
							reinfEventoAnexoRepository.save(reinfEventoAnexo);
						}

						//Adicionar o arquivo assinado digitalmente e enviado a Receita
						Anexo anexoAssinado = this.buscarPorIdTransmissaoETipo(idEvento, ReinfEventoAnexo.TipoArquivoReinf.ASSINADO);
						if (anexoAssinado == null) {
							anexoAssinado = new Anexo();
							if (referencia.equals("2018-05") && evento.equals("R-2010"))
								anexoAssinado.setNome(arquivo.getName());
							else
								anexoAssinado.setNome(dirResposta + "/" + nomeAnexoEnvio.replace(".", "_assinado."));

							anexoAssinado.setDescricao(String.format("Arquivo assinado referente ao evento : %s(%s) do Reinf de referêcia: %s", reinfEvento.getReinfRegistroEvento().getNome(), idEvento, referencia));
							anexoAssinado.setContentType("text/xml");
							anexoAssinado.setCriador("eavila");
							anexoAssinado.setDataCriacao(new Date(System.currentTimeMillis()));
							anexoRepository.save(anexoAssinado);

							ReinfEventoAnexo reinfEventoAnexo = new ReinfEventoAnexo();
							reinfEventoAnexo.setReinfEvento(reinfEvento);
							reinfEventoAnexo.setAnexo(anexoAssinado);
							reinfEventoAnexo.setTipo(ReinfEventoAnexo.TipoArquivoReinf.ASSINADO);
							reinfEventoAnexoRepository.save(reinfEventoAnexo);
						}

						if (evento.getReinfEvento().getEvtTotal().getIdeRecRetorno().getIdeStatus().getCdRetorno() == 1) {//A Receita acusou erro no recebimento do arquivo
							//Para cada erro é criado um registro e associado ao evento
							logger.info("Processando evento:  idTransmissão {} - adicionando erros de transmissão : {}", idEvento);
							for (RegOcorrs reg : evento.getReinfEvento().getEvtTotal().getIdeRecRetorno().getIdeStatus().getRegOcorrs()) {
								ReinfEventoErro reinfEventoErro = new ReinfEventoErro();
								reinfEventoErro.setReinfEvento(reinfEvento);
								reinfEventoErro.setIdErro(reg.getCodResp());
								reinfEventoErro.setDescricao(reg.getDscResp());
								reinfEventoErroRepository.save(reinfEventoErro);

								getJPAQueryFactory().update(qReinfEvento)
									.where(qReinfEvento.codigo.eq(reinfEvento.getCodigo()))
									.set(qReinfEvento.dataEnvio, evento.getReinfEvento().getEvtTotal().getInfoRecEv().getDhProcess())
									.set(qReinfEvento.situacao, ReinfEvento.SituacaoEventoReinf.ENVIADO_ERRO)
									.execute();

								getJPAQueryFactory().update(qAnexo)
									.where(qAnexo.codigo.eq(anexoResposta.getCodigo()))
									.set(qAnexo.dataCriacao, evento.getReinfEvento().getEvtTotal().getInfoRecEv().getDhProcess())
									.execute();
							}
						} else {//Arquivo entregue com sucesso a Receita
							logger.info("Processando evento:  idTransmissão {} - atualizando o status do evento.", idEvento);
							if (reinfEvento.getReinfRegistroEvento().getNome().equals("R-2099")) {//Evento de fechamento
								getJPAQueryFactory().update(qReinfEvento)
									.where(qReinfEvento.codigo.eq(reinfEvento.getCodigo()))
									.set(qReinfEvento.numeroRecibo, evento.getReinfEvento().getEvtTotal().getInfoRecEv().getNrProtEntr()) //Número do protocolo de entrega do REINF
									.set(qReinfEvento.dataEnvio, evento.getReinfEvento().getEvtTotal().getInfoRecEv().getDhProcess())
									.set(qReinfEvento.situacao, ReinfEvento.SituacaoEventoReinf.ENVIADO_SUCESSO)
									.execute();

								getJPAQueryFactory().update(qAnexo)
									.where(qAnexo.codigo.eq(anexoResposta.getCodigo()))
									.set(qAnexo.dataCriacao, evento.getReinfEvento().getEvtTotal().getInfoRecEv().getDhProcess())
									.execute();

								//Evento de fechamento foi enviado a Receita com sucesso. O status do Reinf é atualizado.
								QReinf qReinf = QReinf.reinf;
								getJPAQueryFactory().update(qReinf)
									.where(qReinf.codigo.eq(reinfEvento.getReinf().getCodigo()))
									.set(qReinf.reinfStatus, Reinf.ReinfStatus.EM_PROCESSAMENTO_RECEITA)
									.execute();
							} else {//Demais eventos enviado (R-2010, R-2020, R-2060, etc). O número do recibo e o status são atualizados.
								getJPAQueryFactory().update(qReinfEvento)
									.where(qReinfEvento.codigo.eq(reinfEvento.getCodigo()))
									.set(qReinfEvento.numeroRecibo, evento.getReinfEvento().getEvtTotal().getInfoTotal().getNrRecArqBase()) //Número do recibo do Evento
									.set(qReinfEvento.dataEnvio, evento.getReinfEvento().getEvtTotal().getInfoRecEv().getDhProcess())
									.set(qReinfEvento.situacao, ReinfEvento.SituacaoEventoReinf.ENVIADO_SUCESSO)
									.execute();

								getJPAQueryFactory().update(qAnexo)
									.where(qAnexo.codigo.eq(anexoResposta.getCodigo()))
									.set(qAnexo.dataCriacao, evento.getReinfEvento().getEvtTotal().getInfoRecEv().getDhProcess())
									.execute();
							}
						}
					}
				} catch (Exception e) {
					System.out.println(msgErro);
					e.printStackTrace();
				}
			}
		}

		dia = Calendar.getInstance();
		logger.info("Finalizando - {} {} - Referência : {}", "Importação de arquivos REINF", sdfHora.format(dia.getTime()), referencia);
		System.out.println(String.format("Finalizando - Importação de arquivos REINF %s - Referência : %s", sdfHora.format(dia.getTime()), referencia));

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void processaArquivoRetorno(String referencia, Long codigoEmpresa) throws Exception {
		Reinf reinf = map(this.findReinf(referencia, codigoEmpresa));
		Anexo anexo = null;
		List<ReinfAnexo> reinfAnexos = reinfAnexoRepository.findByReinfCodigo(reinf.getId());
		for (ReinfAnexo ra : reinfAnexos) {
			if(!ra.getAnexo().getNome().contains("importado")) {
				anexo = ra.getAnexo();
				break;
			}
		}

		if (anexo != null) {
			this.destino = parametroRepository.findById(CAMINHO_ARQUIVO).get();
			String strArquivo = "%s/%s/upload/%s";
			String strDirDestino = "%s/%s";
			File arquivoReinf = new File(String.format(strArquivo, this.destino.getValor(), referencia, anexo.getNome()));
			File dirDestino = new File(String.format(strDirDestino, this.destino.getValor(), referencia));

			this.descompactarArquivosReinf(arquivoReinf, dirDestino.getAbsolutePath());
			this.processarRetorno(referencia, codigoEmpresa);

			String strDirImportado = "%s/%s/importado";
			File dirImportado = new File(String.format(strDirImportado, this.destino.getValor(), referencia));
			if (!dirImportado.exists())
				dirImportado.mkdirs();
			String strArquivoImportado = "%s/%s_%d_importado_%d.zip";
			String novoNome = "%s_%d_importado_%d.zip";
			arquivoReinf.renameTo(new File(String.format(strArquivoImportado, dirImportado.getAbsolutePath(), referencia, codigoEmpresa, System.currentTimeMillis())));

			anexo.setNome(String.format(novoNome, referencia, codigoEmpresa, System.currentTimeMillis()));
			anexoRepository.save(anexo);
		}

	}

	private List<File> recuperarRespostas(File dirReinf) {
		System.out.println(">>>>>>>");
		System.out.println(dirReinf);

		List<File> respostas = new ArrayList<>();
		for(File f : dirReinf.listFiles()) {
			if (f.isDirectory())
				respostas.addAll(this.recuperarRespostas(f));
			else if (f.getName().contains("resposta"))
					respostas.add(f);
		}
		return respostas;
	}

	private ReinfEvento recuperarArquivoReinfEventoPorIdEvento(File arquivoResposta, String idEvento, Reinf reinf) {
		ReinfEvento reinfEventoArquivo =  null;
		File dirReinf = new File(arquivoResposta.getParent());
		String nomeEvento = null;

		for(File arquivo : dirReinf.listFiles()) {
			if (!arquivo.isDirectory() &&  !arquivo.getName().contains("resposta") && !arquivo.getName().contains("assinado") ) {
				try {
					Scanner scanner = null;
					scanner = new Scanner(arquivo);

					XmlMapper xmlMapper = new XmlMapper();
					xmlMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
					String xml = scanner.nextLine();

					if (arquivo.getAbsolutePath().contains("R-2010")) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						MaskFormatter mask = new MaskFormatter("##.###.###/####-##");
						mask.setValueContainsLiteralCharacters(false);

						ReinfR2010 reinfR2010 = 	xmlMapper.readValue(xml, ReinfR2010.class);
						if (reinfR2010.getEvtServTom().getId().equals(idEvento)) {
							nomeEvento = "R-2010";
							reinfEventoArquivo = new ReinfEvento();
							reinfEventoArquivo.setReinfRegistroEvento(this.reinfRegistroEventoRepository.findByNome(nomeEvento));
							reinfEventoArquivo.setEventoNotasFiscais(new ArrayList<>());
							String cnpj = reinfR2010.getEvtServTom().getInfoServTom().getIdeEstabObra().getIdePrestServ().getCnpjPrestador();
							cnpj = mask.valueToString(cnpj);

							for (Nfs nfs : reinfR2010.getEvtServTom().getInfoServTom().getIdeEstabObra().getIdePrestServ().getNfs()) {
								NotaFiscal notaFiscal = notaFiscalRepository.findByDataEmissaoAndCredorDocumentoAndNumeroLike(sdf.parse(nfs.getDtEmissaoNF()), cnpj, "%" + nfs.getNumDocto() + "%");
								if (notaFiscal != null) {
									ReinfEventoNotaFiscal reinfEventoNotaFiscal = new ReinfEventoNotaFiscal();
									reinfEventoNotaFiscal.setReinfEvento(reinfEventoArquivo);
									reinfEventoNotaFiscal.setNotaFiscal(notaFiscal);
									reinfEventoArquivo.getEventoNotasFiscais().add(reinfEventoNotaFiscal);
								}
							}
						}
					}
					else if (arquivo.getAbsolutePath().contains("R-2020")) {
						ReinfR2020 reinfR2020 = 	xmlMapper.readValue(xml, ReinfR2020.class);
						if (reinfR2020.getEvtServPrest().getId().equals(idEvento)) {
							nomeEvento = "R-2020";
							reinfEventoArquivo = new ReinfEvento();
							reinfEventoArquivo.setReinfRegistroEvento(this.reinfRegistroEventoRepository.findByNome(nomeEvento));
						}
					}
					else if (arquivo.getAbsolutePath().contains("R-2060")) {
						ReinfR2060 reinfR2060 = 	xmlMapper.readValue(xml, ReinfR2060.class);
						if (reinfR2060.getEvtCPRB().getId().equals(idEvento)) {
							nomeEvento = "R-2060";
							reinfEventoArquivo = new ReinfEvento();
							reinfEventoArquivo.setReinfRegistroEvento(this.reinfRegistroEventoRepository.findByNome(nomeEvento));
						}
					}
					else if (arquivo.getAbsolutePath().contains("R-2098")) {
						ReinfR2098 reinfR2098 = 	xmlMapper.readValue(xml, ReinfR2098.class);
						if (reinfR2098.getEvtReabreEvPer().getId().equals(idEvento)) {
							nomeEvento = "R-2098";
							reinfEventoArquivo = new ReinfEvento();
							reinfEventoArquivo.setReinfRegistroEvento(this.reinfRegistroEventoRepository.findByNome(nomeEvento));
						}
					}
					else if (arquivo.getAbsolutePath().contains("R-2099")) {
						ReinfR2099 reinfR2099 = 	xmlMapper.readValue(xml, ReinfR2099.class);
						if (reinfR2099.getEvtFechaEvPer().getId().equals(idEvento)) {
							nomeEvento = "R-2099";
							reinfEventoArquivo = new ReinfEvento();
							reinfEventoArquivo.setReinfRegistroEvento(this.reinfRegistroEventoRepository.findByNome(nomeEvento));
						}
					}

					if (reinfEventoArquivo != null) {
						FileTime creationTime = (FileTime) Files.getAttribute(arquivo.toPath(), "creationTime");
						reinfEventoArquivo.setReinf(reinf);
						reinfEventoArquivo.setTipoEvento(ReinfEvento.TipoEvento.ORIGINAL);
						reinfEventoArquivo.setCriador("admin");
						reinfEventoArquivo.setDataCriacao(new Date(creationTime.toMillis()));
						reinfEventoArquivo.setDataEnvio(new Date(creationTime.toMillis()));
						reinfEventoArquivo.setIdTransmissao(idEvento);
						reinfEventoArquivo.setSituacao(ReinfEvento.SituacaoEventoReinf.ARQUIVO_GERADO);
						this.reinfEventoRepository.save(reinfEventoArquivo);

						String nomeArquivo = this.referencia + "_" + reinfEventoArquivo.getReinfRegistroEvento().getNome() + ".xml";
						String descricao = "Referente ao evento: " + reinfEventoArquivo.getReinfRegistroEvento().getNome() + " do Reinf de referêcia: " + this.referencia;

						Anexo anexo = new Anexo();
						anexo.setNome(nomeArquivo);
						anexo.setDescricao(descricao);
						anexo.setCriador("admin");
						anexo.setDataCriacao(new Date(System.currentTimeMillis()));
						anexo.setContentType("text/xml");
						anexoRepository.save(anexo);
						anexo.setNome(anexo.getCodigo() + "_" + anexo.getNome());
						anexoRepository.save(anexo);

						Path source = arquivo.toPath();
						Files.move(source, source.resolveSibling(anexo.getNome()));

						ReinfEventoAnexo reinfEventoAnexo = new ReinfEventoAnexo();
						reinfEventoAnexo.setReinfEvento(reinfEventoArquivo);
						reinfEventoAnexo.setAnexo(anexo);
						reinfEventoAnexo.setTipo(ReinfEventoAnexo.TipoArquivoReinf.ENVIO);
						reinfEventoAnexoRepository.save(reinfEventoAnexo);

						break;
					}

				}
				catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return reinfEventoArquivo;
	}

	private Anexo buscarPorIdTransmissaoETipo(String idTransmissao, ReinfEventoAnexo.TipoArquivoReinf tipo) {
		QAnexo qAnexo = QAnexo.anexo;
		QReinfEvento qReinfEvento = QReinfEvento.reinfEvento;
		QReinfEventoAnexo qReinfEventoAnexo = QReinfEventoAnexo.reinfEventoAnexo;
		QReinf qReinf = QReinf.reinf;

		JPAQuery jpaQuery = getJPAQueryFactory().from(qAnexo);

		jpaQuery.from(qAnexo)
			.join(qAnexo.eventos, qReinfEventoAnexo)
			.join(qReinfEventoAnexo.reinfEvento, qReinfEvento)
			.join(qReinfEvento.reinf, qReinf)
			.where(
				qReinfEvento.idTransmissao.eq(idTransmissao)
					.and(qReinfEventoAnexo.tipo.eq(tipo))
			);

		List<Anexo> anexos = jpaQuery.fetch();
		if (anexos == null || anexos.isEmpty())
			return null;

		return anexos.get(0);
	}

	private void compactarArquivosReinf(ReinfTO reinf) {
		try {
			String dirZip = String.format("%s/%s/%s", this.destino.getValor(), reinf.getReferencia(), "download");
			File diretorio = new File(dirZip);
			if (!diretorio.exists())
				diretorio.mkdirs();

			String sourceFile = String.format("%s/%s/%d", this.destino.getValor(), reinf.getReferencia(), reinf.getUnidade().getCodigo());
			FileOutputStream fos = new FileOutputStream(String.format("%s/%s_%s.zip", dirZip, reinf.getReferencia(), reinf.getUnidade().getCodigo() == 1 ? "SA" : "NO"));
			ZipOutputStream zipOut = new ZipOutputStream(fos);

			File fileToZip = new File(sourceFile);
			zipFile(fileToZip, fileToZip.getName(), zipOut);
			zipOut.close();
			fos.close();

			Anexo anexo = new Anexo();
			anexo.setNome(String.format("%s_%s.zip", reinf.getReferencia(), reinf.getUnidade().getCodigo() == 1 ? "SA" : "NO"));
			anexo.setContentType("application/download");
			anexo.setCriador(this.criador);
			anexo.setDataCriacao(new Date(System.currentTimeMillis()));
			anexoRepository.save(anexo);

			ReinfAnexo reinfAnexo = new ReinfAnexo();
			reinfAnexo.setAnexo(anexo);
			reinfAnexo.setReinf(map(reinf));
			reinfAnexo.setTipoAnexo(ReinfAnexo.TipoAnexoReinf.DOWNLOAD);
			reinfAnexo.setStatus(ReinfAnexo.StatusAnexoReinf.GERADO);
			reinfAnexoRepository.save(reinfAnexo);

		}
		catch (FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
		if (fileToZip.isHidden()) {
			return;
		}
		if (fileToZip.isDirectory()) {
			if (fileName.endsWith("/")) {
				zipOut.putNextEntry(new ZipEntry(fileName));
				zipOut.closeEntry();
			} else {
				zipOut.putNextEntry(new ZipEntry(fileName + "/"));
				zipOut.closeEntry();
			}
			File[] children = fileToZip.listFiles();
			for (File childFile : children) {
				zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
			}
			return;
		}
		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipOut.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		fis.close();
	}

	private String descompactarArquivosReinf(File arqReinf, String strDestDir) {
		File destDir = null;
		ZipInputStream zis = null;
		try {
			destDir = new File(strDestDir);
			if (!destDir.exists())
				destDir.mkdir();

//			destDir = new File(destDir.getAbsolutePath() + "/" + arqReinf.getName().substring(0,7)) ;
//			if (!destDir.exists())
//				destDir.mkdir();

			byte[] buffer = new byte[1024];
			zis = new ZipInputStream(new FileInputStream(arqReinf));
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				File newFile = newFile(destDir, zipEntry);
				if (zipEntry.isDirectory()) {
					if (!newFile.isDirectory() && !newFile.mkdirs()) {
						throw new IOException("Failed to create directory " + newFile);
					}
				}
				else {
					// fix for Windows-created archives
					File parent = newFile.getParentFile();
					if (!parent.isDirectory() && !parent.mkdirs()) {
						throw new IOException("Failed to create directory " + parent);
					}

					// write file content
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
				}
				zipEntry = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (zis != null)
					zis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return destDir == null ? System.getProperty("user.home") :  destDir.getAbsolutePath() + "/" + (arqReinf.getName().contains("SA") ? "1" : "4");
	}

	private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}


}
