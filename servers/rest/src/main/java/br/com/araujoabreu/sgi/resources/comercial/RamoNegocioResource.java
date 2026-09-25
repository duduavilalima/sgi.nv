package br.com.araujoabreu.sgi.resources.comercial;

import br.com.araujoabreu.sgi.service.comercial.RamoNegocioService;
import br.com.araujoabreu.sgi.service.comercial.transfer.RamoNegocioPesquisaTO;
import br.com.araujoabreu.sgi.service.comercial.transfer.RamoNegocioTO;
import br.com.araujoabreu.sgi.service.util.AutocompleteTO;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/ramonegocio")
public class RamoNegocioResource extends BaseCrudResource<RamoNegocioTO, RamoNegocioPesquisaTO, RamoNegocioService> {

	@Autowired
	public RamoNegocioResource(RamoNegocioService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/autocomplete")
	public List<RamoNegocioTO> getRamoNegocio(@RequestBody AutocompleteTO autocompleteTO) {
		return getService().getRamoNegocio(autocompleteTO);
	}
}
