package br.com.araujoabreu.sgi.service.compras;

import br.com.araujoabreu.sgi.service.compras.transfer.NotaPagamentoTO;
import br.com.fasolti.commons.client.service.CrudService;
import com.querydsl.core.Tuple;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface NotaPagamentoService extends CrudService<NotaPagamentoTO> {
	BigDecimal obterValorTibutoDeNotaPagamentoPeloNumero(String numero);
	List<Tuple> listarNPsPorData(Date date);
}
