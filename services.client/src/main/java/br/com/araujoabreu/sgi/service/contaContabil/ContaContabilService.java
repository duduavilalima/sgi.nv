package br.com.araujoabreu.sgi.service.contaContabil;

import br.com.araujoabreu.sgi.service.centroFinanceiro.transfer.CentroFinanceiroTO;
import br.com.araujoabreu.sgi.service.contaContabil.transfer.ContaContabilTO;
import br.com.araujoabreu.sgi.service.util.AutoCompleteContaContabilTO;
import br.com.araujoabreu.sgi.service.util.AutocompleteTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface ContaContabilService extends CrudService<ContaContabilTO> {
	List<ContaContabilTO> buscarContaContabilPorNomeOuNumero(String nomeOuNumero);

	List<ContaContabilTO> getContaContabil(AutocompleteTO autocompleteTO);

	List<ContaContabilTO> getContaContabilPorEmpresa(AutocompleteTO autocompleteTO);

	List<ContaContabilTO> getContaContabilByCodEmpresa(Long codEmpresa);

	List<ContaContabilTO> getContaContabilPorEmpresaClassificacao(AutoCompleteContaContabilTO autocompleteTO);

	List<ContaContabilTO> getContaContabilPorEmpresaTipoEspecialClassificacao(AutoCompleteContaContabilTO autocompleteTO);

}
