package br.com.araujoabreu.sgi.service.impl.loteInterfaceFaturamento;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiro;
import br.com.araujoabreu.sgi.compras.domain.*;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterface;
import br.com.araujoabreu.sgi.contabilidade.domain.LoteInterfaceNota;
import br.com.araujoabreu.sgi.contabilidade.domain.QLoteInterface;
import br.com.araujoabreu.sgi.contabilidade.domain.QLoteInterfaceNota;
import br.com.araujoabreu.sgi.service.compras.NotaSaidaService;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.LoteInterfaceNotaService;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfaceNotaPesquisaTO;
import br.com.araujoabreu.sgi.service.loteInterfaceFaturamento.transfer.LoteInterfaceNotaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaFiscalTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaParcelaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaSaidaTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.corporativo.pessoa.domain.Pessoa;
import br.com.fasolti.sgi.repository.loteInterface.LoteInterfaceRepository;
import br.com.fasolti.sgi.repository.loteInterfaceFaturamento.LoteInterfaceNotaRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaParcelaRepository;
import br.com.fasolti.sgi.repository.notaSaida.NotaSaidaRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoteInterfaceNotaServiceImpl extends BaseSearchService<LoteInterfaceNota, LoteInterfaceNotaTO, LoteInterfaceNotaPesquisaTO, LoteInterfaceNotaRepository> implements LoteInterfaceNotaService {

	@Autowired
	public LoteInterfaceNotaServiceImpl(LoteInterfaceNotaRepository repository) {
		super(repository);
	}

	@Autowired
	public NotaSaidaService notaSaidaService;

	@Autowired
	public LoteInterfaceRepository loteInterfaceRepository;

	@Autowired
	public NotaSaidaRepository notaSaidaRepository;

	@Autowired
	public NotaSaidaParcelaRepository notaSaidaParcelaRepository;

	@Override
	public List<LoteInterfaceNotaTO> findNotaByCodigoLote(Long codigo) {
		return map(findNotaByCodigoLoteReturnJPAQuery(codigo).fetch());
	}

	@Override
	public JPAQuery findNotaByCodigoLoteReturnJPAQuery(Long codigo) {
		return select(
			Projections.fields(LoteInterfaceNota.class,
				QLoteInterfaceNota.loteInterfaceNota.codigo,
				QLoteInterfaceNota.loteInterfaceNota.tipo,
				QLoteInterfaceNota.loteInterfaceNota.ordem,
				Projections.fields(LoteInterface.class,
					QLoteInterfaceNota.loteInterfaceNota.loteInterface.codigo
				).as(QLoteInterfaceNota.loteInterfaceNota.loteInterface),
				Projections.fields(Nota.class,
					QLoteInterfaceNota.loteInterfaceNota.nota.codigo,
					QLoteInterfaceNota.loteInterfaceNota.nota.numero,
					QLoteInterfaceNota.loteInterfaceNota.nota.valor,
					Projections.fields(CentroFinanceiro.class,
						QLoteInterfaceNota.loteInterfaceNota.nota.centroFinanceiro.codigo,
						QLoteInterfaceNota.loteInterfaceNota.nota.centroFinanceiro.identificador,
						QLoteInterfaceNota.loteInterfaceNota.nota.centroFinanceiro.identificadorFinal,
						QLoteInterfaceNota.loteInterfaceNota.nota.centroFinanceiro.nome
					).as(QLoteInterfaceNota.loteInterfaceNota.nota.centroFinanceiro)
				).as(QLoteInterfaceNota.loteInterfaceNota.nota)
			)
		)
			.where(QLoteInterfaceNota.loteInterfaceNota.loteInterface.codigo.eq(codigo))
			.orderBy(QLoteInterfaceNota.loteInterfaceNota.nota.numero.asc());
	}

	@Override
	public List<LoteInterfaceNotaTO> findListaByCodNota(Long codigo) {
		QLoteInterfaceNota qLoteInterfaceNota = QLoteInterfaceNota.loteInterfaceNota;
		QLoteInterface qLoteInterface = QLoteInterface.loteInterface;
		QNota qNota = QNota.nota;

		JPAQuery jpaQuery = select();

		jpaQuery.from(qLoteInterfaceNota);
		jpaQuery.join(qLoteInterfaceNota.loteInterface, qLoteInterface).join(qLoteInterfaceNota.nota, qNota);

		jpaQuery.select(
			Projections.fields(
				LoteInterfaceNota.class,
				qLoteInterfaceNota.codigo,
				qLoteInterfaceNota.tipo,
				Projections.fields(
					LoteInterface.class,
					qLoteInterface.codigo
				).as("loteInterface")
			)
		);

		jpaQuery.where(qLoteInterfaceNota.nota.codigo.eq(codigo));

		List<LoteInterfaceNota> lista = jpaQuery.fetch();
		List<LoteInterfaceNotaTO> listaTO = new ArrayList<>();

		for (LoteInterfaceNota loteInterfaceNota : lista) {
			listaTO.add(getMapper().map(loteInterfaceNota, LoteInterfaceNotaTO.class));
		}

		return listaTO;
	}

	@Override
	public List<LoteInterfaceNotaTO> findByCodNotaTipo(Long codigo, Long codTipo) {

		QLoteInterfaceNota qLoteInterfaceNota = QLoteInterfaceNota.loteInterfaceNota;

		JPAQuery jpaQuery = select();

		QBean qBean = Projections.bean(
			LoteInterfaceNota.class,
			qLoteInterfaceNota.codigo,
			qLoteInterfaceNota.tipo
		);

		jpaQuery
			.from(qLoteInterfaceNota)
			.where(qLoteInterfaceNota.nota.codigo.eq(codigo).and(qLoteInterfaceNota.tipo.eq(getTipoByCodigo(codTipo))));

		List<LoteInterfaceNota> lista = jpaQuery.fetch();
		List<LoteInterfaceNotaTO> listaTO = new ArrayList<>();

		for (LoteInterfaceNota loteInterfaceNota : lista) {
			listaTO.add(getMapper().map(loteInterfaceNota, LoteInterfaceNotaTO.class));
		}

		return listaTO;
	}

	private LoteInterfaceNota.Tipo getTipoByCodigo(Long codigo) {
		LoteInterfaceNota.Tipo tipo = null;

		if (codigo == 0L) {
			tipo = LoteInterfaceNota.Tipo.ENVIADA;
		}
		if (codigo == 1L) {
			tipo = LoteInterfaceNota.Tipo.CANCELADA;
		}
		if (codigo == 2L) {
			tipo = LoteInterfaceNota.Tipo.PORTAL_OI;
		}

		return tipo;
	}

	@Override
	@Transactional
	public void atualizarNfEmRecebimentoFaturamento(NotaFiscalTO nfTO) {
		NotaSaidaTO notaSaidaTO = notaSaidaService.getNotaSaidaByCodigo(nfTO.getCodigo());
		notaSaidaRepository.updateStatus(nfTO.getCodigo(), Nota.StatusNota.EM_RECEBIMENTO_FATURAMENTO);
		List<NotaSaidaParcela> parcelas = notaSaidaParcelaRepository.findByNotaSaidaCodigo(nfTO.getCodigo());

		if (notaSaidaTO != null &&parcelas != null && !parcelas.isEmpty()) {
			for (NotaSaidaParcela parcela : parcelas) {
				if (!NotaSaidaParcela.SituacaoParcela.CANCELADA.equals(parcela.getSituacao())
					&& !NotaSaidaParcela.SituacaoParcela.FINALIZADA.equals(parcela.getSituacao())) {
					notaSaidaParcelaRepository.updateStatus(parcela.getCodigo(), NotaSaidaParcela.SituacaoParcela.EM_RECEBIMENTO);
				}
			}
		}
	}

	@Transactional
	public void reprocessarNfEmRecebimentoFaturamento(NotaFiscalTO nfTO) {
		NotaSaidaTO notaSaidaTO = notaSaidaService.getNotaSaidaByCodigo(nfTO.getCodigo());
		notaSaidaRepository.updateStatus(nfTO.getCodigo(), Nota.StatusNota.A_PROCESSAR_FATURAMENTO);
		List<NotaSaidaParcela> parcelas = notaSaidaParcelaRepository.findByNotaSaidaCodigo(nfTO.getCodigo());

		if (notaSaidaTO != null &&parcelas != null && !parcelas.isEmpty()) {
			for (NotaSaidaParcela parcela : parcelas) {
				if (!NotaSaidaParcela.SituacaoParcela.CANCELADA.equals(parcela.getSituacao())
					&& !NotaSaidaParcela.SituacaoParcela.FINALIZADA.equals(parcela.getSituacao())) {
					notaSaidaParcelaRepository.updateStatus(parcela.getCodigo(), NotaSaidaParcela.SituacaoParcela.ABERTA);
				}
			}
		}
	}
}
