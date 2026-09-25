var createController = function($scope, $stateParams) {
	$scope.screenMode = 'NEW';
};

var editController = function($scope, $stateParams) {
	$scope.screenMode = 'EDIT';
	$scope.objectId = $stateParams.objectId;
};

var viewController = function($scope, $stateParams) {
	$scope.screenMode = 'VIEW';
	$scope.objectId = $stateParams.objectId;
};

var receberController = function($scope, $stateParams) {
	$scope.screenMode = 'RECEBER';
	$scope.objectId = $stateParams.objectId;
};

angular.module('SGIApp')

.run(['$rootScope', function($rootScope) {
	$rootScope.$on("$stateChangeError", console.log.bind(console));
}])

.config(['$stateProvider', '$urlRouterProvider', '$locationProvider', function($stateProvider, $urlRouterProvider, $locationProvider) {
    $locationProvider.html5Mode(false);


    $urlRouterProvider
        .when('/404', '/notFound')
        .otherwise('/home');

    $stateProvider

	/* ===== HOME ===== */

    .state('home', {
        url: '/home',
        templateUrl: 'partials/home/home.html',
        data: { titulo: 'Home'}
    })

}]);
