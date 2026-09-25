package br.com.araujoabreu.sgi.service.impl.nota;

import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.Unidade;
import br.com.araujoabreu.sgi.compras.domain.*;
import br.com.araujoabreu.sgi.service.nota.NotaFiscalService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalTO;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaFisica;
import br.com.fasolti.corporativo.security.domain.QUsuario;
import br.com.fasolti.corporativo.security.domain.Usuario;
import br.com.fasolti.sgi.repository.compras.NotaFiscalRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class NotaFiscalServiceImpl extends BaseSearchService<NotaFiscal, NotaFiscalTO, NotaFiscalPesquisaTO, NotaFiscalRepository> implements NotaFiscalService {

	private final QNotaFiscal qNotaFiscal = QNotaFiscal.notaFiscal;
	@Autowired
	private SecurityService<Usuario> securityService;

	@Autowired
	public NotaFiscalServiceImpl(NotaFiscalRepository repository) {
		super(repository);
	}

	@Override
	public NotaFiscalTO buscarNotaFiscalById(Long id) {

		JPAQuery jpaQuery = select();

		QBean qBean = Projections.bean(
			NotaFiscal.class,
			qNotaFiscal.codigo,
			qNotaFiscal.tipoFornecimento,
			qNotaFiscal.dataEmissao,
			Projections.bean(
				Unidade.class,
				qNotaFiscal.empresa.codigo
			).as(qNotaFiscal.empresa)
		);

		jpaQuery
			.select(qBean)
			.from(qNotaFiscal)
			.where(qNotaFiscal.codigo.eq(id));

		NotaFiscal notaFiscal = (NotaFiscal) jpaQuery.fetchOne();

		return map(notaFiscal);
	}

	private void updateTipoNotaFiscal(Long codigo, NotaFiscal.TipoNotaFiscal tipoNotaFiscal) {
		selectUpdate().where(qNotaFiscal.codigo.eq(codigo)).set(qNotaFiscal.tipoNotaFiscal, tipoNotaFiscal).execute();
	}

	@Override
	public void updateTipoNotaFiscalAvulsa(Long codigo) {
		updateTipoNotaFiscal(codigo, NotaFiscal.TipoNotaFiscal.AVULSA);
	}

	@Override
	public void updateTipoNotaFiscalNormal(Long codigo) {
		updateTipoNotaFiscal(codigo, NotaFiscal.TipoNotaFiscal.NORMAL);
	}

	@Override
	public void updateTipoNotaFiscalFundoFixo(Long codigo) {
		updateTipoNotaFiscal(codigo, NotaFiscal.TipoNotaFiscal.FUNDO_FIXO);
	}

	@Override
	public void updateTipoNotaFiscalReembolso(Long codigo) {
		updateTipoNotaFiscal(codigo, NotaFiscal.TipoNotaFiscal.REEMBOLSO);
	}

	@Override
	public void updateTipoNotaFiscalTransporte(Long codigo) {
		updateTipoNotaFiscal(codigo, NotaFiscal.TipoNotaFiscal.TRANSPORTE);
	}

	@Override
	public void updateTipoNotaFiscalAdiantamento(Long codigo) {
		updateTipoNotaFiscal(codigo, NotaFiscal.TipoNotaFiscal.ADIANTAMENTO);
	}

	@Override
	public void updateTipoNotaFiscalDevolucaoPartical(Long codigo) {
		updateTipoNotaFiscal(codigo, NotaFiscal.TipoNotaFiscal.DEVOLUCAO_PARCIAL);
	}

	@Override
	public void updateTipoNotaFiscalDevolucaoTotal(Long codigo) {
		updateTipoNotaFiscal(codigo, NotaFiscal.TipoNotaFiscal.DEVOLUCAO_TOTAL);
	}

	@Override
	public void updateTipoNotaFiscalOcPcFundoFixo(Long codigo) {
		updateTipoNotaFiscal(codigo, NotaFiscal.TipoNotaFiscal.OC_PC_FUNDO_FIXO);
	}

	@Override
	public void updateTipoNotaFiscalOcPcReembolso(Long codigo) {
		updateTipoNotaFiscal(codigo, NotaFiscal.TipoNotaFiscal.OC_PC_REEMBOLSO);
	}

	@Override
	public void updateTipoNotaFiscalOcPcAdiantamento(Long codigo) {
		updateTipoNotaFiscal(codigo, NotaFiscal.TipoNotaFiscal.OC_PC_ADIANTAMENTO);
	}

	@Override
	public void updateTipoNotaFiscalFaturamento(Long codigo) {
		updateTipoNotaFiscal(codigo, NotaFiscal.TipoNotaFiscal.FATURAMENTO);
	}

	@Override
	public void updateTipoNotaFiscalFaturamentoExclusaoLogica(Long codigo) {
		updateTipoNotaFiscal(codigo, NotaFiscal.TipoNotaFiscal.FATURAMENTO_EXCLUSAO_LOGICA);
	}

	@Override
	@Transactional
	public void cancelamento(Long codigo) {
		selectUpdate()
			.where(qNotaFiscal.codigo.eq(codigo))
			.set(qNotaFiscal.responsavelCancelamento, securityService.getUser().getPessoaFisica())
			.set(qNotaFiscal.dataCancelamento, new Date())
			.set(qNotaFiscal.atualizador, securityService.getUsername())
			.set(qNotaFiscal.dataAtualizacao, new Date())
			.set(qNotaFiscal.tipoNotaFiscal, NotaFiscal.TipoNotaFiscal.FATURAMENTO_EXCLUSAO_LOGICA)
			.execute();
	}

	@Override
	public List<Tuple> listarNFsPorData(Date date) {
		QNotaFiscal qNotaFiscal = QNotaFiscal.notaFiscal;
		QPessoa qPessoa = QPessoa.pessoa;
		QPessoaFisica qPessoaFisica = QPessoaFisica.pessoaFisica;
		QUsuario qUsuario = QUsuario.usuario;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
		QItemNotaFiscal qItemNotaFiscal = QItemNotaFiscal.itemNotaFiscal;
		QRateioNotaFiscal qRateioNotaFiscal = QRateioNotaFiscal.rateioNotaFiscal;
		QRateioItemRecebimento qRateioItemRecebimento = QRateioItemRecebimento.rateioItemRecebimento;
		QRecebimento qRecebimento =  QRecebimento.recebimento;
		QItemRecebimento qItemRecebimento = QItemRecebimento.itemRecebimento;


		JPAQuery jpaQuery = getJPAQueryFactory().from(qNotaFiscal).distinct();

		jpaQuery.join(qNotaFiscal.credor, qPessoa);
		jpaQuery.join(qUsuario).on(qNotaFiscal.criador.eq(qUsuario.login));
		jpaQuery.join(qUsuario.pessoaFisica, qPessoaFisica);
		jpaQuery.join(qNotaFiscal.items, qItemNotaFiscal);
		jpaQuery.join(qItemNotaFiscal.rateios, qRateioNotaFiscal);
		jpaQuery.join(qRateioNotaFiscal.centroFinanceiro, qCentroFinanceiro);

		jpaQuery.select(
			qNotaFiscal.codigo,
			qNotaFiscal.valor,
			qNotaFiscal.numero,
			qPessoa.documento,
			qPessoa.nome,
			qUsuario.pessoaFisica.nome,
			qNotaFiscal.dataCriacao,
			qCentroFinanceiro.identificador
		);

		DateExpression<Date> dtCriacao = Expressions.dateTemplate(Date.class, "date({0})", qNotaFiscal.dataCriacao);
		DateExpression<Date> dtParametro = Expressions.dateTemplate(Date.class, "date({0})", date);

		jpaQuery.where(
			dtCriacao.eq(dtParametro)
		);
		jpaQuery.orderBy(qNotaFiscal.numero.asc());

		List<Tuple> nfs = jpaQuery.fetch();

		JPAQuery jpaQueryNfOC = getJPAQueryFactory().from(qNotaFiscal).distinct();

		jpaQueryNfOC.join(qNotaFiscal.credor, qPessoa);
		jpaQueryNfOC.join(qUsuario).on(qNotaFiscal.criador.eq(qUsuario.login));
		jpaQueryNfOC.join(qUsuario.pessoaFisica, qPessoaFisica);
		jpaQueryNfOC.join(qNotaFiscal.recebimentos, qRecebimento);
		jpaQueryNfOC.join(qRecebimento.itens, qItemRecebimento);
		jpaQueryNfOC.join(qItemRecebimento.rateios, qRateioItemRecebimento);
		jpaQueryNfOC.join(qRateioItemRecebimento.centroFinanceiro, qCentroFinanceiro);

		jpaQueryNfOC.select(
			qNotaFiscal.codigo,
			qNotaFiscal.valor,
			qNotaFiscal.numero,
			qPessoa.documento,
			qPessoa.nome,
			qUsuario.pessoaFisica.nome,
			qNotaFiscal.dataCriacao,
			qCentroFinanceiro.identificador
		);

		jpaQueryNfOC.where(
			dtCriacao.eq(dtParametro)
		);
		jpaQueryNfOC.orderBy(qNotaFiscal.numero.asc(), qPessoa.nome.asc());

		nfs.addAll(jpaQueryNfOC.fetch());

		return nfs;

	}

}





