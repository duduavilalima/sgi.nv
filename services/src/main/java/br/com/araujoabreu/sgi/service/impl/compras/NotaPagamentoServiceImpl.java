package br.com.araujoabreu.sgi.service.impl.compras;

import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiro;
import br.com.araujoabreu.sgi.compras.domain.NotaPagamento;
import br.com.araujoabreu.sgi.compras.domain.QNotaPagamento;
import br.com.araujoabreu.sgi.contasApagar.domain.QRateioNotaPagamento;
import br.com.araujoabreu.sgi.service.administracao.transfer.PessoaTO;
import br.com.araujoabreu.sgi.service.compras.NotaPagamentoService;
import br.com.araujoabreu.sgi.service.compras.transfer.NotaPagamentoPesquisaTO;
import br.com.araujoabreu.sgi.service.compras.transfer.NotaPagamentoTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.QPessoa;
import br.com.fasolti.corporativo.pessoa.domain.QPessoaFisica;
import br.com.fasolti.corporativo.security.domain.QUsuario;
import br.com.fasolti.sgi.repository.notaPagamento.NotaPagamentoRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class NotaPagamentoServiceImpl extends BaseSearchService<NotaPagamento, NotaPagamentoTO, NotaPagamentoPesquisaTO, NotaPagamentoRepository> implements NotaPagamentoService {

	@Autowired
	public NotaPagamentoServiceImpl(NotaPagamentoRepository repository) {
		super(repository);
	}

	@Override
	public BigDecimal obterValorTibutoDeNotaPagamentoPeloNumero(String numero) {
		StringTokenizer st = new StringTokenizer(numero, "-");
		String strNumero = (String)st.nextElement();
		JPAQuery<NotaPagamento> jpaQuery = select(Projections.fields(NotaPagamento.class, QNotaPagamento.notaPagamento.codigo, QNotaPagamento.notaPagamento.valorTributo));
		jpaQuery.where(QNotaPagamento.notaPagamento.numero.eq(strNumero));
		NotaPagamento notaPagamento = jpaQuery.fetchOne();
		return notaPagamento.getValorTributo();
	}

	@Override
	public List<Tuple> listarNPsPorData(Date date) {

		QNotaPagamento qNotaPagamento = QNotaPagamento.notaPagamento;
		QPessoa qPessoa = QPessoa.pessoa;
		QPessoaFisica qPessoaFisica = QPessoaFisica.pessoaFisica;
		QUsuario qUsuario = QUsuario.usuario;
		QRateioNotaPagamento qRateioNotaPagamento = QRateioNotaPagamento.rateioNotaPagamento;
		QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;

		JPAQuery jpaQuery = getJPAQueryFactory().from(qNotaPagamento).distinct();

		jpaQuery.join(qNotaPagamento.credor, qPessoa);
		jpaQuery.join(qUsuario).on(qNotaPagamento.criador.eq(qUsuario.login));
		jpaQuery.join(qUsuario.pessoaFisica, qPessoaFisica);
		jpaQuery.join(qNotaPagamento.rateios, qRateioNotaPagamento);
		jpaQuery.join(qRateioNotaPagamento.centroFinanceiro, qCentroFinanceiro);

		jpaQuery.select(
			qNotaPagamento.codigo,
			qNotaPagamento.valor,
			qNotaPagamento.numero,
			qPessoa.documento,
			qPessoa.nome,
			qUsuario.pessoaFisica.nome,
			qNotaPagamento.dataCriacao,
			qCentroFinanceiro.identificador
		);

		DateExpression<Date> dtCriacao = Expressions.dateTemplate(Date.class, "date({0})", qNotaPagamento.dataCriacao);
		DateExpression<Date> dtParametro = Expressions.dateTemplate(Date.class, "date({0})", date);

		jpaQuery.where(
			dtCriacao.eq(dtParametro)
		);

		jpaQuery.orderBy(qNotaPagamento.numero.asc());

		return jpaQuery.fetch();
	}

}
