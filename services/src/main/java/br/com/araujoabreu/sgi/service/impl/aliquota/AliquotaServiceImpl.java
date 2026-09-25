package br.com.araujoabreu.sgi.service.impl.aliquota;

import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.QAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.QCategoriaTributo;
import br.com.araujoabreu.sgi.service.categoriaTributo.AliquotaService;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.AliquotaPesquisaTO;
import br.com.araujoabreu.sgi.service.categoriaTributo.transfer.AliquotaTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.aliquota.AliquotaRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AliquotaServiceImpl extends
	BaseSearchService<
		Aliquota,
		AliquotaTO,
		AliquotaPesquisaTO,
		AliquotaRepository> implements AliquotaService {

	private static final QAliquota qAliquota = QAliquota.aliquota;

	@Autowired
	public AliquotaServiceImpl(AliquotaRepository repository) {
		super(repository);
	}

	@Override
	public List<AliquotaTO> getListaAliquotaByCodCategoriaTributo(Long codigo) {
		List<Aliquota> lista;

		QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;

		QBean qBean = Projections.bean(
			Aliquota.class,
			qAliquota.codigo,
			qAliquota.percentual
		);

		JPAQuery<Aliquota> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qAliquota).join(qAliquota.categoriaTributo, qCategoriaTributo)
			.where(qCategoriaTributo.codigo.eq(codigo));

		lista = jpaQuery.fetch();

		return map(lista);
	}

	@Override
	public BigDecimal getPercentualAliquotaByCodCategoriaTributo(Long codigo) {
		BigDecimal resultado = BigDecimal.ZERO;
		List<AliquotaTO> listaAliquota = getListaAliquotaByCodCategoriaTributo(codigo);

		if (!listaAliquota.isEmpty()) {
			resultado = listaAliquota.get(0).getPercentual();
		}

		return resultado;
	}
}
