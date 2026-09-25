package br.com.araujoabreu.sgi.service.impl.nota;

import br.com.araujoabreu.sgi.controleFiscal.domain.Aliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.CategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.NotaTributoAliquotaEmissao;
import br.com.araujoabreu.sgi.controleFiscal.domain.QAliquota;
import br.com.araujoabreu.sgi.controleFiscal.domain.QCategoriaTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.QNotaTributoAliquotaEmissao;
import br.com.araujoabreu.sgi.controleFiscal.domain.QTributo;
import br.com.araujoabreu.sgi.controleFiscal.domain.Tributo;
import br.com.araujoabreu.sgi.service.nota.NotaTributoAliquotaEmissaoService;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTributoAliquotaEmissaoPesquisaTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTributoAliquotaEmissaoTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.notaSaida.NotaTributoAliquotaEmissaoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotaTributoAliquotaEmissaoServiceImpl extends BaseSearchService<NotaTributoAliquotaEmissao, NotaTributoAliquotaEmissaoTO, NotaTributoAliquotaEmissaoPesquisaTO, NotaTributoAliquotaEmissaoRepository> implements NotaTributoAliquotaEmissaoService {

	private static final QNotaTributoAliquotaEmissao query = QNotaTributoAliquotaEmissao.notaTributoAliquotaEmissao;

	@Autowired
	public NotaTributoAliquotaEmissaoServiceImpl(NotaTributoAliquotaEmissaoRepository repository) {
		super(repository);
	}

	@Override
	public List<NotaTributoAliquotaEmissaoTO> listarPorCodigoNota(Long codigoNota) {
		JPAQuery jpaQuery = select();
		QAliquota qAliquota = QAliquota.aliquota;
		QCategoriaTributo qCategoriaTributo = QCategoriaTributo.categoriaTributo;
		QTributo qTributo = QTributo.tributo;

		QBean qBean = Projections.bean(
			NotaTributoAliquotaEmissao.class,
			query.codigo,
			query.baseCalculo,
			query.valor,
			query.percentual,
			Projections.bean(
				Aliquota.class,
				qAliquota.codigo,
				qAliquota.percentual,
				Projections.bean(
					CategoriaTributo.class,
					qCategoriaTributo.codigo,
					Projections.bean(
						Tributo.class,
						qTributo.codigo,
						qTributo.nome
					).as("tributo")
				).as("categoriaTributo")
			).as("aliquota")
		);

		jpaQuery
			.select(qBean)
			.innerJoin(query.aliquota, qAliquota)
			.innerJoin(qAliquota.categoriaTributo, qCategoriaTributo)
			.innerJoin(qCategoriaTributo.tributo, qTributo)
			.where(query.nota.codigo.eq(codigoNota));

		List<NotaTributoAliquotaEmissao> lista = jpaQuery.fetch();
		List<NotaTributoAliquotaEmissaoTO> listaTO = new ArrayList<>();

		for (NotaTributoAliquotaEmissao notaTributoAliquotaEmissao : lista) {
			NotaTributoAliquotaEmissaoTO to = getMapper().map(notaTributoAliquotaEmissao, NotaTributoAliquotaEmissaoTO.class);
			to.getAliquota().setCodigoCategoriaTributo(notaTributoAliquotaEmissao.getAliquota().getCategoriaTributo().getCodigo());
			to.getAliquota().setCodigoTributo(notaTributoAliquotaEmissao.getAliquota().getCategoriaTributo().getTributo().getCodigo());
			to.getAliquota().setNomeTributo(notaTributoAliquotaEmissao.getAliquota().getCategoriaTributo().getTributo().getNome());
			listaTO.add(to);
		}

		return listaTO;
	}


}
