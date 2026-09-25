package br.com.araujoabreu.sgi.service.faturamento;

import br.com.araujoabreu.sgi.service.faturamento.transfer.FaturamentoAliquotaTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public interface FaturamentoAliquotaService extends CrudService<FaturamentoAliquotaTO> {

	List<FaturamentoAliquotaTO> pesquisarFaturamentoAliquota(FaturamentoAliquotaTO filtroFaturamentoAliquota);

	JPAQuery getJPAQuery();

	void deletarFaturamentoAliquota(Long codigoFaturamento);

	JPAQuery<?> findFaturamentoAliquotaApenasPercentual(Long codigoFaturamento);
}
