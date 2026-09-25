'use strict';

angular.module('utils', [])

.controller('mainController', ['$scope', 'toastr', '$rootScope', '$mdDialog', function($scope, toastr, $rootScope, $mdDialog) {

	var timeout = 5000;
	var extendedTimeout = 20000;

	// Notificações

	var notification = {
		info: function(title, message) {
		    toastr.info(message, title);
		},
		atencao: function(title, message) {
            toastr.warning(message, title);
        },
		success: function(title, message) {
		    toastr.success(message, title);
		},
		error: function(title, message) {
		    toastr.error(message, title);
		},
		criticalError: function(status, statusText, data) {
			console.error(data);
		    $rootScope.loadingProgress = false;
		    // TRATA OUTROS ERROS.
            var message = '<ul><li>' + status + '</li>';
            if (statusText) {
                message += '<li>' + statusText + '</li>';
            }
            if (data) {
		        // TRATA ERRO DE VALIDAÇÃO.
		        if(data.message) {
                    toastr.error(data.message.replace('br.com.fasolti.commons.lang.ValidationException: ', ''), '', {allowHtml: false, timeOut: extendedTimeout });
		        } else if(data.validation) {
                    toastr.error(data.message.replace('br.com.fasolti.commons.lang.ValidationException: ', ''), '', {allowHtml: false, timeOut: extendedTimeout });
                } else if(data.cause) {
					toastr.error(data.cause.message, '', {allowHtml: false, timeOut: extendedTimeout });
                } else {
                    if (data) {
                        if (data.code) message += '<li>' + data.code + '</li>';

                        if (data.message) {
                            message += '<li>' + data.message + '</li>';
                        } else if (data.localizedMessage) {
                            message += '<li>' + data.localizedMessage + '</li>';
                        }

                        if (data.cause) message += '<li>' + data.cause + '</li>';

                        if (angular.isDefined(data.stackTrace)) {
                            if(data.stackTrace.length > 0) {
                                message += '<li><strong>ClassName:</strong> ' + data.stackTrace[0].className + '</li>';
                                message += '<li><strong>FileName:</strong> ' + data.stackTrace[0].fileName + '</li>';
                                message += '<li><strong>LineNumber:</strong> ' + data.stackTrace[0].lineNumber + '</li>';
                                message += '<li><strong>MethodName:</strong> ' + data.stackTrace[0].methodName + '</li>';
                                message += '<li><strong>NativeMethod:</strong> ' + data.stackTrace[0].nativeMethod + '</li>';
                            }
                        }

                        if (data.mostSpecificCause) {
                            if (data.mostSpecificCause.message) {
                                message += '<li><strong>MostSpecificCause:</strong> ' + data.mostSpecificCause.message + '</li>';
                            }
                            if (data.nextException) {
                                message += '<li><strong>NextException:</strong> ' + data.nextException.message + '</li>';
                            }
                        }
                    }

                    message += '</ul>';
                    toastr.error(message, 'Erro crítico', {
                        allowHtml: true,
                        timeOut: extendedTimeout
                    });
                }
            } else {
                message += '</ul>';
                toastr.error(message, 'Erro crítico', {
                    allowHtml: true,
                    timeOut: extendedTimeout
                });
            }
		}
	};

	$scope.notification = notification;

    $scope.notification = notification;
    $rootScope.notification = notification;

    $scope.$on('crud-info', function(event, action, response) {
        if (response.message) {
            notification.info('', response.message);
        }
    });

    $scope.$on('crud-error', function(event, action, response) {
        $rootScope.loadingProgress = false;

        var title = 'Erro', message;

        if (action == 'invalid-mode') {
            message = 'Não foi possível completar a ação - formulário em estado inválido.';
        } else if (action == 'invalid-id') {
            message = 'Não foi possível completar a ação - id não informado.';
        } else
            message = response.message;

        if (message)
            notification.error(title, message);

        console.error('crud-error', action, response);
	});

	$scope.dialogOpen = false;
	$scope.showAlert = function(ev) {
		$mdDialog.show(
			$mdDialog.alert()
			.parent(angular.element(document.querySelector('#popupContainer')))
			.clickOutsideToClose(false)
			.title('Sessão encerrada')
			.textContent('Sua sessão foi encerrada. Faça um novo login.')
			.ariaLabel('Sessão encerrada')
			.ok('Continuar')
		).then(function(answer) {
			$scope.dialogOpen = false;
			var urlHome = '../app/main.html';
    	    window.location.href = urlHome;
		});
		$scope.dialogOpen = true;
	};

    $scope.$on('crud-critical', function(event, action, response) {
		if (response.status === 401 || response.status === 403) {
			if (!$scope.dialogOpen) {
				localStorage.setItem("last_address_logout", window.location.href);
				$scope.showAlert(null);
			}
		} else {
			console.error('crud-critical', action, response);
			notification.criticalError(response.status, response.statusText, response.data);
		}
    });
}])

.factory('ValidationService', [function() {
	var validations = {
		verificarPreenchimento: function(campo) {
			return campo !== undefined && campo !== '';
		},

		verificarTamanhoMenorIgual: function(campo, tamanho) {
			if (!tamanho) {
				throw new Error('Parametro tamanho não está preenchido.');
			}

			return campo.length <= tamanho && this.verificarPreenchimento(campo);
		},

		verificarFormatoEmail: function(email) {
			return email.match(/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i);
		},

		verificarSomenteLetras: function(campo) {
			return campo.match(/\d/g) ? false : true;
		},

		validarEmail: function(email) {
			var validacaoPreenchimentoAndFormato = this.verificarPreenchimento(email) && this.verificarTamanhoMenorIgual(email, 100);
			var validacao = validacaoPreenchimentoAndFormato && this.verificarFormatoEmail(email);

			return validacao;
		},

		validarNome: function(nome) {
			return this.verificarPreenchimento(nome) && this.verificarSomenteLetras(nome);
		},

		validarCpf: function(cpf) {
			cpf = cpf.replace(/[^\d]+/g, '');
			if (cpf === '') return false;
			// Elimina CPFs invalidos conhecidos
			if (cpf.length != 11 || /([0-9])\1{10}/.test(cpf))
				return false;
			var add = 0;
			for (var i = 0; i < 9; i++)
				add += parseInt(cpf.charAt(i)) * (10 - i);
			var rev = 11 - (add % 11);
			if (rev == 10 || rev == 11)
				rev = 0;
			if (rev != parseInt(cpf.charAt(9)))
				return false;
			// Valida 2o digito
			add = 0;
			for (i = 0; i < 10; i++)
				add += parseInt(cpf.charAt(i)) * (11 - i);
			rev = 11 - (add % 11);
			if (rev == 10 || rev == 11)
				rev = 0;
			if (rev != parseInt(cpf.charAt(10)))
				return false;
			return true;
		},

		validarTelefone: function(telefone) {
			return telefone.match(/^[1-9]{2}[2-9][0-9]{7,8}$/g);
		},

		somenteNumeros: function(campo) {
			return campo.match(/\d/g);
		}
	};

	return validations;
}])

.factory('DialogConfirm', ['$mdDialog', '$rootScope', function($mdDialog, $rootScope) {

    var service = {};

    function DialogControllerConfirm($scope, $mdDialog, object, title, msg, titleButtonConfirm, titleButtonCancel, fnConfirm, fnCancel) {
       $scope.title = title;
       $scope.msg = msg;
       $scope.titleButtonConfirm = titleButtonConfirm;
       $scope.titleButtonCancel = titleButtonCancel;
       $scope.action = false;

       $scope.hide = function() {
          $mdDialog.hide();
          $scope.action = false;
       };

       $scope.cancel = function() {
         $scope.action = false;
         fnCancel($scope.hide);
       };

       $scope.confirm = function() {
           $scope.action = true;
           fnConfirm(object, $scope.hide);
       };
    };

    service.show = function(event, object, title, msg, titleButtonConfirm, titleButtonCancel, fnConfirm, fnCancel) {
        $mdDialog.show({
            controller: DialogControllerConfirm,
            locals: {
                 object: object,
                 title: title,
                 msg: msg,
                 titleButtonConfirm: titleButtonConfirm,
                 titleButtonCancel: titleButtonCancel,
                 fnConfirm: fnConfirm,
                 fnCancel: fnCancel
            },
            templateUrl: 'partials/dialogs/confirm_dialog.html',
            parent: angular.element(document.body),
            targetEvent: event,
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    return service;

}]);
