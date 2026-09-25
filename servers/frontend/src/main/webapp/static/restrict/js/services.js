angular.module('services')

.service('appUrl', ['$location', function($location) {
	return function() {
		return $location.protocol() + '://' + $location.host() + ':' + $location.port();
	};
}])

.factory('CalculadorFaturamento', [function() {
    let calculadorFaturamentoService = {};
    let percentualTotal = 100;

    calculadorFaturamentoService.divideMaterialServico = function(object, mode) {

        if (object.tipoFornecimento === 'Só serviço') {
            object.percentualServico = 100;
            object.valorServico = object.valor;
            object.percentualMaterial = 0;
            object.valorMaterial = 0;
            object.valorLiquido = object.valor - object.valorTributos - object.valorRetencao;
            object.valorTributos = object.valorTributosAnterior;
        } else if (object.tipoFornecimento === 'Serviço/Material') {
            object.valorServico = (object.valor * object.percentualServico) / percentualTotal;
            object.percentualMaterial = Math.floor((percentualTotal - object.percentualServico) * 100)/100;
            object.valorMaterial = (object.valor * object.percentualMaterial) / percentualTotal;
            object.valorLiquido = object.valor - object.valorTributos - object.valorRetencao;
            object.valorTributos = object.valorTributosAnterior;
        } else if (object.tipoFornecimento === 'Só material') {
            object.percentualServico = 0.00;
            object.valorServico = 0.00;
            object.percentualMaterial = 100.00;
            object.valorMaterial = object.valor;
            object.valorTributos = 0.00;
            object.valorLiquido = object.valor - object.valorRetencao;
        }

        if (isNaN(object.valorRetencaoAcessoria)) {
            object.valorRetencaoAcessoria = 0;
        }

        if (isNaN(object.valorRetencaoContratual)) {
            object.valorRetencaoContratual = 0;
        }

        if (isNaN(object.percentualRetencaoContratual)) {
            object.percentualRetencaoContratual = 0;
        }

        if (isNaN(object.percentualRetencaoAcessoria)) {
            object.percentualRetencaoAcessoria = 0;
        }
    };

    calculadorFaturamentoService.divideMaterialServicoEtapa = function(faturamento, etapa, percentualTributos) {
        if (angular.isDefined(faturamento)) {
            if (!calculadorFaturamentoService.isCampoVazio(faturamento.percentualServico)) {
                etapa.percentualServico = faturamento.percentualServico;
            } else if (!calculadorFaturamentoService.isCampoVazio(etapa.faturamento) && !calculadorFaturamentoService.isCampoVazio(etapa.faturamento.percentualServico)) {
                etapa.percentualServico = etapa.faturamento.percentualServico;
            }
            etapa.percentualMaterial = faturamento.percentualMaterial;

            if (etapa.valorServico > 0) {
                etapa.valorEtapa = (etapa.valorServico * percentualTotal) / etapa.percentualServico;
                etapa.valorMaterial = (etapa.percentualMaterial * etapa.valorEtapa) / percentualTotal;
                etapa.valorTributos = (etapa.valorEtapa * percentualTributos) / percentualTotal;
            }
        }
         etapa.valorLiquido = etapa.valorEtapa - etapa.valorTributos;
    };

    calculadorFaturamentoService.dialogEtapa = function(faturamento, etapa, percentualTributos) {

        if(angular.isUndefined(faturamento)) {
            return;
        }

        if(calculadorFaturamentoService.isCampoVazioNumerico(etapa.valor))
            etapa.valor = 0;

        if(calculadorFaturamentoService.isCampoVazioNumerico(etapa.valorMulta))
            etapa.valorMulta = 0;

        if(calculadorFaturamentoService.isCampoVazioNumerico(etapa.valorJuros))
            etapa.valorJuros = 0;

        if(calculadorFaturamentoService.isCampoVazioNumerico(etapa.valorPermuta))
            etapa.valorPermuta = 0;

        if(calculadorFaturamentoService.isCampoVazioNumerico(etapa.valorDesconto))
            etapa.valorDesconto = 0;

        if(calculadorFaturamentoService.isCampoVazioNumerico(etapa.percentualRetencaoContratual))
            etapa.percentualRetencaoContratual = 0;

        if(calculadorFaturamentoService.isCampoVazioNumerico(etapa.percentualRetencaoAcessoria))
            etapa.percentualRetencaoAcessoria = 0;

        if(calculadorFaturamentoService.isCampoVazioNumerico(etapa.valorRetencaoContratual))
            etapa.valorRetencaoContratual = 0;

        if(calculadorFaturamentoService.isCampoVazioNumerico(etapa.valorRetencaoAcessoria))
            etapa.valorRetencaoAcessoria = 0;

        if(calculadorFaturamentoService.isCampoVazioNumerico(percentualTributos))
            percentualTributos = 0;


        etapa.valorRetencaoContratual = (etapa.valor * etapa.percentualRetencaoContratual) / percentualTotal;
        etapa.valorRetencaoAcessoria = (etapa.valor * etapa.percentualRetencaoAcessoria) / percentualTotal;

        etapa.valorTributos = (etapa.valor * percentualTributos) / percentualTotal;

        if(calculadorFaturamentoService.isCampoVazioNumerico(etapa.valorRetencaoContratual))
            etapa.valorRetencaoContratual = 0;

        if(calculadorFaturamentoService.isCampoVazioNumerico(etapa.valorRetencaoAcessoria))
            etapa.valorRetencaoAcessoria = 0;

        if(calculadorFaturamentoService.isCampoVazioNumerico(etapa.valorTributos))
            etapa.valorTributos = 0;

        let valorMultaJuros = etapa.valorMulta + etapa.valorJuros;
        let proventos = etapa.valor + valorMultaJuros;
        let descontos = etapa.valorTributos + etapa.valorRetencaoContratual + etapa.valorRetencaoAcessoria + etapa.valorPermuta + etapa.valorDesconto;
        etapa.valorLiquido = proventos - descontos;
    };

    calculadorFaturamentoService.percentualTotalTributo = function(listaTributos, issRetidoCliente) {
        let percentualTotal = 0;
        angular.forEach(listaTributos, function(obj, key) {
            if (obj.aliquota.percentual) {
                if (!issRetidoCliente &&  obj.aliquota.categoriaTributo.tributoTO.nome === 'ISS') {
                    return;
                }
                percentualTotal += Number(obj.aliquota.percentual);
            }
        });
        return percentualTotal;
    };

    calculadorFaturamentoService.formataNumero = function(numero, qtdCasasDecimais) {
        let formato = "";
        if (numero.toString().length < qtdCasasDecimais) {
            for (let x = 0; x < (qtdCasasDecimais - numero.toString().length); x++) {
                formato += "0";
            }
        }
        return formato + numero;
    };

    calculadorFaturamentoService.isCampoVazio = function(campo) {
        if (angular.isUndefined(campo) || campo === null || campo === '') {
            return true;
        } else {
            return false;
        }
    };

    calculadorFaturamentoService.isCampoVazioNumerico = function(campo) {
        if (angular.isUndefined(campo) || campo === null || campo === '' || isNaN(campo) || campo === Number.POSITIVE_INFINITY || campo === Number.NEGATIVE_INFINITY) {
            return true;
        } else {
            return false;
        }
    };

    calculadorFaturamentoService.montarCampoAgenciaConta = function(contaCorrente) {
        if (contaCorrente) {
            let label = contaCorrente.agencia;
            if (contaCorrente.digitoAgencia != null && contaCorrente.digitoAgencia != '' && contaCorrente.digitoAgencia != ' ') {
                label+= '-' + contaCorrente.digitoAgencia;
            }
            label+= ' / ' + contaCorrente.conta;
            if (contaCorrente.digito != null && contaCorrente.digito != '' && contaCorrente.digito != ' ' && !contaCorrente.conta.includes("-")) {
                label+= '-' + contaCorrente.digito;
            }
            return label;
        }
    };

    return calculadorFaturamentoService;
}])

// Foi necessário repetir o código.
.factory('MotivoRetornoDataCancelamentoDialog', ['$mdDialog', 'MotivoRetorno', '$rootScope', function($mdDialog, MotivoRetorno, $rootScope) {
    let service = {};

    function DialogControllerMotivoRetorno($scope, $mdDialog, chave, listMotivos, titulo, fnConfirmar, fnCancelar) {
       // MotivoRetornoDocumentoTO
       $scope.object = {};
       $scope.object.observacao;
       $scope.object.pessoa;
       $scope.object.aplicacao = {nome: '', chave: chave};
       $scope.titulo = titulo;
       $scope.action = false;
       $scope.showDataCancelamento = true;
       $scope.object.dataCancelamento = new Date();

       // Como a data não aparecia mesmo inicializando ela,
       // foi usado o trecho de código abaixo para força o preenchimento do campo 'Cancelamento' ao iniciar o Dialog .
       setTimeout(() => {
          $scope.object.dataCancelamento = new Date();
       }, 0);

       let msg = 'Adicione um motivo...';
       $scope.msg = msg;

       $scope.listMotivos = listMotivos.content;

       $scope.listMotivos.sort(function(a, b) {
                             if (a.motivo > b.motivo) {
                                 return 1;
                               }
                               if (a.motivo < b.motivo) {
                                 return -1;
                               }
                               return 0;
                           });

       $scope.object.motivoSelecionado = {};
       $scope.listMotivosSelecionados = [];

       $scope.$on('changeComboMotivo', function() {
            angular.forEach($scope.listMotivos, function(value, key) {
                if ($scope.object.motivoSelecionado.codigo === value.codigo) {
                    $scope.listMotivos.splice(key, 1)
                }
            });

            $scope.listMotivosSelecionados.push(angular.copy($scope.object.motivoSelecionado));

            delete $scope.object.motivoSelecionado;

            if ($scope.listMotivos.length === 0) {
                delete $scope.msg;
            }
       });

       $scope.removeMotivo = function(motivo) {

           angular.forEach($scope.listMotivosSelecionados, function(value, key) {
               if (motivo.codigo === value.codigo) {
                   $scope.listMotivosSelecionados.splice(key, 1)
               }
           });

           $scope.listMotivos.push(angular.copy(motivo));

           $scope.listMotivos.sort(function(a, b) {
                                if (a.motivo > b.motivo) {
                                    return 1;
                                  }
                                  if (a.motivo < b.motivo) {
                                    return -1;
                                  }
                                  return 0;
                              });

           if ($scope.listMotivos.length > 0) {
               $scope.msg = msg;
           }
       };

       $scope.hide = function() {
          $mdDialog.hide();
       };

       $scope.cancel = function() {
//      $mdDialog.cancel();
        fnCancelar($scope.hide);
       };

       $scope.confirm = function() {
           $scope.action = true;
           $scope.object.listaMotivoRetorno = [];
           $scope.object.dataCancelamento = new Date($scope.object.dataCancelamento);
           angular.forEach($scope.listMotivosSelecionados, function(value, key) {
                let motivoDocumento = {};
                motivoDocumento.motivoRetorno = value;
                $scope.object.listaMotivoRetorno.push(motivoDocumento);
           });

           fnConfirmar($scope.object, $scope.hide);
       };

    }

    service.show = function(event, chaveMotivo, titulo, fnConfirmar, fnCancelar) {
        $rootScope.loadingProgress = true;

        let filtro = {motivoAplicacao: {chave: chaveMotivo}};

        MotivoRetorno.page(null, filtro)
             .$promise.then(function(data) {
                    $rootScope.loadingProgress = false;
                    $mdDialog.show({
                        controller: DialogControllerMotivoRetorno,
                        locals: {
                             $mdDialog: $mdDialog,
                             chave: chaveMotivo,
                             listMotivos: data,
                             titulo: titulo,
                             fnConfirmar: fnConfirmar,
                             fnCancelar: fnCancelar
                        },
                        templateUrl: 'partials/dialogs/motivo.html',
                        parent: angular.element(document.body),
                        targetEvent: event,
                        clickOutsideToClose: false,
                        fullscreen: false
                    });
            }).catch(function(response) {
                $rootScope.loadingProgress = false;
                $rootScope.$emit('crud-critical', 'load', response);
            });
    };

    return service;
}])

.factory('MotivoRetornoDialog', ['$mdDialog', 'MotivoRetorno', '$rootScope', function($mdDialog, MotivoRetorno, $rootScope) {
    let service = {};

    function DialogControllerMotivoRetorno($scope, $mdDialog, chave, listMotivos, titulo, fnConfirmar, fnCancelar) {
       // MotivoRetornoDocumentoTO
       $scope.object = {};
       $scope.object.observacao;
       $scope.object.pessoa;
       $scope.object.aplicacao = {nome: '', chave: chave};
       $scope.titulo = titulo;
       $scope.action = false;

       let msg = 'Adicione um motivo...';
       $scope.msg = msg;

       $scope.listMotivos = listMotivos.content;

       $scope.listMotivos.sort(function(a, b) {
                             if (a.motivo > b.motivo) {
                                 return 1;
                               }
                               if (a.motivo < b.motivo) {
                                 return -1;
                               }
                               return 0;
                           });

       $scope.object.motivoSelecionado = {};
       $scope.listMotivosSelecionados = [];

       $scope.$on('changeComboMotivo', function() {
            angular.forEach($scope.listMotivos, function(value, key) {
                if ($scope.object.motivoSelecionado.codigo === value.codigo) {
                    $scope.listMotivos.splice(key, 1)
                }
            });

            $scope.listMotivosSelecionados.push(angular.copy($scope.object.motivoSelecionado));

            delete $scope.object.motivoSelecionado;

            if ($scope.listMotivos.length === 0) {
                delete $scope.msg;
            }
       });

       $scope.removeMotivo = function(motivo) {

           angular.forEach($scope.listMotivosSelecionados, function(value, key) {
               if (motivo.codigo === value.codigo) {
                   $scope.listMotivosSelecionados.splice(key, 1)
               }
           });

           $scope.listMotivos.push(angular.copy(motivo));

           $scope.listMotivos.sort(function(a, b) {
                                if (a.motivo > b.motivo) {
                                    return 1;
                                  }
                                  if (a.motivo < b.motivo) {
                                    return -1;
                                  }
                                  return 0;
                              });

           if ($scope.listMotivos.length > 0) {
               $scope.msg = msg;
           }
       };

       $scope.hide = function() {
          $mdDialog.hide();
       };
       $scope.cancel = function() {
//          $mdDialog.cancel();
        fnCancelar($scope.hide);
       };

       $scope.confirm = function() {
           $scope.action = true;
           $scope.object.listaMotivoRetorno = [];

           angular.forEach($scope.listMotivosSelecionados, function(value, key) {
                let motivoDocumento = {};

//                motivoDocumento.motivoRetornoDocumento = $scope.object;
                motivoDocumento.motivoRetorno = value;

                $scope.object.listaMotivoRetorno.push(motivoDocumento);
           });

           fnConfirmar($scope.object, $scope.hide);
       };

    }

    service.show = function(event, chaveMotivo, titulo, fnConfirmar, fnCancelar) {
        $rootScope.loadingProgress = true;

        let filtro = {motivoAplicacao: {chave: chaveMotivo}};

        MotivoRetorno.page(null, filtro)
             .$promise.then(function(data) {
                    $rootScope.loadingProgress = false;
                    $mdDialog.show({
                        controller: DialogControllerMotivoRetorno,
                        locals: {
                             $mdDialog: $mdDialog,
                             chave: chaveMotivo,
                             listMotivos: data,
                             titulo: titulo,
                             fnConfirmar: fnConfirmar,
                             fnCancelar: fnCancelar
                        },
                        templateUrl: 'partials/dialogs/motivo.html',
                        parent: angular.element(document.body),
                        targetEvent: event,
                        clickOutsideToClose: false,
                        fullscreen: false
                    });
            }).catch(function(response) {
                $rootScope.loadingProgress = false;
                $rootScope.$emit('crud-critical', 'load', response);
            });
    };

    return service;
}])

.factory("AuthenticateService", ['$http', '$window', '$rootScope', 'Usuario', '$location', 'toastr', function($http, $window, $rootScope, Usuario, $location, toastr) {
        let service = {};

        let authenticate = function() {
            Usuario.usuarioLogado().$promise.then(function(data) {
                if (angular.isDefined(data.login) && data.login != null) {
                    $rootScope.authenticated = true;
                } else {
                    $rootScope.authenticated = false;
                    $window.location='../login.html';
//                    $window.location='/sgi/spring_security_login?service=http://homolog.fasolti.com.br/sgi/app/main.html#!/home';
                }
            }).catch(function(response) {
                $rootScope.loadingProgress = false;
                $rootScope.authenticated = false;
                console.error(response);
                toastr.error('Error',response,{allowHtml: true});
            });
        }

        service.checkLogin = function() {
            authenticate();
        };

        return service;
}])

.factory("ServicoAbaTributosCAR", [ 'CalculadorFaturamento', function(CalculadorFaturamento) {
    let service = {};

    // DEPRECATED //
    service.getValorTributo = function (valorParcela, porcentagem) {
        let resultado = (Number(valorParcela) * Number(porcentagem)) / 100;
        return resultado;
    };

    // DEPRECATED //
    service.setValorNaLista = function (valorParcela, listaTributos) {
        angular.forEach(listaTributos, function(item, key) {
            if (CalculadorFaturamento.isCampoVazio(item.valor)) {
                item.valor = 0;
                item.valor = service.getValorTributo(valorParcela, item.aliquota.percentual);
            }
        });
    };

    //Verificar se a lista tem tributos que não sejam ISS.
    service.verificarListaTemTributos = function (listaTributos) {
        let tamanho = 0;
        tamanho = listaTributos.length;

        angular.forEach(listaTributos, function(item, key) {

            let codigoTributo = item.categoriaTributo.tributoTO.codigo;

            if (codigoTributo === 4) {
                tamanho--;
            }
        });

        if (tamanho > 0) {
            return true;
        } else {
            return false;
        }
    };

    service.verificarListaTemTributosComIss = function (listaTributos) {
        let resultado = false;

        angular.forEach(listaTributos, function(item, key) {
            let codigoTributo = item.categoriaTributo.tributoTO.codigo;
            if (codigoTributo === 4) {
                resultado = true;
            }
        });

        return resultado;
    };

    service.getTotalValorTributos = function (listaTributos) {
        let resultado = 0;

        angular.forEach(listaTributos, function(item, key) {
            resultado += Number(item.valor);
        });

        return resultado;
    };

    service.getSubtotalValorTributos = function (listaTributos) {
        let resultado = 0;

        angular.forEach(listaTributos, function(item, key) {

            let codigoTributo = item.categoriaTributo.tributoTO.codigo;
            if (codigoTributo !== 4) {
                resultado += Number(item.valor);
            }
        });

        return resultado;
    };

    service.getValorTributoPIS = function (listaTributos) {
		let resultado = 0;

		angular.forEach(listaTributos, function(item, key) {

			let codigoTributo = item.categoriaTributo.tributoTO.codigo;
			if (codigoTributo === 1) {
				resultado = Number(item.valor);
			}
		});

		return resultado;
	};

	service.getValorTributoIRRF = function (listaTributos) {
		let resultado = 0;

		angular.forEach(listaTributos, function(item, key) {

			let codigoTributo = item.categoriaTributo.tributoTO.codigo;
			if (codigoTributo === 5) {
				resultado = Number(item.valor);
			}
		});

		return resultado;
	};

	service.getValorTributoISS = function (listaTributos) {
		let resultado = 0;

		angular.forEach(listaTributos, function(item, key) {

			let codigoTributo = item.categoriaTributo.tributoTO.codigo;
			if (codigoTributo === 4) {
				resultado = Number(item.valor);
			}
		});

		return resultado;
	};

	service.getValorTributoINSS = function (listaTributos) {
		let resultado = 0;

		angular.forEach(listaTributos, function(item, key) {

			let codigoTributo = item.categoriaTributo.tributoTO.codigo;
			if (codigoTributo === 6) {
				resultado = Number(item.valor);
			}
		});

		return resultado;
	};

    service.getSubtotalPercentualTributos = function (listaTributos) {
        let resultado = 0;

        angular.forEach(listaTributos, function(item, key) {

            let codigoTributo = item.categoriaTributo.tributoTO.codigo;
            if (codigoTributo !== 4) {
                resultado += Number(item.aliquota.percentual);
            }
        });

        return resultado;
    };

    service.getCidadeIss = function (listaTributos) {
        let resultado = null;

        angular.forEach(listaTributos, function(item, key) {

            let codigoTributo = item.categoriaTributo.tributoTO.codigo;
            if (codigoTributo === 4) {
                resultado = item.categoriaTributo.cidade.uf + '/' + item.categoriaTributo.cidade.nome;
            }

        });

        return resultado;
    };

    return service;
}])

.factory("ServicoDialogTributoCAR", ['$mdDialog', 'Tributo', 'CategoriaTributo', 'CalculadorFaturamento', function($mdDialog, Tributo, CategoriaTributo, CalculadorFaturamento) {
    let service = {};

    service.excluirTributo = function (tributo, listaTributos) {
        let index = listaTributos.indexOf(tributo);
        listaTributos.splice(index, 1);
    };

    //PARÂMETROS: 1- $event / 2- lista de tributos / 3 - parcela / 4 - variacaoTributo / 5 - função
    service.adicionarTributo = function (ev, listaTributos, parcela, variacaoTributo, fnInitAbaTributos) {
        $mdDialog.show({
            controller: ControllerNovoTributo,
            locals: {
                listaTributos: listaTributos,
                parcela: parcela,
                variacaoTributo: variacaoTributo,
                fnInitAbaTributos: fnInitAbaTributos

            },
            templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/tributo.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    //PARÂMETROS: 1- $event / 2 - cidade iss  / 3 - lista de tributos / 4 - parcela / 5 - variacao do tributo / 6 - função
    service.adicionarTributoIss = function (ev, cidadeIss, listaTributos, parcela, variacaoTributo, fnInitAbaTributos) {
        $mdDialog.show({
            controller: ControllerNovoTributoIss,
            locals: {
                cidadeIss: cidadeIss,
                listaTributos: listaTributos,
                parcela: parcela,
                variacaoTributo: variacaoTributo,
                fnInitAbaTributos: fnInitAbaTributos

            },
            templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/tributo_iss.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    //PARÂMETROS: 1- $event / 2 - tributo / 3 - lista de tributos / 4 - parcela / 5 - variacaoTributo / 6 - função
    service.editarTributo = function (ev, tributo, listaTributos, parcela, variacaoTributo, fnInitAbaTributos) {
        $mdDialog.show({
            controller: ControllerEdicaoTributo,
            locals: {
                tributo: tributo,
                listaTributos: listaTributos,
                parcela: parcela,
                variacaoTributo: variacaoTributo,
                fnInitAbaTributos: fnInitAbaTributos

            },
            templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/tributo.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    //PARÂMETROS: 1- $event / 2 - tributo / 3 - lista de tributos / 4 - parcela / 5 - variacaoTributo / 6 - função
    service.editarTributoIss = function (ev, tributo, listaTributos, parcela, variacaoTributo, fnInitAbaTributos) {
        $mdDialog.show({
            controller: ControllerEdicaoTributoIss,
            locals: {
                tributo: tributo,
                listaTributos: listaTributos,
                parcela: parcela,
                variacaoTributo: variacaoTributo,
                fnInitAbaTributos: fnInitAbaTributos

            },
            templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/tributo_iss.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    // CONTROLLER PARA DIALOG DE ADIÇÃO DE TRIBUTO
    function ControllerNovoTributo ($scope, $mdDialog, Tributo, CategoriaTributo, CalculadorFaturamento, listaTributos, parcela, variacaoTributo, fnInitAbaTributos) {

        init();

        function init() {
            $scope.modo = "Novo";

            $scope.novoTributo = {};
            $scope.novoTributo.baseCalculo = 0;
            $scope.novoTributo.percentual = 0;
            $scope.novoTributo.valor = 0;
            $scope.novoTributo.aliquota = {};
            $scope.novoTributo.categoriaTributo = null;
            $scope.novoTributo.validaPrefeitura = false;
            $scope.novoTributo.individual = 0;
            $scope.novoTributo.nota = parcela.notaSaida;

            $scope.tributos = [];
            $scope.listaCategoriaTributo = [];
            $scope.Tributo = Tributo;
            $scope.CategoriaTributo = CategoriaTributo;
            $scope.objetoTributo = null;
            $scope.copiaValor = 0;

            //Lista nota_tributo_aliquota
            $scope.listaTributos = listaTributos;
            buscarTributos();

            $scope.campoCategoriaTributoDesabilitado = true;

            $scope.parcela = parcela;
            $scope.faturamento = parcela.notaSaida.faturamentoEtapaTO.faturamento;
            $scope.variacaoTributo = variacaoTributo;

            $scope.valorValidoMin = true;
            $scope.valorValidoMax = true;

			// Tem que ser o valor da parcela
            $scope.valorNotaFiscal = parcela.valorBruto;

        }

        $scope.$watch('novoTributo.valor', function() {
            $scope.valorValidoMin = $scope.novoTributo.valor < $scope.minValor();
            $scope.valorValidoMax = $scope.novoTributo.valor > $scope.maxValor();
        });

        $scope.minValor = function() {
            return $scope.copiaValor - $scope.variacaoTributo;
        };

        $scope.maxValor = function() {
            return $scope.copiaValor + $scope.variacaoTributo;
        };

        $scope.$watch('novoTributo.baseCalculo', function() {
            if (!$scope.isCampoVazio($scope.novoTributo.aliquota) && !$scope.isCampoVazioNumerico($scope.novoTributo.baseCalculo)) {
                $scope.novoTributo.valor = $scope.novoTributo.baseCalculo * ($scope.getPercentualAliquota($scope.novoTributo.categoriaTributo) / 100);
                $scope.copiaValor = angular.copy($scope.novoTributo.valor);
            }
        });

        function buscarTributos() {

            let filtroTributo = {};
            filtroTributo.tipo = "FEDERAL";
            filtroTributo.exibeNota = true;

            $scope.Tributo.page(null, filtroTributo).$promise.then(function(data) {

                $scope.tributos = data.content;

                angular.forEach($scope.listaTributos, function(item, key) {
                    let codTributo = item.categoriaTributo.tributoTO.codigo;
                    let indexTributo = null;

                    angular.forEach($scope.tributos, function(objTributo, objKey) {
                        if (objTributo.codigo === codTributo) {
                            indexTributo = $scope.tributos.indexOf(objTributo);
                        }

                    });

                    if (indexTributo != null) {
                        $scope.tributos.splice(indexTributo, 1);
                        indexTributo = null;
                    }

                });
            });
        }

        $scope.$on('comboTributoNome', function() {
            $scope.buscarCategoriaTributos($scope.objetoTributo.codigo);

            if(!$scope.isCampoVazio($scope.objetoTributo)) {
                $scope.campoCategoriaTributoDesabilitado = false;
            }

            $scope.novoTributo.aliquota = {};
            $scope.novoTributo.categoriaTributo = null;

            $scope.novoTributo.percentual = 0;
            $scope.novoTributo.baseCalculo = 0;
            $scope.novoTributo.valor = 0;
            $scope.copiaValor = 0;

        });

        $scope.$on('comboCategoriaTributo', function() {

            $scope.novoTributo.aliquota = $scope.getAliquota($scope.novoTributo.categoriaTributo);
            $scope.novoTributo.percentual = angular.copy($scope.novoTributo.aliquota.percentual);

            //tributo INSS
            if ($scope.objetoTributo.codigo === 6) {
                $scope.novoTributo.baseCalculo = $scope.valorNotaFiscal * ($scope.faturamento.percentualServico / 100);
            } else {
                $scope.novoTributo.baseCalculo = angular.copy($scope.valorNotaFiscal);
            }

            $scope.novoTributo.valor = $scope.novoTributo.baseCalculo * ($scope.getPercentualAliquota($scope.novoTributo.categoriaTributo) / 100);

            $scope.copiaValor = angular.copy($scope.novoTributo.valor);

        });

        $scope.buscarCategoriaTributos = function (codigoTributo) {
            let filtroCategoriaTributo = {};
            filtroCategoriaTributo.tributoTO = {};
            filtroCategoriaTributo.tributoTO.codigo = codigoTributo;
            filtroCategoriaTributo.habilitaFaturamento = true;

            $scope.CategoriaTributo.page(null, filtroCategoriaTributo)
                .$promise.then(function(data) {
                    if (data && data) {
						let listaOrdenada = data.content.sort(function(a, b) {
							return $scope.getPercentualAliquota(a) - $scope.getPercentualAliquota(b);
						});

						$scope.listaCategoriaTributo = listaOrdenada;
					}
                }).catch(function(response) {
                    console.error('crud-critical', 'dialog', response);
//                    notification.criticalError(response.status, response.statusText, response.data);
                });
        };


        // Retorna percentual de uma aliquota relacionada ao "Categoria Tributo" passado pelo argumento
        $scope.getPercentualAliquota = function(argCategoriaTributo) {

            if (!$scope.isCampoVazio(argCategoriaTributo)) {
                if (!$scope.isCampoVazio(argCategoriaTributo.aliquotas)) {
                    if (!$scope.isCampoVazio(argCategoriaTributo.aliquotas[0])) {
                        return argCategoriaTributo.aliquotas[0].percentual;
                    }
                }
            }
            return 0;
        };

        // Pega a aliquota da categoria tributo
        $scope.getAliquota = function(argCategoriaTributo) {

            let aliquota = {};

            if (angular.isDefined(argCategoriaTributo)) {
                if (angular.isDefined(argCategoriaTributo.aliquotas)) {
                    if (angular.isDefined(argCategoriaTributo.aliquotas[0])) {
                        aliquota = argCategoriaTributo.aliquotas[0];
                    }
                }
            }
            return aliquota;
        };

        //Funções para validações
        $scope.isCampoVazio = function (campo) {
            return CalculadorFaturamento.isCampoVazio(campo);
        };
        $scope.isCampoVazioNumerico = function (campo) {
            return CalculadorFaturamento.isCampoVazioNumerico(campo);
        };

        //Fechar dialog
        $scope.hide = function() {
            $mdDialog.hide();
        };

        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        //Salvar tributo
        $scope.salvar = function() {
            $scope.listaTributos.push($scope.novoTributo);

            fnInitAbaTributos($scope.listaTributos);
            $mdDialog.hide();
        }


    } // FIM CONTROLLER DIALOG ADIÇÃO DO TRIBUTO


    // CONTROLLER PARA DIALOG DE EDIÇÃO DO TRIBUTO
    function ControllerEdicaoTributo ($scope, $mdDialog, Tributo, CategoriaTributo, CalculadorFaturamento, tributo, listaTributos, parcela, variacaoTributo, fnInitAbaTributos) {

        init();

        function init() {
            $scope.modo = "Edição";

            $scope.tributoOriginal = tributo;
            $scope.novoTributo = angular.copy($scope.tributoOriginal);

            $scope.objetoTributo = angular.copy($scope.novoTributo.categoriaTributo.tributoTO);
            $scope.copiaValor = angular.copy($scope.novoTributo.valor);

            $scope.tributos = [];
            $scope.listaCategoriaTributo = [];
            $scope.Tributo = Tributo;
            $scope.CategoriaTributo = CategoriaTributo;

            //Lista nota_tributo_aliquota
            $scope.listaTributos = listaTributos;
            buscarTributos();
            buscarCategoriaTributos($scope.novoTributo.categoriaTributo.tributoTO.codigo);

            $scope.campoCategoriaTributoDesabilitado = false;

            $scope.parcela = parcela;
            $scope.faturamento = parcela.notaSaida.faturamentoEtapaTO.faturamento;
            $scope.variacaoTributo = variacaoTributo;

            $scope.valorValidoMin = true;
            $scope.valorValidoMax = true;

            $scope.iniciandoWatchBaseCalculo = true;

            $scope.valorNotaFiscal = parcela.notaSaida.valor;

            //atributo para evitar que o $scope.$on seja chamado ao iniciar o dialog.
            $scope.codigoCategoriaTributoAnterior = angular.copy($scope.novoTributo.categoriaTributo.codigo);
        }

        $scope.$watch('novoTributo.valor', function() {
            $scope.valorValidoMin = $scope.novoTributo.valor < $scope.minValor();
            $scope.valorValidoMax = $scope.novoTributo.valor > $scope.maxValor();
        });

        $scope.minValor = function() {
            return $scope.copiaValor - $scope.variacaoTributo;
        };

        $scope.maxValor = function() {
            return $scope.copiaValor + $scope.variacaoTributo;
        };

        $scope.$watch('novoTributo.baseCalculo', function() {
            if ($scope.iniciandoWatchBaseCalculo) {
                $scope.iniciandoWatchBaseCalculo = false;
            } else {
                if (!$scope.isCampoVazio($scope.novoTributo.aliquota) && !$scope.isCampoVazioNumerico($scope.novoTributo.baseCalculo)) {
                    $scope.novoTributo.valor = $scope.novoTributo.baseCalculo * ($scope.getPercentualAliquota($scope.novoTributo.categoriaTributo) / 100);
                    $scope.copiaValor = angular.copy($scope.novoTributo.valor);
                }
            }
        });

        function buscarTributos() {

            let filtroTributo = {};
            filtroTributo.tipo = "FEDERAL";
            filtroTributo.exibeNota = true;

            $scope.Tributo.page(null, filtroTributo).$promise.then(function(data) {

                $scope.tributos = data.content;

                angular.forEach($scope.listaTributos, function(item, key) {
                    let codTributo = item.categoriaTributo.tributoTO.codigo;
                    let indexTributo = null;

                    angular.forEach($scope.tributos, function(objTributo, objKey) {

                        let tributoOriginal = $scope.novoTributo.categoriaTributo.tributoTO;

                        if (objTributo.codigo === codTributo && objTributo.codigo != tributoOriginal.codigo) {
                            indexTributo = $scope.tributos.indexOf(objTributo);
                        }

                    });

                    if (indexTributo != null) {
                        $scope.tributos.splice(indexTributo, 1);
                        indexTributo = null;
                    }

                });
            });
        }

        $scope.$on('comboTributoNome', function() {

            if ($scope.isCampoVazio($scope.novoTributo.categoriaTributo) ||
                ($scope.objetoTributo.codigo !== $scope.novoTributo.categoriaTributo.tributoTO.codigo)) {

                $scope.novoTributo.aliquota = {};
                $scope.novoTributo.categoriaTributo = null;

                $scope.novoTributo.percentual = 0;
                $scope.novoTributo.baseCalculo = 0;
                $scope.novoTributo.valor = 0;
                $scope.copiaValor = 0;

                buscarCategoriaTributos($scope.objetoTributo.codigo);
            }

        });

        $scope.$on('comboCategoriaTributo', function() {

            if ($scope.codigoCategoriaTributoAnterior != $scope.novoTributo.categoriaTributo.codigo) {

                $scope.novoTributo.aliquota = $scope.getAliquota($scope.novoTributo.categoriaTributo);
                $scope.novoTributo.percentual = angular.copy($scope.novoTributo.aliquota.percentual);

                $scope.gerarValoresAutomaticos();

                $scope.codigoCategoriaTributoAnterior = angular.copy($scope.novoTributo.categoriaTributo.codigo);
            }

        });

        // Gera os valores automaticos para os campos "base de calculo" e "valor".
        $scope.gerarValoresAutomaticos = function () {

            //tributo INSS
            /*if ($scope.objetoTributo.codigo === 6) {
                $scope.novoTributo.baseCalculo = $scope.valorNotaFiscal * ($scope.faturamento.percentualServico / 100);
            } else {
                $scope.novoTributo.baseCalculo = angular.copy($scope.valorNotaFiscal);
            }*/

            $scope.novoTributo.valor = $scope.novoTributo.baseCalculo * ($scope.getPercentualAliquota($scope.novoTributo.categoriaTributo) / 100);
            $scope.copiaValor = angular.copy($scope.novoTributo.valor);
        };

        function buscarCategoriaTributos (codigoTributo) {
            let filtroCategoriaTributo = {};
            filtroCategoriaTributo.tributoTO = {};
            filtroCategoriaTributo.tributoTO.codigo = codigoTributo;
            filtroCategoriaTributo.habilitaFaturamento = true;

            $scope.CategoriaTributo.page(null, filtroCategoriaTributo)
                .$promise.then(function(data) {
                    if (data && data) {
						let listaOrdenada = data.content.sort(function(a, b) {
							return $scope.getPercentualAliquota(a) - $scope.getPercentualAliquota(b);
						});

						$scope.listaCategoriaTributo = listaOrdenada;
					}
                }).catch(function(response) {
                    console.error('crud-critical', 'dialog', response);
//                    notification.criticalError(response.status, response.statusText, response.data);
                });
        }

        // Retorna percentual de uma aliquota relacionada ao "Categoria Tributo" passado pelo argumento
        $scope.getPercentualAliquota = function(argCategoriaTributo) {

            if (!$scope.isCampoVazio(argCategoriaTributo)) {
                if (!$scope.isCampoVazio(argCategoriaTributo.aliquotas)) {
                    if (!$scope.isCampoVazio(argCategoriaTributo.aliquotas[0])) {
                        return argCategoriaTributo.aliquotas[0].percentual;
                    }
                }
            }
            return 0;
        };

        // Pega a aliquota da categoria tributo
        $scope.getAliquota = function(argCategoriaTributo) {

            let aliquota = {};

            if (angular.isDefined(argCategoriaTributo)) {
                if (angular.isDefined(argCategoriaTributo.aliquotas)) {
                    if (angular.isDefined(argCategoriaTributo.aliquotas[0])) {
                        aliquota = argCategoriaTributo.aliquotas[0];
                    }
                }
            }
            return aliquota;
        };

        //Funções para validações
        $scope.isCampoVazio = function (campo) {
            return CalculadorFaturamento.isCampoVazio(campo);
        };
        $scope.isCampoVazioNumerico = function (campo) {
            return CalculadorFaturamento.isCampoVazioNumerico(campo);
        };

        //Fechar dialog
        $scope.hide = function() {
            $mdDialog.hide();
        };

        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        //Salvar tributo
        $scope.salvar = function() {
            angular.merge($scope.tributoOriginal, $scope.novoTributo);

            fnInitAbaTributos($scope.listaTributos);
            $mdDialog.hide();
        }


    } // FIM CONTROLLER DIALOG EDIÇÃO TRIBUTO


    // CONTROLLER PARA DIALOG DE ADIÇÃO DE TRIBUTO ISS
    function ControllerNovoTributoIss ($scope, $mdDialog, CategoriaTributo, CalculadorFaturamento, cidadeIss, listaTributos, parcela, variacaoTributo, fnInitAbaTributos) {

        init();

        function init() {
            $scope.modo = "Novo";

            $scope.novoTributo = {};
            $scope.novoTributo.baseCalculo = 0;
            $scope.novoTributo.percentual = 0;
            $scope.novoTributo.valor = 0;
            $scope.novoTributo.aliquota = {};
            $scope.novoTributo.categoriaTributo = null;
            $scope.novoTributo.validaPrefeitura = false;
            $scope.novoTributo.individual = 0;
            $scope.novoTributo.nota = parcela.notaSaida;

            $scope.listaCategoriaTributo = [];
            $scope.CategoriaTributo = CategoriaTributo;
            $scope.copiaValor = 0;
            $scope.cidadeIss = cidadeIss;

            //Lista nota_tributo_aliquota
            $scope.listaTributos = listaTributos;
            buscarCategoriaTributos();

            $scope.parcela = parcela;
            $scope.variacaoTributo = variacaoTributo;

            $scope.valorValidoMin = true;
            $scope.valorValidoMax = true;

            $scope.valorNotaFiscal = parcela.notaSaida.valor;
        }

        $scope.$watch('novoTributo.valor', function() {
            $scope.valorValidoMin = $scope.novoTributo.valor < $scope.minValor();
            $scope.valorValidoMax = $scope.novoTributo.valor > $scope.maxValor();
        });

        $scope.minValor = function() {
            return $scope.copiaValor - $scope.variacaoTributo;
        };

        $scope.maxValor = function() {
            return $scope.copiaValor + $scope.variacaoTributo;
        };

        $scope.$watch('novoTributo.baseCalculo', function() {
            if (!$scope.isCampoVazio($scope.novoTributo.aliquota) && !$scope.isCampoVazioNumerico($scope.novoTributo.baseCalculo)) {
                $scope.novoTributo.valor = $scope.novoTributo.baseCalculo * ($scope.getPercentualAliquota($scope.novoTributo.categoriaTributo) / 100);
                $scope.copiaValor = angular.copy($scope.novoTributo.valor);
            }
        });

        $scope.$on('comboCategoriaTributo', function() {

            $scope.novoTributo.aliquota = $scope.getAliquota($scope.novoTributo.categoriaTributo);
            $scope.novoTributo.percentual = angular.copy($scope.novoTributo.aliquota.percentual);

            $scope.novoTributo.baseCalculo = angular.copy($scope.valorNotaFiscal);

            $scope.novoTributo.valor = $scope.novoTributo.baseCalculo * ($scope.getPercentualAliquota($scope.novoTributo.categoriaTributo) / 100);

            $scope.copiaValor = angular.copy($scope.novoTributo.valor);

        });

        function buscarCategoriaTributos() {
            let filtroCategoriaTributo = {};
            filtroCategoriaTributo.cidade = {};
            filtroCategoriaTributo.cidade.uf = $scope.cidadeIss.uf;
            filtroCategoriaTributo.cidade.codigo = $scope.cidadeIss.codigo;
            filtroCategoriaTributo.habilitaFaturamento = true;

            $scope.CategoriaTributo.page(null, filtroCategoriaTributo)
                .$promise.then(function(data) {
                    $scope.listaCategoriaTributo = data.content;
                }).catch(function(response) {
                    console.error('crud-critical', 'dialog', response);
//                    notification.criticalError(response.status, response.statusText, response.data);
                });
        }


        // Retorna percentual de uma aliquota relacionada ao "Categoria Tributo" passado pelo argumento
        $scope.getPercentualAliquota = function(argCategoriaTributo) {

            if (!$scope.isCampoVazio(argCategoriaTributo)) {
                if (!$scope.isCampoVazio(argCategoriaTributo.aliquotas)) {
                    if (!$scope.isCampoVazio(argCategoriaTributo.aliquotas[0])) {
                        return argCategoriaTributo.aliquotas[0].percentual;
                    }
                }
            }
            return 0;
        };

        // Pega a aliquota da categoria tributo
        $scope.getAliquota = function(argCategoriaTributo) {

            let aliquota = {};

            if (angular.isDefined(argCategoriaTributo)) {
                if (angular.isDefined(argCategoriaTributo.aliquotas)) {
                    if (angular.isDefined(argCategoriaTributo.aliquotas[0])) {
                        aliquota = argCategoriaTributo.aliquotas[0];
                    }
                }
            }
            return aliquota;
        };

        //Funções para validações
        $scope.isCampoVazio = function (campo) {
            return CalculadorFaturamento.isCampoVazio(campo);
        };
        $scope.isCampoVazioNumerico = function (campo) {
            return CalculadorFaturamento.isCampoVazioNumerico(campo);
        };

        //Fechar dialog
        $scope.hide = function() {
            $mdDialog.hide();
        };

        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        //Salvar tributo
        $scope.salvar = function() {
            $scope.listaTributos.push($scope.novoTributo);

            fnInitAbaTributos($scope.listaTributos);
            $mdDialog.hide();
        }


    } // FIM CONTROLLER DIALOG ADIÇÃO DO TRIBUTO ISS


    // CONTROLLER PARA DIALOG DE EDIÇÃO DE TRIBUTO ISS
    function ControllerEdicaoTributoIss ($scope, $mdDialog, CategoriaTributo, CalculadorFaturamento, tributo, listaTributos, parcela, variacaoTributo, fnInitAbaTributos) {

        init();

        function init() {
            $scope.modo = "Edição";

            $scope.tributoOriginal = tributo;
            $scope.novoTributo = angular.copy($scope.tributoOriginal);
            $scope.copiaValor = angular.copy($scope.novoTributo.valor);

            $scope.listaCategoriaTributo = [];
            $scope.CategoriaTributo = CategoriaTributo;
            $scope.cidadeIss = angular.copy(tributo.categoriaTributo.cidade);

            //Lista nota_tributo_aliquota
            $scope.listaTributos = listaTributos;
            buscarCategoriaTributos();

            $scope.parcela = parcela;
            $scope.variacaoTributo = variacaoTributo;

            $scope.valorValidoMin = true;
            $scope.valorValidoMax = true;

            $scope.iniciandoWatchBaseCalculo = true;

            $scope.valorNotaFiscal = parcela.notaSaida.valor;

            //atributo para evitar que o $scope.$on seja chamado ao iniciar o dialog.
            $scope.codigoCategoriaTributoAnterior = angular.copy($scope.novoTributo.categoriaTributo.codigo);
        }

        $scope.$watch('novoTributo.valor', function() {
            $scope.valorValidoMin = $scope.novoTributo.valor < $scope.minValor();
            $scope.valorValidoMax = $scope.novoTributo.valor > $scope.maxValor();
        });

        $scope.minValor = function() {
            return $scope.copiaValor - $scope.variacaoTributo;
        };

        $scope.maxValor = function() {
            return $scope.copiaValor + $scope.variacaoTributo;
        };

        $scope.$watch('novoTributo.baseCalculo', function() {
            if ($scope.iniciandoWatchBaseCalculo) {
                $scope.iniciandoWatchBaseCalculo = false;
            } else {
                if (!$scope.isCampoVazio($scope.novoTributo.aliquota) && !$scope.isCampoVazioNumerico($scope.novoTributo.baseCalculo)) {
                    $scope.novoTributo.valor = $scope.novoTributo.baseCalculo * ($scope.getPercentualAliquota($scope.novoTributo.categoriaTributo) / 100);
                    $scope.copiaValor = angular.copy($scope.novoTributo.valor);
                }
            }
        });

        $scope.$on('comboCategoriaTributo', function() {

            if ($scope.codigoCategoriaTributoAnterior != $scope.novoTributo.categoriaTributo.codigo) {

                $scope.novoTributo.aliquota = $scope.getAliquota($scope.novoTributo.categoriaTributo);
                $scope.novoTributo.percentual = angular.copy($scope.novoTributo.aliquota.percentual);

                $scope.gerarValoresAutomaticos();

                $scope.codigoCategoriaTributoAnterior = angular.copy($scope.novoTributo.categoriaTributo.codigo);

            }


        });

        // Gera os valores automaticos para os campos "base de calculo" e "valor".
        $scope.gerarValoresAutomaticos = function () {
            $scope.novoTributo.valor = $scope.novoTributo.baseCalculo * ($scope.getPercentualAliquota($scope.novoTributo.categoriaTributo) / 100);
            $scope.copiaValor = angular.copy($scope.novoTributo.valor);
        };

        function buscarCategoriaTributos() {
            let filtroCategoriaTributo = {};
            filtroCategoriaTributo.cidade = {};
            filtroCategoriaTributo.cidade.uf = $scope.cidadeIss.uf;
            filtroCategoriaTributo.cidade.codigo = $scope.cidadeIss.codigo;
            filtroCategoriaTributo.habilitaFaturamento = true;

            $scope.CategoriaTributo.page(null, filtroCategoriaTributo)
                .$promise.then(function(data) {
                    $scope.listaCategoriaTributo = data.content;
                }).catch(function(response) {
                    console.error('crud-critical', 'dialog', response);
//                    notification.criticalError(response.status, response.statusText, response.data);
                });
        }

        // Retorna percentual de uma aliquota relacionada ao "Categoria Tributo" passado pelo argumento
        $scope.getPercentualAliquota = function(argCategoriaTributo) {

            if (!$scope.isCampoVazio(argCategoriaTributo)) {
                if (!$scope.isCampoVazio(argCategoriaTributo.aliquotas)) {
                    if (!$scope.isCampoVazio(argCategoriaTributo.aliquotas[0])) {
                        return argCategoriaTributo.aliquotas[0].percentual;
                    }
                }
            }
            return 0;
        };

        // Pega a aliquota da categoria tributo
        $scope.getAliquota = function(argCategoriaTributo) {

            let aliquota = {};

            if (angular.isDefined(argCategoriaTributo)) {
                if (angular.isDefined(argCategoriaTributo.aliquotas)) {
                    if (angular.isDefined(argCategoriaTributo.aliquotas[0])) {
                        aliquota = argCategoriaTributo.aliquotas[0];
                    }
                }
            }
            return aliquota;
        };

        //Funções para validações
        $scope.isCampoVazio = function (campo) {
            return CalculadorFaturamento.isCampoVazio(campo);
        };
        $scope.isCampoVazioNumerico = function (campo) {
            return CalculadorFaturamento.isCampoVazioNumerico(campo);
        };

        //Fechar dialog
        $scope.hide = function() {
            $mdDialog.hide();
        };

        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        //Salvar tributo
        $scope.salvar = function() {
            angular.merge($scope.tributoOriginal, $scope.novoTributo);

            fnInitAbaTributos($scope.listaTributos);
            $mdDialog.hide();
        }

    } // FIM CONTROLLER DIALOG EDIÇÃO DO TRIBUTO ISS

    return service;
}])

.factory("ServicoGerenciaUsuarioLogado", [ function() {
    let service = {};

    //obter gerencia cuja usuario está logado
    service.getGerenciaLogada = function () {
    let gerenciaLogada = [];
            CentroFinanceiro.getGerenciaLogada()
                 .$promise.then(function(data) {
                    $scope.gerenciaLogada = data.content;
        //                console.log(data);
                    $scope.buscarTodasGerencias();
                 }).catch(function(response) {
            $scope.$emit('crud-critical', 'search', response);
            });
        return gerenciaLogada;
        };


    //obter todas gerencias do sistema
    service.buscarTodasGerencias = function() {
    let gerenciaList = {};
    $scope.gerenciaList = Relatorios.buscarTodasGerencias();
        if (possuiPerfilContasReceber) {
            $scope.gerenciaList;
        } else {
            $scope.gerenciaList = [];
            $scope.gerenciaList.push($scope.gerenciaLogada);
            $scope.object.gerencia = $scope.gerenciaLogada;
        }
        return gerenciaList;
    };

    return service;
}])

.factory("BancosService", ['$rootScope', 'Banco', function($rootScope, Banco) {
        let service = {};

        function carregarBancos(comVazio) {
            return Banco.todos().$promise.then(function(data) {
                   bancoList = [];

                    if (comVazio) {
                        bancoList.push({banco: {nome : ''}});
                    }

                    angular.forEach(data, function(value, key) {
                        bancoList.push(value);
                    })
                }).catch(function(response) {
                    $rootScope.$emit('crud-critical', 'contrato load', response);
                    bancoList = [];
            });
        };

        service.obterLista = function(comVazio) {
            return carregarBancos(comVazio);
        };

        return service;
}])

.factory("String", [function() {

    let service = {};

    service.replaceAll = function (string, token, newtoken) {
        if (!string) {
            return '';
        }
    	while (string.indexOf(token) != -1) {
     		string = string.replace(token, newtoken);
    	}
    	return string;
    }

    return service;

}])


.factory("BoletoService", ['String', function(String) {

    // TESTE  - http://boletobancario-codigodebarras.blogspot.com.br/

    let CODIGO_DE_BARRAS_REGEX = /^([0-9]){44}$/;
    let LINHA_DIGITAVEL_NUMERICA_REGEX = /^([0-9]){47}$/;
    let DATA_BASE_FEBRABAN = "1997-10-08";

    let service = {};

    // LINHA DIGITÁVEL
    service.checkFormatoLinhaDigitavel = function(numeroLinhaDigitavel) {
        let linhaDigitavel = String.replaceAll(numeroLinhaDigitavel, '_', '');
        linhaDigitavel = String.replaceAll(numeroLinhaDigitavel, '.', '');
        return LINHA_DIGITAVEL_NUMERICA_REGEX.test(linhaDigitavel.trim());
    }

    service.fatorDeVencimentoLinhaDigitavelEmInteiro = function(linha) {
        let data_boleto = linha.substring(33,37);
        return parseInt(data_boleto);
    }

    service.valorBigDecimalDoTituloDaLinhaDigitavelFormatada = function(linhaDigitavel) {
        if (!service.checkFormatoLinhaDigitavel(linhaDigitavel)) {
            return null;
        }
        let valor = linhaDigitavel.trim().substring(37, 47);
        return parseFloat(valor)/100;
    }

    service.dataVencimentoLinhaDigitavel = function (linhaDigitavel) {
        if (!service.checkFormatoLinhaDigitavel(linhaDigitavel)) {
            return null;
        }

        let fator = service.fatorDeVencimentoLinhaDigitavelEmInteiro(linhaDigitavel);

        if (fator === 0) {
            return null;
        }

        let database = new Date(DATA_BASE_FEBRABAN);

        return new Date(database.setDate(database.getDate() + fator));
    }

    // CODIGO DE BARRAS
    service.checkFormatoCodigoDeBarras = function(numeroCodigoDeBarras) {
        if (numeroCodigoDeBarras === null) {
            return false;
        }
        numeroCodigoDeBarras = numeroCodigoDeBarras ? numeroCodigoDeBarras : '';
        let codigoDeBarras = String.replaceAll(numeroCodigoDeBarras, '_', '');
        codigoDeBarras = String.replaceAll(numeroCodigoDeBarras, '.', '');
        return CODIGO_DE_BARRAS_REGEX.test(codigoDeBarras.trim());
    }

    service.valorBigDecimalDoTituloDoCodigoDeBarras = function(numeroCodigoDeBarras) {
        if (!service.checkFormatoCodigoDeBarras(numeroCodigoDeBarras)) {
            return null;
        }
        let valor = numeroCodigoDeBarras.trim().substring(9, 19);
        return parseFloat(valor)/100;
    }

    service.fatorDeVencimentoDoCodigoDeBarrasEmInteiro = function(numeroCodigoDeBarras) {
        if (service.checkFormatoCodigoDeBarras(numeroCodigoDeBarras)) {
            return parseInt(numeroCodigoDeBarras.trim().substring(5, 9));
        } else {
            return 0;
        }
    }

    service.dataVencimentoCodigoBarras = function (numeroCodigoDeBarras) {
        if (!service.checkFormatoCodigoDeBarras(numeroCodigoDeBarras)) {
            return null;
        }

        let fator = service.fatorDeVencimentoDoCodigoDeBarrasEmInteiro(numeroCodigoDeBarras);

        if (fator === 0) {
            return null;
        }

        let database = new Date(DATA_BASE_FEBRABAN);

        return new Date(database.setDate(database.getDate() + fator));
    }

    return service;

}])

.factory("FechoService", [ 'FechoDespesaGeralService', function(FechoDespesaGeralService) {
    let service = {};

    service.getListaBdi = function(percentualBdiDespesas, percentualBdiBeneficios) {

    	let lista = [];

    	let bdi1 = { bdi: undefined, valor: undefined, custoMensal: undefined, listaBdi: [], valorTotal: undefined };
    	bdi1.bdi = "Despesas indiretas";
    	if (percentualBdiDespesas) {
			bdi1.valor = percentualBdiDespesas;
    	} else {
			bdi1.valor = undefined;
    	}

    	let bdi2 = { bdi: undefined, valor: undefined, custoMensal: undefined, listaBdi: [], valorTotal: undefined };
		bdi2.bdi = "Benefícios";
		if (percentualBdiBeneficios) {
			bdi2.valor = percentualBdiBeneficios;
		} else {
			bdi2.valor = undefined;
		}

		lista.push(bdi1);
		lista.push(bdi2);

		return lista;
    };

    service.getTotalBdi = function(objetoBdi) {
        let total = 0;

        let objBdi = angular.copy(objetoBdi);

        if (!objBdi.valor) {
            objBdi.valor = 0;
        }

        if (!objBdi.custoMensal) {
            objBdi.custoMensal = 0;
        }

    	if ((objBdi.custoMensal || objBdi.custoMensal === 0)  && (objBdi.valor || objBdi.valor === 0)) {
			let percentualPrincipal = service.getPercentualBdi(objBdi, objBdi.bdi) / 100;
			let despesaIndireta = service.getPercentualBdi(objBdi, "Despesas indiretas") / 100;
			let beneficios = service.getPercentualBdi(objBdi, "Benefícios") / 100;
			let dividendo = objBdi.custoMensal * percentualPrincipal;
			let divisor = 1 - (despesaIndireta + beneficios);
			if (divisor <= 0) {
				divisor = 1;
			}
			total = dividendo / divisor;
    	}


  		return Math.round((total + Number.EPSILON) * 100) / 100;
    };

    service.getPercentualBdi = function(objBdi, tipoBdi) {
    	let percentual = 0;

    	if (objBdi.listaBdi && objBdi.listaBdi.length > 0) {
    		angular.forEach(objBdi.listaBdi, function(bdi, key) {
    			if (bdi.bdi === tipoBdi) {
    				if (bdi.valor || bdi.valor === 0) {
    					percentual = bdi.valor;
    					return;
    				} else {
    					percentual = 0;
    					return;
    				}
    			}
			});
    	}
		return percentual;
    };

    service.getCustoMensal = function(totalFechoMaoDeObra, listaDespesa) {
		let total = 0;
		total += Math.round((totalFechoMaoDeObra + Number.EPSILON) * 100) / 100;
		total += FechoDespesaGeralService.getTotalPorTipo(listaDespesa, "Gasto geral");
		total += FechoDespesaGeralService.getTotalPorTipo(listaDespesa, "Material");
		total += FechoDespesaGeralService.getTotalPorTipo(listaDespesa, "Serviço de terceiro");
		return total;
    };

    return service;
}])

.factory("FechoMaoDeObraService", [ function() {
    let service = {};

    service.getTotalAdicionais = function(listaAdicional) {
    	let totalAdicionais = 0;

		angular.forEach(listaAdicional, function(adicional, key) {
			if (adicional && (adicional.valorUnitario || adicional.valorUnitario === 0)) {
				totalAdicionais += adicional.valorUnitario;
			}
		});

		return totalAdicionais;
    };

    service.getValorTotal = function(maoDeObra) {
    	let total = 0;

    	if (maoDeObra && (maoDeObra.valorUnitario || maoDeObra.valorUnitario === 0)
    		&& (maoDeObra.quantidade || maoDeObra.quantidade === 0) && (maoDeObra.totalAdicionais || maoDeObra.totalAdicionais === 0)) {
    		total = Math.round((maoDeObra.quantidade * (maoDeObra.valorUnitario + maoDeObra.totalAdicionais) + Number.EPSILON) * 100) / 100;
    	}

    	return total;
    };

    service.getTotalEncargosSociais = function(encargoSocial, subTotalMaoDeObra) {
		let total = 0;

		if (encargoSocial || encargoSocial === 0) {
			total = Math.round((((subTotalMaoDeObra * encargoSocial) / 100) + Number.EPSILON) * 100) / 100;;
		}
		return total;
	};

	service.getTotal = function(totalEncargosSociais, subTotalMaoDeObra) {
		let total = 0;
		total = totalEncargosSociais + subTotalMaoDeObra;
		return total;
	};

	service.getSubTotalMaoDeObraContratacao = function(listaFechoMaoDeObra, tipoContratacao) {
		let total = 0;

		if (listaFechoMaoDeObra && listaFechoMaoDeObra.length > 0) {
			angular.forEach(listaFechoMaoDeObra, function(fechoMaoDeObra, key) {
				if (fechoMaoDeObra.tipoContratacao && fechoMaoDeObra.tipoContratacao === tipoContratacao) {
					let valorTotal = service.getValorTotal(fechoMaoDeObra);
					total += valorTotal;
				}
			});
		}

		return total;
	};

    return service;
}])

.factory("FechoDespesaGeralService", [ function() {
    let service = {};

    //fixme: refatorar
    service.getTotalPorTipo = function(lista, tipoDespesa) {
    	let total = 0;

    	angular.forEach(lista, function(value, key) {
            if (value.quantidade && value.quantidade > 0 && value.valorUnitario && value.valorUnitario > 0) {
                if (value.tipoDespesaGeral === tipoDespesa) {
                    let valorTotalItem = Math.round((value.quantidade * value.valorUnitario + Number.EPSILON) * 100) / 100;
                    total += valorTotalItem;
                }
            }
		});

		return total;
    };

    service.filtrarListaPorTipo = function(lista, tipoDespesa) {

		let subLista = [];

		angular.forEach(lista, function(value, key) {
			if (value.tipoDespesaGeral === tipoDespesa) {
				subLista.push(value);
			}
		});

		return subLista;
	};

	service.getDescricaoCategoriaDespesaGeral = function(fechoDespesaGeral) {
		let descricaoCategoriaDespesaGeral = "";

		if (fechoDespesaGeral.tipoDespesaGeral) {
			if (fechoDespesaGeral.tipoDespesaGeral === "Gasto geral" && fechoDespesaGeral.categoriaDespesaGeral) {
				descricaoCategoriaDespesaGeral = fechoDespesaGeral.categoriaDespesaGeral.nome;
			} else {
				descricaoCategoriaDespesaGeral = fechoDespesaGeral.descricao;
			}
		}

		return descricaoCategoriaDespesaGeral;
	};

    return service;
}])

.factory("CentroFinanceiroAliquotaService", [ function() {
    let service = {};

    service.getPercentualTributo = function(centroFinanceiroAliquota) {
    	let percentualTributo = 0;

    	if (centroFinanceiroAliquota && centroFinanceiroAliquota.aliquota && centroFinanceiroAliquota.aliquota.percentual) {
            percentualTributo = centroFinanceiroAliquota.aliquota.percentual;
		}

		return percentualTributo;
    };

    //FIXME: Verificar se este método ainda é usado.
    service.getTotalCFAliquota = function(custoMensal, totalBdi, listaCentroFinanceiroAliquota, centroFinanceiroAliquota) {
		let total = 0;

    	if ((custoMensal || custoMensal === 0) && (totalBdi || totalBdi === 0) && listaCentroFinanceiroAliquota && listaCentroFinanceiroAliquota.length > 0) {
            let subTotalTributos = 0;

            angular.forEach(listaCentroFinanceiroAliquota, function(cfAliquota, key) {
				if (cfAliquota && cfAliquota.percentualTributo) {
					subTotalTributos += cfAliquota.percentualTributo;
				}
			});

			if ((custoMensal || custoMensal === 0) && (totalBdi || totalBdi === 0) && centroFinanceiroAliquota && (centroFinanceiroAliquota.percentualTributo || centroFinanceiroAliquota.percentualTributo === 0)) {
				let dividendo = ((custoMensal + totalBdi) * (centroFinanceiroAliquota.percentualTributo / 100));
				let divisor = (1 - (subTotalTributos/100));
				total = dividendo / divisor;
			}
		}

		return total;
    };

    // Base de Tributos = Custo mensal + BDI
    service.getValorTotal = (baseDeTributos, centroFinanceiroAliquota, totalPercentualTributos) => {
        let total = 0;
        let percentualTributo = centroFinanceiroAliquota.percentualTributo;
        let percentual = percentualTributo / 100;

        total = (baseDeTributos * percentual) / (1 - (totalPercentualTributos / 100));

        return total;
    };

    return service;
}])

.factory("EnderecoService", [ function() {
    let service = {};

    service.montarEnderecoCompleto = function(objEndereco) {
        let enderecoCompleto = "";

        if (objEndereco) {
            if (objEndereco.tipoLogradouro && objEndereco.tipoLogradouro.nome && objEndereco.tipoLogradouro.nome.trim()) {
                enderecoCompleto = enderecoCompleto.concat(objEndereco.tipoLogradouro.nome.trim() + ' ');
            }

            if (objEndereco.endereco && objEndereco.endereco.trim()) {
                enderecoCompleto = enderecoCompleto.concat(objEndereco.endereco.trim());
            }

            if (objEndereco.numero && objEndereco.numero.trim()) {
                enderecoCompleto = enderecoCompleto.concat(', ' + objEndereco.numero.trim());
            }

            if (objEndereco.complemento && objEndereco.complemento.trim()) {
                enderecoCompleto = enderecoCompleto.concat(' - ' + objEndereco.complemento.trim());
            }
        }

        return enderecoCompleto;
    };

    return service;
}])

.factory("CentroFinanceiroService", [ function() {
    let service = {};

    service.getDescricaoCidade = function(listaCentroFinanceiroEndereco) {

    	let descricaoCidade = '';

    	let objEndereco = service.getEndereco(listaCentroFinanceiroEndereco);

		if (objEndereco) {
			let enderecos = service.getEnderecos(listaCentroFinanceiroEndereco);

			if (enderecos && enderecos.length > 1) {

				angular.forEach(enderecos, function(item, key) {
					if (objEndereco.cidade && objEndereco.cidade.codigo && item.cidade && item.cidade.codigo ) {
						if (objEndereco.cidade.codigo !== item.cidade.codigo ) {
							descricaoCidade = 'Diversos';
						}
					}
				});

			} else {
				descricaoCidade = objEndereco.cidade.nome;
			}
		}

        return descricaoCidade;
    };

    service.getEndereco = function(listaCentroFinanceiroEndereco) {
    	let listaEnderecos = service.getEnderecos(listaCentroFinanceiroEndereco);

    	if (listaEnderecos && listaEnderecos.length > 0) {
    		return listaEnderecos[0];
    	} else {
    		return null;
    	}
    }

    service.getEnderecos = function(listaCentroFinanceiroEndereco) {
    	let listaEnderecos = listaCentroFinanceiroEndereco;

    	if (listaEnderecos && listaEnderecos.length > 0) {
    		let novaLista = [];

    		angular.forEach(listaEnderecos, function(item, key) {
    			if (item.tipo === 'Prestação de serviço') {
    				item.endereco.tipoEndereco = 'Prestação de serviço';
    			}
    			novaLista.push(item.endereco);
			});

			return novaLista;

    	} else {
    		return null;
    	}
	}

	service.getOrdinalDaSituacao = function(situacao) {
		let ordinal = null;

		switch(situacao) {
            case "Inativo":
                ordinal = 0;
                break;
            case "Ativo":
                ordinal = 1;
                break;
            case "Em elaboração":
                ordinal = 2;
                break;
            case "Em revisão":
                ordinal = 3;
                break;
            case "Em análise - Gerência":
                ordinal = 4;
                break;
            case "Em análise - Diretoria":
                ordinal = 5;
                break;
            case "Em análise - RH":
                ordinal = 6;
                break;
            case "Em análise - Financeiro":
                ordinal = 7;
                break;
            case "Em análise - Contabilidade":
                ordinal = 8;
                break;
            case "Em análise - Comercial":
                ordinal = 9;
                break;
            case "Em análise - Presidência":
                ordinal = 10;
                break;
            case "Encerrado":
                ordinal = 11;
                break;
            case "Cancelado":
                ordinal = 12;
                break;
            case "Paralisado":
                ordinal = 13;
                break;
            case "Reativado":
                ordinal = 14;
                break;
            case "Criado":
                ordinal = 15;
                break;
            case "Em análise - Gestão":
                ordinal = 16;
                break;
            default:
                ordinal = null;
                break;
        }

		return ordinal;
	}

    return service;
}])

.factory("StringService", [ function() {
    let service = {};

    service.replaceAccents = function(val) {
		str = val === null ? '' : val.toString();

		// verifies if the String has accents and replace them
		if (str.search(/[\xC0-\xFF]/g) > -1) {
			str = str
				.replace(/[\xC0-\xC5]/g, "A")
				.replace(/[\xC6]/g, "AE")
				.replace(/[\xC7]/g, "C")
				.replace(/[\xC8-\xCB]/g, "E")
				.replace(/[\xCC-\xCF]/g, "I")
				.replace(/[\xD0]/g, "D")
				.replace(/[\xD1]/g, "N")
				.replace(/[\xD2-\xD6\xD8]/g, "O")
				.replace(/[\xD9-\xDC]/g, "U")
				.replace(/[\xDD]/g, "Y")
				.replace(/[\xDE]/g, "P")
				.replace(/[\xE0-\xE5]/g, "a")
				.replace(/[\xE6]/g, "ae")
				.replace(/[\xE7]/g, "c")
				.replace(/[\xE8-\xEB]/g, "e")
				.replace(/[\xEC-\xEF]/g, "i")
				.replace(/[\xF1]/g, "n")
				.replace(/[\xF2-\xF6\xF8]/g, "o")
				.replace(/[\xF9-\xFC]/g, "u")
				.replace(/[\xFE]/g, "p")
				.replace(/[\xFD\xFF]/g, "y");
		}

		return str;
	};

    return service;
}])

/**
 * Baseado no site:
 * https://developer.mozilla.org/pt-BR/docs/Web/JavaScript/Reference/Global_Objects/Math/round
 */
.factory("NumberService", [ function() {
    let service = {};

    service.ajusteDecimal = function(value) {
        let type = 'round';
        let exp = -2;
		// Se exp é indefinido ou zero...
		if (typeof exp === 'undefined' || +exp === 0) {
			return Math[type](value);
		}
		value = +value;
		exp = +exp;
		// Se o valor não é um número ou o exp não é inteiro...
		if (isNaN(value) || !(typeof exp === 'number' && exp % 1 === 0)) {
			return NaN;
		}
		// Transformando para string
		value = value.toString().split('e');
		value = Math[type](+(value[0] + 'e' + (value[1] ? (+value[1] - exp) : -exp)));
		// Transformando de volta
		value = value.toString().split('e');
		return +(value[0] + 'e' + (value[1] ? (+value[1] + exp) : exp));
	};

    return service;
}])

/**
 * //FIXME: Este serviço foi criado para corrigir os erros de timezone, devido ao horário de verão.
 * Remover este serviço futuramento, se o problema for corrigido no sistema.
 */
.factory("DateTimeService", [ function() {
    let service = {};

    /**
     * PARAM data: deve estar no formato '2019-12-11T02:00:00.000+0000'
     */
    service.ajustarData = function(data) {
		if (data) {
            let localT = data.search("T");

            if (localT && localT > 0) {
                //separa a data no formato '2019-12-11T02:00:00.000+0000' usando a letra 'T' como referencia.
                let dataCompletaSeparada = data.split("T");

                //data simples no formato '2019-12-11'
                let dataSimples = dataCompletaSeparada[0];

                let resultado = moment(dataSimples, "YYYY-MM-DD").hour(12).toDate();
                return resultado;
            }
        }

        return data;
	};

    return service;
}])



;
