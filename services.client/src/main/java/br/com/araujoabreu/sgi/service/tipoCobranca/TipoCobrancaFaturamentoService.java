package br.com.araujoabreu.sgi.service.tipoCobranca;

import br.com.araujoabreu.sgi.service.tipoCobranca.transfer.TipoCobrancaFaturamentoTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface TipoCobrancaFaturamentoService extends CrudService<TipoCobrancaFaturamentoTO> {
	TipoCobrancaFaturamentoTO obterTipoCobrancaFaturamentoSimpleTelaNotaFiscal(Long codigoTipoCobranca);
}
