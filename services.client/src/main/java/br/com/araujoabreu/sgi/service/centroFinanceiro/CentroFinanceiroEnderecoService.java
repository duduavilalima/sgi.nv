package br.com.araujoabreu.sgi.service.centroFinanceiro;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroEnderecoTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface CentroFinanceiroEnderecoService extends CrudService<CentroFinanceiroEnderecoTO> {
	List<CentroFinanceiroEnderecoTO> buscarTelefones(CentroFinanceiroEnderecoTO centroFinanceiroEnderecoTO);

	List<CentroFinanceiroEnderecoTO> buscarListaCentroFinanceiroEndereco(CentroFinanceiroEnderecoTO filtroCfEndereco);
}
