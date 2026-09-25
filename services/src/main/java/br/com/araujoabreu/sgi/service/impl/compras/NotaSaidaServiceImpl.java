package br.com.araujoabreu.sgi.service.impl.compras;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.NaturezaFornecimento;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.QUnidade;
import br.com.araujoabreu.sgi.compras.domain.Nota;
import br.com.araujoabreu.sgi.compras.domain.NotaFiscal;
import br.com.araujoabreu.sgi.compras.domain.NotaSaida;
import br.com.araujoabreu.sgi.compras.domain.QNotaSaida;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface;
import br.com.araujoabreu.sgi.faturamento.domain.Faturamento;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamento;
import br.com.araujoabreu.sgi.faturamento.domain.QFaturamentoEtapa;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.compras.NotaSaidaService;
import br.com.araujoabreu.sgi.service.faturamento.FaturamentoEtapaService;
import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoEtapaTO;
import br.com.araujoabreu.sgi.service.impl.loteInterfaceFaturamento.LoteInterfaceFaturamentoServiceImpl;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.corporativo.pessoa.domain.QCidade;
import br.com.fasolti.corporativo.pessoa.domain.QEndereco;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.documentoFiscal.DocumentoFiscalRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class NotaSaidaServiceImpl extends BaseSearchService<NotaSaida, NotaSaidaTO, NotaSaidaPesquisaTO, NotaSaidaRepository> implements NotaSaidaService {

	private final QNotaSaida qNotaSaida = QNotaSaida.notaSaida;

	@Autowired
	private DocumentoFiscalRepository documentoFiscalRepository;

	@Autowired
	private FaturamentoEtapaService faturamentoEtapaService;

	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	public NotaSaidaServiceImpl(NotaSaidaRepository repository) {
		super(repository);
	}

	@Override
	protected void doBeforeSave(NotaSaida entity, NotaSaidaTO object) throws ServiceException {
		if (object.getDocumentoFiscal() != null && object.getDocumentoFiscal().getCodigo() != null) {
			entity.setDocumentoFiscal(documentoFiscalRepository.findById(object.getDocumentoFiscal().getCodigo()).orElse(null));
		}
	}

	@Override
	public NotaSaidaTO obterNotaSaidaApenasCodigoENumero(Long codigo) {
		JPAQuery jpaQuery = select(Projections.fields(NotaSaida.class, QNotaSaida.notaSaida.codigo, QNotaSaida.notaSaida.numero));
		jpaQuery.where(QNotaSaida.notaSaida.codigo.eq(codigo));

		NotaSaida notaSaida = (NotaSaida) jpaQuery.fetchOne();

		return map(notaSaida);
	}

	@Override
	public NotaSaidaTO obterNotaSaidaApenasCodigoNumeroStatusResponsavelNumeracao(Long codigo) {
		JPAQuery jpaQuery = select(Projections.fields(NotaSaida.class,
			QNotaSaida.notaSaida.codigo,
			QNotaSaida.notaSaida.numero,
			QNotaSaida.notaSaida.status,
			Projections.fields(Pessoa.class,
				QNotaSaida.notaSaida.responsavelNumeracao.codigo,
				QNotaSaida.notaSaida.responsavelNumeracao.nome).as(QNotaSaida.notaSaida.responsavelNumeracao)
			)
		);
		jpaQuery.where(QNotaSaida.notaSaida.codigo.eq(codigo));

		NotaSaida notaSaida = (NotaSaida) jpaQuery.fetchOne();

		return map(notaSaida);
	}

	@Override
	public JPAQuery<?> obterNotaSaisaBasica(Long codigo) {
		//TODO: Fazer.
		return select();
	}

	@Override
	public List<NotaSaidaTO> obterNotasDeFaturamentoPorTipoPrefeitura(Date inicio, Date fim, String tipoLoteInterface) {
		LoteInterface.TipoLoteInterface tipoLote = LoteInterfaceFaturamentoServiceImpl.getTipoLoteByNome(tipoLoteInterface);

		List<NotaSaida> list = select(
			Projections.fields(
				NotaSaida.class,
				qNotaSaida.codigo,
				qNotaSaida.numero,
				qNotaSaida.valor,
				Projections.fields(
					CentroFinanceiro.class,
					qNotaSaida.centroFinanceiro.codigo,
					qNotaSaida.centroFinanceiro.identificadorFinal,
					qNotaSaida.centroFinanceiro.identificador,
					qNotaSaida.centroFinanceiro.nome
				).as(qNotaSaida.centroFinanceiro)
			)
		)
			.leftJoin(qNotaSaida.centroFinanceiro, QCentroFinanceiro.centroFinanceiro)
			.leftJoin(qNotaSaida.empresa, QUnidade.unidade)

			.leftJoin(QUnidade.unidade.endereco, QEndereco.endereco1)
			.leftJoin(QEndereco.endereco1.cidade, QCidade.cidade)
			.where(qNotaSaida.dataEmissao.eq(inicio).or(qNotaSaida.dataEmissao.after(inicio))
				.and(qNotaSaida.dataEmissao.before(fim).or(qNotaSaida.dataEmissao.eq(fim)))
				.and(qNotaSaida.status.eq(Nota.StatusNota.A_PROCESSAR_FATURAMENTO))
				.and(qNotaSaida.tipoNotaFiscal.eq(NotaFiscal.TipoNotaFiscal.FATURAMENTO))
				.and(QCidade.cidade.tipoLoteInterface.eq(tipoLote)))
			.fetch();

		list.sort((o1, o2) -> o1.getNumero().compareTo(o2.getNumero()));

		List<NotaSaidaTO> listTO = map(list);

		listTO.forEach(notaSaidaTO -> notaSaidaTO.setCliente(obterClienteSubclienteNotaSaida(notaSaidaTO.getCodigo())));

		return listTO;
	}

	@Override
	public List<NotaSaidaTO> obterNotasDeFaturamentoNotaNorteBelem(Date inicio, Date fim, String tipoLoteInterface) {
		LoteInterface.TipoLoteInterface tipoLote = LoteInterfaceFaturamentoServiceImpl.getTipoLoteByNome(tipoLoteInterface);

		List<NotaSaida> list = select(
			Projections.fields(
				NotaSaida.class,
				qNotaSaida.codigo,
				qNotaSaida.numero,
				qNotaSaida.valor,
				Projections.fields(
					CentroFinanceiro.class,
					qNotaSaida.centroFinanceiro.codigo,
					qNotaSaida.centroFinanceiro.identificadorFinal,
					qNotaSaida.centroFinanceiro.identificador,
					qNotaSaida.centroFinanceiro.nome
				).as(qNotaSaida.centroFinanceiro)
			)
		)
			.leftJoin(qNotaSaida.centroFinanceiro, QCentroFinanceiro.centroFinanceiro)
			.leftJoin(qNotaSaida.empresa, QUnidade.unidade)

			.leftJoin(QUnidade.unidade.endereco, QEndereco.endereco1)
			.leftJoin(QEndereco.endereco1.cidade, QCidade.cidade)
			.where(qNotaSaida.dataEmissao.eq(inicio)
				.and(qNotaSaida.status.eq(Nota.StatusNota.A_PROCESSAR_FATURAMENTO))
				.and(qNotaSaida.tipoNotaFiscal.eq(NotaFiscal.TipoNotaFiscal.FATURAMENTO))
				.and(qNotaSaida.tipoFornecimento.ne(NaturezaFornecimento.TipoFornecimento.MATERIAIS))
				.and(QCidade.cidade.tipoLoteInterface.eq(tipoLote)))
			.fetch();

		list.sort((o1, o2) -> o1.getNumero().compareTo(o2.getNumero()));

		List<NotaSaidaTO> listTO = map(list);

		listTO.forEach(notaSaidaTO -> notaSaidaTO.setCliente(obterClienteSubclienteNotaSaida(notaSaidaTO.getCodigo())));

		return listTO;
	}

	@Override
	public List<NotaSaidaTO> obterNotasDeFaturamentoNFMaterial(Date inicio, Date fim, String tipoLoteInterface) {
		LoteInterface.TipoLoteInterface tipoLote = LoteInterfaceFaturamentoServiceImpl.getTipoLoteByNome(tipoLoteInterface);

		QCentroFinanceiro qCentroFinanceiro =  QCentroFinanceiro.centroFinanceiro;
		QUnidade qUnidade = QUnidade.unidade;
		QFaturamentoEtapa qFaturamentoEtapa = QFaturamentoEtapa.faturamentoEtapa;
		QFaturamento qFaturamento = QFaturamento.faturamento;

		List<NotaSaida> list = select(
			Projections.fields(
				NotaSaida.class,
				qNotaSaida.codigo,
				qNotaSaida.numero,
				qNotaSaida.valor,
				Projections.fields(
					CentroFinanceiro.class,
					qNotaSaida.centroFinanceiro.codigo,
					qNotaSaida.centroFinanceiro.identificadorFinal,
					qNotaSaida.centroFinanceiro.identificador,
					qNotaSaida.centroFinanceiro.nome
				).as(qNotaSaida.centroFinanceiro)
			)
		)
			.join(qNotaSaida.centroFinanceiro, qCentroFinanceiro)
			.join(qNotaSaida.empresa, qUnidade)
			.join(qNotaSaida.faturamentoEtapas, qFaturamentoEtapa)
			.join(qFaturamentoEtapa.faturamento, qFaturamento)

			.where(qNotaSaida.dataEmissao.eq(inicio).or(qNotaSaida.dataEmissao.after(inicio))
				.and(qNotaSaida.dataEmissao.before(fim).or(qNotaSaida.dataEmissao.eq(fim)))
				.and(qNotaSaida.status.eq(Nota.StatusNota.A_PROCESSAR_FATURAMENTO))
				.and(qNotaSaida.tipoNotaFiscal.eq(NotaFiscal.TipoNotaFiscal.FATURAMENTO))
				.and(qFaturamento.tipoFornecimento.eq(Faturamento.TipoFornecimentoNF.MATERIAL))
			)
			.fetch();

		list.sort((o1, o2) -> o1.getNumero().compareTo(o2.getNumero()));

		List<NotaSaidaTO> listTO = map(list);

		listTO.forEach(notaSaidaTO -> notaSaidaTO.setCliente(obterClienteSubclienteNotaSaida(notaSaidaTO.getCodigo())));

		return listTO;
	}

	private PessoaTO obterClienteSubclienteNotaSaida(Long codigoNota) {
		FaturamentoEtapaTO faturamentoEtapaTO = faturamentoEtapaService.obterFaturamentoEtapaPorNotaSaida2(codigoNota);
		return faturamentoEtapaTO.getFaturamento().getCliente();
	}

	@Override
	public NotaSaidaTO getNotaSaidaByCodigo(Long codigo) {
		QPessoa qPessoa = QPessoa.pessoa;

		JPAQuery jpaQuery = select(
			Projections.fields(
				NotaSaida.class,
				qNotaSaida.codigo,
				qNotaSaida.numero,
				qNotaSaida.infoComplementarContribuinte,
				qNotaSaida.dataNumeracao,
				Projections.fields(
					Pessoa.class,
					qPessoa.codigo,
					qPessoa.nome
				).as("responsavelNumeracao")
			)
		);

		jpaQuery.leftJoin(qNotaSaida.responsavelNumeracao, qPessoa);
		jpaQuery.where(QNotaSaida.notaSaida.codigo.eq(codigo));

		NotaSaida notaSaida = (NotaSaida) jpaQuery.fetchOne();

		return map(notaSaida);
	}

	@Override
	public List<NotaSaidaTO> getNotaSaidasByCodigoFaturamento(Long codigoFaturamento) {
		return map(jpaQueryNotaSaidasByCodigoFaturamento(codigoFaturamento).fetch());
	}

	@Override
	public JPAQuery jpaQueryNotaSaidasByCodigoFaturamento(Long codigoFaturamento) {
		JPAQuery jpaQuery = select();
		jpaQuery
			.select(
				Projections.fields(NotaSaida.class,
					QNotaSaida.notaSaida.codigo,
					QNotaSaida.notaSaida.numero,
					QNotaSaida.notaSaida.status,
					QNotaSaida.notaSaida.dataEmissao,
					QNotaSaida.notaSaida.valor,
					QNotaSaida.notaSaida.issRetidoCliente)
			).where(QNotaSaida.notaSaida.codigo.in(JPAExpressions
			.selectFrom(QFaturamentoEtapa.faturamentoEtapa)
			.orderBy(QFaturamentoEtapa.faturamentoEtapa.numero.desc())
			.where(QFaturamentoEtapa.faturamentoEtapa.faturamento.codigo.eq(codigoFaturamento))
			.select(QFaturamentoEtapa.faturamentoEtapa.notaSaida.codigo))
		).orderBy(QNotaSaida.notaSaida.numero.desc());

		return jpaQuery;
	}

	@Override
	public Long obterNumerosFaturamento(String numero, Long codigoUnidade, Integer ano) {
		JPAQuery jpaQuery = select();

		BooleanBuilder booleanBuilder = new BooleanBuilder();
		booleanBuilder.and(QNotaSaida.notaSaida.numero.eq(numero));
		booleanBuilder.and(QNotaSaida.notaSaida.empresa.codigo.eq(codigoUnidade));
		booleanBuilder.and(QNotaSaida.notaSaida.tipoNotaFiscal.eq(NotaFiscal.TipoNotaFiscal.FATURAMENTO));

		if (ano != null) {
			booleanBuilder.and(QNotaSaida.notaSaida.dataEmissao.year().eq(ano));
		}

		jpaQuery
			.select(
				Projections.fields(NotaSaida.class, QNotaSaida.notaSaida.numero)
			).where(booleanBuilder).orderBy(QNotaSaida.notaSaida.codigo.asc());

		return jpaQuery.fetchCount();
	}

	@Override
	public Long obterNumerosFaturamento(String numero, Long codigoUnidade) {
		return obterNumerosFaturamento(numero, codigoUnidade, null);
	}

	@Override
	public boolean isIssRetidoCliente(Long codigoNota) {
		NotaSaida notaSaida = select(
			Projections.fields(NotaSaida.class,
				qNotaSaida.codigo,
				qNotaSaida.issRetidoCliente)
		).where(qNotaSaida.codigo.eq(codigoNota)).fetchOne();

		return notaSaida != null ? notaSaida.getIssRetidoCliente() : false;
	}

	@Override
	@Transactional
	public void updateCodigoAssociarNFSubstituta(Long codigo, Long codigoNotaAssociadaCancelamento) {
		selectUpdate()
			.set(qNotaSaida.notaAssociadaCancelamento.codigo, codigoNotaAssociadaCancelamento)
			.set(qNotaSaida.dataRespNotaAssociadaCancelamento, new Date())
			.set(qNotaSaida.dataAtualizacao, new Date())
			.set(qNotaSaida.atualizador, getUser().getUsername())
			.set(qNotaSaida.respNotaAssociadaCancelamento.codigo, securityService.getUser().getPessoaFisica().getCodigo())
			.where(qNotaSaida.codigo.eq(codigo)).execute();
	}

	@Override
	public void updateReverterCancelamentoERemoverCodigoAssociarNFSubstituta(Long codigo) {
		NotaSaida notaSaida = getRepository().findById(codigo).get();
		notaSaida.setNotaAssociadaCancelamento(null);
		notaSaida.setDataRespNotaAssociadaCancelamento(null);
		notaSaida.setRespNotaAssociadaCancelamento(null);
		getRepository().save(notaSaida);
	}

	@Override
	@Transactional()
	public void updateInfoComplementarContribuinte(Long codigo, String infoComplementarContribuinte) {
		selectUpdate()
			.where(qNotaSaida.codigo.eq(codigo))
			.set(qNotaSaida.infoComplementarContribuinte, infoComplementarContribuinte)
			.set(qNotaSaida.atualizador, securityService.getUsername())
			.set(qNotaSaida.dataAtualizacao, new Date())
			.execute();
	}

	@Transactional()
	private void updateStatus(Long codigo, Nota.StatusNota status) {
		selectUpdate()
			.where(qNotaSaida.codigo.eq(codigo))
			.set(qNotaSaida.status, status)
			.set(qNotaSaida.atualizador, securityService.getUsername())
			.set(qNotaSaida.dataAtualizacao, new Date())
			.execute();
	}

	@Override
	@Transactional()
	public void updateStatusAbertaFaturamento(Long codigo) {
		updateStatus(codigo, Nota.StatusNota.ABERTA_FATURAMENTO);
	}

	@Override
	@Transactional()
	public void updateStatusEmRecebimentoFaturamento(Long codigo) {
		updateStatus(codigo, Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO);
	}

	@Override
	public String getProtocoloEntrega(Long codigoNota) {
		NotaSaida notaSaida = select(
			Projections.fields(NotaSaida.class,
				qNotaSaida.protocoloEntrega)
		).where(qNotaSaida.codigo.eq(codigoNota)).fetchOne();

		return notaSaida != null ? notaSaida.getProtocoloEntrega() : null;
	}

	@Override
	public Boolean temNotaAbertaDoFaturamento(Long codigoFaturamento) {
		Long count = select(
			Projections.fields(NotaSaida.class,
				qNotaSaida.codigo)
		)
			.innerJoin(qNotaSaida.faturamentoEtapas, QFaturamentoEtapa.faturamentoEtapa)
			.innerJoin(QFaturamentoEtapa.faturamentoEtapa.faturamento, QFaturamento.faturamento)
			.where(QFaturamento.faturamento.codigo.eq(codigoFaturamento).and(qNotaSaida.status.eq(NotaSaida.StatusNota.ABERTA_FATURAMENTO)))
			.fetchCount();

		return count > 0;
	}

	@Override
	public void cancelamento(Long codigo) {

	}
}
