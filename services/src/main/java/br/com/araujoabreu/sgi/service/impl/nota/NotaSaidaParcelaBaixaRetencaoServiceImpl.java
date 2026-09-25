package br.com.araujoabreu.sgi.service.impl.nota;

import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcela;
import br.com.araujoabreu.sgi.compras.domain.NotaSaidaParcelaBaixaRetencao;
import br.com.araujoabreu.sgi.compras.domain.QNotaSaidaParcela;
import br.com.araujoabreu.sgi.compras.domain.QNotaSaidaParcelaBaixaRetencao;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface;
import br.com.araujoabreu.sgi.contabilidade.domain.QLoteInterface;
import br.com.araujoabreu.sgi.service.administracao.transfer.ValueTO;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaBaixaRetencaoService;
import br.com.araujoabreu.sgi.service.nota.NotaSaidaParcelaService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaBaixaRetencaoTO;
import br.com.araujoabreu.sgi.tesouraria.domain.ContaCorrente;
import br.com.araujoabreu.sgi.tesouraria.domain.QContaCorrente;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Banco;
import br.com.fasolti.corporativo.pessoa.domain.PessoaFisica;
import br.com.fasolti.corporativo.pessoa.domain.QBanco;
import br.com.fasolti.sgi.repository.administracao.PessoaFisicaRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaBaixaRetencaoRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotaSaidaParcelaBaixaRetencaoServiceImpl extends BaseSearchService<NotaSaidaParcelaBaixaRetencao, NotaSaidaParcelaBaixaRetencaoTO, NotaSaidaParcelaBaixaRetencaoTO, NotaSaidaParcelaBaixaRetencaoRepository> implements NotaSaidaParcelaBaixaRetencaoService {

	private static final QNotaSaidaParcelaBaixaRetencao qNotaSaidaParcelaBaixaRetencao = QNotaSaidaParcelaBaixaRetencao.notaSaidaParcelaBaixaRetencao;
	private static final QNotaSaidaParcela qNotaSaidaParcela = QNotaSaidaParcela.notaSaidaParcela;
	private static final QContaCorrente qContaCorrente = QContaCorrente.contaCorrente;
	private static final QBanco qBanco = QBanco.banco;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private NotaSaidaParcelaService notaSaidaParcelaService;

	@Autowired
	public NotaSaidaParcelaBaixaRetencaoServiceImpl(NotaSaidaParcelaBaixaRetencaoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<NotaSaidaParcelaBaixaRetencao> jpaQuery, NotaSaidaParcelaBaixaRetencaoTO filter) {
		jpaQuery.join(qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela, qNotaSaidaParcela);
		jpaQuery.join(qNotaSaidaParcelaBaixaRetencao.contaCorrente, qContaCorrente);
		jpaQuery.join(qContaCorrente.banco, qBanco);

		jpaQuery.select(createQBean());

		jpaQuery.orderBy(qNotaSaidaParcelaBaixaRetencao.dataContabilizacao.asc(),
			qNotaSaidaParcelaBaixaRetencao.dataPagamento.asc(),
			qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.ordem.asc());
	}

	protected QBean<NotaSaidaParcelaBaixaRetencao> createQBean() {
		return Projections.fields(NotaSaidaParcelaBaixaRetencao.class,
			qNotaSaidaParcelaBaixaRetencao.codigo,
			qNotaSaidaParcelaBaixaRetencao.dataPagamento,
			qNotaSaidaParcelaBaixaRetencao.dataContabilizacao,
			qNotaSaidaParcelaBaixaRetencao.valorBaixa,
			qNotaSaidaParcelaBaixaRetencao.valorJuros,
			qNotaSaidaParcelaBaixaRetencao.criador,
			qNotaSaidaParcelaBaixaRetencao.dataCriacao,
			Projections.fields(NotaSaidaParcela.class,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.codigo,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.situacao,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.dataPagamento,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.dataVencimento,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.ordem,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.valorBruto,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.valorLiquido,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.valorDesconto,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.valorJuros,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.valorMulta,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.valorPermuta,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.valorDevolucao,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.dataContabilizacao,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.saldoRetencao,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.valorBaixaRetencao,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.valorRetencaoRetida,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.valorTributo,
				qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.valorRetencaoPrevista
			)
				.as(qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela),
			Projections.fields(ContaCorrente.class,
				qContaCorrente.codigo,
				qContaCorrente.agencia,
				qContaCorrente.conta,
				qContaCorrente.digito,
				qContaCorrente.digitoAgencia,
				qContaCorrente.tipoContaCorrente,
				qContaCorrente.conciliacao,
				qContaCorrente.internetBanking,
				qContaCorrente.boletoFaturamento,
				qContaCorrente.homologado,
				qContaCorrente.principal,
				qContaCorrente.tipoHomologacao,
				qContaCorrente.situacao,
				Projections.fields(Banco.class,
					qBanco.codigo,
					qBanco.nome,
					qBanco.descricao)
					.as(qContaCorrente.banco)
			)
				.as(qNotaSaidaParcelaBaixaRetencao.contaCorrente)
		);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, NotaSaidaParcelaBaixaRetencaoTO exampleObj) {

	}

	@Override
	protected void doAfterLoad(NotaSaidaParcelaBaixaRetencaoTO to) {
		to.setCriador(nomeUsuario(to.getCriador()));
	}

	@Override
	protected void doBeforeSave(NotaSaidaParcelaBaixaRetencao entity, NotaSaidaParcelaBaixaRetencaoTO object) throws ServiceException {
		entity.setCriador(getUser().getUsername());
		entity.setDataCriacao(new Date());
	}

	@Override
	@Transactional
	protected void doAfterSave(NotaSaidaParcelaBaixaRetencao entity, NotaSaidaParcelaBaixaRetencaoTO object) throws ServiceException {
		atulizarParcela(object.getNotaSaidaParcela().getCodigo(), object.getValorBaixa(), object.getValorJuros());
	}

	@Override
	@Transactional
	protected void doAfterUpdate(NotaSaidaParcelaBaixaRetencao entity, NotaSaidaParcelaBaixaRetencaoTO object) throws ServiceException {
		atulizarParcela(object.getNotaSaidaParcela().getCodigo(), object.getValorBaixa(), object.getValorJuros());
	}

	private void atulizarParcela(long codigoParcela, BigDecimal valorBaixa, BigDecimal valorJuros) {
		notaSaidaParcelaService.atualizarBaixaRetencaoValor(codigoParcela, valorBaixa, valorJuros);
	}

	private String nomeUsuario(String usarname) {
		List<PessoaFisica> list = pessoaFisicaRepository.findByUsuarioLogin(usarname);
		if (list != null && !list.isEmpty()) {
			return list.get(0).getNome();
		}
		return usarname;
	}

	@Override
	public List<NotaSaidaParcelaBaixaRetencaoTO> obterBaixasRetencaoDaNotaSaidaParcela(long codigNotaSaidaParcela) {
		List<NotaSaidaParcelaBaixaRetencaoTO> listTO = map(select(createQBean())
			.innerJoin(qNotaSaidaParcelaBaixaRetencao.contaCorrente, qContaCorrente)
			.innerJoin(qContaCorrente.banco, qBanco)
			.where(qNotaSaidaParcelaBaixaRetencao.notaSaidaParcela.codigo.eq(codigNotaSaidaParcela))
			.orderBy(qNotaSaidaParcelaBaixaRetencao.dataPagamento.desc(), qNotaSaidaParcelaBaixaRetencao.dataContabilizacao.desc())
			.fetch());
		listTO.forEach(to -> to.setCriador(nomeUsuario(to.getCriador())));
		return listTO;
	}

	@Override
	@Transactional
	public void removerBaixaRetencao(long codigoNotaSaidaBaixaRetencao) throws ValidationException {
		Optional<NotaSaidaParcelaBaixaRetencao> notaSaidaParcelaBaixaRetencao = getRepository().findById(codigoNotaSaidaBaixaRetencao);
		if (!notaSaidaParcelaBaixaRetencao.isPresent()) {
			throw new ValidationException("BAIXARETENCAO", "Baixa retenção não existe");
		}
		BigDecimal valorJuros = notaSaidaParcelaBaixaRetencao.get().getValorJuros() == null ? BigDecimal.ZERO : notaSaidaParcelaBaixaRetencao.get().getValorJuros();
		notaSaidaParcelaService.atualizarBaixaRetencaoRemovidaValor(
			notaSaidaParcelaBaixaRetencao.get().getNotaSaidaParcela().getCodigo(),
			notaSaidaParcelaBaixaRetencao.get().getValorBaixa().add(valorJuros));
		selectDelete().where(qNotaSaidaParcelaBaixaRetencao.codigo.eq(codigoNotaSaidaBaixaRetencao)).execute();
	}

	@Override
	public ValueTO verificarLoteBaixas(long codigoBaixa) throws ValidationException {
		NotaSaidaParcelaBaixaRetencao notaSaidaParcelaBaixaRetencao = getRepository().findById(codigoBaixa).orElse(null);

		NotaSaidaParcela notaSaidaParcela = (NotaSaidaParcela) notaSaidaParcelaService.findNotaSaidaByCodigoNotaSaidaParcela(notaSaidaParcelaBaixaRetencao.getNotaSaidaParcela().getCodigo()).fetchOne();

		if (notaSaidaParcelaBaixaRetencao == null) {
			throw new ValidationException("BAIXARETENCAO", "Baixa não existe");
		}

		QBean<LoteInterface> qBean = Projections.bean(LoteInterface.class, QLoteInterface.loteInterface.codigo);

		JPAQuery<LoteInterface> jpaQueryLoteInterface = getJPAQueryFactory().selectFrom(QLoteInterface.loteInterface);

		jpaQueryLoteInterface
			.select(qBean)
			.from(QLoteInterface.loteInterface)
			.where(
				QLoteInterface.loteInterface.dataEvento.eq(notaSaidaParcelaBaixaRetencao.getDataContabilizacao())
					.and(QLoteInterface.loteInterface.tipoLoteInterface.eq(LoteInterface.TipoLoteInterface.Baixas_diarias))
				.and(QLoteInterface.loteInterface.unidade.codigo.eq(notaSaidaParcela.getNotaSaida().getEmpresa().getCodigo()))
			);

		ValueTO valueTO;
		if (jpaQueryLoteInterface.fetchCount() > 0) {
			valueTO = ValueTO.Builder.newInstance().value(true).build();
		} else {
			valueTO = ValueTO.Builder.newInstance().value(false).build();
		}

		return valueTO;
	}

}
