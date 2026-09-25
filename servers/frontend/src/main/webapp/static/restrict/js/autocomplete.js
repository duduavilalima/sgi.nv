angular.module('autocompleteservice', ['ngResource'])


.factory('AutocompleteService', ['$resource', '$restServer', function ($resource, $restServer) {

	return $resource($restServer + 'rest/:action', null, {
		getPessoas: {
			method: 'GET',
			url: $restServer + 'rest/pessoa/autocomplete/:nome',
			isArray : true
		},
		getPessoaJuridica: {
			method: 'GET',
			url: $restServer + 'rest/pessoa/pessoajuridica/autocomplete/:nome',
			isArray : true
		},
		getContaContabil: {
			method:'POST',
			url: $restServer + 'rest/contacontabil/autocomplete',
			isArray: true
		},
		getContaContabilPorEmpresa: {
			method:'POST',
			url: $restServer + 'rest/contacontabil/autocomplete/empresa',
			isArray: true
		},
        getRamoNegocio: {
            method:'POST',
            url: $restServer + 'rest/ramonegocio/autocomplete',
            isArray: true
        },
		getCidade: {
		    method:'POST',
			url: $restServer + 'rest/cidade/autocomplete',
			isArray: true
		},
		getPessoaJuridicaCliente: {
			method: 'GET',
			url: $restServer + 'rest/pessoajuridica/autocomplete/cliente/:nome',
			isArray : true
		},
		getContaContabilPorEmpresaClassificacao: {
			method: 'POST',
			url: $restServer + 'rest/contacontabil/autocomplete/empresa/classificacao',
			isArray : true
		},
		getContaContabilPorEmpresaTipoEspecialClassificacao: {
			method: 'POST',
			url: $restServer + 'rest/contacontabil/autocomplete/empresa/tipoEspecialClassificacao',
			isArray : true
        },
		getDespesaFluxoCaixa: {
			method:'POST',
			url: $restServer + 'rest/despesas/autocomplete',
			isArray: true
		}
	});

}])

