package br.com.araujoabreu.sgi.service.impl.fecho;

import br.com.araujoabreu.sgi.comercial.domain.CategoriaDespesaGeral;
import br.com.araujoabreu.sgi.comercial.domain.FechoDespesaGeral;
import br.com.araujoabreu.sgi.comercial.domain.QCategoriaDespesaGeral;
import br.com.araujoabreu.sgi.comercial.domain.QFecho;
import br.com.araujoabreu.sgi.comercial.domain.QFechoDespesaGeral;
import br.com.araujoabreu.sgi.compras.domain.QUnidadeMedida;
import br.com.araujoabreu.sgi.compras.domain.UnidadeMedida;
import br.com.araujoabreu.sgi.service.fecho.FechoDespesaGeralService;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoDespesaGeralPesquisaTO;
import br.com.araujoabreu.sgi.service.fecho.transfer.FechoDespesaGeralTO;
import br.com.fasolti.commons.services.BaseSearchService;
import br.com.fasolti.sgi.repository.fecho.FechoDespesaGeralRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class FechoDespesaGeralServiceImpl extends BaseSearchService<FechoDespesaGeral, FechoDespesaGeralTO, FechoDespesaGeralPesquisaTO, FechoDespesaGeralRepository> implements FechoDespesaGeralService {

	private static final QFechoDespesaGeral qFechoDespesaGeral = QFechoDespesaGeral.fechoDespesaGeral;

	@Autowired
	public FechoDespesaGeralServiceImpl(FechoDespesaGeralRepository repository) {
		super(repository);
	}

	@Override
	public @ResponseBody
	List<FechoDespesaGeralTO> despesaByCodFecho(@PathVariable String id) throws Exception {
		try {
			return buscarDespesaByCodFecho(id);
		} catch (Exception e) {
			throw new Exception(e.getCause());
		}
	}

	private List<FechoDespesaGeralTO> buscarDespesaByCodFecho(String id) {
		return getListaDespesaByCodFecho(Long.parseLong(id));
	}

	public List<FechoDespesaGeralTO> getListaDespesaByCodFecho(Long codFecho) {
		if (codFecho == null) {
			return null;
		}

		QFecho qFecho = QFecho.fecho;
		QCategoriaDespesaGeral qCategoriaDespesaGeral = QCategoriaDespesaGeral.categoriaDespesaGeral;
		QUnidadeMedida qUnidadeMedida = QUnidadeMedida.unidadeMedida;

		QBean qBean = Projections.bean(
			FechoDespesaGeral.class,
			qFechoDespesaGeral.codigo,
			qFechoDespesaGeral.tipoDespesaGeral,
			qFechoDespesaGeral.valorUnitario,
			qFechoDespesaGeral.quantidade,
			qFechoDespesaGeral.descricao,
			Projections.bean(CategoriaDespesaGeral.class, qCategoriaDespesaGeral.codigo, qCategoriaDespesaGeral.nome).as("categoriaDespesaGeral"),
			Projections.bean(UnidadeMedida.class, qUnidadeMedida.codigo, qUnidadeMedida.descricao).as("unidadeMedida")
		);

		JPAQuery<FechoDespesaGeral> jpaQuery = select();

		jpaQuery
			.select(qBean)
			.from(qFechoDespesaGeral)
			.join(qFechoDespesaGeral.fecho, qFecho)
			.join(qFechoDespesaGeral.unidadeMedida, qUnidadeMedida)
			.leftJoin(qFechoDespesaGeral.categoriaDespesaGeral, qCategoriaDespesaGeral)
			.where(qFecho.codigo.eq(codFecho));

		List<FechoDespesaGeral> lista = jpaQuery.fetch();

		return map(lista);
	}

	@Override
	public BigDecimal getSubTotal(List<FechoDespesaGeralTO> lista, String tipoDespesa) {
		BigDecimal total = BigDecimal.ZERO;

		if (lista != null && !lista.isEmpty()) {
			for (FechoDespesaGeralTO despesa : lista) {
				if (despesa.getTipoDespesaGeral().equals(tipoDespesa)) {
					total = total.add(despesa.getValorTotalItem());
				}
			}
		}

		return total.setScale(2, RoundingMode.HALF_EVEN);
	}

}
