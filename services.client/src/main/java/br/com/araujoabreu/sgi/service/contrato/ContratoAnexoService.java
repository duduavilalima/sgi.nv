package br.com.araujoabreu.sgi.service.contrato;

import br.com.araujoabreu.sgi.service.contrato.transfer.ContratoAnexoTO;
import br.com.araujoabreu.sgi.service.tesouraria.transfer.AnexoTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface ContratoAnexoService extends CrudService<ContratoAnexoTO> {
	void remover(Long id) throws Exception;

	void baixarAnexo(String id, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws Exception;

	AnexoTO verificarAnexo(Long id) throws Exception;
}
