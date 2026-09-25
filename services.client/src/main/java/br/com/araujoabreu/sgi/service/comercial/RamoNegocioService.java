package br.com.araujoabreu.sgi.service.comercial;

import br.com.araujoabreu.sgi.service.comercial.transfer.RamoNegocioTO;
import br.com.araujoabreu.sgi.service.util.AutocompleteTO;
import br.com.fasolti.commons.client.service.CrudService;

import java.util.List;

public interface RamoNegocioService extends CrudService<RamoNegocioTO> {
	List<RamoNegocioTO> buscarRamoNegocioPorSiglaouDescricao(String siglaOuDescricao);

	List<RamoNegocioTO> getRamoNegocio(AutocompleteTO autocompleteTO);
}
