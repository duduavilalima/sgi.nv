package br.com.araujoabreu.sgi.service.impl.motivoDescontoDevolucao;

import br.com.araujoabreu.sgi.compras.domain.MotivoDescontoDevolucao;
import br.com.araujoabreu.sgi.compras.domain.MotivoDescontoDevolucaoContaContabil;
import br.com.araujoabreu.sgi.compras.domain.QMotivoDescontoDevolucao;
import br.com.araujoabreu.sgi.compras.domain.QMotivoDescontoDevolucaoContaContabil;
import br.com.araujoabreu.sgi.compras.domain.QNotaFiscal;
import br.com.araujoabreu.sgi.contabilidade.domain.QContaContabil;
import br.com.araujoabreu.sgi.service.contaContabil.ContaContabilService;
import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.MotivoDescontoDevolucaoContaContabilService;
import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.transfer.MotivoDescontoDevolucaoContaContabilPesquisaTO;
import br.com.araujoabreu.sgi.service.motivo_desconto_devolucao.transfer.MotivoDescontoDevolucaoContaContabilTO;
import br.com.araujoabreu.sgi.service.nota.NotaFiscalService;
import br.com.araujoabreu.sgi.service.unidade.UnidadeService;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.motivoDescontoDevolucao.MotivoDescontoDevolucaoContaContabilRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MotivoDescontoDevolucaoContaContabilServiceImpl extends BaseSearchService<MotivoDescontoDevolucaoContaContabil, MotivoDescontoDevolucaoContaContabilTO, MotivoDescontoDevolucaoContaContabilPesquisaTO, MotivoDescontoDevolucaoContaContabilRepository> implements MotivoDescontoDevolucaoContaContabilService {

	private static final QMotivoDescontoDevolucaoContaContabil query = QMotivoDescontoDevolucaoContaContabil.motivoDescontoDevolucaoContaContabil;

	@Autowired
	private NotaFiscalService notaFiscalService;

	@Autowired
	private UnidadeService unidadeService;

	@Autowired
	private ContaContabilService contaContabilService;

	@Autowired
	public MotivoDescontoDevolucaoContaContabilServiceImpl(MotivoDescontoDevolucaoContaContabilRepository repository) {
		super(repository);
	}

	/**
	 * @param codigoNota
	 * @return
	 */
	@Override
	public List<MotivoDescontoDevolucaoContaContabilTO> listarDescontoPorMatrizNF(Long codigoNota) {
		return listarDevolucaoPorMatrizNfETipo(codigoNota, MotivoDescontoDevolucaoContaContabil.Tipo.DESCONTO);
	}

	/**
	 * @param codigoNota
	 * @return
	 */
	@Override
	public List<MotivoDescontoDevolucaoContaContabilTO> listarDevolucaoPorMatrizNF(Long codigoNota) {
		return listarDevolucaoPorMatrizNfETipo(codigoNota, MotivoDescontoDevolucaoContaContabil.Tipo.DEVOLUCAO);
	}

	private List<MotivoDescontoDevolucaoContaContabilTO> listarDevolucaoPorMatrizNfETipo(Long codigoNota, MotivoDescontoDevolucaoContaContabil.Tipo tipo) {
		QMotivoDescontoDevolucao qMotivoDescontoDevolucao = QMotivoDescontoDevolucao.motivoDescontoDevolucao;

		QBean qBean = Projections.bean(
			MotivoDescontoDevolucaoContaContabil.class,
			query.codigo,
			Projections.bean(
				MotivoDescontoDevolucao.class,
				qMotivoDescontoDevolucao.codigo
			).as("motivoDescontoDevolucao")
		);

		JPAQuery<MotivoDescontoDevolucaoContaContabil> jpaQuery = select();

		QContaContabil qContaContabil = QContaContabil.contaContabil;
		QNotaFiscal qNotaFiscal = QNotaFiscal.notaFiscal;

		jpaQuery
			.select(qBean)
			.from(query).join(query.contaContabil, qContaContabil).join(query.motivoDescontoDevolucao, qMotivoDescontoDevolucao)
			.where(
				query.tipo.eq(tipo).or(query.tipo.eq(MotivoDescontoDevolucaoContaContabil.Tipo.DESCONTO_DEVOLUCAO))
					.and(
						qContaContabil.codigo.in(
							(JPAExpressions.selectFrom(qContaContabil).where(qContaContabil.empresa.codigo.eq(
								JPAExpressions.selectFrom(qNotaFiscal).where(qNotaFiscal.codigo.eq(codigoNota)).select(qNotaFiscal.empresa.matriz.codigo)
							)).select(qContaContabil.codigo))
						)
					)
			);

		List<MotivoDescontoDevolucaoContaContabil> lista = jpaQuery.fetch();
		List<MotivoDescontoDevolucaoContaContabilTO> listaTO = new ArrayList<>();

		for (MotivoDescontoDevolucaoContaContabil mddcc : lista) {
			MotivoDescontoDevolucaoContaContabilTO to = getMapper().map(mddcc, MotivoDescontoDevolucaoContaContabilTO.class);
			listaTO.add(to);
		}

		return listaTO;
	}

}
