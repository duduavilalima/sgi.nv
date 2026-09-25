package br.com.araujoabreu.sgi.service.custos;

import br.com.araujoabreu.sgi.service.custos.transfer.ImportacaoOrcamentoEmpresarialTO;

public interface CustosService {

	void importacaoOrcamentoEmpresarial(ImportacaoOrcamentoEmpresarialTO importacaoOrcamentoEmpresarialTO) throws Exception;
}
