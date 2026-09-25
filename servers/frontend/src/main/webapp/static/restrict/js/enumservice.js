angular.module('enumservice', ['ngResource'])


.factory('EnumService', ['$resource', '$restServer', function ($resource, $restServer) {

	return $resource($restServer + 'rest/enumservice/simples/:pacote/:classe/:enum', null, {
		enumValues: {
			method: 'GET',
			isArray : true
		}
	});

}])