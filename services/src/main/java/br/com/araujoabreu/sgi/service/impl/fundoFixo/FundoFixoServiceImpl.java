package br.com.araujoabreu.sgi.service.impl.fundoFixo;

import br.com.araujoabreu.sgi.fundoFixo.domain.FundoFixo;
import br.com.araujoabreu.sgi.fundoFixo.domain.QFundoFixo;
import br.com.araujoabreu.sgi.service.fundoFixo.FundoFixoService;
import br.com.araujoabreu.sgi.service.fundoFixo.transfer.FundoFixoPesquisaTO;
import br.com.araujoabreu.sgi.service.fundoFixo.transfer.FundoFixoTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.fundofixo.FundoFixoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundoFixoServiceImpl extends BaseSearchService<FundoFixo, FundoFixoTO, FundoFixoPesquisaTO, FundoFixoRepository> implements FundoFixoService {

	private static final QFundoFixo qFundoFixo = QFundoFixo.fundoFixo;

	@Autowired
	private FundoFixoRepository fundoFixoRepository;

	@Autowired
	public FundoFixoServiceImpl(FundoFixoRepository repository) {
		super(repository);
	}

	@Override
	public Long buscarQtdFundoFixoPorCfId(Long id) {
		try {

			QBean qBean = Projections.bean(
				FundoFixo.class,
				qFundoFixo.codigo
			);

			JPAQuery<FundoFixo> jpaQuery = select();

			jpaQuery
				.select(qBean)
				.from(qFundoFixo)
				.where(qFundoFixo.centroFinanceiro.codigo.eq(id));

			return jpaQuery.fetchCount();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
