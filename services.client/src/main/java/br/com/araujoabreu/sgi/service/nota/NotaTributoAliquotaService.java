package br.com.araujoabreu.sgi.service.nota;

import br.com.araujoabreu.sgi.service.contasApagar.transfer.PagamentoTO;
import br.com.araujoabreu.sgi.service.nota.transfer.NotaTributoAliquotaTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.math.BigDecimal;
import java.util.List;

public interface NotaTributoAliquotaService extends CrudService<NotaTributoAliquotaTO> {

	List<NotaTributoAliquotaTO> listarPorCodigoNota(Long codigoNota);

	PagamentoTO buscarPagamentoById(Long codigoNotaTributoAliquota);

	JPAQuery getJPAQuery();

	List<NotaTributoAliquotaTO> listarPorCodigoNotaOrdem(Long codigoNota, Integer ordem);

	List<NotaTributoAliquotaTO> listarPorCodigoNotaPorOrdem(Long codigoNota, Integer ordem);

	void deletarNotaTributoAliquota(Long codigo);

	void updateValor(Long codigo, BigDecimal valor);

	void updateBaseCalculo(Long codigo, BigDecimal baseCalculo);
}
