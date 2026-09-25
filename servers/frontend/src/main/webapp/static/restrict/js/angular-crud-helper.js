/*jshint -W030*/
/*jshint -W097*/
/*jshint indent: 4*/
/*global angular*/
/*global _*/

'use strict';

angular.module('pxCrudHelper', [])

.controller('CrudController', ['$scope', 'ctrl', 'Resource', function($scope, ctrl, Resource) {
	var original = {};

	ctrl.loadObject = function() {
		if (!ctrl.objectId) {
			$scope.$emit('crud-error', 'invalid-id');
			return;
		}

		var result = Resource.get({ id: ctrl.objectId }).$promise;

		return result.then(function(object) {
			ctrl.object = object;
			original = angular.copy(object);
			ctrl.doAfterLoad && ctrl.doAfterLoad(ctrl.object);
			$scope.$emit('crud-info', 'load', object);
			return object;
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
			return response;
		});
	};

	ctrl.createObject = function() {
		// TODO Configuração para decidir se vai ao servidor para buscar um objeto novo
		//ctrl.object = new Resource();
		ctrl.object = Resource.get(null,
			function(data) {
				ctrl.doAfterCreate && ctrl.doAfterCreate(data);
			},
			function(error) {
				$scope.$emit('crud-critical', action, response);
			}
		);
	};

	ctrl.cancel = function(form) {
		ctrl.object = angular.copy(original);
		form && form.$setPristine();

		$scope.$emit('crud-success', 'cancel');
	};

	ctrl.save = function(form) {
		if (ctrl.screenMode != 'NEW' && ctrl.screenMode != 'EDIT') {
			$scope.$emit('crud-error', 'invalid-mode');
			return;
		}

		if (ctrl.doBeforeSave && !ctrl.doBeforeSave(ctrl.object)) return;

		var action, result;
		if (ctrl.screenMode == 'NEW') {
			action = 'save';
			result = ctrl.object.$save();
		} else {
			action = 'update';
			result = ctrl.object.$update({ id: ctrl.objectId });
		}

		return result.then(function(data) {
			ctrl.doAfterSave && ctrl.doAfterSave(ctrl.object);

			// Always goes to edit mode after successful save/update
			ctrl.screenMode = 'EDIT';

			ctrl.object = angular.copy(data);
			form && form.$setPristine();

			$scope.$emit('crud-success', action, data);

			return data;
		}).catch(function(response) {
			$scope.$emit('crud-critical', action, response);
			return response;
		});
	};

	ctrl.delete = function() {
		if (ctrl.screenMode != 'EDIT') {
			$scope.$emit('crud-error', 'invalid-mode');
			return;
		}

		var result = ctrl.object.$delete({ id: ctrl.object.id });

		return result.then(function(data) {
			$scope.$emit('crud-success', 'delete', data);
			//TODO $scope.voltar();
			return data;
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'delete', response);
			return response;
		});
	};

	if (ctrl.screenMode == 'NEW') {
		ctrl.createObject();
	} else {
		ctrl.loadObject();
	}
}])

.controller('SearchController', ['$scope', 'Resource', function($scope, Resource) {
	$scope.clear = function() {
		$scope.object = {};
		$scope.form.$setPristine();
	};

	$scope.pageChanged = function() {
		$scope.search();
	};

	$scope.resetSearch = function() {
		$scope.currentPage = 1;
		$scope.search();
	};

	$scope.search = function(silent) {
		var result = Resource.page(params, $scope.object).$promise;

		return result.then(function(data) {
			processFetchAllData(data);
			$scope.$emit('crud-success', 'search', data);

			// Include the silent flag in the data so the event processor knows it
			data.silentSearch = silent;

			return data;
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'search', response);
			return response;
		});
	}

	$scope.pagedSearch = function(silent) {
		//TODO Implementar ordenação na grid
		//FIXME		var params = { sortField: 'nome' };
		var params = {};
		if ($scope.pageSize) params.pageSize = $scope.pageSize;
		if ($scope.currentPage) params.page = $scope.currentPage - 1;
		var result = Resource.page(params, $scope.object).$promise;

		return result.then(function(data) {
			processSearchData(data);
			$scope.$emit('crud-success', 'search', data);

			// Include the silent flag in the data so the event processor knows it
			data.silentSearch = silent;

			return data;
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'search', response);
			return response;
		});
	};

	$scope.fetchAll = function() {
		var result = Resource.all().$promise;

		return result.then(function(data) {
			processFetchAllData(data);
			$scope.$emit('crud-success', 'fetchAll', data);
			return data;
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'fetchAll', response);
			return response;
		});
	};

	var processSearchData = function(page) {
		$scope.result = page;
		$scope.currentPage = page.number + 1;
		$scope.totalItems = page.totalElements;
		$scope.itemsPerPage = page.size;
	};

	var processFetchAllData = function(data) {
		$scope.result = data;
		$scope.currentPage = 1;
		$scope.totalItems = data.length;
		$scope.itemsPerPage = data.length;
	};

	$scope.allSelected = false;

	$scope.selectedItems = [];

	$scope.selectAll = function(selected) {
		if (selected)
			$scope.selectedItems = $scope.result.slice();
		else
			$scope.selectedItems = [];

		angular.forEach($scope.result, function(item) {
			item.selected = selected;
		});
	};

	$scope.selectItem = function(selected, item) {
		if ($scope.allSelected && !selected)
			$scope.allSelected = false;

		// Just for sure - Might get here with this already set by the interface
		item.selected = selected;

		var i = $scope.selectedItems.indexOf(item);
		if (selected) {
			if (i < 0) $scope.selectedItems.push(item);
		} else {
			$scope.selectedItems.splice(i, 1);
		}
	};

	$scope.deleteSelected = function() {
		if ($scope.selectedItems.length === 0) return;

		if ($scope.doBeforeDelete && !$scope.doBeforeDelete(ctrl.object)) return;

		var result = Resource.deleteList($scope.selectedItems).$promise;

		return result.then(function(data) {
			$scope.doAfterDelete && $scope.doAfterDelete(data);
			$scope.$emit('crud-success', 'delete', data);

			// Remove itens from result and reset selected items
			$scope.result = _.difference($scope.result, $scope.selectedItems);
			$scope.totalItems = $scope.result.length;
			$scope.selectedItems = [];

			return data;
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'delete', response);
			return response;
		});
	};

	//TODO Replace for a generic non-standard action
	$scope.copySelected = function() {
		if ($scope.selectedItems.length === 0) return;

		if ($scope.doBeforeCopy && !$scope.doBeforeCopy(ctrl.object)) return;

		var result = Resource.copyList($scope.selectedItems).$promise;

		return result.then(function(data) {
			$scope.doAfterCopy && $scope.doAfterCopy(data);
			$scope.$emit('crud-success', 'copy', data);

			// Refreshes page results to include copied items
			$scope.buscar();

			return data;
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'copy', response);
			return response;
		});
	};

	if (!$scope.object)
		$scope.object = {};

	if (!$scope.pageSize)
		$scope.pageSize = 12;
}])

;
