package br.com.araujoabreu.sgi.service.impl.centroFinanceiro;

import br.com.araujoabreu.sgi.administracao.domain.CentroFinanceiroAliquota;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiro;
import br.com.araujoabreu.sgi.administracao.domain.QCentroFinanceiroAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.CategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.QAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.QCategoriaTributo;
import br.com.araujoabreu.sgi.service.centroFinanceiro.CentroFinanceiroAliquotaService;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroAliquotaPesquisaTO;
import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroAliquotaTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.centroFinanceiro.CentroFinanceiroAliquotaRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CentroFinanceiroAliquotaServiceImpl extends BaseSearchService<CentroFinanceiroAliquota, CentroFinanceiroAliquotaTO, CentroFinanceiroAliquotaPesquisaTO, CentroFinanceiroAliquotaRepository> implements CentroFinanceiroAliquotaService {

	private static final QCentroFinanceiroAliquota qCentroFinanceiroAliquota = QCentroFinanceiroAliquota.centroFinanceiroAliquota;

	@Autowired
	public CentroFinanceiroAliquotaServiceImpl(CentroFinanceiroAliquotaRepository repository) {
		super(repository);
	}

	@Override
	public List<CentroFinanceiroAliquotaTO> getListByCodCentroFinanceiro(String id) throws Exception {
		try {
			return buscarListaByCentroFinanceiroId(id);
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	private List<CentroFinanceiroAliquotaTO> buscarListaByCentroFinanceiroId(String id) {
		List<CentroFinanceiroAliquotaTO> listaTO = new ArrayList<>();

		if (id != null) {

			QCentroFinanceiro qCentroFinanceiro = QCentroFinanceiro.centroFinanceiro;
			QAliquota qAliquota = QAliquota.aliquota;
			QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;

			QBean qBean = Projections.bean(
				CentroFinanceiroAliquota.class,
				qCentroFinanceiroAliquota.codigo,
				Projections.bean(
					Aliquota.class,
					qAliquota.codigo,
					qAliquota.percentual,
					Projections.bean(
						CategoriaTributo.class,
						qCategoriaTributo.codigo
					).as("categoriaTributo")
				).as("aliquota")
			);

			JPAQuery<CentroFinanceiroAliquota> jpaQuery = select(qBean);

			jpaQuery
				.from(qCentroFinanceiroAliquota)
				.join(qCentroFinanceiroAliquota.centroFinanceiro, qCentroFinanceiro)
				.join(qCentroFinanceiroAliquota.aliquota, qAliquota)
				.join(qAliquota.categoriaTributo, qCategoriaTributo)
				.where(qCentroFinanceiro.codigo.eq(Long.parseLong(id)));

			List<CentroFinanceiroAliquota> lista = jpaQuery.fetch();

			for (CentroFinanceiroAliquota centroFinanceiroAliquota : lista) {
				Long codigoCategoriaTributo = centroFinanceiroAliquota.getAliquota().getCategoriaTributo().getCodigo();
				centroFinanceiroAliquota.getAliquota().setCategoriaTributo(null);

				CentroFinanceiroAliquotaTO to = getMapper().map(centroFinanceiroAliquota, CentroFinanceiroAliquotaTO.class);
				to.getAliquota().setCodigoCategoriaTributo(codigoCategoriaTributo);
				listaTO.add(to);
			}
		}

		return listaTO;
	}


}
